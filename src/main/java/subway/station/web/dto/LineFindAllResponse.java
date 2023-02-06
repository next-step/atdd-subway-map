package subway.station.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ViewLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations = new ArrayList<>();

    @Builder
    public ViewLineResponse(Long id, String name, String color, Station upStation, Station downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations.addAll(List.of(upStation, downStation));
    }
}
