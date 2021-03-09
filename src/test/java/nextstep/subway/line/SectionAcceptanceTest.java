package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.LineSteps.Line.분당선;
import static nextstep.subway.line.LineSteps.Line.일호선;
import static nextstep.subway.line.LineSteps.지하철_노선_생성요청;
import static nextstep.subway.station.StationSteps.Station.강남역;
import static nextstep.subway.station.StationSteps.Station.역삼역;
import static nextstep.subway.station.StationSteps.지하철역_생성요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long upStationId;
    private Long downStationId;
    private int distance;

    private LineRequest lineRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        upStationId = 지하철역_생성요청(강남역.name).as(StationResponse.class).getId();
        downStationId = 지하철역_생성요청(역삼역.name).as(StationResponse.class).getId();
        distance = 10;

        lineRequest = new LineRequest(일호선.name, 일호선.color, upStationId, downStationId, distance);
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // given (지하철_노선_생성됨)
        LineResponse lineResponse = 지하철_노선_생성요청(lineRequest).as(LineResponse.class);
        Long lineId = lineResponse.getId();

        // when (지하철노선_구간_등록요청)
        SectionRequest request = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(request)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();

        // then (지하철노선_구간_등록됨)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteLine() {
        // 지하철노선_구간_등록요청
        // 지하철노선_구간_제거요청
        // 지하철노선_구간_삭제됨
    }

    @DisplayName("지하철 노선에 등록된 구간 정보로 역목록을 조회한다.")
    @Test
    void getStationsBySectionInformation() {
        // 지하철노선_구간_등록요청
        // 지하철노선_구간역목록_조회요청
        // 지하철노선_구간역록목_응답됨
        // 지하철노선_구간역록목_포함됨
    }
}
