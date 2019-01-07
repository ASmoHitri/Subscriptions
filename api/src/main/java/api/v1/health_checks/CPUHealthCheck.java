package api.v1.health_checks;

import com.sun.management.OperatingSystemMXBean;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;

@Health
@ApplicationScoped
public class CPUHealthCheck implements HealthCheck {

    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named(CPUHealthCheck.class.getSimpleName());

        OperatingSystemMXBean systemBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double CPULoad = systemBean.getSystemCpuLoad() * 100; // usage percent
        if (CPULoad > 98) {
            return healthCheckResponseBuilder.down().build();
        }
        return healthCheckResponseBuilder.up().build();
    }
}