package nextstep.subway.lines.application.dto;

import nextstep.subway.lines.domain.Line;
import nextstep.subway.stations.applicaion.dto.StationResponse;
import nextstep.subway.stations.domain.Station;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class LineSaveResponse {

    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

    public LineSaveResponse() {

    }

    public LineSaveResponse(final Long id, final String name, final String color, final StationResponse upStation,
                            final StationResponse downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = Arrays.asList(upStation, downStation);
    }

    public static LineSaveResponse of(Line line) {
        Station upStation = line.getUpStation();
        Station downStation = line.getDownStation();
        return new LineSaveResponse(line.getId(), line.getName(), line.getColor(),
                new StationResponse(upStation), new StationResponse(downStation));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }
}
