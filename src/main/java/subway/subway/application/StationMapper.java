package subway.subway.application;

import org.springframework.stereotype.Component;
import subway.subway.application.in.command.StationRegisterCommand;
import subway.subway.domain.Station;
import subway.subway.domain.StationInfo;

@Component
public class StationMapper {
    public StationInfo toMap(StationRegisterCommand command) {
        return new StationInfo(command.getName());
    }
}
