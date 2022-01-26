package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineStationResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stationResponses;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineStationResponse() {
    }

    public LineStationResponse(final Long id, final String name, final String color, final LocalDateTime createdDate, final LocalDateTime modifiedDate, final List<Station> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stationResponses = stationResponses;
    }

    public LineStationResponse(Line line, final List<nextstep.subway.domain.Station> stations) {
        this(line.getId(), line.getName(), line.getColor(), line.getCreatedDate(), line.getModifiedDate(),
                stations.stream().map(it -> new Station(it.getId(), it.getName())).collect(Collectors.toList()));
    }

    public static class Station {
        private Long id;
        private String name;

        public Station(final Long id, final String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public List<Station> getStationResponses() {
        return stationResponses;
    }
}
