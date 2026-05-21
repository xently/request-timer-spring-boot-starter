# Request Timer Spring Boot Starter

![Maven Central Version](https://img.shields.io/maven-central/v/ke.co.xently/request-timer-spring-boot-starter)
[![Java CI with Maven](https://github.com/xently/request-timer-spring-boot-starter/actions/workflows/maven.yml/badge.svg)](https://github.com/xently/request-timer-spring-boot-starter/actions/workflows/maven.yml)
[![Maven Central Publish](https://github.com/xently/request-timer-spring-boot-starter/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/xently/request-timer-spring-boot-starter/actions/workflows/maven-publish.yml)

A Spring Boot starter that adds an elapsed time header to HTTP responses. It automatically calculates the time taken from the moment the request is received until the response is about to be committed.

- **Spring Boot**: 4.x
- **Java**: 21 (LTS)
- **Web Support**: WebMvc (Servlet) & WebFlux (Reactive)

## Features

- **Automatic Timing**: Capture request duration without any code changes.
- **Dual Support**: Works seamlessly with both Spring WebMvc and Spring WebFlux.
- **Customizable**: Change the header name and filter order via simple properties.
- **High Precedence**: By default, it runs at the highest precedence to capture the full request lifecycle.

## Modules

- `request-timer-spring-boot-starter`: The autoconfiguration starter providing the request timing filters.
- `demo-webmvc`: Demo application using Spring WebMvc and H2.
- `demo-webflux`: Demo application using Spring WebFlux and H2.

## Quick Start

### Requirements
- Java 21
- Maven 3.9+

### Run all tests
```bash
./mvnw test
```

### Run the Demo Applications
To run the WebMvc demo:
```bash
./mvnw -pl demo-webmvc spring-boot:run
```

To run the WebFlux demo:
```bash
./mvnw -pl demo-webflux spring-boot:run
```

The APIs provide a simple book management service:
- `GET /api/v1/books` - Retrieve all books (includes `X-ElapsedTime` header in response).

## Configuration

The starter can be configured using the `request-timer` prefix in your `application.yml` or `application.properties`.

```yaml
request-timer:
  # Name of the response header. Default is X-ElapsedTime.
  header-name: X-ElapsedTime
  # Order of the filter/WebFilter. Default is Ordered.HIGHEST_PRECEDENCE.
  order: -2147483648
  # The date pattern for the elapsed time value. Default is yyyyMMddHHmmssSSS.
  default-header-date-pattern: yyyyMMddHHmmssSSS
```

## Customization

You can provide your own `HeaderValueProvider` bean to customize how the elapsed time value is generated (e.g. to return duration in milliseconds):

```java
import co.ke.xently.request.timer.utils.HeaderValueProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean
    public HeaderValueProvider customHeaderValueProvider() {
        return startTime -> String.valueOf(System.currentTimeMillis() - startTime) + "ms";
    }
}
```
