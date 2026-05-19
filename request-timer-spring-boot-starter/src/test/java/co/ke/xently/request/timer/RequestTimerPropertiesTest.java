package co.ke.xently.request.timer;

import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTimerPropertiesTest {

    @Test
    void shouldHaveDefaultValues() {
        var properties = new RequestTimerProperties();
        assertThat(properties.getHeaderName()).isEqualTo("X-ElapsedTime");
        assertThat(properties.getOrder()).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
    }

    @Test
    void shouldSetValues() {
        var properties = new RequestTimerProperties();
        properties.setHeaderName("X-Custom-Header");
        properties.setOrder(100);
        assertThat(properties.getHeaderName()).isEqualTo("X-Custom-Header");
        assertThat(properties.getOrder()).isEqualTo(100);
    }

    @Test
    void testEqualsHashCodeToString() {
        var p1 = new RequestTimerProperties();
        var p2 = new RequestTimerProperties();
        var p3 = new RequestTimerProperties();
        p3.setHeaderName("Different");

        assertThat(p1).isEqualTo(p2);
        assertThat(p1).isNotEqualTo(p3);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        assertThat(p1.hashCode()).isNotEqualTo(p3.hashCode());
        assertThat(p1.toString()).contains("headerName=X-ElapsedTime");
    }
}
