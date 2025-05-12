package org.example.crmservice.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Индикатор состояния, проверяющий регистрацию сервиса в Eureka.
 */
@Component
public class EurekaRegistrationHealthIndicator implements HealthIndicator {

    @Value("${spring.application.name}")
    private String serviceName;

    private final EurekaDiscoveryClient eurekaDiscoveryClient;


    /**
     * Конструктор для {@link EurekaRegistrationHealthIndicator}.
     * @param eurekaDiscoveryClient Клиент Eureka для получения информации об экземплярах сервиса.
     */
    public EurekaRegistrationHealthIndicator(EurekaDiscoveryClient eurekaDiscoveryClient) {
        this.eurekaDiscoveryClient = eurekaDiscoveryClient;
    }

    /**
     * Проверяет состояние регистрации сервиса в Eureka.
     * @return {@link Health#up()} если сервис зарегистрирован, иначе {@link Health#down()}.
     *         В случае ошибки возвращает {@link Health#down()} с деталями исключения.
     */
    @Override
    public Health health() {
        try {
            List<ServiceInstance> instances =eurekaDiscoveryClient.getInstances(serviceName);
            boolean isRegistered = !instances.isEmpty();
            return (isRegistered)
                    ? Health.up().withDetail("message", "Service is registered in eureka.").build()
                    : Health.down().withDetail("message", "Service is not registered in eureka.").build();
        } catch (Exception e){
            return Health.down(e).build();
        }
    }
}
