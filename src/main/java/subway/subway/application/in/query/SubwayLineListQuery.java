package subway.subway.application.in.query;

import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.List;

public interface SubwayLineListQuery {

    List<SubwayLineResponse> findAll();
}
