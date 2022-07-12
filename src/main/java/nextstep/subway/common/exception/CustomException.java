package nextstep.subway.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.toString());
        this.responseCode = responseCode;
    }
}
