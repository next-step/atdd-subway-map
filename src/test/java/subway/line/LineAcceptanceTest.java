package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리에 대한 인수테스트입니다.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest implements LineFixture {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 노선 목록 조회 시 생성한 노선을 찾을 수 있다.")
    @Test
    void createStationLine() {
        // when
        ExtractableResponse<Response> response = createLineByNameAndStation("신분당선", "지하철1", "지하철2");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.body().jsonPath().getList("name", String.class))
                .containsExactly("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성한 후 지하철 목록 조회가 가능한지 확인한다.")
    @Test
    void getLines() {
        // given
        createLineByNameAndStation("신분당선", "지하철1", "지하철2");
        createLineByNameAndStation("수인분당선", "지하철3", "지하철4");

        // when
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.jsonPath().getList("name", String.class)).hasSize(2)
                .containsExactlyInAnyOrder("수인분당선", "신분당선");
    }

}
