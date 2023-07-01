package line;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.SchemaInitSql;
import subway.StationInitSql;
import subway.SubwayApplication;
import subway.line.LineCreateRequest;

@SchemaInitSql
@StationInitSql
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final String API_CREATE_LINE = "/lines";
    private static final String API_GET_LINE = "/lines";
    private static final String API_GET_LINE_LIST = "/lines";
    private static final String API_MODIFY_LINE = "/lines";

    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        ExtractableResponse<Response> createdResponse = 노선생성("신분당선", "bg-red-600", 1, 2, 10);

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createdResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(createdResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(createdResponse.jsonPath().getList("stations.id")).containsSequence(List.of(1, 2));
    }

    private ExtractableResponse<Response> 노선생성(String name, String color, long upStationId, long downStationId, int distance) {
        LineCreateRequest request = new LineCreateRequest(name, color, upStationId, downStationId, distance);

        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post(API_CREATE_LINE)
                          .then().log().all()
                          .extract();
    }

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 노선생성("신분당선", "bg-red-600", 1, 2, 10);

        Long createdLineId = createResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().get(getLineRequestUrl(createdLineId))
                   .then().log().all()
                   .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id")).containsSequence(List.of(1, 2));
    }

    private String getLineRequestUrl(long id) {
        return API_GET_LINE + "/" + id;
    }

    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void getLineList() {
        // given
        노선생성("신분당선", "bg-red-600", 1, 2, 10);
        노선생성("분당선", "bg-green-600", 1, 3, 20);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when().get(API_GET_LINE_LIST)
                                                            .then().log().all()
                                                            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsAnyOf("신분당선", "분당선");
    }

    @DisplayName("지하철 노선을 수정한다")
    @Test
    void modifyLine() {
        // given
        // 지하철 노선을 생성
        ExtractableResponse<Response> createdResponse = 노선생성("신분당선", "bg-red-600", 1, 2, 10);
        Long createdLineId = createdResponse.jsonPath().getLong("id");

        // when
        // 지하철 노선을 수정
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                   .when().put(getModifyLineRequestUrl(createdLineId))
                   .then().log().all()
                   .extract();

        // then
        // 해당 지하철 노선 정보는 수정된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private String getModifyLineRequestUrl(long id) {
        return API_MODIFY_LINE + "/" + id;
    }
}
