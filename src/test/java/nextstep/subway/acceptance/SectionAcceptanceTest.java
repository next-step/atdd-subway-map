package nextstep.subway.acceptance;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonLineAcceptance.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.common.CommonSectionAcceptance.구간_파라미터_생성;
import static nextstep.subway.acceptance.common.CommonSectionAcceptance.지하철_구간_추가;
import static nextstep.subway.acceptance.common.CommonStationAcceptance.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.*;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     *  given : 노선이 주어진다.
     *  given : 추가할 구간 정보가 주어진다.
     *  when : 주어진 노선에 대한 구간을 등록한다.
     *  then : 구간 등록이 성공한다.
     */
    @Test
    @DisplayName("지하철 구간 등록")
    void createSection(){
        //given
        String upStationId = 지하철역_생성_요청("뚝섬역")
                .jsonPath()
                .getString("id");

        String downStationId = 지하철역_생성_요청("신도림역")
                .jsonPath()
                .getString("id");

        String newSectionDownStationId = 지하철역_생성_요청("문래역")
                .jsonPath()
                .getString("id");

        ExtractableResponse<Response> 신분당선 =
                지하철_노선_생성_요청("신분당선", "bg-red-600", "100", upStationId, downStationId);

        //given
        Map<String, String> params = 구간_파라미터_생성(downStationId, newSectionDownStationId);

        //when
        신분당선 = 지하철_구간_추가(신분당선, params);

        //then
        assertThat(신분당선.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }



    /**
     * given : 노선과 둘 이상의 구간이 주어진다.
     * when  : 주어진 노선의 구간을 삭제 요청을 한다.
     * then  : 종점역이 삭제된다.
     */
    @Test
    @DisplayName("구간 제거 기능")
    void deleteSection() {
        //given
        String 뚝섬역 = 지하철역_생성_요청("뚝섬역")
                .jsonPath()
                .getString("id");

        String 신도림역 = 지하철역_생성_요청("신도림역")
                .jsonPath()
                .getString("id");

        String 문래역 = 지하철역_생성_요청("문래역")
                .jsonPath()
                .getString("id");

        ExtractableResponse<Response> _2호선 = 지하철_노선_생성_요청("2호선", "bg_green_600", "10", 뚝섬역, 신도림역);

        Map<String, String> 신도림_문래_구간_요청값 = 구간_파라미터_생성(신도림역, 문래역);

        ExtractableResponse<Response> 신도림_문래_구간 = 지하철_구간_추가(_2호선, 신도림_문래_구간_요청값);

        //when
        String 신도림_문래_구간_location = 신도림_문래_구간.header("location");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .param("stationId",문래역)
                .when().delete(신도림_문래_구간_location)
                .then().log().all().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * given : 노선이 주어진다.
     * when  : 노선을 검색했을 때
     * then  : 해당 노선에 포함된 모든 역과 함께 조회된다.
     */
    @Test
    @DisplayName("역 목록 조회")
    void selectStations(){
        //given
        String 뚝섬역 = 지하철역_생성_요청("뚝섬역")
                .jsonPath()
                .getString("id");

        String 신도림역 = 지하철역_생성_요청("신도림역")
                .jsonPath()
                .getString("id");

        ExtractableResponse<Response> _2호선 = 지하철_노선_생성_요청("2호선", "bg_green_600", "10", 뚝섬역, 신도림역);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get(_2호선.header("location"))
                .then().log().all().extract();

        //then
        List<Station> stations = response.jsonPath().getList("stations");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg_green_600");
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(Integer.parseInt(뚝섬역), Integer.parseInt(신도림역));
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("뚝섬역", "신도림역");
    }

}
