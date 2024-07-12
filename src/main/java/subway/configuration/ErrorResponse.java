package subway.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.exception.SubwayDomainExceptionType;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    String error;
    String message;

    public static ErrorResponse fromSubwayException(SubwayDomainExceptionType type) {
        return new ErrorResponse(type.getName(), type.getMessage());
    }
}
