package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineJpaMapper {

    public SubwayLineJpa mapFrom(SubwayLine subwayLine) {
        return new SubwayLineJpa(
                subwayLine.isNew() ? null : subwayLine.getId().getValue(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getSectionList()
                        .stream()
                        .map(this::mapFrom)
                        .collect(Collectors.toList()));
    }

    private SubwaySectionJpa mapFrom(SubwaySection subwaySection) {
        return new SubwaySectionJpa(
                subwaySection.isNew() ? null : subwaySection.getId().getValue(),
                subwaySection.getStartStationId().getValue(),
                subwaySection.getStartStationName(),
                subwaySection.getEndStationId().getValue(),
                subwaySection.getEndStationName(),
                subwaySection.getDistance().getValue());
    }

    public SubwayLineResponse mapFrom(SubwayLineJpa subwayLineJpa) {
        return new SubwayLineResponse(
                subwayLineJpa.getId(),
                subwayLineJpa.getName(),
                subwayLineJpa.getColor(),
                subwayLineJpa
                        .getSubwaySections()
                        .stream()
                        .flatMap(section -> mapFrom(section).stream()).collect(Collectors.toList()));
    }

    private List<SubwayLineResponse.StationResponse> mapFrom(SubwaySectionJpa subwaySectionJpa) {
        return List.of(mapFrom(subwaySectionJpa.getStartStationId(), subwaySectionJpa.getStartStationName()),
                mapFrom(subwaySectionJpa.getEndStationId(), subwaySectionJpa.getEndStationName()));
    }

    private SubwayLineResponse.StationResponse mapFrom(Long id, String name) {
        return new SubwayLineResponse.StationResponse(id, name);
    }
}
