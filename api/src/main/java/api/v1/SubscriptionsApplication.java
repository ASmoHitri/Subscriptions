package api.v1;


import com.kumuluz.ee.discovery.annotations.RegisterService;
import com.kumuluz.ee.logs.cdi.Log;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService
@Log
@ApplicationPath("/api/v1")
public class SubscriptionsApplication extends Application{
}
