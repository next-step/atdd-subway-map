package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.sun.tools.javac.util.List;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.model.Distance;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * Given 노션에 등록할 지하철 역을 등록하고
     * When 지하철 노선 생성을 요청 하면
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
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 지하철 노선을 등록하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 실패 - 중복 이름")
    @Test
    void createLineThatFailing() {
        // given
        LineRequest preCreateRequest = LineRequest.builder()
            .name(LineStep.nextName())
            .color(LineStep.nextColor())
            .upStationId((long) 1)
            .upStationId((long) 2)
            .distance(new Distance(100))
            .build();

        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        LineStep.지하철_노선_생성_요청(preCreateRequest);

        // when
        LineRequest postCreateRequest = LineRequest.builder()
            .name(preCreateRequest.getName())
            .color(LineStep.nextColor())
            .upStationId((long) 3)
            .upStationId((long) 4)
            .distance(new Distance(100))
            .build();
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(postCreateRequest);

        // then
        AcceptanceTestThen.fromWhen(createResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT)
                          .hasNotLocation();
    }

    /**
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 새로운 지하철 노선을 등록하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        LineRequest lineRequest1 = LineRequest.builder()
            .name(LineStep.nextName())
            .color(LineStep.nextColor())
            .upStationId((long) 1)
            .upStationId((long) 2)
            .distance(new Distance(100))
            .build();
        LineStep.지하철_노선_생성_요청(lineRequest1);

        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        LineRequest lineRequest2 = LineRequest.builder()
            .name(LineStep.nextName())
            .color(LineStep.nextColor())
            .upStationId((long) 3)
            .upStationId((long) 4)
            .distance(new Distance(100))
            .build();
        LineStep.지하철_노선_생성_요청(lineRequest2);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();

        //then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("name",
                                       List.of(lineRequest1.getName(), lineRequest2.getName())
                          );
    }

    /**
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 지하철 노선을 등록하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
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
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 지하철 노선을 등록하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(new Distance(100));

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", LineStep.nextName());
        params.put("color", LineStep.nextColor());

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, params);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.OK);
    }

    /**
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 지하철 노선 생성을 요청 하고
     * And 노선에 등록할 지하철 역을 등록하고
     * And 새로운(수정할) 지하철 노선 등록하고
     * When 이미 존재하는 지하철 노선 이름으로 새로운 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 실패한다.
     */
    @DisplayName("지하철 노선 수정 실패 - 중복 이름")
    @Test
    void updateLineThatFailing() {
        // given
        LineRequest preCreateRequest = LineRequest.builder()
            .name(LineStep.nextName())
            .color(LineStep.nextColor())
            .upStationId((long) 1)
            .upStationId((long) 2)
            .distance(new Distance(100))
            .build();
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        LineStep.지하철_노선_생성_요청(preCreateRequest);

        LineRequest postCreateRequest = LineRequest.builder()
            .name(LineStep.nextName())
            .color(LineStep.nextColor())
            .upStationId((long) 3)
            .upStationId((long) 4)
            .distance(new Distance(100))
            .build();
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
        ExtractableResponse<Response> createResponse = LineStep.지하철_노선_생성_요청(postCreateRequest);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", preCreateRequest.getName());
        params.put("color", LineStep.nextColor());

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, params);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT);
    }

    /**
     * Given 노선에 등록할 지하철 역을 등록하고
     * And 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        StationStep.지하철역_생성_요청();
        StationStep.지하철역_생성_요청();
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
