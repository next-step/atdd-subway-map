package subway.domain.exception;

import lombok.Getter;

@Getter
public class SubwayDomainException extends RuntimeException {
    private final SubwayDomainExceptionType exceptionType;

    public SubwayDomainException(SubwayDomainExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }
}
