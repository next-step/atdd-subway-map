package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String exceptionMessage;

    public ExceptionResponse(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
