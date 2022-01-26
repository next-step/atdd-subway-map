package nextstep.subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    @Autowired
    private StationStep stationStep;
    @Autowired
    private LineStep lineStep;
    @Autowired
    private SectionStep sectionStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        stationStep.지하철역_생성_요청("1역");
        stationStep.지하철역_생성_요청("2역");
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
        ExtractableResponse<Response> response = lineStep.지하철_노선_생성_요청(
            LineStep.DUMMY_NAME, LineStep.DUMMY_UP_STATION_ID, LineStep.DUMMY_DOWN_STATION_ID
        );

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .existsLocation();
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
        lineStep.지하철_노선_생성_요청(
            LineStep.DUMMY_NAME, LineStep.DUMMY_UP_STATION_ID, LineStep.DUMMY_DOWN_STATION_ID
        );
        stationStep.지하철역_생성_요청("3역");
        stationStep.지하철역_생성_요청("4역");

        // when
        ExtractableResponse<Response> postResponse = lineStep.지하철_노선_생성_요청(
            LineStep.DUMMY_NAME, 3L, 4L
        );

        // then
        AcceptanceTestThen.fromWhen(postResponse)
                          .equalsHttpStatus(HttpStatus.CONFLICT)
                          .notExistsLocation();
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
        String STATION_NAME_SUFFIX = "-역";
        String LINE_NAME_SUFFIX = "-호선";
        List<String> names = LongStream.iterate(1, index -> index + 2)
                                       .limit(5)
                                       .boxed()
                                       .map(index -> {
                                           String lineName = index + LINE_NAME_SUFFIX;
                                           stationStep.지하철역_생성_요청(
                                               index + STATION_NAME_SUFFIX
                                           );
                                           stationStep.지하철역_생성_요청(
                                               (index + 1) + STATION_NAME_SUFFIX
                                           );
                                           lineStep.지하철_노선_생성_요청(
                                               index + LINE_NAME_SUFFIX, index, index + 1
                                           );
                                           return lineName;
                                       })
                                       .collect(Collectors.toList());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();
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
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(
            LineStep.DUMMY_NAME, 1L, 2L
        );
        List<String> stationNames = LongStream.iterate(3, i -> i + 1)
                                                         .limit(10)
                                                         .boxed()
                                                         .map(index -> {
                                                             String stationName = index + "역";
                                                             stationStep.지하철역_생성_요청(stationName);
                                                             sectionStep.지하철_구간_생성_요청(lineId, index - 1, index);
                                                             return stationName;
                                                         })
                                                         .collect(Collectors.toList());

        // when
        ExtractableResponse<Response> response =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.GET);

        stationNames.add(0, "2역");
        stationNames.add(0, "1역");

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("stations.name", stationNames);
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
        LineRequest request = lineStep.dummyRequest();
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(request);

        // when
        request.setName("변경된 이름");
        request.setColor("변경된 색상");

        ExtractableResponse<Response> editResponse =
            AcceptanceTestWhen.fromGiven(createResponse)
                              .requestLocation(Method.PUT, request);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.OK);
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
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(
            lineStep.dummyRequest()
        );

        // when
        AcceptanceTestWhen when = AcceptanceTestWhen.fromGiven(createResponse);
        ExtractableResponse<Response> deleteResponse = when.requestLocation(Method.DELETE);
        ExtractableResponse<Response> findResponse = when.requestLocation(Method.GET);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.NO_CONTENT);
        AcceptanceTestThen.fromWhen(findResponse)
                          .equalsHttpStatus(HttpStatus.NOT_FOUND);
    }
}
