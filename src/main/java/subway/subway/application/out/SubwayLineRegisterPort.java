package subway.subway.application.out;

import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.List;

public interface SubwayLineRegisterPort {
    SubwayLineResponse register(SubwayLine subwayLine);
}
