package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.common.CommonSteps.서버_에러_응답;
import static nextstep.subway.acceptance.line.LineSteps.*;
import static nextstep.subway.acceptance.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성_테스트() {
        // when 지하철역을 생성하면
        ExtractableResponse<Response> response = 지하철역_생성(GANGNAM_STATION_NAME);
        노선_생성_검증(response);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void 지하철역_목록_조회_테스트() {
        // given 2개의 지하철역을 생성하고
        String firstStationName = GANGNAM_STATION_NAME;
        String secondStationName = YUKSAM_STATION_NAME;
        지하철역_생성(firstStationName);
        지하철역_생성(secondStationName);

        // when 지하철역 목록을 조회하면
        List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name");

        // then 2개의 지하철역을 응답 받는다
        assertAll(
                // then 지하철역이 생성된다
                () -> assertThat(stationNames.size()).isEqualTo(2),
                // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
                () -> assertThat(stationNames).containsAnyOf(firstStationName),
                () -> assertThat(stationNames).containsAnyOf(secondStationName)
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제한다.")
    @Test
    void 지하철역_삭제_테스트() {
        // 지하철역 생성
        ExtractableResponse<Response> createResponse = 지하철역_생성(GANGNAM_STATION_NAME);
        // 지하철역 삭제
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(createResponse.jsonPath().getLong("id"));
        // 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        역_삭제_검증(GANGNAM_STATION_NAME);
    }

    /**
     * Given 지하철역을 생성하고
     * And 생성된 지하철역으로 노선을 생성하고 구간을 생성한다.
     * When 그 지하철역을 삭제하면
     * Then 구간으로 등록된 역을 삭제할 수 없습니다. 라는 메시지를 응답받는다.
     */
    @DisplayName("구간으로 등록된 지하철역 삭제시 실패")
    @Test
    void 구간으로_등록된_지하철역_삭제시_실패_테스트() {
        // Given 지하철역 생성
        ExtractableResponse<Response> 노선 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        // 지하철역 삭제
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(노선.jsonPath().getLong("id"));
        // 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        Throwable exception = Assertions.assertThrows((Throwable.class), () -> {
            throw new IllegalArgumentException("station.used.section");
        });

        assertAll(
                // Then 서버 에러를 응답 받는다.
                () -> 서버_에러_응답(deleteResponse),
                // Then 구간의 삭제는 하행역만 가능합니다.
                () -> assertEquals("station.used.section", exception.getMessage())
        );
    }

}