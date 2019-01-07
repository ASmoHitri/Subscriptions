package api.v1.health_checks;

import configurations.AppConfigs;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class DummyHealthCheck implements HealthCheck {

    @Inject
    private AppConfigs appConfig;

    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named(DummyHealthCheck.class.getSimpleName());

        Boolean available = appConfig.getServiceAvailable();
        if (!available) {
            return healthCheckResponseBuilder.down().build();
        }
        return healthCheckResponseBuilder.up().build();
    }
}
