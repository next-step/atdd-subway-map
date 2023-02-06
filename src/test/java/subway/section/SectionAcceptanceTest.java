package subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.common.Endpoints;
import subway.line.LineFixtures;
import subway.line.presentation.AddSectionRequest;
import subway.line.presentation.CreateLineRequest;
import subway.line.presentation.LineResponse;
import subway.station.presentation.StationResponse;
import subway.utils.RestAssuredClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestHelper.응답_코드가_일치한다;
import static subway.station.StationFixtures.강남역_생성_요청;
import static subway.station.StationFixtures.서울대입구역_생성_요청;
import static subway.station.StationFixtures.신논현역_생성_요청;
import static subway.station.StationFixtures.지하철역을_생성한다;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest extends AcceptanceTest {
    private long 강남역_아이디;
    private long 서울대입구역_아이디;
    private long 신논현역_아이디;
    private CreateLineRequest 신분당선_생성_요청;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        강남역_아이디 = 지하철역을_생성한다(강남역_생성_요청);
        서울대입구역_아이디 = 지하철역을_생성한다(서울대입구역_생성_요청);
        신논현역_아이디 = 지하철역을_생성한다(신논현역_생성_요청);

        신분당선_생성_요청 = new CreateLineRequest(
                "신분당선",
                LineFixtures.RED,
                서울대입구역_아이디,
                강남역_아이디,
                10L
        );
    }

    @Test
    void 노선에_구간을_등록한다() {
        // Given 노선에 구간을 등록하면
        // 서울대입구역 - 신논현역 - 강남역
        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();
        var upStationId = 강남역_아이디;
        var downStationId = 신논현역_아이디;
        var distance = 10L;

        var response = RestAssuredClient.post(
                Endpoints.sections(노선_아이디),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);

        // When 노선 조회 시
        var 신분당선_조회_응답 = RestAssuredClient.get(
                Endpoints.endpointWithParam(Endpoints.LINES, 노선_아이디)
        );

        // Then 등록된 구간이 조회된다.
        노선의_정보가_일치한다(
                신분당선_조회_응답.as(LineResponse.class),
                신분당선_생성_요청.getName(),
                신분당선_생성_요청.getColor()
        );
        노선이_해당_역을_정확히_포함한다(신분당선_조회_응답.as(LineResponse.class), List.of(서울대입구역_생성_요청.getName(), 강남역_생성_요청.getName(), 신논현역_생성_요청.getName()));
    }

    @Test
    void 구간_등록_시_하행역이_해당_노선에_등록되어있다면_예외를_던진다() {
        // When 노선에 등록된 역을 구간의 하행역으로 등록하면
        // Then 예외를 던진다.

        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();

        var upStationId = 강남역_아이디;
        var downStationId = 서울대입구역_아이디;
        var distance = 5L;

        var response = RestAssuredClient.post(
                Endpoints.sections(노선_아이디),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );

        응답_코드가_일치한다(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void 구간_등록_시_상행역과_하행역이_같다면_예외를_던진다() {
        // When 상행역과 하행역을 같은 역으로 등록하면
        // Then 예외를 던진다.

        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();

        var upStationId = 신논현역_아이디;
        var downStationId = 신논현역_아이디;
        var distance = 5L;

        var response = RestAssuredClient.post(
                Endpoints.sections(노선_아이디),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );

        응답_코드가_일치한다(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void 구간_등록_시_새로운_구간의_상행역이_해당_노선의_하행_종점역이_아니면_예외를_던진다() {
        // Given 구간 등록 시
        // When 상행역이 해당 노선의 하행 종점역이 아니면
        // Then 예외를 던진다.

        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();

        var upStationId = 서울대입구역_아이디;
        var downStationId = 신논현역_아이디;
        var distance = 5L;

        var response = RestAssuredClient.post(
                Endpoints.sections(노선_아이디),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );

        응답_코드가_일치한다(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    private long 하행역이_강남역인_노선을_생성한다() {
        신분당선_생성_요청 = new CreateLineRequest(
                "신분당선",
                LineFixtures.RED,
                서울대입구역_아이디,
                강남역_아이디,
                10L
        );
        return 노선을_생성하고_노선_아이디를_반환한다(신분당선_생성_요청);
    }

    @Test
    void 노선에_등록된_구간을_제거한다() {
        // Given 노선에 등록된 구간을 제거하면
        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();
        노선에_신논현역이_하행역인_구간을_등록한다(노선_아이디);

        var response = RestAssuredClient.delete(
                Endpoints.sections(노선_아이디),
                신논현역_아이디
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.NO_CONTENT);

        // When 노선 조회 시
        var 노선_조회_응답 = RestAssuredClient.get(
                Endpoints.endpointWithParam(Endpoints.LINES, 노선_아이디)
        );

        // Then 삭제한 구간이 조회되지 않는다.
        노선이_해당_역을_포함하지_않는다(노선_조회_응답.as(LineResponse.class), 신논현역_아이디);
    }

    @Test
    void 하행_종점역이_아닌_역을_제거하면_예외를_던진다() {
        // Given 구간 삭제 시
        // When 마지막 구간이 아닌 역을 제거하면
        // Then 예외를 던진다.

        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();
        노선에_신논현역이_하행역인_구간을_등록한다(노선_아이디);

        var response = RestAssuredClient.delete(
                Endpoints.sections(노선_아이디),
                강남역_아이디
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void 노선에_구간이_1개인_경우_역_삭제시_예외를_던진다() {
        // Given 구간 삭제 시
        // When 노선에 구간이 1개인 경우
        // Then 예외를 던진다.

        var 노선_아이디 = 하행역이_강남역인_노선을_생성한다();

        var response = RestAssuredClient.delete(
                Endpoints.sections(노선_아이디),
                강남역_아이디
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.BAD_REQUEST);
    }

    private void 노선에_신논현역이_하행역인_구간을_등록한다(long lineId) {
        var upStationId = 강남역_아이디;
        var downStationId = 신논현역_아이디;
        var distance = 10L;

        var response = RestAssuredClient.post(
                Endpoints.sections(lineId),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);
    }

    public static void 노선이_해당_역을_정확히_포함한다(
            LineResponse response,
            List<String> stationNames
    ) {
        assertThat(response.getStations().stream().map(StationResponse::getName))
                .containsExactly(stationNames.toArray(new String[stationNames.size()]));
    }

    public static void 노선이_해당_역을_포함하지_않는다(
            LineResponse lineResponse,
            long stationId
    ) {
        assertThat(lineResponse.getStations().stream().map(StationResponse::getId))
                .doesNotContain(stationId);
    }

    public static long 노선을_생성하고_노선_아이디를_반환한다(Object request) {
        var response = RestAssuredClient.post(
                Endpoints.LINES,
                request
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);
        return response.jsonPath().getLong("id");
    }

    public static void 노선의_정보가_일치한다(
            LineResponse response,
            String expectedLineName,
            String expectedColor
    ) {
        assertThat(response.getName()).isEqualTo(expectedLineName);
        assertThat(response.getColor()).isEqualTo(expectedColor);
    }
}
