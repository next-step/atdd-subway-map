package nextstep.subway.acceptance.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;
import nextstep.subway.utils.AcceptanceTestThen;

@DisplayName("구간 추가 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private static final long FIRST_SECTION_UP_STATION = 1;
    private static final long FIRST_SECTION_DOWN_STATION = 2;
    private static final long NEW_SECTION_UP_STATION = FIRST_SECTION_DOWN_STATION;
    private static final long NEW_SECTION_DOWN_STATION = 3;

    private LineStep lineStep;
    private StationStep stationStep;
    private SectionStep sectionStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationStep = new StationStep();
        lineStep = new LineStep(stationStep);
        sectionStep = new SectionStep();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  구간 등록을 요청한다.
     * Then  구간 등록이 성공한다.
     */
    @DisplayName("구간 등록")
    @Test
    void addSection() {
        // given
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId(NEW_SECTION_UP_STATION)
            .downStationId(NEW_SECTION_DOWN_STATION)
            .distance(new Distance(100))
            .build();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(sectionRequest)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when()
                                                            .post("/lines/1/sections")
                                                            .then().log().all()
                                                            .extract();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .hasLocation();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  등록할 구간의 상행역을 등록되어있는 구간의 하행역이 아니도록 구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 상행역이 등록되어있는 구간의 하행역이 아닐때")
    @Test
    void addSectionThatFailing1() {
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId(FIRST_SECTION_UP_STATION)
            .downStationId(NEW_SECTION_UP_STATION)
            .distance(new Distance(100))
            .build();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(sectionRequest)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when()
                                                            .post("/lines/1/sections")
                                                            .then().log().all()
                                                            .extract();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage(ErrorMessage.NOT_FOUND_SECTION_DOCKING_POINT.getMessage())
                          .hasNotLocation();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  등록할 구간의 하행역을 구간에 이미 등록되있는 역으로 구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 하행역이 구간에 이미 등록되있는 역일때 ")
    @Test
    void addSectionThatFailing2() {
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId(NEW_SECTION_UP_STATION)
            .downStationId(FIRST_SECTION_DOWN_STATION)
            .distance(new Distance(100))
            .build();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(sectionRequest)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when()
                                                            .post("/lines/1/sections")
                                                            .then().log().all()
                                                            .extract();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage(ErrorMessage.ALREADY_REGISTERED_STATION_IN_SECTION.getMessage())
                          .hasNotLocation();
    }
}
