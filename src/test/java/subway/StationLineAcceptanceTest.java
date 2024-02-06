package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.application.line.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    @Autowired
    private HibernateUtil hibernateUtil;

    @BeforeEach
    void setUp() {
        hibernateUtil.clear();
    }

    /**
     * Given 지하철 역을 2개 생성하고
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // given
        String upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getString("id");
        String downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"))
            .jsonPath().getString("id");
        // when
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", upStationId, downStationId).jsonPath()
            .getLong("id");
        assertThat(id).isNotNull();

        // then
        List<String> lineNames =
            RequestFixtures.지하철_노선_목록_조회하기().jsonPath().getList(".", LineResponse.class).stream()
                .map(
                    LineResponse::getName).collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("신분당선");
    }


    /**
     * Given 지하철역 4개를 생성하고
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getStationLines() {
        // given
        String upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getString("id");
        String downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"))
            .jsonPath()
            .getString("id");
        지하철_노선을_생성한다("신분당선", "bg-red-600", upStationId, downStationId);
        RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("판교역")).jsonPath()
            .getLong("id");
        RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("양재역")).jsonPath()
            .getLong("id");
        지하철_노선을_생성한다("2호선", "bg-green-600", "3", "4");

        // when
        List<String> lineNames =
            RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().body().jsonPath().getList(".", LineResponse.class).stream().map(
                    LineResponse::getName).collect(Collectors.toList());

        // then
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }


    /**
     * Given 지하철역 2개를 생성하고
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getStationLine() {
        // given
        String upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getString("id");
        String downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"))
            .jsonPath().getString("id");

        String id = 지하철_노선을_생성한다("신분당선", "bg-red-600", upStationId, downStationId).jsonPath()
            .getString("id");
        // when
        String name =
            RequestFixtures.지하철_노선_조회하기(id).jsonPath().getString("name");

        // then
        assertThat(name).isEqualTo("신분당선");
    }

    /**
     * Given 지하철역 2개를 생성하고
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        String upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getString("id");
        String downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"))
            .jsonPath().getString("id");
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", upStationId, downStationId).jsonPath()
            .getLong("id");

        // when
        Map<String, String> updateParams = Fixtures.getCreateLineParams("다른신분당선", "bg-red-600", "1",
            "2");

        ExtractableResponse<Response> response = RequestFixtures.지하철_노선_수정하기(id, updateParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 역을 생성하고
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        String upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getString("id");
        String downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"))
            .jsonPath().getString("id");
        Long id = 지하철_노선을_생성한다("신분당선", "bg-red-600", upStationId, downStationId).jsonPath()
            .getLong("id");
        // when
        ExtractableResponse<Response> response = RequestFixtures.지하철_노선_삭제하기(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 지하철_노선을_생성한다(String name, String color,
        String upStationId, String downStationId) {
        return RequestFixtures.지하철노선_생성_요청하기(
            Fixtures.getCreateLineParams(name, color, upStationId, downStationId));
    }
}

