package subway.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;
import subway.domain.Station;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse from(Line line, Station upStation, Station downStation) {
        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(List.of(StationResponse.from(upStation), StationResponse.from(downStation)))
            .build();
    }

}
