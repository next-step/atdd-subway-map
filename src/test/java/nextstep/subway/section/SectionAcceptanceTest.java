package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.section.SectionSteps.*;
import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private StationResponse 석촌역;
    private StationResponse 남한산성입구역;
    private StationResponse 산성역;
    private LineRequest lineRequest;

    @BeforeEach
    void setup() {
        super.setUp();

        int distance = 4;
        석촌역 = 지하철역_생성_요청("석촌역").as(StationResponse.class);
        남한산성입구역 = 지하철역_생성_요청("남한산성입구역").as(StationResponse.class);
        산성역 = 지하철역_생성_요청("산성역").as(StationResponse.class);

        lineRequest = new LineRequest("8호선", "pink", 석촌역.getId(), 남한산성입구역.getId(), distance);

    }

    @DisplayName("지하철 노선 구간 추가 한다.")
    @Test
    void createLineSection() {
        LineResponse pinkLine = 지하철_노선_생성요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(남한산성입구역.getId(), 산성역.getId(), 3);
        ExtractableResponse<Response> createResponse =
                지하철_노선에_구간_등록_요청(sectionRequest, pinkLine.getId());

        지하철_노선_구간_응답_확인(createResponse.statusCode(), HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 구간 제거 한다.")
    @Test
    void deleteLineSection() {
        LineResponse pinkLine = 지하철_노선_생성요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(남한산성입구역.getId(), 산성역.getId(), 3);
        지하철_노선에_구간_등록_요청(sectionRequest, pinkLine.getId());

        ExtractableResponse<Response> remove = 지하철_구간_제거_요청(pinkLine.getId(), 산성역.getId());

        지하철_노선_구간_응답_확인(remove.statusCode(), HttpStatus.NO_CONTENT);
    }

    @DisplayName("지하철 노선 구간 역 목록을 조회한다.")
    @Test
    void getLineSection() {
        LineResponse pinkLine = 지하철_노선_생성요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(남한산성입구역.getId(), 산성역.getId(), 3);

        지하철_노선에_구간_등록_요청(sectionRequest, pinkLine.getId());

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(pinkLine);

        지하철_노선_응답_확인(response.statusCode(), HttpStatus.OK);

        List<Long> stationIds = stationResponseToList(지하철역_조회_요청());
        List<Long> resultStationIds = Stream.of(석촌역, 남한산성입구역, 산성역)
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        지하철역_목록_응답_확인(stationIds, resultStationIds);
    }

    List<Long> stationResponseToList(ExtractableResponse<Response> stationResponse) {
        return stationResponse.jsonPath().getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
