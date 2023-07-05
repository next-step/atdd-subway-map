package subway.subway.application.in;

import subway.subway.application.in.command.StationRegisterCommand;
import subway.subway.application.query.StationResponse;

public interface StationRegisterUsecase {
    StationResponse saveStation(StationRegisterCommand command);

}
