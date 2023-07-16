package subway.subway.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.rds_module.entity.SubwaySectionJpa;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayLineResponseMapper {

    public SubwayLineResponse from(SubwayLineJpa subwayLineJpa) {
        return new SubwayLineResponse(
                subwayLineJpa.getId(),
                subwayLineJpa.getName(),
                subwayLineJpa.getColor(),
                subwayLineJpa
                        .getSubwaySections()
                        .stream()
                        .flatMap(section -> from(section).stream()).collect(Collectors.toList()));
    }

    private List<SubwayLineResponse.StationInfo> from(SubwaySectionJpa subwaySectionJpa) {
        return List.of(from(subwaySectionJpa.getUpStationId(), subwaySectionJpa.getUpStationName()),
                from(subwaySectionJpa.getDownStationId(), subwaySectionJpa.getDownStationName()));
    }

    private SubwayLineResponse.StationInfo from(Long id, String name) {
        return new SubwayLineResponse.StationInfo(id, name);
    }
}
