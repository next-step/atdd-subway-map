package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StationLineRequest {
    private StationLineRequest() {}
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
            return Line.builder()
                       .name(name)
                       .color(color)
                       .upStationId(upStationId)
                       .distance(distance)
                       .build();
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

        public Line toEntity(Long id) {
            return Line.builder()
                       .id(id)
                       .name(this.getName())
                       .color(this.getColor())
                       .build();
        }
    }

}
