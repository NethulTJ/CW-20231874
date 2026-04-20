/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cw;

/**
 *
 * @author Nethul Jayasuriya
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CampusData {
    public static final Map<String, Room> ROOMS = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> SENSORS = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> READINGS = new ConcurrentHashMap<>();

    static {
        Room starterRoom = new Room();
        starterRoom.setId("LIB-301");
        starterRoom.setName("Library Quiet Study");
        starterRoom.setCapacity(40);
        ROOMS.put(starterRoom.getId(), starterRoom);
    }

    private CampusData() {
    }

    public static String makeId(String id, String prefix) {
        if (id == null || id.isBlank()) {
            return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return id.trim();
    }

    public static List<SensorReading> readingsFor(String sensorId) {
        return READINGS.computeIfAbsent(sensorId, key -> new ArrayList<>());
    }

    public static class Room {
        private String id;
        private String name;
        private int capacity;
        private List<String> sensorIds = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public List<String> getSensorIds() {
            return sensorIds;
        }

        public void setSensorIds(List<String> sensorIds) {
            this.sensorIds = sensorIds == null ? new ArrayList<>() : sensorIds;
        }
    }

    public static class Sensor {
        private String id;
        private String type;
        private String status;
        private double currentValue;
        private String roomId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(double currentValue) {
            this.currentValue = currentValue;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }

    public static class SensorReading {
        private String id;
        private long timestamp;
        private double value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    public static class Message {
        private String message;

        public Message() {
        }

        public Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ErrorBody {
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorBody() {
        }

        public ErrorBody(int status, String error, String message, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}

