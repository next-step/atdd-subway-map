package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.지하철_노선_생성요청;
import static nextstep.subway.section.SectionSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private LineResponse pinkLine;
    private StationResponse 천호역;
    private StationResponse 송파역;
    private StationResponse 잠실역;
    private StationResponse 석촌역;
    private StationResponse 남한산성입구역;


    @BeforeEach
    void setup() {
        int distance = 4;

        천호역 = 지하철역_생성_요청("천호역").as(StationResponse.class);
        송파역 = 지하철역_생성_요청("송파역").as(StationResponse.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(StationResponse.class);
        석촌역 = 지하철역_생성_요청("석촌역").as(StationResponse.class);
        남한산성입구역 = 지하철역_생성_요청("남한산성입구역").as(StationResponse.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "8호선");
        map.put("color", "pink");
        map.put("upStationId", 석촌역.getId());
        map.put("downStationId", 남한산성입구역.getId());
        map.put("distance", distance);

        pinkLine = 지하철_노선_생성요청(map).as(LineResponse.class);
    }

    @DisplayName("지하철 노선 구간 추가 한다.")
    @Test
    void createLineSection() {
        ExtractableResponse<Response> createResponse =
                지하철_노선에_구간_등록_요청(석촌역.getId(), 남한산성입구역.getId(), pinkLine.getId(), 3);

        지하철_노선_구간_응답_확인(createResponse.statusCode(), HttpStatus.CREATED);
    }


    @DisplayName("지하철 노선 구간 역 목록을 조회한다.")
    @Test
    void getLineSection() {

    }

    @DisplayName("지하철 노선 구간 제거 한다.")
    @Test
    void deleteLineSection() {
        지하철_노선에_구간_등록_요청(석촌역.getId(), 남한산성입구역.getId(), pinkLine.getId(), 4);

        ExtractableResponse<Response> remove = 지하철_구간_제거_요청(pinkLine.getId(), 남한산성입구역.getId());

        지하철_노선_구간_응답_확인(remove.statusCode(), HttpStatus.NO_CONTENT);
    }
}
