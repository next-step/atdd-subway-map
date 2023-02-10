package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

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
        구간_생성_요청(lineId, 새로운_구간);

        //Then
        List<String> 종착역목록 = 노선_조회_요청(lineId)
                .jsonPath().getList("stations.name");
        assertThat(종착역목록).contains(새로운_하행역_이름);
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
