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
  "adminName": "NEthul Trevor",
  "adminEmail": "nethul.20231874@iit.ac.lk",
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

## Report Answers
# 1. The Resource Lifecycle in JAX-RS And Memory Management

A JAX-RS resource class will typically have request scope by default, meaning that its instances are recreated for each request rather than being shared as a singleton across multiple users. While request-specific information contained within resource classes cannot be shared among multiple users, any shared in-memory objects including maps and lists will still remain global application variables. Thus, several concurrent requests will still be able to read or update the same data simultaneously, and I will have to use thread-safe implementations of collections like ConcurrentHashMap or add appropriate synchronisation mechanisms to prevent race conditions and inconsistent results.

# 2. Advantages Of Using Hypermedia/HATEOAS

Using hypermedia in RESTful services is a good idea because it makes APIs self-explanatory. In other words, hypermedia allows the client to follow certain paths provided by the server, eliminating the need for manual inspection of documentation and hardcoding endpoint structure into clients' source code. From the developer's point of view, hypermedia gives them freedom to evolve their API as much as needed, while client applications will always know what endpoints can be accessed at any moment, and the server will lead them to valid paths.

# 3. Sending Back Only Room IDs or Rooms As Well

Returning only room IDs will reduce bandwidth usage because less information is returned per request. On the other hand, this approach requires the client to make several follow-up requests to obtain information about specific rooms. In other words, using full room objects consumes more bandwidth, but provides greater convenience because all necessary data is available in a single request. Since testing this API is one of the purposes of the coursework, returning full room objects is more suitable in this situation.



# 4. Is DELETE Idempotent?

Yes, DELETE is idempotent in my implementation. Idempotent requests are the kind of requests where the repeated invocation produces the same final state of the server even if executed many times in succession. For instance, if I invoke DELETE and it deletes a certain room on the first time, subsequent calls to delete the very same room will not cause any change because the room is already deleted. Subsequent calls might fail and yield 404 Not Found, but the point is that there will be no change in the final state of the server – this is exactly the condition of being idempotent.

# 5. What Happens If A Client Violates @Consumes(MediaType.APPLICATION_JSON)?

The @Consumes(MediaType.APPLICATION_JSON) annotation means that the method accepts JSON-encoded request body as input. In case a client invokes this method using some other media type, JAX-RS engine rejects such call right away and doesn't allow to execute this particular method. Normally, when client uses the wrong media type for POST/PUT requests, JAX-RS returns 415 Unsupported Media Type HTTP status. The good thing about using this feature is that it prevents us from having a situation where our business logic receives data in the unsupported format.

# 6. Why @QueryParam Is Better For Filtering Collections

Using @QueryParam is generally more preferable because filtering is an optional feature. When we use filtering, we don't create a separate resource – rather, we provide a collection of resources with an optional criterion to filter out the elements that do not match certain conditions. That is why /sensors?type=CO2 looks logically consistent as opposed to /sensors/type/CO2. Another advantage of using query parameters is the possibility to add extra criteria for searching. For instance, we could say that the user is interested both in type CO2 and in ACTIVE sensors.



# 7. Advantages Of Sub-Resource Locator Design Pattern

Sub-Resource Locator design pattern contributes towards keeping APIs tidy as the responsibility of nested paths can be delegated to their own classes. Rather than having all of the methods dealing with sensors and readings defined within a single huge resource class, the major sensor resource will delegate responsibility of /sensors/{id}/readings path to a new SensorReadingResource. Such an approach would increase code readability and simplify testing of code in the future. This technique is especially useful when developing a bigger API, where everything cannot fit in one huge controller.

# 8. Why It's More Correct To Use 422 Status Code For Invalid Reference Provided As Part Of JSON Body

If we get a valid JSON body from the client, but one of the fields in the body references to some ID which is not there (for example,roomId), 422 unprocessable entity status code should be used, rather than the standard 404 Not Found error message. In this particular case, endpoint does exist, therefore we cannot say that it is not available or that there was no request to an existing path. However, in this situation the server understood the structure of our request correctly, but still failed to process it due to the provided semantically invalid value.

# 9. Why Showing Stack Trace May Harm Security

The exposure of raw stack traces by an external client may expose internal application information, such as package name, class name, method name, filepath, and even a version number. This may make it easier for a hacker to get more details on how the application looks like and find possible security holes in it. The exposed stack trace may expose some of the inner workings of your application, thus making its security vulnerable to attacks. Therefore, having a global ExceptionMapper<Throwable> is more secure since it provides less information to a client and shows only 500 Internal Server Error.


# 10. Why Should JAX-RS Filters Be Used for Logging Rather Than Using Logger.info() in Each Resource Method?

JAX-RS filters should be used for logging since logging is a cross-cutting concern that affects the entire API but not individual resource methods. By using Logger.info() manually in each resource method, I will end up writing similar code in all endpoints, which makes the code less readable and prone to errors in future updates. Filters help me to log requests and responses at a central location regardless of the API calls made to the application.
