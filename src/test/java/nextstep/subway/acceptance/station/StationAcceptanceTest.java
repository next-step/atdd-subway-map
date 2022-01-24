package nextstep.subway.acceptance.station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.station.domain.dto.StationRequest;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    private StationStep stationStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationStep = new StationStep();
    }

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given, when
        ExtractableResponse<Response> response = stationStep.지하철역_생성_요청();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .hasLocation();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 생성 실패 - 중복 이름")
    @Test
    void createStationThatFailing() {
        // given
        String name = stationStep.nextName();
        StationRequest preRequest = stationStep.dummyRequest();
        StationRequest postRequest = stationStep.dummyRequest();

        preRequest.setName(name);
        postRequest.setName(name);
        stationStep.지하철역_생성_요청(preRequest);

        // when
        ExtractableResponse<Response> response = stationStep.지하철역_생성_요청(postRequest);

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CONFLICT)
                          .equalsErrorMessage(
                              ErrorMessage.DUPLICATE_COLUMN.getMessage(ColumnName.STATION_NAME.getName())
                          )
                          .hasNotLocation();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @ValueSource(ints = {
        5, 10
    })
    @DisplayName("지하철역 목록 조회")
    @ParameterizedTest
    void getStations(int size) {
        /// given
        List<StationRequest> requests = Stream.generate(stationStep::dummyRequest)
                                              .limit(size)
                                              .collect(Collectors.toList());
        requests.forEach(stationStep::지하철역_생성_요청);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/stations")
                                                            .then().log().all()
                                                            .extract();

        // then
        List<String> names = requests.stream()
                                     .map(StationRequest::getName)
                                     .collect(Collectors.toList());
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.OK)
                          .containsAll("name", names);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = stationStep.지하철역_생성_요청();

        // when
        AcceptanceTestWhen when = AcceptanceTestWhen.fromGiven(createResponse);
        ExtractableResponse<Response> deleteResponse = when.requestLocation(Method.DELETE);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.NO_CONTENT);
    }
}
