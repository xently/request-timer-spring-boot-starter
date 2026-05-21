package co.ke.xently.request.timer.webmvc;

import co.ke.xently.request.timer.RequestTimerProperties;
import co.ke.xently.request.timer.utils.DefaultHeaderValueProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class RequestTimerFilterTest {

    private RequestTimerProperties properties;
    private RequestTimerFilter filter;

    @BeforeEach
    void setUp() {
        properties = new RequestTimerProperties();
        filter = new RequestTimerFilter(properties, new DefaultHeaderValueProvider());
    }

    @Test
    void shouldAddHeaderOnSuccess() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
    }

    @Test
    void shouldAddHeaderOnSendError() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        doAnswer(invocation -> {
            var res = (HttpServletResponse) invocation.getArgument(1);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldAddHeaderOnSendErrorWithMsg() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        doAnswer(invocation -> {
            var res = (HttpServletResponse) invocation.getArgument(1);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error message");
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
        assertThat(response.getErrorMessage()).isEqualTo("Error message");
    }

    @Test
    void shouldAddHeaderOnSendRedirect() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        doAnswer(invocation -> {
            var res = (HttpServletResponse) invocation.getArgument(1);
            res.sendRedirect("/new-location");
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
        assertThat(response.getRedirectedUrl()).isEqualTo("/new-location");
    }

    @Test
    void shouldAddHeaderOnGetWriter() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        doAnswer(invocation -> {
            var res = (HttpServletResponse) invocation.getArgument(1);
            res.getWriter().write("test");
            return null;
        }).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
        assertThat(response.getContentAsString()).isEqualTo("test");
    }
}
