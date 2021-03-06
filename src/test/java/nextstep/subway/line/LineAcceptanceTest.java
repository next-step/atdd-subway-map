package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
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

    private static final String SHINBUNDANG_LINE = "SBD";
    private static final String KANGNAM_LINE = "KN";

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        ExtractableResponse<Response> response = registerLine(SHINBUNDANG_LINE);
        String uri = getResponseURI(response);

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(uri).isNotBlank();
        assertThat(SHINBUNDANG_LINE).isEqualTo(response.jsonPath().get("name"));
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse1 = registerLine(SHINBUNDANG_LINE);
        ExtractableResponse<Response> createResponse2 = registerLine(KANGNAM_LINE);
        String uri = getResponseURI(createResponse1).split("/")[1];

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = executeAction(HttpMethod.GET, uri,null);

        // then
        // 지하철_노선_목록_응답됨
        assertHttpStatusOK(response);

        // 지하철_노선_목록_포함됨
        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map(list -> Long.parseLong(list.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> responseLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(list -> list.getId())
                .collect(Collectors.toList());

        assertThat(responseLineIds).containsAll(expectedLineIds);

    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse1 = registerLine(SHINBUNDANG_LINE);
        String uri = getResponseURI(createResponse1);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = executeAction(HttpMethod.GET, uri,null);

        // then
        // 지하철_노선_응답됨
        assertHttpStatusOK(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = registerLine(SHINBUNDANG_LINE);
        String uri = getResponseURI(createResponse);

        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("color","bg-blue-600");
        updateParam.put("name", "구분당선");

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> response = executeAction(HttpMethod.PUT, uri, updateParam);

        // then
        // 지하철_노선_수정됨
        assertHttpStatusOK(response);
        assertThat(uri.split("/")[2]).isEqualTo(response.jsonPath().get("id"));
        assertThat(updateParam.get("name")).isEqualTo(response.jsonPath().get("name"));
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = registerLine(SHINBUNDANG_LINE);
        String uri = getResponseURI(createResponse);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = executeAction(HttpMethod.DELETE, uri, null);

        // then
        // 지하철_노선_삭제됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /** 중복코드 리팩터링 */
    private ExtractableResponse<Response> registerLine(String stationAlias){

        ExtractableResponse<Response> createResponse;
        Map<String, String> param1 = new HashMap<>();

        if(stationAlias==null|| SHINBUNDANG_LINE.equals(stationAlias)) {
            param1.put("color", "bg-red-600");
            param1.put("name", "신분당선");
        }else{
            param1.put("color", "bg-green-600");
            param1.put("name", "강남역");
        }

        createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return createResponse;
    }

    private String getResponseURI(ExtractableResponse<Response> response){
        if(response!=null) {
            return response.header("Location");
        }else
            return null;
    }

    private ExtractableResponse<Response> executeAction(HttpMethod expectedAction, String uri, Map<String, String> param){

        RequestSpecification givenLog = RestAssured.given().log().all();

        Response response;
        if(HttpMethod.GET.equals(expectedAction)) {
            response = givenLog.when().get(uri);
        }else if(HttpMethod.DELETE.equals(expectedAction)){
            response = givenLog.when().delete(uri);
        }else if(HttpMethod.PUT.equals(expectedAction)){
            response =  givenLog.body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .put(uri);
        }else{
            response = new RestAssuredResponseImpl();
        }

        return response.then().log().all().extract();
    }

    private void assertHttpStatusOK(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
