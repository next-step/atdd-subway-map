package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StationLineRequest {
    private StationLineRequest() {}
    @Getter
    public static class PostRequest {
        @NotBlank(message = "name is required")
        private String name;
        @NotBlank(message = "color is required")
        private String color;
        @Min(message = "upStationId is Minimum is [1L]", value = 1L)
        private Long upStationId;
        @Min(message = "downStationId Minimum is [1L]", value = 1L)
        private Long downStationId;
        @Min(message = "distance Minimum is [1]", value = 1)
        private Integer distance;

        public Line toEntity() {
            return new Line(name, color, upStationId, downStationId, distance);
        }
    }


    @Getter
    public static class PutRequest {
        private final String  name;
        private final String color;

        public PutRequest(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public Line toEntity() {
            return new Line(name, color);
        }
    }

}
