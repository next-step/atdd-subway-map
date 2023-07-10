package acceptance;

import static fixture.given.StationLineRequestFixture.distance;
import static fixture.given.StationLineRequestFixture.red;
import static fixture.given.StationLineRequestFixture.신분당선;
import static fixture.given.StationLineSectionRequestFixture.구간거리;
import static fixture.given.StationModifyRequestFixture.또다른지하철역이름;
import static fixture.given.StationModifyRequestFixture.새로운지하철역이름;
import static fixture.given.StationModifyRequestFixture.지하철역이름;
import static fixture.then.ApiStatusFixture.*;
import static fixture.then.StationLineSectionThenFixture.노선구간추가_상행역설정_오류_검사;
import static fixture.then.StationLineSectionThenFixture.노선구간추가_하행역이_이미존재할떄_오류_검사;
import static fixture.when.StationApiFixture.지하철역_생성_요청;
import static fixture.when.StationLineApiFixture.지하철역_노선_등록_요청_후_id_추출;
import static fixture.when.StationLineSectionApiFixture.지하철_노선_구간_추가_등록;

import config.AcceptanceTestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTestConfig {

    /**
     * Given 지하철역과 노선을 생성하고 <br> When 지하철 구간을 추가하면 <br> Then 성공적으로 생성시 HTTP 생성 응답 코드를 얻는다 <br>
     */
    @DisplayName("지하철 노선 구간 등록 (성공)")
    @Test
    void createSection() {

        //given
        long 상행역_id = 지하철역_생성_요청(지하철역이름).jsonPath().getLong("id");
        long 하행역_id = 지하철역_생성_요청(새로운지하철역이름).jsonPath().getLong("id");
        long 추가_하행역_id = 지하철역_생성_요청(또다른지하철역이름).jsonPath().getLong("id");
        long 지하철역_노선_id = 지하철역_노선_등록_요청_후_id_추출(신분당선, red, 상행역_id, 하행역_id, distance);

        //when
        ExtractableResponse<Response> response = 지하철_노선_구간_추가_등록(
            지하철역_노선_id, 추가_하행역_id, 하행역_id, 구간거리
        );

        //then
        API_생성_응답코드_검사(response);
    }

    /**
     * Given 지하철역과 노선을 생성하고 <br>
     * When 지하철 구간을 추가할때 <br>
     * Then 새로운 구간의 상행역이 노선의 하행 종점이여야한다. <br>
     */
    @DisplayName("지하철 노선 구간 등록(실패) - 추가 구간 상행역이, 노선 하행역이 아니면 등록되지 않아야한다.")
    @Test
    void createSection_NotValidEndStation() {

        //given
        long 상행역_id = 지하철역_생성_요청(지하철역이름).jsonPath().getLong("id");
        long 하행역_id = 지하철역_생성_요청(새로운지하철역이름).jsonPath().getLong("id");
        long 추가_하행역_id = 지하철역_생성_요청(또다른지하철역이름).jsonPath().getLong("id");
        long 지하철역_노선_id = 지하철역_노선_등록_요청_후_id_추출(신분당선, red, 상행역_id, 하행역_id, distance);

        //when
        ExtractableResponse<Response> response = 지하철_노선_구간_추가_등록(
            지하철역_노선_id, 추가_하행역_id, 상행역_id, 구간거리
        );

        //then
        API_잘못된요청_응답코드_검사(response);
        노선구간추가_상행역설정_오류_검사(response);
    }

    /**
     *  Given 지하철역과 노선을 생성하고 <br>
     *  When 지하철 구간을 추가할때 <br>
     *  Then 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다. <br>
     */
    @DisplayName("지하철 노선 구간 등록(실패) - 추가 구간 하행역이 구간에 존재하는 하행역이면 에러")
    @Test
    void createSection_existEndStation() {

        //given
        long 상행역_id = 지하철역_생성_요청(지하철역이름).jsonPath().getLong("id");
        long 하행역_id = 지하철역_생성_요청(새로운지하철역이름).jsonPath().getLong("id");
        long 지하철역_노선_id = 지하철역_노선_등록_요청_후_id_추출(신분당선, red, 상행역_id, 하행역_id, distance);

        //when
        ExtractableResponse<Response> response = 지하철_노선_구간_추가_등록(
            지하철역_노선_id, 하행역_id, 하행역_id, 구간거리
        );

        //then
        API_잘못된요청_응답코드_검사(response);
        노선구간추가_하행역이_이미존재할떄_오류_검사(response);
    }


    /**
     * Given 지하철역과 노선을 생성하고 지하철 구간을 추가한 후 <br>
     * When 지하철 구간을 삭제하면 <br>
     * Then 지하철 노선 조회시 삭제한 구간을 확인할 수 없다. <br>
     */
    @DisplayName("지하철 구간 삭제(성공)")
    @Test
    void deleteRemove() {


    }

    /**
     *  Given 지하철역과 노선을 생성하고 지하철 구간을 추가한 후 <br>
     *  When 지하철 구간을 삭제할 때 <br>
     *  Then 하행 종점역이 아니면 삭제할 수 없다. <br>
     */

    /**
     *  Given 지하철역과 노선을 생성하고 <br>
     *  When 지하철 구간을 삭제할 때 <br>
     *  Then 구간이 1개인 경우 역을 삭제할 수 없다. <br>
     */
}
