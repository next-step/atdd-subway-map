package subway.subway.application.in;

import subway.subway.application.query.StationResponse;

import java.util.List;

public interface StationListQuery {
    List<StationResponse> findAll();
}
