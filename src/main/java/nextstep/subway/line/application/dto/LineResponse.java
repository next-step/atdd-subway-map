package nextstep.subway.line.application.dto;

import com.google.common.collect.Iterables;
import lombok.Getter;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LineResponse {
    private final long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations = new ArrayList<>();

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        addStationResponses(line);
    }

    private void addStationResponses(Line line) {
        addStationResponse(line);
        addLastStationResponse(line);
    }

    private void addStationResponse(Line line) {
        line.getSections().forEach(sec -> {
            StationResponse sr = new StationResponse(sec.getUpStation().getId(), sec.getUpStation().getName());
            stations.add(sr);
        });
    }

    private void addLastStationResponse(Line line) {
        Section lastSection = Iterables.getLast(line.getSections());
        stations.add(new StationResponse(lastSection.getDownStation().getId(), lastSection.getDownStation().getName()));
    }
}
