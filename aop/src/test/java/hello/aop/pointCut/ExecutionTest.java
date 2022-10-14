package hello.aop.pointCut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;
    @BeforeEach
    public void init() throws NoSuchMethodException {

        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod = {}" ,helloMethod );
    }

    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(java.lang.String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch1() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch2() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch3() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchFalse() {
        pointcut.setExpression("execution(public String hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage() {
        pointcut.setExpression("execution(public String hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMath() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMathSuperType() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMathInternalFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(public String hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    //  (String)
    @Test
    void argsMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 파라미터가 없어야함
    //  ()
    @Test
    void argsMatchNoArgs() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *())");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 하나의 파라미터, 모든 타입 허용
    @Test
    void argsMatchStar() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(*))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

  // 다수의 파라미터, 모든 타입 허용
    @Test
    void argsMatchAll() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    // String으로 시작, 다수의 모든 파라미터, 모든 타입
    // (String) (String, XXX) (String, XXX, XXX)
    @Test
    void argsMatchComplex() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String, ..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }




}
