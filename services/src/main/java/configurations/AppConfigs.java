package configurations;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("service")
public class AppConfigs {

    @ConfigValue(value = "maintenance-mode", watch = true)
    private Boolean maintenanceMode;

    @ConfigValue(value = "service-available", watch = true)
    private Boolean serviceAvailable;

    public Boolean getMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Boolean getServiceAvailable() {
        return serviceAvailable;
    }

    public void setServiceAvailable(Boolean serviceAvailable) {
        this.serviceAvailable = serviceAvailable;
    }
}
