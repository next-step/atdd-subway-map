package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineReadResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static LineReadResponse of(final Line line) {
        return new LineReadResponse(line.getId(), line.getName(), line.getColor(),
                mapToStationResponses(line.getSections()), line.getCreatedDate(), line.getModifiedDate());
    }

    private static List<StationResponse> mapToStationResponses(final Sections sections) {
        return sections.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public LineReadResponse(final Long id, final String name, final String color, final List<StationResponse> stations,
                            final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    private static class StationResponse {
        private Long id;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        private StationResponse(final Station station) {
            this(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
        }

        private StationResponse(final Long id, final String name, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public LocalDateTime getModifiedDate() {
            return modifiedDate;
        }
    }
}
