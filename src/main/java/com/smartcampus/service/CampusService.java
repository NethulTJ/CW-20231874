package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CampusService {
    public static final Map<String, Room> ROOMS = new ConcurrentHashMap<String, Room>();
    public static final Map<String, Sensor> SENSORS = new ConcurrentHashMap<String, Sensor>();
    public static final Map<String, List<SensorReading>> READINGS = new ConcurrentHashMap<String, List<SensorReading>>();

    static {
        Room room = new Room();
        room.setId("LIB-301");
        room.setName("Library Quiet Study");
        room.setCapacity(40);
        ROOMS.put(room.getId(), room);
    }

    private CampusService() {
    }

    public static List<SensorReading> getReadings(String sensorId) {
        List<SensorReading> readings = READINGS.get(sensorId);
        if (readings == null) {
            readings = new ArrayList<SensorReading>();
            READINGS.put(sensorId, readings);
        }
        return readings;
    }

    public static String makeId(String currentId, String prefix) {
        if (currentId == null || currentId.trim().isEmpty()) {
            return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return currentId.trim();
    }
}
