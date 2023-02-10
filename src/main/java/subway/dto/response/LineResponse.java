package subway.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {

        List<Section> sections = line.getSections().getSectionList();
        Station upStation = sections.get(0).getUpStation();
        Station downStation = sections.get(sections.size() - 1).getDownStation();

        List<StationResponse> stations = List.of(StationResponse.of(upStation), StationResponse.of(downStation));

        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(stations)
            .build();
    }

}
