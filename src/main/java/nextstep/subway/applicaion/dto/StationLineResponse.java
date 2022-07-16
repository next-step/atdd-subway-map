package nextstep.subway.applicaion.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Line;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sections;

    public StationLineResponse(Long id, String name, String color) {
        this(id, name, color, Collections.emptyList());
    }

    public static StationLineResponse form(Line line) {
        return new StationLineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static StationLineResponse of(Line line, List<SectionResponse> sections) {
        return new StationLineResponse(line.getId(), line.getName(), line.getColor(), sections);
    }


}
