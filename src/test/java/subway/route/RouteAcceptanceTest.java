package subway.route;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.RestAssuredUtils;
import subway.common.RestAssuredCondition;
import subway.route.domain.Route;
import subway.route.repository.RouteRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RouteAcceptanceTest {

    @Autowired
    RouteRepository routeRepository;

    private final String ROUTE_API_URI = "/api/routes";
    private final String SLASH = "/";

    @BeforeEach
    void setUp() {
        routeRepository.deleteAll();
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

        ExtractableResponse<Response> response =
                RestAssuredUtils
                        .create(new RestAssuredCondition(ROUTE_API_URI, params));
        Route route = response.body().jsonPath().getObject(".", Route.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> inquiryStationsResponse =
                RestAssuredUtils
                        .inquiry(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId()));

        assertThat(inquiryStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(inquiryStationsResponse.body().jsonPath().getObject(".", Route.class)).isEqualTo(route);

    }

    @DisplayName("지하철 노선 목록을 조회")
    @Test
    void inquiryRoutes() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");

        RestAssuredUtils.create(new RestAssuredCondition(ROUTE_API_URI, params));

        params.put("name", "2호선");

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

            ExtractableResponse<Response> response =
                    RestAssuredUtils
                            .create(new RestAssuredCondition(ROUTE_API_URI, params));
            Route route = response.body().jsonPath().getObject(".", Route.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            params.put("name", "2호선");

            ExtractableResponse<Response> updateResponse =
                    RestAssuredUtils
                            .update(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId(), params));

            assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(updateResponse.body().jsonPath().getObject(".", Route.class).getName()).isEqualTo("2호선");

    }

    @DisplayName("지하철 노선을 삭제")
    @Test
    void deleteRoute() {
            Map<String, String> params = new HashMap<>();
            params.put("name", "1호선");

            ExtractableResponse<Response> response =
                    RestAssuredUtils
                            .create(new RestAssuredCondition(ROUTE_API_URI, params));
            Route route = response.body().jsonPath().getObject(".", Route.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> deleteResponse =
                    RestAssuredUtils
                            .delete(new RestAssuredCondition(ROUTE_API_URI + SLASH + route.getId()));

            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
