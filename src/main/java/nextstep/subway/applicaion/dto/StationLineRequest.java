package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class StationLineRequest {
    public static class PostRequest {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;

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

        public Line toEntity() {
            return new Line(name, color, upStationId, downStationId);
        }
    }


    public static class PatchRequest {
        private final String  name;
        private final String color;

        public PatchRequest(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        public Line toEntity() {
            return new Line(name, color);
        }
    }

}
