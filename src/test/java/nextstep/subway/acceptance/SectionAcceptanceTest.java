package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.LineApi;
import nextstep.subway.api.StationApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관리 기능")
@Sql({"classpath:subway.init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 신논현역;
    private ExtractableResponse<Response> 정자역;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        강남역 = StationApi.createStationApi("강남역");
        신논현역 = StationApi.createStationApi("신논현역");
        정자역 = StationApi.createStationApi("정자역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 추가하면
     * Then 지하철 노선 조회시 추가 된 지하철 역을 조회할 수 있다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        // given
        long 강남역_번호 = 강남역.jsonPath().getLong("id");
        long 신논현역_번호 = 신논현역.jsonPath().getLong("id");

        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역_번호, 신논현역_번호, 10);

        // when
        long 정자역_번호 = 정자역.jsonPath().getLong("id");
        long 신분당선_번호 = 신분당선.jsonPath().getLong("id");

        LineApi.addSectionApi(신분당선_번호, 신논현역_번호, 정자역_번호, 5);

        // then
        ExtractableResponse<Response> 신분당선_조회_응답 = LineApi.getLineByIdApi(신분당선_번호);
        List<String> stationNames = 신분당선_조회_응답.jsonPath().getList("stations.name", String.class);

        assertAll(
                () -> assertThat(stationNames).hasSize(3),
                () -> assertThat(stationNames).containsAnyOf("강남역", "신논현역", "정자역")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 기존 노선의 종점역과 새로운 노선의 상행성이 일치하지 않도록 추가하면
     * Then 지하철 노선은 정상적으로 추가되지 않는다.
     */
    @DisplayName("지하철 구간 등록 예외(하행역과 상행역)")
    @Test
    void addSectionExceptionUnmatchedException() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 노선이 기존 노선의 역에 등록되어 있을 때 추가하면
     * Then 지하철 노선은 정상적으로 추가되지 않는다.
     */
    @DisplayName("지하철 구간 등록 예외(이미 존재하는 역 등록)")
    @Test
    void addSectionExceptionAlreadyExistsStationException() {

    }
}
