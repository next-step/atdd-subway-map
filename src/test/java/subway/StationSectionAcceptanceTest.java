package subway;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationUtils.*;
import static subway.StationUtils.LINE_BLUE;

@DisplayName("지하철 역 노선 구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationSectionAcceptanceTest {
    @BeforeEach
    void setup() {
        StationUtils.createStation(GANG_NAM_STATION);
        StationUtils.createStation(SIN_SA_STATION);
        StationUtils.createStation(PAN_GYEO_STATION);
        StationUtils.createStation(SIN_NONE_HYEON_STATION);

        Map<String, Object> SIN_BUN_DANG_STATION_LINE = new HashMap<>();
        SIN_BUN_DANG_STATION_LINE.put("name", SIN_BUN_DANG_LINE_NAME);
        SIN_BUN_DANG_STATION_LINE.put("color", LINE_RED);
        SIN_BUN_DANG_STATION_LINE.put("upStationId", 1L);
        SIN_BUN_DANG_STATION_LINE.put("downStationId", 2L);
        SIN_BUN_DANG_STATION_LINE.put("distance", 10);

        StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);

        Map<String, Object> body = new HashMap<>();
        body.put("upStationId", 2L);
        body.put("downStationId", 3L);
        StationUtils.createStationSection(body);
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When C, D역을 잇는 구간을 생성하면
     * Then 노선의 상행 종점역은 A가 되고, 하행 종점역은 D가 된다.
     */
    @Test
    void createStationSection() {
        Map<String, Object> body = new HashMap<>();
        body.put("upStationId", 3);
        body.put("downStationId", 4);
        ExtractableResponse<Response> response = StationUtils.createStationSection(body);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        JsonPath jsonPath = StationUtils.selectStationLine(1L).jsonPath();

        assertThat(jsonPath.getLong("id")).isEqualTo(1L);
        assertThat(jsonPath.getString("name")).isEqualTo(SIN_BUN_DANG_LINE_NAME);
        assertThat(jsonPath.getString("color")).isEqualTo(LINE_RED);
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When B, D역을 잇는 구간을 생성하면
     * Then "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다." 오류 메시지가 나온다.
     */
    @Test
    void createStationSectionError1() {
        Map<String, Object> body = new HashMap<>();
        body.put("upStationId", 2);
        body.put("downStationId", 4);
        ExtractableResponse<Response> response = StationUtils.createStationSection(body);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다");
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When D, A역을 잇는 구간을 생성하면
     * Then "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다." 오류 메시지가 나온다.
     */
    @Test
    void createStationSectionError2() {
        Map<String, Object> body = new HashMap<>();
        body.put("upStationId", 4);
        body.put("downStationId", 1);
        ExtractableResponse<Response> response = StationUtils.createStationSection(body);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다");
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When C역을 삭제하면
     * Then 노선을 하행 종점역은 B가 된다.
     */
    @Test
    void deleteStationSection() {
        ExtractableResponse<Response> response = StationUtils.deleteStationSection(3L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = selectStationLine(1L).jsonPath();
        assertThat(jsonPath.getLong("downStationId")).isEqualTo(2L);
    }

    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When A역을 삭제하면
     * When B역을 삭제하면
     * Then "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다." 오류 메시지가 나온다.
     */
    @Test
    void deleteStationSectionError1() {
        ExtractableResponse<Response> response1 = StationUtils.deleteStationSection(1L);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response1.body().asString()).contains("마지막 구간만 제거할 수 있다");

        ExtractableResponse<Response> response2 = StationUtils.deleteStationSection(2L);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response2.body().asString()).contains("마지막 구간만 제거할 수 있다");
    }

    /**
     * Given 지하철 역 E, F를 생성 후
     * Given E, F역을 잇는 노선을 생성 후
     * When F역을 삭제하면
     * Then "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다." 오류 메시지가 나온다.
     */
    @Test
    void deleteStationSectionError2() {
        StationUtils.createStation(GURO_STATION);
        StationUtils.createStation(SINDORIM_STATION);

        Map<String, Object> ONE_STATION_LINE = new HashMap<>();
        ONE_STATION_LINE.put("name", ONE_LINE_NAME);
        ONE_STATION_LINE.put("color", LINE_BLUE);
        ONE_STATION_LINE.put("upStationId", 5L);
        ONE_STATION_LINE.put("downStationId", 6L);
        ONE_STATION_LINE.put("distance", 20);

        StationUtils.createStationLine(ONE_STATION_LINE);

        ExtractableResponse<Response> response = StationUtils.deleteStationSection(5L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우");
    }

}
