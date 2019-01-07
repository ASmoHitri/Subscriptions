package api.v1.interceptors;

import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.CloseableThreadContext;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.UUID;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogInterceptor {
    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

        HashMap info = new HashMap();
        info.put("environment", configurationUtil.get("kumuluzee.env.name").orElse(null));
        info.put("applicationName", configurationUtil.get("kumuluzee.name").orElse(null));
        info.put("applicationVersion", configurationUtil.get("kumuluzee.version").orElse(null));
        info.put("uniqueRequestId", UUID.randomUUID().toString());

        try (final CloseableThreadContext.Instance closeableContext = CloseableThreadContext.putAll(info)) {
            return context.proceed();
        }
    }
}