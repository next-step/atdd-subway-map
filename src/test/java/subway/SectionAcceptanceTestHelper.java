package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.error.exception.ErrorCode;

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
        final String errorMessage = 에러_메세지_가져오기(response);
        assertThat(errorMessage).isEqualTo(ErrorCode.CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION.getMessage());

        노선에_찾으려고하는_역이_없는지_확인(lineId, stationName);
    }

    static void 노선에_이미_존재하는_역에_대한_구간_등록_실패를_확인(final ExtractableResponse<Response> response) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String errorMessage = 에러_메세지_가져오기(response);
        assertThat(errorMessage).isEqualTo(ErrorCode.CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE.getMessage());
    }

    static ExtractableResponse<Response> 구간_제거_요청(final long lineId, final long stationId) {
        return RestAssured
                .given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    static void 구간이_정상적으로_제거되었는지_확인(final ExtractableResponse<Response> response, final long lienId, final String stationName) {
        응답_코드_검증(response, HttpStatus.NO_CONTENT);
        노선에_찾으려고하는_역이_없는지_확인(lienId, stationName);
    }

    static void 마지막_구간이_아닌_구간_삭제에_대한_실패를_확인(final ExtractableResponse<Response> response, final long lineId, final String stationName) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String errorMessage = 에러_메세지_가져오기(response);
        assertThat(errorMessage).isEqualTo(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_LAST_SECTION.getMessage());

        노선에_찾으려고하는_역이_있는지_확인(lineId, stationName);
    }

    static void 마지막으로_남은_구간_삭제에_대한_실패를_확인(final ExtractableResponse<Response> response, final long lineId, final String stationName) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String errorMessage = 에러_메세지_가져오기(response);
        assertThat(errorMessage).isEqualTo(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_LAST_REMAINING_SECTION.getMessage());

        노선에_찾으려고하는_역이_있는지_확인(lineId, stationName);
    }

    static void 노선에_존재하지_않는_역_삭제에_대한_실패를_확인(final ExtractableResponse<Response> response) {
        응답_코드_검증(response, HttpStatus.UNPROCESSABLE_ENTITY);
        final String errorMessage = 에러_메세지_가져오기(response);
        assertThat(errorMessage).isEqualTo(ErrorCode.CANNOT_REMOVE_SECTION_WHAT_IS_NOT_EXISTS_STATION_IN_LINE.getMessage());
    }

    private static void 노선에_찾으려고하는_역이_있는지_확인(final long lineId, final String stationName) {
        final ExtractableResponse<Response> 노선_상세_조회_응답 = 노선_상세_조회함(lineId);
        final List<String> 노선에_등록되어있는_역_이름_목록 = 노선_상세_조회_응답.jsonPath().getList("stations.name", String.class);
        assertThat(노선에_등록되어있는_역_이름_목록).containsAnyOf(stationName);
    }

    private static void 노선에_찾으려고하는_역이_없는지_확인(final long lineId, final String stationName) {
        final ExtractableResponse<Response> 노선_상세_조회_응답 = 노선_상세_조회함(lineId);
        final List<String> 노선에_등록되어있는_역_이름_목록 = 노선_상세_조회_응답.jsonPath().getList("stations.name", String.class);
        assertThat(노선에_등록되어있는_역_이름_목록).doesNotContain(stationName);
    }
}
