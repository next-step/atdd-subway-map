package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Section;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SectionResponse {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public static SectionResponse form(Section section) {
        return new SectionResponse(section.getId(), section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }
}
