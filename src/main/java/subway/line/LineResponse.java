package subway.line;

import lombok.Builder;
import lombok.Getter;
import subway.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder
    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse of(Line line, StationResponse upStationResponse, StationResponse downStationResponse) {
        LineResponse lineResponse = LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .build();

        lineResponse.addStationResponses(upStationResponse, downStationResponse);

        return lineResponse;
    }

    private void addStationResponses(StationResponse upStation, StationResponse downStation) {
        List<StationResponse> stationResponses = new ArrayList<>();
        stationResponses.add(upStation);
        stationResponses.add(downStation);
        this.stations = stationResponses;
    }
}
