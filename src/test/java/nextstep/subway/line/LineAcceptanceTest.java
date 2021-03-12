package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.StationHelper;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    Long stationId1;
    Long stationId2;
    final int DISTANCE = 5;

    @BeforeEach
    void 미리_역_생성(){
        ExtractableResponse<Response> createStation1 = StationHelper.지하철역_생성_요청("강남");
        ExtractableResponse<Response> createStation2 = StationHelper.지하철역_생성_요청("선릉");
        stationId1 = StationHelper.생성된_지하철역_ID_가져오기(createStation1);
        stationId2 = StationHelper.생성된_지하철역_ID_가져오기(createStation2);
    }


    Map<String, String> 파라미터_생성(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);

        return param;
    }

    ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, int distance) {
        return RestAssured.given()
                .body(파라미터_생성(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성_요청_및_확인() {
        // when
        ExtractableResponse<Response> response =
                LineHelper.지하철_노선_생성_요청("선릉", "green darken-1");

        // then
        지하철_노선_생성_요청_응답됨(response);
    }

    void 지하철_노선_생성_요청_응답됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음

        ExtractableResponse<Response> createStation3 = StationHelper.지하철역_생성_요청("대청");
        ExtractableResponse<Response> createStation4 = StationHelper.지하철역_생성_요청("일원");
        Long stationId3 = StationHelper.생성된_지하철역_ID_가져오기(createStation3);
        Long stationId4 = StationHelper.생성된_지하철역_ID_가져오기(createStation4);

        ExtractableResponse<Response> createResponse1 =
                LineHelper.지하철_노선_생성_요청("1호선", "green darken-1");

        ExtractableResponse<Response> createResponse2 =
                LineHelper.지하철_노선_생성_요청("2호선", "green darken-2");

        // when
        ExtractableResponse<Response> getResponses = 지하철_노선_목록_조회_결과_요청();

        // then
        지하철_노선_목록_응답됨(getResponses);

        List<LineResponse> lineResponses = 지하철_노선_목록_조회_결과_리스트(getResponses);
        지하철_노선_목록_리스트_사이즈_확인(lineResponses);

        List<Long> expectedLineIds = 지하철_노선_목록_예상_아이디_리스트(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_노선_목록_결과_아이디_리스트(getResponses);
        지하철_노선_목록이_예상목록_포함하는지_확인(expectedLineIds, resultLineIds);
    }

    void 지하철_노선_목록_응답됨(ExtractableResponse<Response> getResponses){assertThat(getResponses.statusCode()).isEqualTo(HttpStatus.OK.value());}
    void 지하철_노선_목록_리스트_사이즈_확인(List<LineResponse> lineResponses){assertThat(lineResponses.size()).isEqualTo(2);}
    void 지하철_노선_목록이_예상목록_포함하는지_확인(List<Long> expectedLineIds, List<Long> resultLineIds){assertThat(resultLineIds).containsAll(expectedLineIds);}

    ExtractableResponse<Response> 지하철_노선_목록_조회_결과_요청(){
        return RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }

    List<LineResponse> 지하철_노선_목록_조회_결과_리스트(ExtractableResponse<Response> getResponses){
        return getResponses.jsonPath().getList(".", LineResponse.class);
    }

    List<Long> 지하철_노선_목록_예상_아이디_리스트(ExtractableResponse<Response>... responses){
        return Arrays.stream(responses)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    List<Long> 지하철_노선_목록_결과_아이디_리스트(ExtractableResponse<Response> getResponses){
        return 지하철_노선_목록_조회_결과_리스트(getResponses).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        // when
        ExtractableResponse<Response> getResponse = LineHelper.지하철_노선_조회_요청(id);

        // then
        지하철_노선_응답됨(getResponse);
    }

    void 지하철_노선_응답됨(ExtractableResponse<Response> getResponse){assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());}

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        // when
        Map<String, String> param = LineHelper.파라미터_생성("선정릉", "red darken-1");
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(id, param);

        // then
        지하철_노선_수정_확인(param, updateResponse);
    }

    ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, Map<String, String> param){
        return RestAssured.given()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().put("/lines/" + id)
                .then().log().all().extract();
    }

    void 지하철_노선_수정_확인(Map<String, String> param, ExtractableResponse<Response> updateResponse){
        assertThat(updateResponse.jsonPath().getMap(".").get("name"))
                .isEqualTo(param.get("name"));
        assertThat(updateResponse.jsonPath().getMap(".").get("color"))
                .isEqualTo(param.get("color"));
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(id);

        // then
        지하철_노선_삭제_응답됨(deleteResponse);
    }

    ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id){
        return RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all().extract();
    }

    void 지하철_노선_삭제_응답됨(ExtractableResponse<Response> deleteResponse){assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());}
}
