package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LineModificationRequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final String color;

    public LineModificationRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
