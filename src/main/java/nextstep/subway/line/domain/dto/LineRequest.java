package nextstep.subway.line.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import nextstep.subway.line.domain.model.Distance;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Distance distance;

    private LineRequest() {}

    private LineRequest(String name, String color, long upStationId, long downStationId, Distance distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Builder builder() {
        return new Builder();
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

    public Distance getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setUpStationId(long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDownStationId(long downStationId) {
        this.downStationId = downStationId;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public static class Builder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Distance distance;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public LineRequest build() {
            return new LineRequest(name, color, upStationId, downStationId, distance);
        }
    }
}
