package com.mycompany.cw;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    public Map<String, Object> discovery() {
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", "Smart Campus API");
        body.put("version", "v1");
        body.put("contact", "hamed.hamzeh@westminster.ac.uk");
        body.put("resources", resources);
        return body;
    }

    @GET
    @Path("/rooms")
    public List<CampusData.Room> getRooms() {
        return new ArrayList<>(CampusData.ROOMS.values());
    }

    @POST
    @Path("/rooms")
    public Response createRoom(CampusData.Room room, @Context UriInfo uriInfo) {
        if (room == null) {
            throw new BadRequestException("Room body is required.");
        }

        room.setId(CampusData.makeId(room.getId(), "ROOM"));
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        CampusData.ROOMS.put(room.getId(), room);
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/rooms/{roomId}")
    public CampusData.Room getRoom(@PathParam("roomId") String roomId) {
        CampusData.Room room = CampusData.ROOMS.get(roomId);
        if (room == null) {
            throw new ApiException(Response.Status.NOT_FOUND, "Room " + roomId + " was not found.");
        }
        return room;
    }

    @DELETE
    @Path("/rooms/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        CampusData.Room room = CampusData.ROOMS.get(roomId);
        if (room == null) {
            throw new ApiException(Response.Status.NOT_FOUND, "Room " + roomId + " was not found.");
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new ApiException(Response.Status.CONFLICT, "Room " + roomId + " still has sensors assigned and cannot be deleted.");
        }

        CampusData.ROOMS.remove(roomId);
        return Response.ok(new CampusData.Message("Room deleted successfully.")).build();
    }

    @GET
    @Path("/sensors")
    public List<CampusData.Sensor> getSensors(@QueryParam("type") String type) {
        List<CampusData.Sensor> sensors = new ArrayList<>(CampusData.SENSORS.values());
        if (type == null || type.isBlank()) {
            return sensors;
        }
        return sensors.stream()
                .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type))
                .toList();
    }

    @GET
    @Path("/sensors/{sensorId}")
    public CampusData.Sensor getSensor(@PathParam("sensorId") String sensorId) {
        CampusData.Sensor sensor = CampusData.SENSORS.get(sensorId);
        if (sensor == null) {
            throw new ApiException(Response.Status.NOT_FOUND, "Sensor " + sensorId + " was not found.");
        }
        return sensor;
    }

    @POST
    @Path("/sensors")
    public Response createSensor(CampusData.Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null) {
            throw new BadRequestException("Sensor body is required.");
        }

        CampusData.Room room = CampusData.ROOMS.get(sensor.getRoomId());
        if (room == null) {
            throw new ApiException(Response.Status.BAD_REQUEST, 
        "roomId " + sensor.getRoomId() + " does not exist.");
        }

        sensor.setId(CampusData.makeId(sensor.getId(), "SENSOR"));
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }

        CampusData.SENSORS.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @Path("/sensors/{sensorId}/readings")
    public ReadingResource readings(@PathParam("sensorId") String sensorId) {
        if (!CampusData.SENSORS.containsKey(sensorId)) {
            throw new ApiException(Response.Status.NOT_FOUND, "Sensor " + sensorId + " was not found.");
        }
        return new ReadingResource(sensorId);
    }

    public static class ReadingResource {
        private final String sensorId;

        public ReadingResource(String sensorId) {
            this.sensorId = sensorId;
        }

        @GET
        public List<CampusData.SensorReading> getReadings() {
            return CampusData.readingsFor(sensorId);
        }

        @POST
        public Response addReading(CampusData.SensorReading reading, @Context UriInfo uriInfo) {
            if (reading == null) {
                throw new BadRequestException("Reading body is required.");
            }

            CampusData.Sensor sensor = CampusData.SENSORS.get(sensorId);
            if (!"ACTIVE".equalsIgnoreCase(sensor.getStatus())) {
                throw new ApiException(Response.Status.FORBIDDEN, "Readings can only be added to sensors in ACTIVE state.");
            }

            reading.setId(CampusData.makeId(reading.getId(), "READING"));
            if (reading.getTimestamp() == 0L) {
                reading.setTimestamp(System.currentTimeMillis());
            }

            CampusData.readingsFor(sensorId).add(reading);
            sensor.setCurrentValue(reading.getValue());

            URI location = uriInfo.getAbsolutePathBuilder().path(reading.getId()).build();
            return Response.created(location).entity(reading).build();
        }
    }
}
