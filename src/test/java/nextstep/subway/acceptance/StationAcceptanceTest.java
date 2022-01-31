package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static nextstep.subway.acceptance.stationstep.StationRequestStep.*;
import static nextstep.subway.acceptance.stationstep.StationValidateStep.*;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 역_생성("강남역");

        // then
        역_생성_응답_검증(response, HttpStatus.CREATED);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        // given
        String 역1 = "강남역";
        역_생성(역1);

        // given
        String 역2 = "역삼역";
        역_생성(역2);

        // when
        ExtractableResponse<Response> response = 역_목록_조회();

        // then
        역_목록_조회_응답_검증(response, HttpStatus.OK, 역1, 역2);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 역_생성("강남역");

        // when
        ExtractableResponse<Response> response = 역_삭제(createResponse);

        // then
        역_삭제_응답_검증(response, HttpStatus.NO_CONTENT);
    }

    /**
     *  scenario: 지하철역 이름 중복 생성 금지
     *  given   : 지하철역 생성을 요청하고
     *  when    : 같은 이름의 지하철역 생성을 요청하면
     *  then    : 지하철역 생성이 안된다. (409)
     */
    @DisplayName("지하철역 이름 중복 검증")
    @Test
    void validateStationName() {
        // given
        역_생성("강남역");

        // when
        ExtractableResponse<Response> duplicatedStationResponse = 역_생성("강남역");

        // then
        역_이름_중복_응답_검증(duplicatedStationResponse, HttpStatus.CONFLICT);
    }
}
