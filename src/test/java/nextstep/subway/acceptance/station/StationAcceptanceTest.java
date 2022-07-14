package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.station.StationRestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 신림역 = "신림역";
    private static final String 신도림역 = "신도림역";
    private static final String STATION_NAME = "name";
    private static final String STATION_ID = "id";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when & then 지하철역을 생성하면 지하철역이 생성된다
        ExtractableResponse<Response> 강남역_생성 = 지하철역_생성(강남역);
        assertThat(강남역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        assertThat(stationNames).containsAnyOf(강남역);
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStation() {
        // given 2개의 지하철역 생성하고
        ExtractableResponse<Response> 신림역_생성 = 지하철역_생성(신림역);
        assertThat(신림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> 신도림역_생성 = 지하철역_생성(신도림역);
        assertThat(신도림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 지하철역 목록을 조회하면
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        // then 생성한 두개의 지하철역을 응답 받는다.
        assertAll(
            () -> assertThat(stationNames).hasSize(2),
            () -> assertThat(stationNames).contains(신림역),
            () -> assertThat(stationNames).contains(신도림역)
        );
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철 역을 제거한다.")
    void deleteStation() {
        // given 지하철역을 생성하고
        ExtractableResponse<Response> 신림역_생성 = 지하철역_생성(신림역);
        assertThat(신림역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 그 지하철역을 삭제하면
        String 신림역_ID = 신림역_생성.jsonPath().getString(STATION_ID);
        ExtractableResponse<Response> 지하철역_삭제 = 지하철역_삭제(신림역_ID);
        assertThat(지하철역_삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        ExtractableResponse<Response> 지하철역_조회 = 지하철역_조회();
        assertThat(지하철역_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 지하철역_이름_조회(지하철역_조회);
        assertThat(stationNames).doesNotContain(신림역);
    }

    private List<String> 지하철역_이름_조회(ExtractableResponse<Response> response) {
        return response
            .jsonPath().getList(STATION_NAME, String.class);
    }
}
