package nextstep.subway.line.application.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public LineRequest() {
        this.name = null;
        this.color = null;
        this.upStationId = null;
        this.downStationId = null;
        this.distance = null;
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Line toLine() {
        return Line.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance).build();
    }

    public Line toLine(final Long id) {
        return Line.builder()
                .id(id)
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance).build();
    }

    public static class LineRequestBuilder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public LineRequestBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public LineRequestBuilder color(final String color) {
            this.color = color;
            return this;

        }
        public LineRequestBuilder upStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;

        }

        public LineRequestBuilder downStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;

        }

        public LineRequestBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public LineRequest build() {
            return new LineRequest(name, color, upStationId, downStationId, distance);
        }

    }

    public static LineRequestBuilder builder() {
        return new LineRequestBuilder();
    }

}
