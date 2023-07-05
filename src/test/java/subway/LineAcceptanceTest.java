package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.factory.LineFactory;
import subway.utils.RestAssuredClient;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static final String urlPath = "/lines";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // when
        RestAssuredClient.requestPost(urlPath, LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> response = RestAssuredClient.requestGet(urlPath)
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames).contains(LineFactory.LINE_NAMES[0]);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        RestAssuredClient.requestPost(urlPath, LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value());
        RestAssuredClient.requestPost(urlPath, LineFactory.create(LineFactory.LINE_NAMES[2]))
            .statusCode(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> response = RestAssuredClient.requestGet(urlPath)
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames.size()).isEqualTo(2);
        assertThat(lineNames).contains(LineFactory.LINE_NAMES[0], LineFactory.LINE_NAMES[1]);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> creationResponse = RestAssuredClient.requestPost(urlPath,
                LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value()).extract();

        // when
        long lineId = creationResponse.jsonPath().get("id");
        String path = generatePathForId(lineId);
        ExtractableResponse<Response> response = RestAssuredClient.requestGet(path)
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String) response.jsonPath().get("name")).isEqualTo(LineFactory.LINE_NAMES[0]);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> creationResponse = RestAssuredClient.requestPost(urlPath,
                LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value()).extract();

        // when
        long lineId = creationResponse.jsonPath().get("id");
        String path = generatePathForId(lineId);

        String updatedLineName = "수인분당선";
        String updatedLineColor = "bg-yellow-600";
        ExtractableResponse<Response> response =
            RestAssuredClient.requestPut(path,
                    LineFactory.createNameAndColorUpdateParams(updatedLineName, updatedLineColor))
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath responseJsonPath = response.jsonPath();
        assertThat((String) responseJsonPath.get("name")).isEqualTo(updatedLineName);
        assertThat((String) responseJsonPath.get("color")).isEqualTo(updatedLineColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> creationResponse = RestAssuredClient.requestPost(urlPath,
                LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value()).extract();

        // when
        long lineId = creationResponse.jsonPath().get("id");
        String path = generatePathForId(lineId);
        ExtractableResponse<Response> response = RestAssuredClient.requestDelete(path)
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    private String generatePathForId(long id) {
        return new StringBuilder()
            .append(urlPath)
            .append("/")
            .append(id)
            .toString();
    }
}
