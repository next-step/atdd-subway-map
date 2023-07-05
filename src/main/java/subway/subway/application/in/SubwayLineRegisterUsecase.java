package subway.subway.application.in;

import subway.subway.application.in.command.SubwayLineRegisterCommand;
import subway.subway.application.query.SubwayLineResponse;

public interface SubwayLineRegisterUsecase {

    SubwayLineResponse registerSubwayLine(SubwayLineRegisterCommand command);
}
