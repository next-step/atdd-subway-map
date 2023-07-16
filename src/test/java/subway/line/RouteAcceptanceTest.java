package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.RestAssuredUtils;
import subway.common.RestAssuredCondition;
import subway.line.dto.LineResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RouteAcceptanceTest {

    private final String ROUTE_API_URI = "/api/routes";

    private final String STATION_API_URI = "/api/stations";

    private final String SLASH = "/";

    @BeforeEach
    void setUpStation() {
        createStation();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 && 지하철노선 조회")
    @Test
    void createRoute() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-500");
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "20");

        ExtractableResponse<Response> response =
                RestAssuredUtils
                        .create(new RestAssuredCondition(ROUTE_API_URI, params));
        LineResponse route = response.body().jsonPath().getObject(".", LineResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> inquiryStationsResponse =
                RestAssuredUtils
                        .inquiry(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId()));

        assertThat(inquiryStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(inquiryStationsResponse.body().jsonPath().getObject(".", LineResponse.class)).isEqualTo(route);

    }

    @DisplayName("지하철 노선 목록을 조회")
    @Test
    void inquiryRoutes() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        RestAssuredUtils.create(new RestAssuredCondition(ROUTE_API_URI, params));

        params.put("name", "2호선");
        params.put("color", "bg-green-500");
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "20");

        RestAssuredUtils.create(new RestAssuredCondition(ROUTE_API_URI, params));

        ExtractableResponse<Response> response =
                RestAssuredUtils
                        .inquiry(new RestAssuredCondition(ROUTE_API_URI));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name", String.class).size()).isEqualTo(2);

    }

    @DisplayName("지하철 노선을 수정")
    @Test
    void updateRoute() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssuredUtils
                        .create(new RestAssuredCondition(ROUTE_API_URI, params));
        LineResponse route = response.body().jsonPath().getObject(".", LineResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        params.put("name", "2호선");
        params.put("color", "bg-green-500");
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "20");

        ExtractableResponse<Response> updateResponse =
                RestAssuredUtils
                        .update(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId(), params));

        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateResponse.body().jsonPath().getObject(".", LineResponse.class).getName()).isEqualTo("2호선");

    }

    @DisplayName("지하철 노선을 삭제")
    @Test
    void deleteRoute() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssuredUtils
                        .create(new RestAssuredCondition(ROUTE_API_URI, params));
        LineResponse route = response.body().jsonPath().getObject(".", LineResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> deleteResponse =
                RestAssuredUtils
                        .delete(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId()));

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void createStation() {
        Map<String, String> params = new HashMap<>();

        params.put("name", "강남역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

        params.put("name", "삼성역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

    }

}
