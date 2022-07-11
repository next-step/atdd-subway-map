package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철노선을 생성하면
     * Then 지하철노선이 생성된다
     * Then 지하철노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        create(params, "/stations");
        params.put("name", "역삼역");
        create(params, "/stations");
        params.put("name", "선릉역");
        create(params, "/stations");

        params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");

        ExtractableResponse<Response> response = create(params, "/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getList("name", "/lines").get(0)).isEqualTo("2호선");
    }
}
