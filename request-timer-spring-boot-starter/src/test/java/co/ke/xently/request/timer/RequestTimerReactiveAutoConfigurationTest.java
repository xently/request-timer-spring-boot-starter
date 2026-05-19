package co.ke.xently.request.timer;

import co.ke.xently.request.timer.webflux.RequestTimerWebFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestTimerReactiveAutoConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RequestTimerAutoConfiguration.class));

    @Test
    void shouldRegisterWebFilterWhenAutoConfigurationIsEnabled() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RequestTimerWebFilter.class);
        });
    }

    @Test
    void shouldAddHeaderWithElapsedTime() {
        contextRunner.run(context -> {
            var filter = context.getBean(RequestTimerWebFilter.class);
            var request = MockServerHttpRequest.get("/test").build();
            var exchange = MockServerWebExchange.from(request);
            var filterChain = mock(WebFilterChain.class);

            when(filterChain.filter(any())).thenReturn(Mono.delay(Duration.ofMillis(10)).then());

            filter.filter(exchange, filterChain).block();
            exchange.getResponse().setComplete().block();

            var headerValue = exchange.getResponse().getHeaders().getFirst("X-ElapsedTime");
            assertThat(headerValue).isNotNull();
            var elapsedTime = Long.parseLong(headerValue);
            assertThat(elapsedTime).isGreaterThanOrEqualTo(10);
        });
    }

    @Test
    void shouldAddCustomHeaderWithElapsedTime() {
        contextRunner.withPropertyValues("request-timer.header-name=X-Custom-Timer")
                .run(context -> {
                    var filter = context.getBean(RequestTimerWebFilter.class);
                    var request = MockServerHttpRequest.get("/test").build();
                    var exchange = MockServerWebExchange.from(request);
                    var filterChain = mock(WebFilterChain.class);

                    when(filterChain.filter(any())).thenReturn(Mono.empty());

                    filter.filter(exchange, filterChain).block();
                    exchange.getResponse().setComplete().block();

                    assertThat(exchange.getResponse().getHeaders().getFirst("X-Custom-Timer")).isNotNull();
                });
    }
}
