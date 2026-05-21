package co.ke.xently.request.timer.webflux;

import co.ke.xently.request.timer.RequestTimerProperties;
import co.ke.xently.request.timer.utils.HeaderValueProvider;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RequestTimerWebFilter implements WebFilter, Ordered {
    private final RequestTimerProperties properties;
    private final HeaderValueProvider headerValueProvider;

    public RequestTimerWebFilter(RequestTimerProperties properties, HeaderValueProvider headerValueProvider) {
        this.properties = properties;
        this.headerValueProvider = headerValueProvider;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        var startTime = System.currentTimeMillis();
        exchange.getResponse().beforeCommit(() -> {
            exchange.getResponse()
                    .getHeaders()
                    .add(properties.getHeaderName(), this.headerValueProvider.getHeaderValue(startTime));
            return Mono.empty();
        });
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return properties.getOrder();
    }
}
