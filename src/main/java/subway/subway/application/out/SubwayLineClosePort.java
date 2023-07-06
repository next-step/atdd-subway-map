package subway.subway.application.out;

import subway.subway.domain.SubwayLine;

public interface SubwayLineClosePort {

    void closeSubwayLine(SubwayLine.Id id);
}
