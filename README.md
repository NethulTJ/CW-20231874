# Smart Campus API Coursework

This project follows:

- Maven WAR project
- Jersey servlet deployment
- Apache Tomcat deployment
- `web.xml` configuration
- `javax.ws.rs.*` imports
- `Application` subclass with `@ApplicationPath("/api/v1")`

## API Structure

Base URL:

```text
http://localhost:8080/CW/api/v1/
```

Main endpoints:

- `GET /api/v1/`
- `GET /api/v1/rooms`
- `POST /api/v1/rooms`
- `GET /api/v1/rooms/{roomId}`
- `DELETE /api/v1/rooms/{roomId}`
- `GET /api/v1/sensors`
- `POST /api/v1/sensors`
- `GET /api/v1/sensors/{sensorId}`
- `GET /api/v1/sensors/{sensorId}/readings`
- `POST /api/v1/sensors/{sensorId}/readings`

## How To Run

Build the WAR file:

```powershell
mvn clean package
```

If `mvn` is not on your PATH, use a local Maven installation or the Maven bundled with your IDE.

Deploy the generated WAR to Apache Tomcat.

After deployment, open:

```text
http://localhost:8080/CW/
```

This will redirect to the API entry point:

```text
http://localhost:8080/CW/api/v1/
```



## Sample curl Commands

Discovery endpoint:

```bash
curl -X GET http://localhost:8080/CW/api/v1/
```

Create a room:

```bash
curl -X POST http://localhost:8080/CW/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"ENG-101\",\"name\":\"Engineering Lab\",\"capacity\":35}"
```

Get all rooms:

```bash
curl -X GET http://localhost:8080/CW/api/v1/rooms
```

Get a room by ID:

```bash
curl -X GET http://localhost:8080/CW/api/v1/rooms/ENG-101
```

Create a sensor:

```bash
curl -X POST http://localhost:8080/CW/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":450.0,\"roomId\":\"ENG-101\"}"
```

Get all sensors:

```bash
curl -X GET http://localhost:8080/CW/api/v1/sensors
```

Filter sensors by type:

```bash
curl -X GET "http://localhost:8080/CW/api/v1/sensors?type=CO2"
```

Add a reading:

```bash
curl -X POST http://localhost:8080/CW/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":470.5}"
```

Get sensor readings:

```bash
curl -X GET http://localhost:8080/CW/api/v1/sensors/CO2-001/readings
```

Get updated parent sensor:

```bash
curl -X GET http://localhost:8080/CW/api/v1/sensors/CO2-001
```

## Discovery Endpoint

Use:

```text
GET http://localhost:8080/CW/api/v1/
```

Expected response:

```json
{
  "version": "v1",
  "rooms": "/api/v1/rooms",
  "sensors": "/api/v1/sensors"
}
```

## Project Structure

- `com.smartcampus.resource`
- `com.smartcampus.model`
- `com.smartcampus.service`
- `com.smartcampus.exception`
- `com.smartcampus.filter`
