package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonStationAcceptance.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        Map<String, String> params = getParamsStationMap("뚝섬역");
        ExtractableResponse<Response> response = 지하철역_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("location")).isNotBlank();
        assertThat(response.jsonPath().<String>get("name")).isEqualTo("뚝섬역");
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
        Map<String, String> params = getParamsStationMap("뚝섬역");
        지하철역_생성_요청(params);

        //given
        Map<String, String> params2 = getParamsStationMap("연신내역");
        지하철역_생성_요청(params2);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        List<String> names = response.jsonPath().getList("name");
        assertThat(names).containsExactly("뚝섬역", "연신내역");
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
        Map<String, String> params = getParamsStationMap("뚝섬역");
        ExtractableResponse<Response> response
                                = 지하철역_생성_요청(params);

        // when
        response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(response.header("location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //when
        response = 지하철역_조회_요청();

        //then
        assertThat(response.jsonPath().getList("name")).isEmpty();
    }

    /**
     * Scenario: 중복이름으로 지하철역 생성
     *  Given 지하철역 생성을 요청 하고
     *  When 같은 이름으로 지하철역 생성을 요청 하면
     *  Then 지하철역 생성이 실패한다.
     */
    @Test
    @DisplayName("지하철역 중복 이름으로 생성 불가")
    void duplicated_station_name_interdict() {
        //given
        Map<String, String> 뚝섬역 = getParamsStationMap("뚝섬역");
        지하철역_생성_요청(뚝섬역);

        //when
        ExtractableResponse<Response> response
                        = 지하철역_생성_요청(뚝섬역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("중복된 지하철역을 생성할 수 없습니다.");
    }

}
