package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationSectionRequest;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import nextstep.subway.domain.StationSection;
import org.springframework.stereotype.Component;

@Component
public class StationSectionMapper {
    public StationSection of(StationSectionRequest request) {
        return new StationSection(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
    }

    public StationSectionResponse of(StationSection stationSection) {
        return new StationSectionResponse(
                stationSection.getId(),
                stationSection.getUpStationId(),
                stationSection.getDownStationId(),
                stationSection.getDistance()
        );
    }
}
