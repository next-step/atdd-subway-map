package subway.subway.application.in;

import subway.subway.application.in.command.SubwayLineDetailQueryCommand;
import subway.subway.application.query.SubwayLineResponse;

public interface SubwayLineDetailQuery {

    SubwayLineResponse findOne(SubwayLineDetailQueryCommand command);
}
