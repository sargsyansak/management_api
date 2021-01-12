package am.management.management_api.config;

import am.management.management_api.service.impl.BusinessLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class BeanConfig {

    @Bean
    public BusinessLogic createBusinessLogicBean() {
        return new BusinessLogic();
    }
}
