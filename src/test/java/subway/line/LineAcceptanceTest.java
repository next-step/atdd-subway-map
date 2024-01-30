package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.station.StationResponse;
import util.RestAssuredUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static Long SINSA_STATION_ID;
    private static Long GWANGGYO_STATION_ID;
    private static Long DAEHWA_STATION_ID;
    private static Long OGEUM_STATION_ID;

    @BeforeEach
    void setFixture() {
        SINSA_STATION_ID = RestAssuredUtil.post(Map.of("name", "신사역"), "stations")
                .as(StationResponse.class).getId();
        GWANGGYO_STATION_ID = RestAssuredUtil.post(Map.of("name", "광교역"), "stations")
                .as(StationResponse.class).getId();
        DAEHWA_STATION_ID = RestAssuredUtil.post(Map.of("name", "대화역"), "stations")
                .as(StationResponse.class).getId();
        OGEUM_STATION_ID = RestAssuredUtil.post(Map.of("name", "오금역"), "stations")
                .as(StationResponse.class).getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", SINSA_STATION_ID);
        params.put("downStationId", GWANGGYO_STATION_ID);
        params.put("distance", 10L);

        ExtractableResponse<Response> response
                = RestAssuredUtil.post(params, "/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames
                = RestAssuredUtil.get("/lines")
                .jsonPath().getList("name", String.class);
        assertThat(lineNames.get(0)).isEqualTo("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void findAllLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", SINSA_STATION_ID);
        params.put("downStationId", GWANGGYO_STATION_ID);
        params.put("distance", 10L);

        RestAssuredUtil.post(params, "/lines");

        Map<String, Object> params2 = new HashMap<>();
        params2.put("name", "3호선");
        params2.put("color", "bg-navy-600");
        params2.put("upStationId", DAEHWA_STATION_ID);
        params2.put("downStationId", OGEUM_STATION_ID);
        params2.put("distance", 20L);

        RestAssuredUtil.post(params2, "/lines");

        // then
        List<String> lineNames
                = RestAssuredUtil.get("/lines")
                .jsonPath().getList("name", String.class);
        assertThat(lineNames.get(0)).isEqualTo("신분당선");
        assertThat(lineNames.get(1)).isEqualTo("3호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO : 지하철노선 조회 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void findLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", SINSA_STATION_ID);
        params.put("downStationId", GWANGGYO_STATION_ID);
        params.put("distance", 10L);

        long id = RestAssuredUtil.post(params, "/lines").jsonPath().getLong("id");

        // then
        String lineName
                = RestAssuredUtil.get("/lines/" + id).jsonPath().getString("name");

        assertThat(lineName).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO : 지하철노선 수정 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", SINSA_STATION_ID);
        params.put("downStationId", GWANGGYO_STATION_ID);
        params.put("distance", 10L);

        long id = RestAssuredUtil.post(params, "/lines").jsonPath().getLong("id");

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("name", "4호선");
        updateParams.put("color", "bg-blue-600");

        RestAssuredUtil.put(updateParams, "/lines/" + id);

        // then
        String lineName
                = RestAssuredUtil.get("/lines/" + id).jsonPath().getString("name");

        assertThat(lineName).isEqualTo("4호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO : 지하철노선 삭제 인수 테스트 메서드 작성
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", SINSA_STATION_ID);
        params.put("downStationId", GWANGGYO_STATION_ID);
        params.put("distance", 10L);

        long id = RestAssuredUtil.post(params, "/lines").jsonPath().getLong("id");

        // when
        RestAssuredUtil.delete("/lines/" + id);

        // then
        List<String> lineNames
                = RestAssuredUtil.get("/lines").jsonPath().getList("name", String.class);

        assertThat(lineNames).doesNotContain("신분당선");
    }
}
