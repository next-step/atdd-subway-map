package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

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

        public Line toEntity() {
            return new Line(name, color);
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
            return new Line(id, name, color);
        }
    }

}
