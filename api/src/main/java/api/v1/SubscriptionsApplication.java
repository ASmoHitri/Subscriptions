package api.v1;


import com.kumuluz.ee.discovery.annotations.RegisterService;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService
@ApplicationPath("/api/v1")
public class SubscriptionsApplication extends Application{
}
