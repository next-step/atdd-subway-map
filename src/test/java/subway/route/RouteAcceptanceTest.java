package subway.route;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.CommonRestAssured;
import subway.common.CommonRestAssuredUseCondition;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RouteAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createRoute() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");

        ExtractableResponse<Response> response = CommonRestAssured.create(new CommonRestAssuredUseCondition("/api/routes", params));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

}
