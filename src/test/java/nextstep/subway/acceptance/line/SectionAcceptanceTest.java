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
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Distance;
import nextstep.subway.utils.AcceptanceTestThen;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineStep lineStep;
    private StationStep stationStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationStep = new StationStep();
        lineStep = new LineStep(stationStep);
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
            .upStationId((long) 2)
            .downStationId((long) 3)
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
     * And   등록할 구간의 상행역이 등록되어있는 구간의 하행역이 아닐때
     * When  구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 상행역이 등록되어있는 구간의 하행역이 아닐때")
    @Test
    void addSectionThatFailing1() {
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId((long) 3)
            .downStationId((long) 2)
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
                          .hasLocation();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * And   등록할 구간의 하행역이 구간에 이미 등록되있는 역일때
     * When  구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 하행역이 구간에 이미 등록되있는 역일때 ")
    @Test
    void addSectionThatFailing2() {
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId((long) 3)
            .downStationId((long) 1)
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
                          .hasLocation();
    }
}
