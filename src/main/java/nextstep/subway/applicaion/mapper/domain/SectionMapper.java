package nextstep.subway.applicaion.mapper.domain;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper implements DomainMapper<SectionRequest, Section> {

    @Override
    public Section map(SectionRequest sectionRequest) {
        return Section.builder()
                .upStationId(sectionRequest.getUpStationId())
                .downStationId(sectionRequest.getDownStationId())
                .distance(sectionRequest.getDistance())
                .build();
    }
}
