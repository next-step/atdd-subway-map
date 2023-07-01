package subway.controller.dto;

import subway.domain.EndStations;
import subway.exception.LineNotEstablishedBySameEndStationException;

public class LineRequest {

    private final String name;

    private final String color;

    private final Long upStationId;

    private final Long downStationId;

    private final Long distance;

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = valid(downStationId);
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

    private Long valid(Long downStationId) {
        if (isSameWithUpStationId(downStationId)) {
            throw new LineNotEstablishedBySameEndStationException(
                String.format("노선의 두 종점역은 동일할 수 없습니다: %d / %d", upStationId, downStationId)
            );
        }
        return downStationId;
    }

    private boolean isSameWithUpStationId(Long downStationId) {
        return downStationId.equals(upStationId);
    }

    public static class UpdateRequest {

        private final String name;

        private final String color;

        private final EndStations endStations;

        private final Long distance;

        public UpdateRequest(String name, String color, EndStations endStations, Long distance) {
            this.name = name;
            this.color = color;
            this.endStations = endStations;
            this.distance = distance;
        }

        public boolean hasName() {
            return name != null && !name.isBlank();
        }

        public boolean hasColor() {
            return color != null && !color.isBlank();
        }

        public boolean hasEndStations() {
            return endStations != null;
        }

        public boolean hasDistance() {
            return distance != null && distance > 0L;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        public EndStations getEndStations() {
            return endStations;
        }

        public Long getDistance() {
            return distance;
        }

        public static class Builder {
            private String name;

            private String color;

            private EndStations endStations;

            private Long distance;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder color(String color) {
                this.color = color;
                return this;
            }

            public Builder endStations(EndStations endStations) {
                this.endStations = endStations;
                return this;
            }

            public Builder distance (Long distance) {
                this.distance = distance;
                return this;
            }

            public UpdateRequest build() {
                return new UpdateRequest(name, color, endStations, distance);
            }
        }
    }

}
