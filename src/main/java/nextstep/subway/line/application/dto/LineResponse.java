package nextstep.subway.line.application.dto;

import nextstep.subway.station.applicaion.dto.StationResponse;

import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse() {
        this.id = null;
        this.name = null;
        this.color = null;
        this.stations = null;
    }

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public static class LineResponseBuilder {
        private Long id;
        private String name;
        private String color;
        private List<StationResponse> stations;

        public LineResponse.LineResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public LineResponse.LineResponseBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public LineResponse.LineResponseBuilder color(final String color) {
            this.color = color;
            return this;

        }

        public LineResponse.LineResponseBuilder stations(final List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public LineResponse build() {
            return new LineResponse(id, name, color, stations);
        }

    }

    public static LineResponse.LineResponseBuilder builder() {
        return new LineResponse.LineResponseBuilder();
    }

}
