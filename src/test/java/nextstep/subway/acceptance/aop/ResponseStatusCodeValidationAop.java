package nextstep.subway.acceptance.aop;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Aspect
@Component
public class ResponseStatusCodeValidationAop {

    @Pointcut("@annotation(nextstep.subway.acceptance.aop.ValidateCreated)")
    private void validateCreatedPointcut() {}

    @Pointcut("@annotation(nextstep.subway.acceptance.aop.ValidateOk)")
    private void validateOkPointcut() {}

    @Pointcut("@annotation(nextstep.subway.acceptance.aop.ValidateNoContent)")
    private void validateNoContentPointcut() {}

    @AfterReturning(value = "validateCreatedPointcut()", returning = "returnObject")
    public void validateCreatedPointcut(JoinPoint joinPoint, Object returnObject) {
        validateStatusCode(convertToResponse(returnObject), HttpStatus.CREATED);
    }

    @AfterReturning(value = "validateOkPointcut()", returning = "returnObject")
    public void validateOkPointcut(JoinPoint joinPoint, Object returnObject) {
        validateStatusCode(convertToResponse(returnObject), HttpStatus.OK);
    }

    @AfterReturning(value = "validateNoContentPointcut()", returning = "returnObject")
    public void validateNoContentPointcut(JoinPoint joinPoint, Object returnObject) {
        validateStatusCode(convertToResponse(returnObject), HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> convertToResponse(Object returnObject) {
        return (ExtractableResponse<Response>) returnObject;
    }

    private void validateStatusCode(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

}
