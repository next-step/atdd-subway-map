package subway.controller.dto;

import java.util.Set;
import subway.entity.Station;
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

    private Long valid(Long downStationId) {
        if (downStationId.equals(upStationId)) {
            throw new LineNotEstablishedBySameEndStationException(
                String.format("노선의 두 종점역은 동일할 수 없습니다: %d / %d", upStationId, downStationId)
            );
        }
        return downStationId;
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

    public static class UpdateRequest {

        private final String name;

        private final String color;

        private final Set<Station> endStations;

        private final Long distance;

        public UpdateRequest(String name, String color, Set<Station> endStations, Long distance) {
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
            return endStations != null && endStations.size() > 0;
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

        public Set<Station> getEndStations() {
            return endStations;
        }

        public Long getDistance() {
            return distance;
        }

        public static class Builder {
            private String name;

            private String color;

            private Set<Station> endStations;

            private Long distance;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder color(String color) {
                this.color = color;
                return this;
            }

            public Builder endStations(Set<Station> endStations) {
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
