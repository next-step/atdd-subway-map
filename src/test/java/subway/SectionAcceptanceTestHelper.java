package subway;

import common.exception.ErrorCode;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineAcceptanceTestHelper.노선_상세_조회함;

public class SectionAcceptanceTestHelper extends AcceptanceTestHelper {

    static ExtractableResponse<Response> 구간_등록_요청(final long lineId, final long upStationId, final long downStationId, final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    static void 구간이_정상적으로_등록되었는지_확인(final ExtractableResponse<Response> response, final long lineId, final String stationName) {
        응답_코드_검증(response, HttpStatus.OK);
        final ExtractableResponse<Response> 노선_상세_조회_응답 = 노선_상세_조회함(lineId);
        final List<String> 노선에_등록되어있는_역_이름_목록 = 노선_상세_조회_응답.jsonPath().getList("stations.name", String.class);
        assertThat(노선에_등록되어있는_역_이름_목록).containsAnyOf(stationName);
    }

    static void 구간_등록함(final long lineId, final long upStationId, final long downStationId, final int distance) {
        final ExtractableResponse<Response> response = 구간_등록_요청(lineId, upStationId, downStationId, distance);
        응답_코드_검증(response, HttpStatus.OK);
    }

    static void 하행_종점역을_상행역으로_하는_구간_등록_실패를_확인(final ExtractableResponse<Response> response, final long lineId, final String stationName) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String error_message = 에러_메세지_가져오기(response);
        assertThat(error_message).isEqualTo(ErrorCode.CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION.getMessage());

        노선에_구간이_등록되지_않았는지_확인(lineId, stationName);
    }

    static void 노선에_이미_존재하는_역에_대한_구간_등록_실패를_확인(final ExtractableResponse<Response> response, final long lineId, final String stationName) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String error_message = 에러_메세지_가져오기(response);
        assertThat(error_message).isEqualTo(ErrorCode.CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE.getMessage());

        노선에_구간이_등록되지_않았는지_확인(lineId, stationName);
    }

    private static void 노선에_구간이_등록되지_않았는지_확인(final long lineId, final String stationName) {
        final ExtractableResponse<Response> 노선_상세_조회_응답 = 노선_상세_조회함(lineId);
        final List<String> 노선에_등록되어있는_역_이름_목록 = 노선_상세_조회_응답.jsonPath().getList("stations.name", String.class);
        assertThat(노선에_등록되어있는_역_이름_목록).doesNotContain(stationName);
    }
}
