package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.application.dto.LineResponse;
import subway.application.dto.StationResponse;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;
import subway.utils.DatabaseCleanup;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관리 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("acceptance")
public class SectionAcceptanceTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    /**
     * Given 지하철 노선이 생성을 요청 하고
     * When 지하철 노선에 새로운 구간 추가를 요청 하면
     * Then 노선에 새로운 구간이 추가된다.
     */
    @DisplayName("구간을 등록한다.")
    @Test
    void createSection() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");
        Long 선릉역 = 지하철역_생성_요청("선릉역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        // when
        ExtractableResponse<Response> response = 지하철구간_생성_요청(역삼역, 선릉역, 이호선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선이 생성을 요청 하고
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아니면
     * Then 구간이 추가되지 않는다.
     */
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createSectionWithInvalidUpStation() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");
        Long 선릉역 = 지하철역_생성_요청("선릉역");
        Long 삼성역 = 지하철역_생성_요청("삼성역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        // when
        ExtractableResponse<Response> response = 지하철구간_생성_요청(삼성역, 강남역, 이호선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성을 요청 하고
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 새로운 구간의 하행역이 해당 노선에 등록되어있는 상행 종점역이 아니면
     * Then 구간이 추가되지 않는다.
     */
    @DisplayName("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    void createSectionWithInvalidDownStation() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");
        Long 선릉역 = 지하철역_생성_요청("선릉역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        // when
        ExtractableResponse<Response> response = 지하철구간_생성_요청(역삼역, 강남역, 이호선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성을 요청 하고
     * Given 지하철 노선에 새로운 구간 추가를 요청 하고
     * When 지하철 노선에 구간 제거를 요청 하면
     * Then 노선에 구간이 제거된다.
     */
    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");
        Long 선릉역 = 지하철역_생성_요청("선릉역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        지하철구간_생성_요청(역삼역, 선릉역, 이호선);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선, 선릉역)
                .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.")
    @Test
    void deleteSectionWithInvalidStation() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");
        Long 선릉역 = 지하철역_생성_요청("선릉역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        지하철구간_생성_요청(역삼역, 선릉역, 이호선);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선, 역삼역)
                .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteSectionWithInvalidSize() {
        // given
        Long 강남역 = 지하철역_생성_요청("강남역");
        Long 역삼역 = 지하철역_생성_요청("역삼역");

        Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선, 역삼역)
                .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철구간_생성_요청(Long upStationId, Long downStationId, Long lineId) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);

        ExtractableResponse<Response> response = RestAssured
                .given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().extract();
        return response;
    }

    private static Long 지하철노선_생성_요청(Long 상행역, Long 하행역) {
        return LineFixture.지하철노선_생성_요청(Map.of(
                "name", "이호선",
                "color", "green",
                "upStationId", 상행역,
                "downStationId", 하행역,
                "distance", 10
        )).as(LineResponse.class).getId();
    }

    private static Long 지하철역_생성_요청(String name) {
        return StationFixture.지하철역_생성_요청(name).as(StationResponse.class).getId();
    }
}
