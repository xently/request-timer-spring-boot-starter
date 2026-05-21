package co.ke.xently.request.timer;

import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.text.SimpleDateFormat;

@NullMarked
@ToString
@EqualsAndHashCode(exclude = {"dateFormat"})
@ConfigurationProperties(prefix = "request-timer")
public class RequestTimerProperties {
    /**
     * Name of the response header that will contain the elapsed time.
     */
    @Getter
    @Setter
    private String headerName = "X-ElapsedTime";
    /**
     * Order of the filter.
     */
    @Getter
    @Setter
    private int order = Ordered.HIGHEST_PRECEDENCE;

    @Setter(value = AccessLevel.NONE)
    public static final String TIMER_DATE_PATTERN = "yyyyMMddHHmmssSSS";

    @Getter
    private String defaultHeaderDatePattern = TIMER_DATE_PATTERN;

    @Getter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(TIMER_DATE_PATTERN);

    public void setDefaultHeaderDatePattern(String defaultHeaderDatePattern) {
        this.dateFormat = new SimpleDateFormat(defaultHeaderDatePattern);
        this.defaultHeaderDatePattern = dateFormat.toPattern();
    }
}
