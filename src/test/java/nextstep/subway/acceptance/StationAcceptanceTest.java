package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.StationApi;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@Sql({"classpath:subway.init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String 강남역 = "강남역";
        ExtractableResponse<Response> response = StationApi.createStationApi(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> 모든역_조회_응답 = StationApi.getAllStationApi();
        List<String> stationNames = 모든역_조회_응답.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        String 강남역 = "강남역";
        String 신논현역 = "신논현역";

        ExtractableResponse<Response> 강남역_생성_응답 = StationApi.createStationApi(강남역);
        ExtractableResponse<Response> 신논현역_생성_응답 = StationApi.createStationApi(신논현역);

        // when
        ExtractableResponse<Response> 모든역_조회_응답 = StationApi.getAllStationApi();
        List<String> stationNames = 모든역_조회_응답.jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf("강남역", "신논현역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        String 강남역 = "강남역";
        ExtractableResponse<Response> 강남역_생성_응답 = StationApi.createStationApi(강남역);
        Long 강남역_고유번호 = 강남역_생성_응답.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> 강남역_삭제_응답 = StationApi.deleteStationByIdApi(강남역_고유번호);
        assertThat(강남역_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> 모든역_조회_응답 = StationApi.getAllStationApi();
        List<String> stationNames = 모든역_조회_응답.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(0);
    }
}