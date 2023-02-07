package subway.station;

import subway.common.Endpoints;
import subway.station.presentation.StationRequest;
import subway.utils.RestAssuredClient;

public class StationFixtures {
    public static final StationRequest 강남역_생성_요청= new StationRequest("강남역");
    public static final StationRequest 서울대입구역_생성_요청= new StationRequest("서울대입구역");
    public static final StationRequest 신논현역_생성_요청= new StationRequest("신논현역");
    public static final StationRequest 낙성대역_생성_요청= new StationRequest("낙성대역");

    public static long 지하철역을_생성한다(Object stationRequest) {
        return RestAssuredClient.post(Endpoints.STATIONS, stationRequest)
                .jsonPath().getLong("id");
    }
}
