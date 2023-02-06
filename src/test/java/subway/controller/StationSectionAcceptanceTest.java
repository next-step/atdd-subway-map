package subway.controller;


import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.line.ReadLineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.controller.StationUtils.*;

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
        StationUtils.createColor(LINE_RED);
        StationUtils.createColor(LINE_BLUE);
        StationUtils.createLine(SIN_BUN_DANG_LINE_NAME, LINE_RED, 1L, 2L, 10L);
        StationUtils.extendLine(2L, 3L, 20L);
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When C, D역을 잇는 구간을 생성하면
     * Then 노선의 상행 종점역은 A가 되고, 하행 종점역은 D가 된다.
     */
    @Test
    void createStationSection() {
        ExtractableResponse<Response> response = StationUtils.extendLine(3L, 4L, 30L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = StationUtils.selectLine(1L).jsonPath();

        assertThat(jsonPath.getLong("id")).isEqualTo(1L);
        assertThat(jsonPath.getString("name")).isEqualTo(SIN_BUN_DANG_LINE_NAME);
        assertThat(jsonPath.getString("color")).isEqualTo(LINE_RED);

        List<ReadLineResponse> responses = jsonPath.getList("stations", ReadLineResponse.class);
        assertThat(responses.get(responses.size() - 1).getId()).isEqualTo(4L);
    }


    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When B, D역을 잇는 구간을 생성하면
     * Then "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다." 오류 메시지가 나온다.
     */
    @Test
    void createStationSectionError1() {
        ExtractableResponse<Response> response = StationUtils.extendLine(2L, 4L, 40L);

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
        ExtractableResponse<Response> response = StationUtils.extendLine(3L, 1L, 40L);

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
        ExtractableResponse<Response> response = StationUtils.reduceLine(3L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        JsonPath jsonPath = selectLine(1L).jsonPath();
        List<ReadLineResponse> responses = jsonPath.getList("stations", ReadLineResponse.class);
        assertThat(responses).extracting(ReadLineResponse::getId).containsExactly(1L, 2L);
    }

    /**
     * Given 지하철 역 A, B, C, D를 생성 후
     * Given A, B, C역을 잇는 노선을 생성 후
     * When A역을 삭제하면
     * Then "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다." 오류 메시지가 나온다.
     */
    @Test
    void deleteStationSectionError1() {
        ExtractableResponse<Response> response1 = StationUtils.reduceLine(1L);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response1.body().asString()).contains("마지막 구간만 제거할 수 있다");
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
        StationUtils.createLine(ONE_LINE_NAME, LINE_BLUE, 5L, 6L, 20L);

        ExtractableResponse<Response> response = reduceLine(2, 6);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우");
    }

}
