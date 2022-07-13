package nextstep.subway.applicaion.mapper.domain;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper implements DomainMapper<SectionRequest, Section> {

    @Override
    public Section map(SectionRequest sectionRequest) {
        return this.map(null, sectionRequest);
    }

    public Section map(Long lineId, SectionRequest sectionRequest) {
        return Section.builder()
                .lineId(lineId)
                .upStationId(sectionRequest.getUpStationId())
                .downStationId(sectionRequest.getDownStationId())
                .distance(sectionRequest.getDistance())
                .build();
    }
}
