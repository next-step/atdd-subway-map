package subway.common;

import org.apache.commons.lang3.RandomStringUtils;
import subway.controller.station.StationResponse;

public class Section {
    public static class RequestBody {
        public Long upStation;
        public Long downStation;
        public Long distance;

        private RequestBody(Long upStation, Long downStation, Long distance) {
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
        }
    }

    public static Section.RequestBody REQUEST_BODY() {
        final String name = RandomStringUtils.randomAlphanumeric(10);
        final String color = RandomStringUtils.randomAlphanumeric(10);
        final Long distance = 10L;

        StationResponse 상행역 = Station.랜덤역생성();
        StationResponse 하행역 = Station.랜덤역생성();

        return new Section.RequestBody(상행역.getId(), 하행역.getId(), distance);
    }
    public static Section.RequestBody REQUEST_BODY(Long upStationId, Long downStationId) {
        final String name = RandomStringUtils.randomAlphanumeric(10);
        final String color = RandomStringUtils.randomAlphanumeric(10);
        final Long distance = 10L;
        return new Section.RequestBody(upStationId, downStationId, distance);
    }
}
