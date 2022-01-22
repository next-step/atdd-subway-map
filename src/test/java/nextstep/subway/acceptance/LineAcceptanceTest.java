package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.LineAcceptanceUtil.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineAcceptanceUtil.지하철_노선_수정_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {

        // given
        지하철_노선_생성_요청("신분당선", "bg-red-600");

        // given
        지하철_노선_생성_요청("2호선", "bg-red-600");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all().extract();

        // then
        assertThat(response.jsonPath().getList("name")).containsExactly("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {

        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");
        String location = response.header("location");

        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then().log().all().extract();

        // then
        assertThat(response2.jsonPath().getString("name")).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "bg-red-600");
        String location = response.header("location");

        // given
        지하철_노선_수정_요청(location, "구분당선", "bg-blue-600");


        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response2.jsonPath().getString("color")).isEqualTo("bg-blue-600"),
                () -> assertThat(response2.jsonPath().getString("name")).isEqualTo("구분당선")
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");
        String location = response.header("location");

        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given()
                .when()
                .delete(location)
                .then().log().all().extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    /*
       Given 지하철 노선 생성을 요청 하고
       When 같은 이름으로 지하철 노선 생성을 요청 하면
       Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicatedLine() {

        // given
        지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
