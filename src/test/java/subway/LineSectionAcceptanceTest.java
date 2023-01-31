package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.line.LineRequest;
import subway.dto.section.SectionRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 구간 관련 기능")
public class LineSectionAcceptanceTest extends AbstractAcceptanceTest {
    private static final String 신분당선 = "신분당선";
    private static final LineRequest 신분당선_요청;
    private static final SectionRequest 신논현_양재_구간;
    private static final SectionRequest 양재_양재_구간;
    private static final SectionRequest 강남역_판교_구간;
    private static final SectionRequest 신논현역_신사역_구간;
    private static final Long 신사역_ID = 1L;
    private static final Long 신논현역_ID = 2L;
    private static final Long 강남역_ID = 3L;
    private static final Long 양재역_ID = 4L;
    private static final Long 판교역_ID = 5L;

    static {
        신분당선_요청 = new LineRequest(신분당선, "bg-red-600", 신사역_ID, 신논현역_ID, 10);
        신논현_양재_구간 = new SectionRequest(신논현역_ID, 양재역_ID, 10);
        양재_양재_구간 = new SectionRequest(양재역_ID, 양재역_ID, 10);
        강남역_판교_구간 = new SectionRequest(강남역_ID, 판교역_ID, 10);
        신논현역_신사역_구간 = new SectionRequest(신논현역_ID, 신사역_ID, 10);
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 구간을 등록하면
     * Then 해당 노선 조회 시 추가된 구간이 포함되어 있다.
     */
    @DisplayName("구간을 등록한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void addSection() {
        String location = 노선_생성(신분당선_요청).extract().header("location");
        구간_등록(location, 신논현_양재_구간);

        List<Long> 신분당선_지하철_ID_목록 = get(location).extract()
                .jsonPath()
                .getList("stations.id", Long.class);
        assertThat(신분당선_지하철_ID_목록).containsExactly(신사역_ID, 신논현역_ID, 양재역_ID);
    }

    /**
     * Given 노선을 생성하고
     * When 해당 노선에 상행역과 하행역이 같은 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("상행역과 하행역이 같은 구간은 등록할 수 없다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void addSectionException() {
        String location = 노선_생성(신분당선_요청).extract().header("location");

        구간_등록(location, 양재_양재_구간).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 잘못된 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역과 일치하지 않은 경우 구간을 등록할 수 없다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void addSectionException1() {
        String location = 노선_생성(신분당선_요청).extract().header("location");

        구간_등록(location, 강남역_판교_구간).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 잘못된 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어있는 경우 구간을 등록할 수 없다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void addSectionException2() {
        String location = 노선_생성(신분당선_요청).extract().header("location");

        구간_등록(location, 신논현역_신사역_구간).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성 후 해당 노선에 구간을 등록하고
     * When 해당 노선에서 마지막 구간(하행 종점역)을 제거하면
     * Then 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
     */
    @DisplayName("구간을 제거한다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void deleteSection() {
        String location = 노선_생성(신분당선_요청).extract().header("location");
        구간_등록(location, 신논현_양재_구간);

        구간_삭제(location, 양재역_ID);

        List<Long> 신분당선_지하철_ID_목록 = get(location).extract()
                .jsonPath()
                .getList("stations.id", Long.class);
        assertThat(신분당선_지하철_ID_목록).containsExactly(신사역_ID, 신논현역_ID);
    }

    /**
     * Given 구간이 1개 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에서 마지막 구간(하행 종점역)을 제거하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("구간이 1개인 경우 제거할 수 없다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void deleteSectionException1() {
        String location = 노선_생성(신분당선_요청).extract().header("location");

        구간_삭제(location, 신논현역_ID).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 2개인 노선을 생성하고
     * When 해당 노선에서 마지막 구간(하행 종점역)이 아닌 다른 역을 제거하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("마지막 구간의 하행 종점역이 아닌 경우 구간을 제거할 수 없다.")
    @Sql("/sql/setup-station.sql")
    @Test
    void deleteSectionException2() {
        String location = 노선_생성(신분당선_요청).extract().header("location");
        구간_등록(location, 신논현_양재_구간);

        구간_삭제(location, 신논현역_ID).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ValidatableResponse 노선_생성(LineRequest request) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request).
                when()
                    .post("/lines").
                then().log().all();
    }

    private ValidatableResponse get(String path) {
        return given().log().all().
                when()
                    .get(path).
                then().log().all();
    }

    private ValidatableResponse 구간_등록(String location, SectionRequest request) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request).
                when()
                    .post(location + "/sections").
                then().log().all();
    }

    private ValidatableResponse 구간_삭제(String location, Long stationId) {
        return given().log().all()
                    .param("stationId", stationId).
                when()
                    .delete(location + "/sections").
                then().log().all();
    }
}
