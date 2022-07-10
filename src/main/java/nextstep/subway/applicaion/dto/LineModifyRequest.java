package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.LineContent;

import javax.validation.constraints.NotBlank;

@Getter
public class LineModifyRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;

    public LineContent toLineContent() {
        return LineContent.create(name, color);
    }
}
