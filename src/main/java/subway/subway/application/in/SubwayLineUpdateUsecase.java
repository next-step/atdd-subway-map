package subway.subway.application.in;

import subway.subway.application.in.command.SubwayLineUpdateCommand;

public interface SubwayLineUpdateUsecase {

    void updateSubwayLine(SubwayLineUpdateCommand command);
}
