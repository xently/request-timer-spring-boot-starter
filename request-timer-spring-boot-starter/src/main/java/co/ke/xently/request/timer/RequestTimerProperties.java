package co.ke.xently.request.timer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

@Data
@ConfigurationProperties(prefix = "request-timer")
public class RequestTimerProperties {
    /**
     * Name of the response header that will contain the elapsed time.
     */
    private String headerName = "X-ElapsedTime";
    /**
     * Order of the filter.
     */
    private int order = Ordered.HIGHEST_PRECEDENCE;
}
