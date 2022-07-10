package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.LineContent;

@Getter
public class LineModifyRequest {

    private String name;
    private String color;

    public LineContent toLineContent() {
        return LineContent.create(name, color);
    }
}
