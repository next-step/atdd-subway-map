package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.provider.LineProvider;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static nextstep.subway.provider.LineProvider.*;
import static nextstep.subway.provider.StationProvider.지하쳘역_등록됨;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
        지하쳘역_등록됨(List.of("지하철역", "새로운지하철역", "또다른지하철역"));
    }

    /**
     * Given 지하철 노선 등록 파라미터를 통해
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * CreateParam 정보: ( name, color, upStationId, downStationId, distance )
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createSubwayLine() throws Exception {
        // given
        final String 노선_이름 = "신분당선";
        final String 노선_색 = "bg-green-600";
        final Long 상행역_ID = 지하쳘역_등록됨("상행역");
        final Long 하행역_ID = 지하쳘역_등록됨("하행역");
        final int 노선_길이 = 10;

        // when
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 노선_생성_요청(노선_이름, 노선_색, 상행역_ID, 하행역_ID, 노선_길이);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_생성_응답, CREATED);
        노선이_정상적으로_생성되었는지_확인(지하철노선_생성_응답);
        노선_목록에_생성한_지하철노선이_있는지_확인(노선_이름);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getSubwayLines() throws Exception {
        // given
        final Long 지하철역_ID = 지하쳘역_등록됨("지하철역");
        final Long 다른지하철역_ID = 지하쳘역_등록됨("다른지하철역");
        final Long 또다른지하철역_ID = 지하쳘역_등록됨("또다른지하철역");
        노선_생성됨("신분당선", "bg-red-600", 지하철역_ID, 다른지하철역_ID, 10);
        노선_생성됨("분당선", "bg-green-600", 지하철역_ID, 또다른지하철역_ID, 10);

        // when
        final ExtractableResponse<Response> 지하철노선_목록_조회_응답 = 노선_목록_조회_요청();

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_목록_조회_응답, OK);
        LineProvider.노선_목록에_생성한_지하철노선이_있는지_확인(List.of("신분당선", "분당선"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getSubwayLine() throws Exception {
        // given
        final Long 지하철역_ID = 지하쳘역_등록됨("지하철역");
        final Long 다른지하철역_ID = 지하쳘역_등록됨("다른지하철역");
        final Long 생성된_노선_ID = 노선_생성됨("신분당선", "bg-red-600", 지하철역_ID, 다른지하철역_ID, 10);

        // when
        final ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답 = 노선_상세_정보_조회_요청(생성된_노선_ID);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_상세_정보_조회_응답, OK);
        조회한_노선이_생성한_노선인지_확인(생성된_노선_ID, 지하철노선_상세_정보_조회_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateSubwayLine() throws Exception {
        // given
        final Long 지하철역_ID = 지하쳘역_등록됨("지하철역");
        final Long 다른지하철역_ID = 지하쳘역_등록됨("다른지하철역");
        final Long 생성된_노선_ID = 노선_생성됨("신분당선", "bg-red-600", 지하철역_ID, 다른지하철역_ID, 10);

        // when
        final ExtractableResponse<Response> 지하철노선_정보_변경_응답 = 노선_정보_변경_요청(생성된_노선_ID, "다른분당선", "bg-green-600");

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_정보_변경_응답, OK);

        final ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답 = 노선_상세_정보_조회_요청(생성된_노선_ID);
        노선_변경이_잘_이루어졌는지_확인("다른분당선", "bg-green-600", 지하철노선_상세_정보_조회_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제합니다.")
    @Test
    void deleteSubwayLine() throws Exception {
        // given
        final Long 지하철역_ID = 지하쳘역_등록됨("지하철역");
        final Long 다른지하철역_ID = 지하쳘역_등록됨("다른지하철역");
        final Long 생성된_노선_ID = 노선_생성됨("신분당선", "bg-red-600", 지하철역_ID, 다른지하철역_ID, 10);

        // when
        final ExtractableResponse<Response> 지하철노선_삭제_응답 = 노선_삭제_요청(생성된_노선_ID);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_삭제_응답, NO_CONTENT);
        노선이_정상적으로_삭제되었는지_확인();
    }
}
