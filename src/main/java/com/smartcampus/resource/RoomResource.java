package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.service.CampusService;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public List<Room> getRooms() {
        return new ArrayList<Room>(CampusService.ROOMS.values());
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null) {
            throw new javax.ws.rs.BadRequestException("Room body is required.");
        }

        room.setId(CampusService.makeId(room.getId(), "ROOM"));
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<String>());
        }

        CampusService.ROOMS.put(room.getId(), room);
        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(location).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        Room room = CampusService.ROOMS.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room " + roomId + " was not found.");
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = CampusService.ROOMS.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room " + roomId + " was not found.");
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId + " still has sensors assigned and cannot be deleted.");
        }

        CampusService.ROOMS.remove(roomId);
        Map<String, String> body = new LinkedHashMap<String, String>();
        body.put("message", "Room deleted successfully.");
        return Response.ok(body).build();
    }
}
