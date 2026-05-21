package co.ke.xently.request.timer.utils;

@FunctionalInterface
public interface HeaderValueProvider {
    String getHeaderValue(long startTime);
}
