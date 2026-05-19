package co.ke.xently.request.timer.webmvc;

import co.ke.xently.request.timer.RequestTimerProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class RequestTimerFilter extends OncePerRequestFilter {
    private final RequestTimerProperties properties;

    public RequestTimerFilter(RequestTimerProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var startTime = System.currentTimeMillis();
        var wrappedResponse = new TimerResponseWrapper(response, startTime, properties.getHeaderName());

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            wrappedResponse.addTimerHeader();
        }
    }

    private static class TimerResponseWrapper extends HttpServletResponseWrapper {
        private final long startTime;
        private final String headerName;
        private boolean headerAdded = false;

        public TimerResponseWrapper(HttpServletResponse response, long startTime, String headerName) {
            super(response);
            this.startTime = startTime;
            this.headerName = headerName;
        }

        @Override
        public void sendError(int sc) throws IOException {
            addTimerHeader();
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            addTimerHeader();
            super.sendError(sc, msg);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            addTimerHeader();
            super.sendRedirect(location);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            addTimerHeader();
            return super.getOutputStream();
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            addTimerHeader();
            return super.getWriter();
        }

        @Override
        public void flushBuffer() throws IOException {
            addTimerHeader();
            super.flushBuffer();
        }

        public void addTimerHeader() {
            if (!headerAdded && !isCommitted()) {
                var elapsedTime = System.currentTimeMillis() - startTime;
                super.addHeader(headerName, String.valueOf(elapsedTime));
                headerAdded = true;
            }
        }
    }
}
