package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.domain.*;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SubwayLineMapper {

    public SubwayLine toSubwayLine(SubwayLineJpa subwayLineJpa) {
        return SubwayLine.of(
                new SubwayLine.Id(Objects.requireNonNull(subwayLineJpa.getId())),
                subwayLineJpa.getName(),
                subwayLineJpa.getColor(),
                new Station.Id(subwayLineJpa.getStartSectionId()),
                subwayLineJpa.getSubwaySections()
                        .stream()
                        .map(this::toSubwaySection)
                        .collect(Collectors.toList()));
    }

    private SubwaySection toSubwaySection(SubwaySectionJpa sectionJpa) {
        return SubwaySection.of(
                new SubwaySection.Id(Objects.requireNonNull(sectionJpa.getId())),
                new SubwaySectionStation(new Station.Id(sectionJpa.getUpStationId()), sectionJpa.getUpStationName()),
                new SubwaySectionStation(new Station.Id(sectionJpa.getDownStationId()), sectionJpa.getDownStationName()),
                Kilometer.of(sectionJpa.getDistance()));
    }
}
