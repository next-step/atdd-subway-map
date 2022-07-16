package nextstep.subway.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonExceptionResponse {
    private String message;

    @Builder
    public CommonExceptionResponse(Exception exception) {
        this.message = exception.getLocalizedMessage();
    }

}
