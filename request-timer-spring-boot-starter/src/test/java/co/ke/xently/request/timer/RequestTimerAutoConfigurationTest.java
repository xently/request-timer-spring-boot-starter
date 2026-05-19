package co.ke.xently.request.timer;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RequestTimerAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RequestTimerAutoConfiguration.class));

    @Test
    void shouldRegisterFilterWhenAutoConfigurationIsEnabled() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(FilterRegistrationBean.class);
            assertThat(context.getBean(FilterRegistrationBean.class).getFilter()).isInstanceOf(RequestTimerFilter.class);
        });
    }

    @Test
    void shouldAllowCustomHeaderName() {
        contextRunner.withPropertyValues("request-timer.header-name=X-Custom-Timer")
                .run(context -> {
                    assertThat(context).hasSingleBean(RequestTimerProperties.class);
                    assertThat(context.getBean(RequestTimerProperties.class).getHeaderName()).isEqualTo("X-Custom-Timer");
                });
    }

    @Test
    void shouldAddHeaderWithElapsedTime() {
        contextRunner.run(context -> {
            var registrationBean = context.getBean(FilterRegistrationBean.class);
            var filter = registrationBean.getFilter();
            var request = new MockHttpServletRequest();
            var response = new MockHttpServletResponse();
            var filterChain = (FilterChain) (req, res) -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };

            filter.doFilter(request, response, filterChain);

            assertThat(response.getHeader("X-ElapsedTime")).isNotNull();
            var elapsedTime = Long.parseLong(Objects.requireNonNull(response.getHeader("X-ElapsedTime")));
            assertThat(elapsedTime).isGreaterThanOrEqualTo(10);
        });
    }

    @Test
    void shouldAddCustomHeaderWithElapsedTime() {
        contextRunner.withPropertyValues("request-timer.header-name=X-Custom-Timer")
                .run(context -> {
                    var registrationBean = context.getBean(FilterRegistrationBean.class);
                    var filter = registrationBean.getFilter();
                    var request = new MockHttpServletRequest();
                    var response = new MockHttpServletResponse();
                    var filterChain = mock(FilterChain.class);

                    filter.doFilter(request, response, filterChain);

                    assertThat(response.getHeader("X-Custom-Timer")).isNotNull();
                });
    }
}
