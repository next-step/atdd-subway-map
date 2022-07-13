package nextstep.subway.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@Getter
@Setter
public class ErrorResponse {

    private String message;


    public ErrorResponse( String message) {
        this.message = message;
    }

}
