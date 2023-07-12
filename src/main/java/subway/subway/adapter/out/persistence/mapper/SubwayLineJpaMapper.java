package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineJpaMapper {

    public SubwayLineJpa toSubwayLineJpa(SubwayLine subwayLine) {
        return new SubwayLineJpa(
                subwayLine.isNew() ? null : subwayLine.getId().getValue(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getStartStationId().getValue(),
                List.of(toSubwaySectionJpa(subwayLine)));
    }

    private SubwaySectionJpa toSubwaySectionJpa(SubwayLine subwayLine) {
        Station.Id startStationId = subwayLine.getStartStationId();

        return new SubwaySectionJpa(
                startStationId.getValue(),
                subwayLine.getUpStationName(startStationId),
                subwayLine.getDownStationId(startStationId).getValue(),
                subwayLine.getDownStationName(startStationId),
                subwayLine.getSectionDistance(startStationId).getValue());
    }

    public SubwayLineResponse toSubwayLineResponse(SubwayLineJpa subwayLineJpa) {
        return new SubwayLineResponse(
                subwayLineJpa.getId(),
                subwayLineJpa.getName(),
                subwayLineJpa.getColor(),
                subwayLineJpa
                        .getSubwaySections()
                        .stream()
                        .flatMap(section -> toStationResponseList(section).stream()).collect(Collectors.toList()));
    }

    private List<SubwayLineResponse.StationInfo> toStationResponseList(SubwaySectionJpa subwaySectionJpa) {
        return List.of(toStationResponse(subwaySectionJpa.getStartStationId(), subwaySectionJpa.getStartStationName()),
                toStationResponse(subwaySectionJpa.getEndStationId(), subwaySectionJpa.getEndStationName()));
    }

    private SubwayLineResponse.StationInfo toStationResponse(Long id, String name) {
        return new SubwayLineResponse.StationInfo(id, name);
    }
}
