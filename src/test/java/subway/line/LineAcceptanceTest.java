package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.common.Endpoints;
import subway.line.presentation.CreateLineRequest;
import subway.line.presentation.LineResponse;
import subway.line.presentation.UpdateLineRequest;
import subway.utils.RestAssuredClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestHelper.생성_헤더;
import static subway.common.TestHelper.응답_코드가_일치한다;
import static subway.line.LineFixtures.노선을_생성한다;
import static subway.line.LineFixtures.노선의_정보가_일치한다;
import static subway.station.StationFixtures.강남역_생성_요청;
import static subway.station.StationFixtures.낙성대역_생성_요청;
import static subway.station.StationFixtures.서울대입구역_생성_요청;
import static subway.station.StationFixtures.신논현역_생성_요청;
import static subway.station.StationFixtures.지하철역을_생성한다;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private long 강남역_아이디;
    private long 서울대입구역_아이디;
    private long 신논현역_아이디;
    private long 낙성대역_아이디;
    private CreateLineRequest 신분당선_생성_요청;
    private CreateLineRequest 이호선_생성_요청;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        강남역_아이디 = 지하철역을_생성한다(강남역_생성_요청);
        서울대입구역_아이디 = 지하철역을_생성한다(서울대입구역_생성_요청);
        신논현역_아이디 = 지하철역을_생성한다(낙성대역_생성_요청);
        낙성대역_아이디 = 지하철역을_생성한다(신논현역_생성_요청);

        신분당선_생성_요청 = new CreateLineRequest(
                "신분당선",
                LineFixtures.RED,
                강남역_아이디,
                서울대입구역_아이디,
                10L
        );
        이호선_생성_요청 = new CreateLineRequest(
                "2호선",
                LineFixtures.BLUE,
                신논현역_아이디,
                낙성대역_아이디,
                10L
        );
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // Given 지하철 노선을 생성하면
        var response = RestAssuredClient.post(
                Endpoints.LINES,
                new CreateLineRequest(
                        "신분당선",
                        LineFixtures.RED,
                        강남역_아이디,
                        서울대입구역_아이디,
                        10L
                )
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);

        // When 지하철 노선 목록 조회 시
        List<LineResponse> lineResponses = RestAssuredClient.get(Endpoints.LINES).jsonPath().getList(".", LineResponse.class);

        // Then 생성한 노선을 찾을 수 있다.
        assertThat(lineResponses).hasSize(1);
        var 신분당선_응답 = lineResponses.get(0);
        노선의_정보가_일치한다(
                신분당선_응답,
                "신분당선",
                LineFixtures.RED,
                List.of(강남역_아이디, 서울대입구역_아이디)
        );
    }

    @DisplayName("여러 개의 지하철 노선을 조회한다.")
    @Test
    void findAllLines() {
        // Given 지하철 노선을 생성하고
        var 신분당선_생성_응답 = 노선을_생성한다(신분당선_생성_요청);
        var 이호선_생성_응답 = 노선을_생성한다(이호선_생성_요청);

        // When 지하철 노선 목록을 조회하면
        List<LineResponse> lineResponses = RestAssuredClient.get(Endpoints.LINES).jsonPath().getList(".", LineResponse.class);

        // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(lineResponses).hasSize(2);
        var lineNames = lineResponses.stream().map(LineResponse::getName).collect(Collectors.toList());
        assertThat(lineNames).containsExactly(신분당선_생성_요청.getName(), 이호선_생성_요청.getName());
    }

    @DisplayName("특정 지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        // Given 지하철 노선을 생성하고
        var 신분당선_생성_응답 = 노선을_생성한다(신분당선_생성_요청);

        // When 생성한 지하철 노선을 조회하면
        var 신분당선_응답 = RestAssuredClient.get(생성_헤더(신분당선_생성_응답)).as(LineResponse.class);

        // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        노선의_정보가_일치한다(
                신분당선_응답,
                신분당선_생성_요청.getName(),
                신분당선_생성_요청.getColor(),
                List.of(신분당선_생성_요청.getUpStationId(),
                신분당선_생성_요청.getDownStationId())
        );
    }

    @DisplayName("특정 지하철 노선을 수정한다.")
    @Test
    void updateLineById() {
        // Given 지하철 노선을 생성하고
        var 신분당선_생성_응답 = 노선을_생성한다(신분당선_생성_요청);

        // When 생성한 지하철 노선을 수정하면
        String updateLineName = "4호선";
        String updateColor = LineFixtures.GREEN;
        var updateLineResponse = RestAssuredClient.put(
                생성_헤더(신분당선_생성_응답),
                new UpdateLineRequest(
                        updateLineName,
                        updateColor
                )
        );

        // Then line is updated.
        var 사호선_응답 = updateLineResponse.as(LineResponse.class);
        노선의_정보가_일치한다(
                사호선_응답,
                updateLineName,
                updateColor,
                List.of(신분당선_생성_요청.getUpStationId(),
                신분당선_생성_요청.getDownStationId())
        );
    }

    @DisplayName("특정 지하철 노선을 삭제한다.")
    @Test
    void deleteLineById() {
        // Given 지하철 노선을 생성하고
        var 신분당선_생성_응답 = 노선을_생성한다(신분당선_생성_요청);

        // When 생성한 노선을 삭제하면
        ExtractableResponse<Response> 신분당선_삭제_응답 = RestAssuredClient.delete(생성_헤더(신분당선_생성_응답));
        응답_코드가_일치한다(신분당선_삭제_응답.statusCode(), HttpStatus.NO_CONTENT);

        // Then 해당 지하철 노선 정보는 삭제된다.
        // = 해당 지하철 노선 정보 조회 시 NOT_FOUND를 응답받는다.
        ExtractableResponse<Response> 신분당선_조회_응답 = RestAssuredClient.get(생성_헤더(신분당선_생성_응답));
        응답_코드가_일치한다(신분당선_조회_응답.statusCode(), HttpStatus.NOT_FOUND);
    }
}
