package subway.line.dto.response;

import subway.line.domain.Stations;

public class StationsResponse {

    private final Long upStationId;

    private final Long downStationId;


    public StationsResponse(Long upStationId, Long downStationId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public static StationsResponse of(Stations stations) {
        return new StationsResponse(stations.getUpStation().getId(), stations.getDownStation().getId());
    }

}
