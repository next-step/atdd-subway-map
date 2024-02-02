package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineCreateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.path.json.JsonPath.from;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     *  When : 지하철 노선을 생성하고
     *  Then : 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철노선을_생성하고_조회한다() {
        // given
        createStation("건대입구");
        createStation("어린이대공원");

        LineCreateRequest lineCreateRequest = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                1L,
                2L,
                10
        );

        // when
        ExtractableResponse<Response> response = createLine(lineCreateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsAnyOf(
                "건대입구", "어린이대공원"
        );
    }

    /**
     *  Given 2개의 지하철 노선을 생성하고
     *  When 지하철 노선 목록을 조회하면
     *  Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 생성한_지하철노선을_모두_조회한다() {
        // given
        createStation("건대입구");
        createStation("어린이대공원");
        createStation("군자");

        LineCreateRequest lineCreateRequest_1 = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                1L,
                2L,
                10
        );
        createLine(lineCreateRequest_1);

        LineCreateRequest lineCreateRequest_2 = new LineCreateRequest(
                "분당선",
                "bg-green-600",
                1L,
                3L,
                20
        );
        createLine(lineCreateRequest_2);

        // when
        ExtractableResponse<Response> response = getLines();
        String responseJson = response.body().asString();
        List<Map<String, ?>> lines = from(responseJson).getList("");

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(lines.get(0).get("name")).isEqualTo("신분당선");
        assertThat((List<Map<String, ?>>) lines.get(0).get("stations")).extracting("name").containsExactlyInAnyOrder("건대입구", "어린이대공원");

        assertThat(lines.get(1).get("name")).isEqualTo("분당선");
        assertThat((List<Map<String, ?>>) lines.get(1).get("stations")).extracting("name").containsExactlyInAnyOrder("건대입구", "군자");
    }



    private ExtractableResponse<Response> createLine(LineCreateRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getLines() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        return response;
    }
}
