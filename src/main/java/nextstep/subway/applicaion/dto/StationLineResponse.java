package nextstep.subway.applicaion.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;

@Getter
@AllArgsConstructor
public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private Sections sections;

    public static StationLineResponse form(Line line) {
        return new StationLineResponse(line.getId(), line.getName(), line.getColor(), line.getSections());
    }
}
