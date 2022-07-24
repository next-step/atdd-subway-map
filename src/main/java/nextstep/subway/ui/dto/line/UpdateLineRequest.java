package nextstep.subway.ui.dto.line;

import lombok.Getter;

@Getter
public class UpdateLineRequest {
    private String name;
    private String color;

    public UpdateLineRequest() {
    }
}
