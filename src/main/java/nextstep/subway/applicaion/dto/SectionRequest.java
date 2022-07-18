package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

import javax.validation.constraints.Min;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostRequest {
        @Min(value = 1L, message = "downStationId is Required value")
        private Long downStationId;
        @Min(value = 1L, message = "upStationId is Required value")
        private Long upStationId;
        private Integer distance;

        public Section toEntity() {
            return new Section(downStationId, upStationId, distance);
        }

        public Section toEntity(Line line) {
            return new Section(line, upStationId, downStationId, distance);
        }
    }

}
