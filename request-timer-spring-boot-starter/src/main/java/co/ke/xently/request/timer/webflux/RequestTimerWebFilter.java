package co.ke.xently.request.timer.webflux;

import co.ke.xently.request.timer.RequestTimerProperties;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RequestTimerWebFilter implements WebFilter, Ordered {
    private final RequestTimerProperties properties;

    public RequestTimerWebFilter(RequestTimerProperties properties) {
        this.properties = properties;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        var startTime = System.currentTimeMillis();
        exchange.getResponse().beforeCommit(() -> {
            var elapsedTime = System.currentTimeMillis() - startTime;
            exchange.getResponse()
                    .getHeaders()
                    .add(properties.getHeaderName(), String.valueOf(elapsedTime));
            return Mono.empty();
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return properties.getOrder();
    }
}
