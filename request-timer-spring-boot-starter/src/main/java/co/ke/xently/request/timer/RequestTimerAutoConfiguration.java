package co.ke.xently.request.timer;

import co.ke.xently.request.timer.webflux.RequestTimerWebFilter;
import co.ke.xently.request.timer.webmvc.RequestTimerFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(RequestTimerProperties.class)
public class RequestTimerAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(Filter.class)
    static class ServletRequestTimerConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public FilterRegistrationBean<RequestTimerFilter> requestTimerFilter(RequestTimerProperties properties) {
            var registrationBean = new FilterRegistrationBean<RequestTimerFilter>();
            registrationBean.setFilter(new RequestTimerFilter(properties));
            registrationBean.setOrder(properties.getOrder());
            return registrationBean;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFilter.class)
    static class ReactiveRequestTimerConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RequestTimerWebFilter requestTimerWebFilter(RequestTimerProperties properties) {
            return new RequestTimerWebFilter(properties);
        }
    }
}
