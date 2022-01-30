package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private int distance;

    public LineRequest() {

    }

    private LineRequest(String name, String color, long upStationId, long downStationId, int distance) {
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

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }


    public static class Builder {
        private String name;
        private String color;
        private long upStationId;
        private long downStationId;
        private int distance;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public LineRequest build() {
            if (name == null
                    || color == null
                    || upStationId == 0
                    || downStationId == 0
                    || distance == 0) {
                throw new IllegalArgumentException("Cannot create LineRequest");
            }

            return new LineRequest(name, color, upStationId, downStationId, distance);
        }
    }
}
