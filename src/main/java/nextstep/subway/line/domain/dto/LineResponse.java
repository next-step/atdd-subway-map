package nextstep.subway.line.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.model.Line;
import nextstep.subway.station.domain.dto.StationResponse;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public LineResponse(Long id, String name, String color,
                        List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stations = line.getStations()
                                              .stream()
                                              .map(StationResponse::from)
                                              .collect(Collectors.toList());
        return builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .createdDate(line.getCreatedDate())
            .modifiedDate(line.getModifiedDate())
            .stations(stations)
            .build();
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

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<StationResponse> stations;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Builder stations(List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public LineResponse build() {
            return new LineResponse(
                id, name, color, stations, createdDate, modifiedDate
            );
        }
    }
}
