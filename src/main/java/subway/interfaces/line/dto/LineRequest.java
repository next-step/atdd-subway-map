package subway.interfaces.line.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

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

    public static class Section {
        private final Long upStationId;
        private final Long downStationId;
        private final Long distance;

        public Section(Long upStationId, Long downStationId, Long distance) {
            this.upStationId = upStationId;
            this.downStationId = downStationId;
            this.distance = distance;
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
    }
}
