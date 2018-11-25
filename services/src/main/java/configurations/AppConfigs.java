package configurations;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("tests")
public class AppConfigs {

    @ConfigValue(value="maintenance-mode", watch=true)
    private Boolean maintenanceMode;

    public Boolean getMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenaneceMode(Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

}
