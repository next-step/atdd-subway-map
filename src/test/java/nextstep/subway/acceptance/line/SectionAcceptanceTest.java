package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.utils.RestAssert;
import nextstep.subway.utils.RestTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineStep.노선_생성_요청;
import static nextstep.subway.acceptance.line.SectionStep.구간_생성_요청;
import static nextstep.subway.acceptance.station.StationStep.지하철역_생성_요청;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * When 생성된 노선과 생성된 상행, 하행 역을 종점역 기반으로 구간 등록을 요청하면
     * Then 구간 등록이 성공한다
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void 구간생성() {
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 구간_상행역Id = 하행종점역Id;
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("신흥역"));
        //when
        ExtractableResponse<Response> response = 구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        //then
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
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;
        Long 구간_상행역Id = 상행종점역Id; // 하행종점역이아닌 상행종점역으로 설정
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("신흥역"));

        //when
        ExtractableResponse<Response> response = 구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

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
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 첫번째_구간_상행역Id = 하행종점역Id;
        Long 첫번쨰_구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("신흥역"));
        구간_생성_요청(노선Id, 첫번째_구간_상행역Id, 첫번쨰_구간_하행역Id, 거리);

        Long 두번째_구간_상행역Id = 첫번쨰_구간_하행역Id;
        Long 두번쨰_구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("단대오거리역"));
        구간_생성_요청(노선Id, 두번째_구간_상행역Id, 두번쨰_구간_하행역Id, 거리);

        Long 구간_상행역Id = 두번쨰_구간_하행역Id;
        Long 구간_하행역Id = 첫번째_구간_상행역Id;
        // when
        ExtractableResponse<Response> response = 구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        // then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * Given 생성된 노선과 생성된 상행, 하행 역을 종점역 기반으로 구간 등록을 요청하고
     * Given 새로운 상행, 하행 역을 구간에 등록하고
     * When 생성된 구간을 하행종점역이 아닌 구간으로 삭제하는 요청을 보내면
     * Then 구간 삭제가 실패한다.
     */
    @DisplayName("삭제할 구간에 하행종점역이 아닌 요청의 구간이면 지하철 구간 삭제가 실패한다")
    @Test
    void 구간_삭제_실패_삭제할_구간이_하행종점역이_아닐때() {
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 구간_상행역Id = 하행종점역Id;
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("신흥역"));
        구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);
        //when
        ExtractableResponse<Response> response = SectionStep.구간_삭제_요청(노선Id, 상행종점역Id);

        //then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * Given 생성된 노선과 생성된 상행, 하행 역을 종점역 기반으로 구간 등록을 요청하면
     * When 구간을 삭제하는 요청을 보내면
     * Then 구간 삭제가 실패한다.
     */
    @DisplayName("삭제할 구간의 노선에 오직 종점역 구간만 있으면 구간 삭제가 실패한다")
    @Test
    void 구간_삭제_실패_오직_종점역만_존재할때() {
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));

        //when
        ExtractableResponse<Response> response = SectionStep.구간_삭제_요청(노선Id, 상행종점역Id);

        //then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 새로운 지하철 노선 생성을 요청해서 노선을 등록하고
     * Given 새로운 지하철역 상행, 하행역을 등록하고
     * Given 생성된 노선과 생성된 상행, 하행 역을 종점역 기반으로 구간 등록을 요청하고
     * Given 새로운 상행, 하행 역 구간에 등록하고
     * When 최종 하행종점역의 구간으로 삭제하는 요청을 보내면
     * Then 구간 삭제가 성공한다.
     */
    @DisplayName("구간 삭제 성공")
    @Test
    void 구간_삭제_성공() {
        //given
        Long 상행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("모란역"));
        Long 하행종점역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("수진역"));
        Long 노선Id = RestTestUtils.getCreatedResourceId(노선_생성_요청("8호선", "bg-pink-600", 상행종점역Id, 하행종점역Id));
        int 거리 = 1000;

        Long 구간_상행역Id = 하행종점역Id;
        Long 구간_하행역Id = RestTestUtils.getCreatedResourceId(지하철역_생성_요청("신흥역"));
        구간_생성_요청(노선Id, 구간_상행역Id, 구간_하행역Id, 거리);

        Long 최종_하행종점역Id = 구간_하행역Id;

        //when
        ExtractableResponse<Response> response = SectionStep.구간_삭제_요청(노선Id, 최종_하행종점역Id);

        //then
        RestAssert.that(response)
                .응답_상태_확인(HttpStatus.OK);
    }

}
