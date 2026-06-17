# Request Timer Spring Boot Starter
## Effortless Request Performance Monitoring

---

## The Problem
- **Visibility**: How long do requests actually take from the server's perspective?
- **Debugging**: Identifying slow endpoints in production without heavy profiling tools.
- **Client Awareness**: Letting clients/front-end know about server processing time automatically.
- **Inconsistency**: Different teams implementing timing logic differently across microservices.

---

## The Solution
### **Request Timer Spring Boot Starter**

- **Automatic**: Just add the dependency and it works.
- **Transparent**: Adds a custom header (e.g., `X-ElapsedTime`) to every response.
- **Accurate**: Captures the full lifecycle from request entry to response commitment.
- **Universal**: Supports both **Spring WebMvc** (Servlet) and **Spring WebFlux** (Reactive).

---

## Key Features

- ✅ **Zero Code Changes**: Auto-configuration kicks in immediately.
- ✅ **High Precision**: Uses `System.currentTimeMillis()` by default.
- ✅ **Configurable**: Customize header names, date formats, and filter order.
- ✅ **Extensible**: Provide your own `HeaderValueProvider` for custom logic (e.g., nanoseconds, logging).
- ✅ **High Precedence**: Runs at `HIGHEST_PRECEDENCE` by default to catch EVERYTHING.

---

## Quick Start: Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ke.co.xently</groupId>
    <artifactId>request-timer-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```

*Requirements: Java 21+ and Spring Boot 4.x*

---

## How It Works (The Result)

A simple `GET` request:
```http
GET /api/v1/books HTTP/1.1
```

The Response:
```http
HTTP/1.1 200 OK
X-ElapsedTime: 20260614162000123
Content-Type: application/json
...
```
*Default format: `yyyyMMddHHmmssSSS`*

---

## Configuration

Customize behavior in `application.yml`:

```yaml
request-timer:
  header-name: X-Response-Time     # Default: X-ElapsedTime
  order: -2147483648               # Default: HIGHEST_PRECEDENCE
  default-header-date-pattern: "ms" # Or any date pattern
```

---

## Advanced Customization

Need to return duration in milliseconds instead of a timestamp?

```java
@Configuration
public class TimingConfig {
    @Bean
    public HeaderValueProvider customHeaderValueProvider() {
        // Return duration in ms
        return startTime -> (System.currentTimeMillis() - startTime) + "ms";
    }
}
```

---

## Why Use This?

1. **Standardization**: Use the same timing header across all company services.
2. **Low Overhead**: Lightweight filters with minimal performance impact.
3. **Reactive Ready**: No need to worry about `WebFilter` vs `Filter` – we handle both.
4. **Immediate ROI**: Zero-effort setup for instant performance insights.

---

## Questions?
### Get started today!
Check out the `demo-webmvc` and `demo-webflux` modules in the repo for examples.
