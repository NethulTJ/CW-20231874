package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.CampusService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<Sensor>(CampusService.SENSORS.values());
        if (type == null || type.trim().isEmpty()) {
            return sensors;
        }

        List<Sensor> filtered = new ArrayList<Sensor>();
        for (Sensor sensor : sensors) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null) {
            throw new javax.ws.rs.BadRequestException("Sensor body is required.");
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            throw new LinkedResourceNotFoundException("roomId is required.");
        }

        Room room = CampusService.ROOMS.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("roomId " + sensor.getRoomId() + " does not exist.");
        }

        sensor.setId(CampusService.makeId(sensor.getId(), "SENSOR"));
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }

        CampusService.SENSORS.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(location).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = CampusService.SENSORS.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor " + sensorId + " was not found.");
        }
        return sensor;
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        if (!CampusService.SENSORS.containsKey(sensorId)) {
            throw new NotFoundException("Sensor " + sensorId + " was not found.");
        }
        return new SensorReadingResource(sensorId);
    }
}
