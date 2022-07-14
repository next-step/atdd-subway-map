package nextstep.subway.ui.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResult {
    private int errorCode;
    private String errorMessage;
}
