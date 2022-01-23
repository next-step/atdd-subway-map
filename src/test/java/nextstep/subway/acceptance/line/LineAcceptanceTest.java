package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.section.domain.model.Distance;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;
import nextstep.subway.utils.DatabaseCleanup;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * When 지하철 역이 이미 2개 존재하고
     * Then 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();

        // given, when
        ExtractableResponse<Response> response = LineStep.지하철_노선_생성_요청(new Distance(100));

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .hasLocation();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 실패 - 중복 이름")
    @Test
    void createLineThatFailing() {
        LineRequest request = LineRequest.builder()
            .name(LineStep.nextRequest())
            .color(LineStep.nextColor())
            .build();

        // given
        LineStep.지하철_노선_생성_요청(request);

        // when
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(request);

        // then
        AcceptanceTestThen.fromWhen(createResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT)
                          .hasNotLocation();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        LineStep.지하철_노선_생성_요청(new Distance(100));
        LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();

        //then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        ExtractableResponse<Response> response =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.GET);

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", LineStep.nextRequest());
        params.put("color", LineStep.nextColor());

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, params);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.OK);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운(수정할) 지하철 노선 생성을 요청 하고
     * When 이미 존재하는 지하철 노선 이름으로 새로운 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 실패한다.
     */
    @DisplayName("지하철 노선 수정 실패 - 중복 이름")
    @Test
    void updateLineThatFailing() {
        // given
        LineRequest request = LineRequest.builder()
            .name(LineStep.nextRequest())
            .color(LineStep.nextColor())
            .build();

        LineStep.지하철_노선_생성_요청(request);
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", request.getName());
        params.put("color", LineStep.nextColor());

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, params);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        AcceptanceTestWhen when = AcceptanceTestWhen.fromGiven(createResponse);
        ExtractableResponse<Response> deleteResponse = when.requestLocation(Method.DELETE);
        ExtractableResponse<Response> findResponse = when.requestLocation(Method.GET);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.NO_CONTENT);
        AcceptanceTestThen.fromWhen(findResponse)
                          .equalsHttpStatus(HttpStatus.NOT_IMPLEMENTED);
    }
}
