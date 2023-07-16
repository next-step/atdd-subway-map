package subway.subway.application.out.query;

import subway.subway.application.query.StationResponse;

import java.util.List;

public interface StationListQueryPort {
    List<StationResponse> findAll();
}
