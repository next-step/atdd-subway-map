package nextstep.subway.applicaion.mapper.response;

import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionResponseMapper implements ResponseMapper<Section, SectionResponse> {

    @Override
    public SectionResponse map(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .lineId(section.getLineId())
                .upStationId(section.getUpStationId())
                .downStationId(section.getDownStationId())
                .distance(section.getDistance())
                .build();
    }
}
