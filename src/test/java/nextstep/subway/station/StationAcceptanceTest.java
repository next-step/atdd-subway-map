package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    Map<String, String> 파라미터_생성(String name){
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return param;
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성_및_확인() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        return RestAssured.given().log().all()
                .body(파라미터_생성(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public Long 생성된_지하철역_ID_가져오기(ExtractableResponse<Response> createResponse){
        return Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void 지하철역_생성시_중복안되게_처리() {
        // given
        ExtractableResponse<Response> response1 = 지하철역_생성_요청("강남역");

        // when
        ExtractableResponse<Response> response2 = 지하철역_생성_요청("강남역");

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void 지하철역_목록_조회() {
        /// given
        ExtractableResponse<Response> createResponse1 =
                지하철역_생성_요청("강남역");

        ExtractableResponse<Response> createResponse2 =
                지하철역_생성_요청("역삼역");

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_결과_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = 지하철역_목록_예상_아이디_리스트(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_노선_목록_결과_아이디_리스트(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    ExtractableResponse<Response> 지하철역_목록_조회_결과_요청(){
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    List<Long> 지하철역_목록_예상_아이디_리스트(ExtractableResponse<Response>... responses){
        return Arrays.stream(responses)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    List<Long> 지하철_노선_목록_결과_아이디_리스트(ExtractableResponse<Response> response){
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void 지하철역_삭제() {
        // given
        ExtractableResponse<Response> createResponse =
                지하철역_생성_요청("강남역");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    ExtractableResponse<Response> 지하철역_삭제_요청(String uri){
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
