package co.ke.xently.request.timer.webflux;

import co.ke.xently.request.timer.RequestTimerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestTimerWebFilterTest {

    @Test
    void shouldAddHeaderOnCommit() {
        var properties = new RequestTimerProperties();
        var filter = new RequestTimerWebFilter(properties);
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        var chain = mock(WebFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        StepVerifier.create(exchange.getResponse().setComplete())
                .verifyComplete();

        assertThat(exchange.getResponse().getHeaders().getFirst(properties.getHeaderName())).isNotNull();
    }

    @Test
    void shouldReturnCorrectOrder() {
        var properties = new RequestTimerProperties();
        var filter = new RequestTimerWebFilter(properties);
        assertThat(filter.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
    }
}
