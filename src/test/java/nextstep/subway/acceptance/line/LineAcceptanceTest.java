package nextstep.subway.acceptance.line;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.utils.AcceptanceTestThen;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private static final Long FIRST_LINE_ID = 1L;
    @Autowired
    private StationStep stationStep;
    @Autowired
    private LineStep lineStep;

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
        // when
        ExtractableResponse<Response> createResponse = lineStep.지하철_노선_생성_요청(
            LineStep.DUMMY_NAME, LineStep.DUMMY_UP_STATION_ID, LineStep.DUMMY_DOWN_STATION_ID
        );
        ExtractableResponse<Response> findResponse = lineStep.지하철_노선_조회_요청(FIRST_LINE_ID);

        // then
        AcceptanceTestThen.fromWhen(createResponse)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .existsLocation();
        AcceptanceTestThen.fromWhen(findResponse)
                          .equalsHttpStatus(HttpStatus.OK);
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
        String 일호선 = "1호선";
        String 이호선 = "2호선";

        lineStep.지하철_노선_생성_요청(
            일호선, LineStep.DUMMY_UP_STATION_ID, LineStep.DUMMY_DOWN_STATION_ID
        );
        stationStep.지하철역_생성_요청("3역");
        stationStep.지하철역_생성_요청("4역");
        lineStep.지하철_노선_생성_요청(
            이호선, 3L, 4L
        );

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .when()
                       .get("/lines")
                       .then().log().all()
                       .extract();
        //then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("name", Arrays.asList(일호선, 이호선));
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
        lineStep.지하철_노선_생성_요청(LineStep.DUMMY_NAME, FIRST_LINE_ID, 2L);

        // when
        ExtractableResponse<Response> findResponse = lineStep.지하철_노선_조회_요청(FIRST_LINE_ID);

        // then
        AcceptanceTestThen.fromWhen(findResponse)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("stations.name", Arrays.asList("1역", "2역"));
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
        lineStep.지하철_노선_생성_요청(lineStep.dummyParams());

        // when
        LineRequest changeRequest = lineStep.dummyParams();
        changeRequest.setName("변경된 이름");
        changeRequest.setColor("변경된 색상");
        ExtractableResponse<Response> editResponse = lineStep.지하철_노선_수정_요청(FIRST_LINE_ID, changeRequest);
        ExtractableResponse<Response> findResponse = lineStep.지하철_노선_조회_요청(FIRST_LINE_ID);

        // then
        AcceptanceTestThen.fromWhen(editResponse)
                          .equalsHttpStatus(HttpStatus.OK);
        AcceptanceTestThen.fromWhen(findResponse)
                          .equalsHttpStatus(HttpStatus.OK)
                          .equalsResult("name", changeRequest.getName())
                          .equalsResult("color", changeRequest.getColor());
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
        lineStep.지하철_노선_생성_요청(lineStep.dummyParams());

        // when
        ExtractableResponse<Response> deleteResponse = lineStep.지하철_노선_삭제_요청(FIRST_LINE_ID);
        ExtractableResponse<Response> getResponse = lineStep.지하철_노선_조회_요청(FIRST_LINE_ID);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.NO_CONTENT);
        AcceptanceTestThen.fromWhen(getResponse)
                          .equalsHttpStatus(HttpStatus.NOT_FOUND);
    }
}
