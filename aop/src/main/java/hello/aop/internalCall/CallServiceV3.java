package hello.aop.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {

    @Autowired
    InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal();
    }
}