package nextstep.subway.lines.application.dto;

import nextstep.subway.lines.domain.Line;
import nextstep.subway.lines.domain.Section;
import nextstep.subway.lines.domain.Sections;
import nextstep.subway.stations.applicaion.dto.StationResponse;
import nextstep.subway.stations.domain.Station;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LineSaveResponse {

    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

    public LineSaveResponse() {

    }

    public LineSaveResponse(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = sections.extractStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public static LineSaveResponse of(Line line) {
        return new LineSaveResponse(line.getId(), line.getName(), line.getColor(), line.getSections());
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
