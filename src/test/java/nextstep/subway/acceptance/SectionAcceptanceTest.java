package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.SectionRequest;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SectionAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    int port;

    Long stationId1;
    Long stationId2;
    Long stationId3;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
    }

    @BeforeAll
    public void beforeAll() {
        RestAssured.port = port;

        //테스트 환경: 1-2-3 으로 이어진 노선 구간 가정
        stationId1 = createStation("양재역").jsonPath().getLong("id");
        stationId2 = createStation("양재시민의숲역").jsonPath().getLong("id");
        stationId3 = createStation("청계산입구역").jsonPath().getLong("id");
    }

    /**
     * given: 노선 추가
     * when: 구간 등록
     * then: 해당 구간이 추가됨
     */
    @Test
    @Sql(scripts = "/sql/truncate_line_table.sql")
    void 지하철구간_추가() {
        //given
        long id = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(stationId2, stationId3, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + id + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> lineResponse = get("/lines/" + id);
        assertThat(lineResponse.jsonPath().getLong("downStationId")).isEqualTo(1);
        assertThat(lineResponse.jsonPath().getLong("upStationId")).isEqualTo(3);
    }

    /**
     * when: 요청하는 상행역이 하행 종점역이 아닌 구간 등록 요청
     * then: 400에러 발생
     */
    @Test
    @Sql(scripts = "/sql/truncate_line_table.sql")
    void 실패_400_요청하는_상행역이_현재_하행_종점이_아님_지하철구간_추가() {
        //given
        long id = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(stationId1, stationId3, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + id + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



    /**
     * when: 요청하는 하행역이 이미 존재하는 역인 구간 등록 요청
     * then: 400에러 발생
     */
    @Test
    @Sql(scripts = "/sql/truncate_line_table.sql")
    void 실패_400_요청하는_하행역이_이미_존재_지하철구간_추가() {
        //given
        long id = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(stationId2, stationId1, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + id + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post("/stations", params);
    }

    private ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        return post("/lines", lineRequest);
    }
}