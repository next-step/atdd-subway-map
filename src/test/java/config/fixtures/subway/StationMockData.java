package config.fixtures.subway;

import subway.dto.StationRequest;
import subway.entity.Station;

import java.util.List;

public class StationMockData {

    public static final StationRequest 가산디지털단지 = new StationRequest("가산디지털단지");
    public static final StationRequest 구로디지털단지 = new StationRequest("구로디지털단지");
    public static final StationRequest 강남 = new StationRequest("강남");
    public static final StationRequest 홍대입구 = new StationRequest("홍대입구");
    public static final StationRequest 종각 = new StationRequest("종각");
    public static final StationRequest 신림 = new StationRequest("신림");
    public static final StationRequest 잠실 = new StationRequest("잠실");
    public static final StationRequest 교대 = new StationRequest("교대");
    public static final StationRequest 역삼 = new StationRequest("역삼");
    public static final StationRequest 서울역 = new StationRequest("서울역");
    public static final List<StationRequest> 역_10개 =
            List.of(가산디지털단지, 구로디지털단지, 강남, 홍대입구, 종각, 신림, 잠실, 교대, 역삼, 서울역);
}
