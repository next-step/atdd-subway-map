package subway.line;

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
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.station.dto.StationResponse;
import subway.util.assertUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.station.StationAcceptanceTest.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 신논현역;

    @BeforeEach
    void init() {
        //given - 지하철역이 등록되어있다.
        강남역 = 지하철역이_생성됨("강남역");
        역삼역 = 지하철역이_생성됨("역삼역");
        신논현역 = 지하철역이_생성됨("신논현역");
    }

    /**
     * 지하철노선 생성
     *  - When 지하철 노선을 생성하면
     *  - Then 요청한 지하철 노선이 생성된다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void Should_지하철노선을_생성하면_Then_지하철노선이_생성된다() {
        // when
        var 이호선_request = new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L);
        ExtractableResponse<Response> 이호선_response = 지하철노선을_생성한다(이호선_request);

        // then
        지하철노선이_정상적으로_생성(이호선_response);
        LineResponse line = 이호선_response.as(LineResponse.class);
        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("2호선");
            assertThat(line.getStationIds()).containsExactlyInAnyOrderElementsOf(List.of(강남역.getId(), 역삼역.getId()));
        });
    }

    /**
     * 지하철노선목록 조회
     *  - Given 2개의 지하철 노선을 생성하고
     *  - When 지하철 노선 목록을 조회하면
     *  - Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void Should_지하철노선을_생성하고_When_지하철노선을_조회하면_Then_지하철노선이_조회된다() {
        //given
        var 이호선_request = new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L);
        ExtractableResponse<Response> 이호선_response = 지하철노선을_생성한다(이호선_request);
        지하철노선이_정상적으로_생성(이호선_response);

        var 신분당선_request = new LineRequest("신분당선", "red", 신논현역.getId(), 강남역.getId(), 5L);
        ExtractableResponse<Response> 신분당선_response = 지하철노선을_생성한다(신분당선_request);
        지하철노선이_정상적으로_생성(신분당선_response);

        // when
        ExtractableResponse<Response> linesResponse = 지하철노선을_조회한다();

        // then
        List<LineResponse> lines = 지하철노선_목록이_정상적으로_조회(linesResponse);
        assertAll(() -> {
            assertThat(lines).hasSize(2);
            assertThat(List.of(역삼역.getId(), 강남역.getId(), 신논현역.getId()))
                    .containsExactlyInAnyOrderElementsOf(
                            lines.stream()
                                    .flatMap(line -> line.getStationIds().stream())
                                    .distinct()
                                    .collect(Collectors.toList()));
        });
    }

    /**
     * 지하철노선 조회
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 조회하면
     *  - Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("특정 지하철 노선을 조회한다.")
    @Test
    void Should_지하철노선을_생성하고_When_특정한_지하철노선을_조회하면_Then_특정한_지하철노선이_조회된다() {
        // given
        LineResponse 이호선_생성_응답 = 지하철노선이_생성됨(new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L));
        LineResponse 이호선 = 노선목록에서_특정_지하철역을_조회(이호선_생성_응답.getId());

        // when
        ExtractableResponse<Response> lineResponse = 특정_지하철노선을_조회한다(이호선_생성_응답.getId());

        // then
        var 이호선_단일_조회 = 지하철노선이_정상적으로_조회(lineResponse);
        assertAll(() -> {
            assertThat(이호선_단일_조회.getName()).isEqualTo("2호선");
            assertThat(이호선_단일_조회.getStationIds()).containsExactlyInAnyOrderElementsOf(List.of(강남역.getId(), 역삼역.getId()));
        });
    }

    /**
     * 지하철노선 수정
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 수정하면
     *  - Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void Should_지하철노선을_생성하고_When_지하철노선을_수정하면_Then_해당_지하철노선이_수정된다() {
        // given
        var 이호선_request = new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L);
        LineResponse 이호선_생성_응답 = 지하철노선이_생성됨(이호선_request);
        LineResponse 이호선 = 노선목록에서_특정_지하철역을_조회(이호선_생성_응답.getId());

        //when
        var 이호선_update_request = new LineRequest(이호선_request.getName(), "green", 강남역.getId(), 역삼역.getId(), 이호선_request.getDistance());
        ExtractableResponse<Response> lineResponse = 지하철노선을_수정한다(이호선.getId(), 이호선_update_request);

        // then
        assertUtils.assertHttpStatus(lineResponse.statusCode(), HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> updateLineResponse = 특정_지하철노선을_조회한다(이호선.getId());
        var 이호선_update = 지하철노선이_정상적으로_조회(updateLineResponse);
        assertAll(() -> {
            assertThat(이호선_update.getName()).isEqualTo("2호선");
            assertThat(이호선_update.getColor()).isEqualTo("green");
            assertThat(이호선_update.getStationIds()).containsExactlyInAnyOrderElementsOf(List.of(강남역.getId(), 역삼역.getId()));
        });
    }

    /**
     * 지하철노선 삭제
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 삭제하면
     *  - Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void Should_지하철노선을_생성하고_When_지하철노선을_삭제하면_Then_해당_지하철노선이_삭제된다() {
        // given
        LineResponse 이호선_생성_응답 = 지하철노선이_생성됨(new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L));
        LineResponse 이호선 = 노선목록에서_특정_지하철역을_조회(이호선_생성_응답.getId());

        //when
        ExtractableResponse<Response> lineResponse = 지하철노선을_삭제한다(이호선.getId());

        // then
        assertUtils.assertHttpStatus(lineResponse.statusCode(), HttpStatus.NO_CONTENT.value());

        // then
        var linesResponseLastest = 지하철노선을_조회한다();
        List<LineResponse> lines_lastest = 지하철노선_목록이_정상적으로_조회(linesResponseLastest);
        assertThat(lines_lastest).isEmpty();
    }

    private LineResponse 노선목록에서_특정_지하철역을_조회(Long id) {
        ExtractableResponse<Response> linesResponse = 지하철노선을_조회한다();
        List<LineResponse> lines = 지하철노선_목록이_정상적으로_조회(linesResponse);
        return lines.stream()
                .filter(line -> id.equals(line.getId()))
                .findFirst()
                .get();
    }

    private static ExtractableResponse<Response> 지하철노선을_생성한다(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_조회한다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 특정_지하철노선을_조회한다(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/lines/%d", id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_수정한다(Long id, LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(String.format("/lines/%d", id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_삭제한다(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("/lines/%d", id))
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철노선이_생성됨(LineRequest request) {
        ExtractableResponse<Response> response = 지하철노선을_생성한다(request);
        지하철노선이_정상적으로_생성(response);
        return response.as(LineResponse.class);
    }

    private static void 지하철노선이_정상적으로_생성(ExtractableResponse<Response> response) {
        assertUtils.assertHttpStatus(response.statusCode(), HttpStatus.CREATED.value());
    }

    private List<LineResponse> 지하철노선_목록이_정상적으로_조회(ExtractableResponse<Response> response) {
        assertUtils.assertHttpStatus(response.statusCode(), HttpStatus.OK.value());
        return response.jsonPath().getList(".", LineResponse.class);
    }

    private static LineResponse 지하철노선이_정상적으로_조회(ExtractableResponse<Response> response) {
        assertUtils.assertHttpStatus(response.statusCode(), HttpStatus.OK.value());
        return response.as(LineResponse.class);
    }

    public static LineResponse 지하철노선이_조회됨(Long id) {
        ExtractableResponse<Response> lineResponse = 특정_지하철노선을_조회한다(id);
        return 지하철노선이_정상적으로_조회(lineResponse);
    }
}
