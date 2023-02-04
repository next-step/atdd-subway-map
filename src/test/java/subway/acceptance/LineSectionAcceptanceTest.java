package subway.acceptance;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.section.SectionRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.LineAcceptanceTest.노선_생성;
import static subway.acceptance.StationAcceptanceTest.역_생성;
import static subway.fixture.LineFixture.신분당선_요청;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.*;

@DisplayName("지하철 노선 구간 관련 기능")
public class LineSectionAcceptanceTest extends AbstractAcceptanceTest {

    @BeforeEach
    void setUp() {
        역_생성("강남역");
        역_생성("역삼역");
        역_생성("교대역");
        역_생성("양재역");
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 구간을 등록하면
     * Then 해당 노선 조회 시 추가된 구간이 포함되어 있다.
     */
    @DisplayName("구간을 등록한다.")
    @Test
    void addSection() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        구간_등록(location, 역삼역_교대역_구간_요청);

        assertThat(노선_지하철역_ID_목록_조회(location)).containsExactly(강남역_ID, 역삼역_ID, 교대역_ID);
    }

    /**
     * Given 노선을 생성하고
     * When 해당 노선에 상행역과 하행역이 같은 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("상행역과 하행역이 같은 구간은 등록할 수 없다.")
    @Test
    void addSectionException() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        구간_등록(location, 양재역_양재역_구간_요청).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 잘못된 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역과 일치하지 않은 경우 구간을 등록할 수 없다.")
    @Test
    void addSectionException1() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        구간_등록(location, 교대역_양재역_구간_요청).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에 잘못된 구간을 등록하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어있는 경우 구간을 등록할 수 없다.")
    @Test
    void addSectionException2() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        구간_등록(location, 역삼역_강남역_구간_요청).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성 후 해당 노선에 구간을 등록하고
     * When 해당 노선에서 마지막 구간(하행 종점역)을 제거하면
     * Then 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
     */
    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);
        구간_등록(location, 역삼역_교대역_구간_요청);

        구간_삭제(location, 교대역_ID);

        assertThat(노선_지하철역_ID_목록_조회(location)).containsExactly(강남역_ID, 역삼역_ID);
    }

    /**
     * Given 구간이 1개 포함되어있는 지하철 노선을 생성하고
     * When 해당 노선에서 마지막 구간(하행 종점역)을 제거하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("구간이 1개인 경우 제거할 수 없다.")
    @Test
    void deleteSectionException1() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        구간_삭제(location, 역삼역_ID).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 구간이 2개인 노선을 생성하고
     * When 해당 노선에서 마지막 구간(하행 종점역)이 아닌 다른 역을 제거하면
     * Then 400 에러가 발생한다.
     */
    @DisplayName("마지막 구간의 하행 종점역이 아닌 경우 구간을 제거할 수 없다.")
    @Test
    void deleteSectionException2() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);
        구간_등록(location, 역삼역_교대역_구간_요청);

        구간_삭제(location, 역삼역_ID).statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ValidatableResponse 구간_등록(String location, SectionRequest request) {
        return post(location + "/sections", request);
    }

    private ValidatableResponse 구간_삭제(String location, Long stationId) {
        return given()
                    .param("stationId", stationId).
                when()
                    .delete(location + "/sections").
                then().log().all();
    }

    private List<Long> 노선_지하철역_ID_목록_조회(String location) {
        return get(location).extract()
                .jsonPath()
                .getList("stations.id", Long.class);
    }
}
