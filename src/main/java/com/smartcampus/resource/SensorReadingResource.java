package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.service.CampusService;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return CampusService.getReadings(sensorId);
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        if (reading == null) {
            throw new javax.ws.rs.BadRequestException("Reading body is required.");
        }

        Sensor sensor = CampusService.SENSORS.get(sensorId);
        if (sensor.getStatus() != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in MAINTENANCE and cannot accept new readings.");
        }

        reading.setId(CampusService.makeId(reading.getId(), "READING"));
        if (reading.getTimestamp() == 0L) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        CampusService.getReadings(sensorId).add(reading);
        sensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder().path(reading.getId()).build();
        return Response.created(location).entity(reading).build();
    }
}
