package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;
import subway.subway.domain.SubwaySections;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineJpaMapper {

    public SubwayLineJpa from(SubwayLine subwayLine) {
        return new SubwayLineJpa(
                subwayLine.isNew() ? null : subwayLine.getId().getValue(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getStartStationId().getValue(),
                from(subwayLine.getSections()));
    }

    private List<SubwaySectionJpa> from(List<SubwaySection> subwaySections) {
        return subwaySections.stream()
                .map(this::from)
                .collect(Collectors.toList());
    }
    private SubwaySectionJpa from(SubwaySection subwaySection) {
        return new SubwaySectionJpa(
                subwaySection.getUpStationId().getValue(),
                subwaySection.getUpStationName(),
                subwaySection.getDownStationId().getValue(),
                subwaySection.getDownStationName(),
                subwaySection.getDistance().getValue());
    }

}
