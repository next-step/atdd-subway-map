package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.domain.*;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SubwayLineMapper {

    public SubwayLine mapFrom(SubwayLineJpa subwayLineJpa) {
        return SubwayLine.of(
                new SubwayLine.Id(Objects.requireNonNull(subwayLineJpa.getId())),
                subwayLineJpa.getName(),
                subwayLineJpa.getColor(),
                subwayLineJpa.getSubwaySections()
                        .stream()
                        .map(this::mapFrom)
                        .collect(Collectors.toList()));
    }

    private SubwaySection mapFrom(SubwaySectionJpa sectionJpa) {
        return SubwaySection.of(
                new SubwaySection.Id(Objects.requireNonNull(sectionJpa.getId())),
                new SubwaySectionStation(new Station.Id(sectionJpa.getStartStationId()), sectionJpa.getStartStationName()),
                new SubwaySectionStation(new Station.Id(sectionJpa.getEndStationId()), sectionJpa.getEndStationName()),
                Kilometer.of(sectionJpa.getDistance()));
    }
}
