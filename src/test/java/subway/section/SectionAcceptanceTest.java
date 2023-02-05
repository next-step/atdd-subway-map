package subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.common.Endpoints;
import subway.line.LineFixtures;
import subway.line.presentation.AddSectionRequest;
import subway.line.presentation.CreateLineRequest;
import subway.line.presentation.LineResponse;
import subway.utils.RestAssuredClient;

import static subway.common.TestHelper.응답_코드가_일치한다;
import static subway.line.LineFixtures.노선을_생성하고_노선_아이디를_반환한다;
import static subway.line.LineFixtures.노선의_정보가_일치한다;
import static subway.station.StationFixtures.강남역_생성_요청;
import static subway.station.StationFixtures.낙성대역_생성_요청;
import static subway.station.StationFixtures.서울대입구역_생성_요청;
import static subway.station.StationFixtures.신논현역_생성_요청;
import static subway.station.StationFixtures.지하철역을_생성한다;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private long 강남역_아이디;
    private long 서울대입구역_아이디;
    private long 신논현역_아이디;
    private long 낙성대역_아이디;
    private CreateLineRequest 신분당선_생성_요청;
    private CreateLineRequest 이호선_생성_요청;
    private long 신분당선_아이디;
    private long 이호선_아이디;

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

        신분당선_아이디 = 노선을_생성하고_노선_아이디를_반환한다(신분당선_생성_요청);
        이호선_아이디 = 노선을_생성하고_노선_아이디를_반환한다(이호선_생성_요청);
    }

    @Test
    void 노선에_구간을_등록한다() {
        // Given 노선에 구간을 등록하면
        var downStationId = 강남역_아이디;
        var upStationId = 서울대입구역_아이디;
        var distance = 10L;

        var response = RestAssuredClient.post(
                Endpoints.sections(신분당선_아이디),
                new AddSectionRequest(
                        downStationId,
                        upStationId,
                        distance
                )
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);

        // When 노선 조회 시
        var 신분당선_조회_응답 = RestAssuredClient.get(
                Endpoints.endpointWithParam(Endpoints.LINES, 신분당선_아이디)
        );

        // Then 등록된 구간이 조회된다.
        // TODO, 등록된 모든 역이 보여야함.
        노선의_정보가_일치한다(
                신분당선_조회_응답.as(LineResponse.class),
                신분당선_생성_요청.getName(),
                신분당선_생성_요청.getColor(),
                신분당선_생성_요청.getUpStationId(),
                신분당선_생성_요청.getDownStationId()
        );
    }

    @Test
    void 구간_등록_시_하행역이_해당_노선에_등록되어있다면_예외를_던진다() {
        // When 노선에 등록된 역을 구간의 하행역으로 등록하면
        // Then 예외를 던진다.
    }

    @Test
    void 구간_등록_시_상행역과_하행역이_같다면_예외를_던진다() {
        // When 상행역과 하행역을 같은 역으로 등록하면
        // Then 예외를 던진다.
    }

    @Test
    void 구간_등록_시_새로운_구간의_상행역이_해당_노선의_하행_종점역이_아니면_예외를_던진다() {
        // Given 구간 등록 시
        // When 상행역이 해당 노선의 하행 종점역이 아니면
        // Then 예외를 던진다.
    }

    @Test
    void 노선에_등록된_구간을_제거한다() {
        // Given 노선에 등록된 구간을 제거하면

        // When 노선 조회 시

        // Then 삭제한 구간이 조회되지 않는다.
    }

    @Test
    void 마지막_구간이_아닌_역을_제거하면_예외를_던진다() {
        // Given 구간 삭제 시
        // When 마지막 구간이 아닌 역을 제거하면
        // Then 예외를 던진다.
    }

    @Test
    void 노선에_구간이_1개인_경우_역_삭제시_예외를_던진다() {
        // Given 구간 삭제 시
        // When 노선에 구간이 1개인 경우
        // Then 예외를 던진다.
    }
}
