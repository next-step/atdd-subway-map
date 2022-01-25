package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.station.domain.dto.StationRequest;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private StationStep stationStep;
    private LineStep lineStep;
    private SectionStep sectionStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        
        lineStep = new LineStep();
        stationStep = new StationStep();
        sectionStep = new SectionStep();

        stationStep.지하철역_생성_요청();
        stationStep.지하철역_생성_요청();
    }

    /**
     * Given 노션에 등록할 지하철 역을 등록하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given, when
        ExtractableResponse<Response> response = lineStep.지하철_노선_생성_요청();

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
        String name = lineStep.nextName();
        LineRequest preRequest = lineStep.dummyRequest();
        LineRequest postRequest = lineStep.dummyRequest();
        preRequest.setName(name);
        postRequest.setName(name);
        stationStep.지하철역_생성_요청();
        lineStep.지하철_노선_생성_요청(preRequest);

        // when
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(postRequest);

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
        List<LineRequest> requests = LongStream.iterate(1, index -> index + 2)
                                               .limit(5)
                                               .boxed()
                                               .map(index -> {
                                                   stationStep.지하철역_생성_요청();
                                                   stationStep.지하철역_생성_요청();
                                                   LineRequest lineRequest = lineStep.dummyRequest();
                                                   lineRequest.setName(index + "호선");
                                                   lineRequest.setUpStationId(index);
                                                   lineRequest.setDownStationId(index + 1);
                                                   lineStep.지하철_노선_생성_요청(lineRequest);
                                                   return lineRequest;
                                               })
                                               .collect(Collectors.toList());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();

        List<String> names = requests.stream()
                                     .map(LineRequest::getName)
                                     .collect(Collectors.toList());
        //then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("name", names);
    }

    /**
     * Given 지하철 노선을 등록하고
     * And 지하철 구간을 등록하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선과 구간에 등록된 지하철역을 포함해 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        long lineId = 1;
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청();
        List<StationRequest> stationRequests = LongStream.iterate(1, i -> i + 1)
                                                         .limit(10)
                                                         .boxed()
                                                         .map(index -> {
                                                             StationRequest stationRequest = stationStep.dummyRequest();
                                                             stationRequest.setName(index + "역");
                                                             stationStep.지하철역_생성_요청(stationRequest);
                                                             sectionStep.지하철_구간_생성_요청(lineId, index, index + 1);
                                                             return stationRequest;
                                                         })
                                                         .collect(Collectors.toList());

        // when
        ExtractableResponse<Response> response =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.GET);

        List<String> names = stationRequests.stream()
                                            .map(StationRequest::getName)
                                            .collect(Collectors.toList());
        names.add(0, "2역");
        names.add(0, "1역");

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("stations.name", names);
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
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청();

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", lineStep.nextName());
        params.put("color", lineStep.nextColor());

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
        LineRequest request = lineStep.dummyRequest();
        lineStep.지하철_노선_생성_요청(request);
        stationStep.지하철역_생성_요청();
        stationStep.지하철역_생성_요청();
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(2, 3);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", request.getName());
        params.put("color", lineStep.nextColor());

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, params);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT)
                          .equalsErrorMessage(
                              ErrorMessage.DUPLICATE_COLUMN.getMessage(ColumnName.LINE_NAME.getName())
                          );
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
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청();

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
