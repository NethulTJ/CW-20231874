# Smart Campus API Coursework

This project follows the tutorial style while matching the coursework specification:

- Maven WAR project
- Jersey servlet deployment
- Apache Tomcat deployment
- `web.xml` configuration
- `javax.ws.rs.*` imports
- `Application` subclass with `@ApplicationPath("/api/v1")`

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
