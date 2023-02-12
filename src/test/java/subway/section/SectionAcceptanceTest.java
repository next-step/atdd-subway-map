package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.exception.IllegalSectionException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceTest.노선_생성_요청;
import static subway.line.LineAcceptanceTest.노선_조회_요청;
import static subway.station.StationAcceptanceTest.지하철역_생성_요청;

@DisplayName("구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private final static String 신분당선_이름 = "신분당선";
    private final static String 신분당선_색 = "bg-red-600";

    private final static Long 새로운_하행역_아이디 = 1L;
    private final static Long 기존_하행역_아이디 = 2L;
    private final static Long 기존_상행역_아이디 = 3L;

    private final static String 새로운_하행역_이름 = "새로운 구간의 지하철역";
    private final static String 기존_하행역_이름 = "기존 구간의 지하철역";
    private final static String 기존_상행역_이름 = "기존 구간의 다른 지하철역";

    private final static Long 기존_노선_거리 = 10L;
    private final static Long 새로운_구간_거리 = 15L;

    private final static LineRequest 신분당선 = LineRequest.of(
            신분당선_이름, 신분당선_색, 기존_하행역_아이디, 기존_상행역_아이디, 기존_노선_거리);

    private final static SectionRequest 새로운_구간 = new SectionRequest(
            새로운_하행역_아이디, 기존_하행역_아이디, 새로운_구간_거리
    );

    private final static SectionRequest 예외_구간 = new SectionRequest(
            기존_상행역_아이디, 새로운_하행역_아이디, 새로운_구간_거리
    );

    @BeforeEach
    void setUp() {
        지하철역_생성_요청(새로운_하행역_이름);
        지하철역_생성_요청(기존_하행역_이름);
        지하철역_생성_요청(기존_상행역_이름);
    }

    /**
     * Given 노선을 생성한다.
     * When 상행역이 노선의 하행종점역인 구간을 생성한다.
     * Then 노선 조회시 새로운 구간의 하행역을 확인할 수 있다.
     */
    @DisplayName("노선에 구간이 정상적으로 등록된다.")
    @Test
    void 구간_등록() {
        //Given
        long lineId = 노선_생성_요청(신분당선)
                .jsonPath().getLong("id");

        //When
        String newName = 구간_생성_요청(lineId, 새로운_구간)
                .jsonPath().getList("stations.name", String.class).get(0);

        //Then
        List<String> 종착역목록 = 노선_조회_요청(lineId)
                .jsonPath().getList("stations.name");
        assertThat(종착역목록).contains(newName);
    }

    /**
     * Given 노선을 생성한다.
     * When 하행역이 노선에 등록된 새로운 구간을 생성한다.
     * Then 구간 등록시 에러가 발생한다.
     */
    @DisplayName("구간 등록시 에러가 발생한다.")
    @Test
    void 구간_등록_예외() {
        //Given
        long lineId = 노선_생성_요청(신분당선)
                .jsonPath().getLong("id");

        //When
        ExtractableResponse<Response> response = 구간_생성_요청(lineId, 예외_구간);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 노선을 생성하고 구간을 추가한다..
     * When 지하철 노선의 하행종점역을 제거한다.
     * Then 노선 조회시 삭제한 역을 찾을 수 없다.
     */
    @DisplayName("구간이 정상적으로 삭제된다.")
    @Test
    void 구간_삭제() {
        //Given
        long lineId = 노선_생성_요청(신분당선)
                .jsonPath().getLong("id");
        ExtractableResponse<Response> response = 구간_생성_요청(lineId, 새로운_구간);

        //When
        Long newStationId = response.jsonPath().getList("stations.id", Long.class).get(0);
        String newStationName = response.jsonPath().getList("stations.name", String.class).get(0);
        구간_삭제_요청(lineId, newStationId);

        //Then
        List<String> 종착역목록 = 노선_조회_요청(lineId)
                .jsonPath().getList("stations.name");
        assertThat(종착역목록).doesNotContain(newStationName);
    }

    /**
     * Given 노선을 생성한다.
     * When 구간이 1개일 때, 구간 삭제를 시도한다.
     * Then 에러가 발생한다.
     */
    @DisplayName("구간이 한개인 노선에서 역을 제거시 예외 발생")
    @Test
    void 구간_삭제_예외_구간_한개() {
        //Given
        ExtractableResponse<Response> response = 노선_생성_요청(신분당선);

        //When
        long lineId = response.jsonPath().getLong("id");
        long stationId = response.jsonPath().getList("stations.id", Long.class).get(0);
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(lineId, stationId);

        //Then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ExtractableResponse<Response> 구간_삭제_요청(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 구간_생성_요청(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
