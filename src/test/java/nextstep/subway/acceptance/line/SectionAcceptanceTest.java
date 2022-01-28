package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.utils.RestAssert;
import nextstep.subway.utils.RestTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * When 생성된 노선과 생성된 상행, 하행 역을 기반으로 구간 등록을 요청하면
     * Then 구간 등록이 성공한다
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void 구간생성() {
//        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(LineStep.노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 구간_상행역Id = 하행종점역Id;
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("신흥역"));
        //when
        ExtractableResponse<Response> response = SectionStep.구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.CREATED);
    }

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * When 새로운 구간의 상행역을 생성된 노선의 하행 종점역이 아닌 다른 역으로 요청하면
     * Then 구간 등록이 실패한다
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐 때 등록이 실패한다")
    @Test
    void 구간생성_유효성검증_구간_상행역은_노선의_하행_종점역() {
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(LineStep.노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;
        Long 구간_상행역Id = 상행종점역Id; // 하행종점역이아닌 상행종점역으로 설정
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("신흥역"));

        //when
        ExtractableResponse<Response> response = SectionStep.구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        // then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 첫번째 지하철역 상행, 하행역을 등록하고
     * Given 새로운 두번째 지하철역 상행, 하행역을 등록하고
     * Given 생성된 노선에 생성된 첫번째 지하철역 상행, 하행역을 구간으로 등록하고
     * Given 생성된 노선에 생성된 두번째 지하철역 상행, 하행역을 구간으로 등록하고
     * When 새로운 구간의 하행역을 첫번째 지하철역 상행역으로 설정한 뒤 요청하면
     * Then 구간 등록이 실패한다
     */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 이미 등록되어있는 역이라면 등록이 실패한다.")
    @Test
    void 구간생성_유효성검증_구간_하행역은_노선에_등록되어있지않아야함() {
//        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(LineStep.노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 첫번째_구간_상행역Id = 하행종점역Id;
        Long 첫번쨰_구간_하행역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("신흥역"));
        SectionStep.구간_생성_요청(노선Id, 첫번째_구간_상행역Id, 첫번쨰_구간_하행역Id, 거리);

        Long 두번째_구간_상행역Id = 첫번쨰_구간_하행역Id;
        Long 두번쨰_구간_하행역Id = RestTestUtils.getCreatedResourceId(StationStep.지하철역_생성_요청("단대오거리역"));
        SectionStep.구간_생성_요청(노선Id, 두번째_구간_상행역Id, 두번쨰_구간_하행역Id, 거리);

        Long 구간_상행역Id = 두번쨰_구간_하행역Id;
        Long 구간_하행역Id = 첫번째_구간_상행역Id;
        // when
        ExtractableResponse<Response> response = SectionStep.구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        // then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.BAD_REQUEST);
    }

}
