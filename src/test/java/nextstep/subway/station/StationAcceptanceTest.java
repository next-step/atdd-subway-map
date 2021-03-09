package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.SoftAssertions;
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
import static org.springframework.http.HttpStatus.OK;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/stations";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        // 지하철_역_생성_요청
        ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        // 지하철_역_생성됨
        지하철_역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성하면 실패한다.")
    @Test
    void createStationWithDuplicateName() {
        String name = "강남역";

        // given
        // 지하철_역_등록되어_있음
        지하철_역_등록되어_있음(name);

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(name);

        // then
        지하철_역_생성_실패됨(response);
    }

    @DisplayName("지하철 역 목록을 조회한다.")
    @Test
    void getStations() {
        String name1 = "강남역";
        String name2 = "역삼역";

        // given
        // 지하철_역_등록되어_있음
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성_요청(name1);
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성_요청(name2);

        // when
        // 지하철_역_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        지하철_역_목록_응답됨(response,
                Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        // 지하철_역_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청("강남역");

        // when
        // 지하철_역_제거_요청
        ExtractableResponse<Response> response = 지하철_역_제거_요청(createResponse);

        // then
        // 지하철_역_제거됨
        지하철_역_제거됨(response);
    }

    private Map<String, String> createParam(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return param;
    }

    private ExtractableResponse<Response> 지하철_역_생성_요청(String name) {
        return RestAssured.given().log().all()
                .body(createParam(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(PATH)
                .then().log().all()
                .extract();
    }

    private void 지하철_역_생성됨(ExtractableResponse<Response> response) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
        softly.assertThat(response.header("Location"))
                .isNotBlank();
        softly.assertAll();
    }

    private ExtractableResponse<Response> 지하철_역_등록되어_있음(String name) {
        return 지하철_역_생성_요청(name);
    }

    private void 지하철_역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(PATH)
                .then().log().all()
                .extract();
    }

    private void 지하철_역_목록_응답됨(ExtractableResponse<Response> actualResponse, List<ExtractableResponse<Response>> expectedResponses) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualResponse.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = expectedResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = actualResponse.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        softly.assertThat(resultLineIds)
                .containsAll(expectedLineIds);
        softly.assertThat(actualResponse.statusCode())
                .isEqualTo(OK.value());
        softly.assertAll();
    }

    private ExtractableResponse<Response> 지하철_역_제거_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철_역_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
