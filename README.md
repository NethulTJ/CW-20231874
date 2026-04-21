# Smart Campus API Coursework

This project is a simple JAX-RS REST API for the `5COSC022W` coursework. It follows the tutorial style more closely now, so it uses fewer files and less abstraction while still covering the main rubric areas:

- JAX-RS application setup with `/api/v1`
- Discovery endpoint
- Room management
- Sensor creation and filtering
- Nested sensor readings with a sub-resource locator
- One simple custom exception class and one global mapper
- Request and response logging

## Run

Use a local Maven installation:

```powershell
mvn compile
mvn exec:java
```

The server will stay running until you stop the process in NetBeans or the terminal.

Base URL:

```text
http://localhost:8080/api/v1
```

## Suggested Demo Order

### 1. Discovery

`GET /api/v1`

### 2. Rooms

- `GET /api/v1/rooms`
- `POST /api/v1/rooms`
- `GET /api/v1/rooms/{roomId}`
- `DELETE /api/v1/rooms/{roomId}`

Example room body:

```json
{
  "id": "ENG-101",
  "name": "Engineering Lab",
  "capacity": 35
}
```

### 3. Sensors

- `POST /api/v1/sensors`
- `GET /api/v1/sensors`
- `GET /api/v1/sensors?type=CO2`

Example sensor body:

```json
{
  "id": "CO2-001",
  "type": "CO2",
  "status": "ACTIVE",
  "currentValue": 450.0,
  "roomId": "ENG-101"
}
```

### 4. Readings

- `GET /api/v1/sensors/CO2-001/readings`
- `POST /api/v1/sensors/CO2-001/readings`

Example reading body:

```json
{
  "value": 470.5
}
```

### 5. Error Handling Demo

- Try creating a sensor with a non-existent `roomId` to get `422`
- Try deleting a room that still has sensors to get `409`
- Try posting a reading to a sensor whose status is not `ACTIVE` to get `403`
- Try sending `text/plain` to a JSON endpoint to show `415`

## Simple Structure

- `RestApplication` sets the versioned API path.
- `CW` starts the Grizzly server.
- `ApiResource` holds the main endpoints.
- `ApiResource.ReadingResource` is the sub-resource for `/sensors/{id}/readings`.
- `CampusData` keeps the models and in-memory data in one place.
- `ApiException` and `ApiExceptionMapper` keep error handling simple.
- `ApiLoggingFilter` prints each request and response to the console.

## Dependency Note

The project now uses a smaller dependency set that matches the embedded server approach:

- `jersey-container-grizzly2-http`
- `jersey-media-json-jackson`
- `jersey-hk2`

`jersey-hk2` is important because Jersey often fails at runtime without its injection provider.

Only the top-level classes in `com.mycompany.cw` are now part of the active build. Older subfolder classes are ignored by Maven so they do not affect compilation.

## Report Notes

These are the main theory questions you still need to write up in your own words inside the report:

- JAX-RS default resource lifecycle and thread safety
- HATEOAS and why discovery links help clients
- Returning room IDs only vs full room objects
- Whether `DELETE` is idempotent
- What happens when `@Consumes(MediaType.APPLICATION_JSON)` is violated
- Why `@QueryParam` is better for filtering collections
- Why sub-resource locators help large APIs
- Why `422` is better than `404` for broken references inside valid JSON
- Why hiding stack traces matters for security
