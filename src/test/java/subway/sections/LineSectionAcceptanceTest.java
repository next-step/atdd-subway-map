package subway.sections;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.fixture.LineFixture;
import subway.fixture.SectionFixture;
import subway.fixture.StationFixture;
import subway.util.RestAssuredUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.RestAssuredUtil.생성_요청;
import static subway.util.RestAssuredUtil.조회_요청;

@Sql("/truncate_table.sql")
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineSectionAcceptanceTest {
    private static ExtractableResponse<Response> 신림역;
    private static ExtractableResponse<Response> 보라매역;
    private static ExtractableResponse<Response> 대방역;
    private static ExtractableResponse<Response> 서원역;
    private static ExtractableResponse<Response> 판교역;
    private static ExtractableResponse<Response> 청계산입구역;
    private static ExtractableResponse<Response> 신분당선;

    @BeforeEach
    void before() {
        신림역 = 생성_요청(
                StationFixture.createStationParams("신림역"),
                "/stations");
        보라매역 = 생성_요청(
                StationFixture.createStationParams("보라매역"),
                "/stations");
//        대방역 = 생성_요청(
//                StationFixture.createStationParams("대방역"),
//                "/stations");
        서원역 = 생성_요청(
                StationFixture.createStationParams("서원역"),
                "/stations");
//        판교역 = 생성_요청(
//                StationFixture.createStationParams("판교역"),
//                "/stations");
//        청계산입구역 = 생성_요청(
//                StationFixture.createStationParams("청계산입구역"),
//                "/stations");
//
//        신분당선 = 생성_요청(
//                LineFixture.createLineParams("신분당선", "RED", 판교역.jsonPath().getLong("id"), 청계산입구역.jsonPath().getLong("id"), 20L), "/lines");

    }

    /**
     * given 지하철 노선을 생성하고
     * when 지하철 구간을 생성하면
     * then 지하철 구간이 생성된다.
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createLineSection() {
        //given
        ExtractableResponse<Response> 신림선 = 생성_요청(
                LineFixture.createLineParams("신림선", "BLUE", 신림역.jsonPath().getLong("id"), 보라매역.jsonPath().getLong("id"), 20L),
                "/lines");

        //when
        ExtractableResponse<Response> 신림선_구간_생성 = 생성_요청(
                SectionFixture.createSectionParams(보라매역.jsonPath().getLong("id"), 서원역.jsonPath().getLong("id"), 10L),
                "/lines/" + 신림선.jsonPath().getLong("id") + "/sections"
        );

        //then
        assertThat(신림선_구간_생성.statusCode()).isEqualTo(201);
        assertThat(신림선_구간_생성.jsonPath().getString("upStations.name")).isEqualTo(보라매역.jsonPath().getString("name"));
        assertThat(신림선_구간_생성.jsonPath().getString("downStations.name")).isEqualTo(서원역.jsonPath().getString("name"));
    }

}
