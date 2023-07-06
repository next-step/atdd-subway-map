package subway.subway.application.out;

import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.Optional;

public interface SubwayLineLoadPort {
    SubwayLine findOne(SubwayLine.Id id);
}
