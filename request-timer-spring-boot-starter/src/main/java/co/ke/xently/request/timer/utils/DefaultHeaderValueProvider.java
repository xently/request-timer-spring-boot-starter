package co.ke.xently.request.timer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static co.ke.xently.request.timer.RequestTimerProperties.TIMER_DATE_PATTERN;

public final class DefaultHeaderValueProvider implements HeaderValueProvider {
    private final SimpleDateFormat dateFormat;

    public DefaultHeaderValueProvider(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultHeaderValueProvider() {
        this(new SimpleDateFormat(TIMER_DATE_PATTERN));
    }

    @Override
    public String getHeaderValue(long startTime) {
        var date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
