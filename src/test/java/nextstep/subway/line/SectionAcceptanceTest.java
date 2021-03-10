package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.LineTestUtils;
import nextstep.subway.utils.StationTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.LineTestUtils.노선_파라미터_설정;
import static nextstep.subway.utils.LineTestUtils.지하철_노선_생성_요청;
import static nextstep.subway.utils.StationTestUtils.역_파라미터_설정;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /* AC
    1. 지하철 노선에 새로운 구간을 등록할 수 있어야 한다.
        1.1 새로운 구간의 상행역이 현재 등록 되어 있는 하행 종점역인 경우 -> 정상 응답
        1.2 새로운 구간의 상행역이 현재 등록 되어 있는 하행 종점역이 아닌 경우 -> 에러 응
        1.3 새로운 구간의 하행역이 현재 등록 되어 있는 경우 -> 에러 응답
    2. 지하철 노선에 구간을 제거할 수 있어야 한다.
        1.1 노선의 구간이 2개 이상이면서, 제일 마지막 구간(구간의 하행역 == 하행 종점역)을 삭제하는 경우 -> 정상 응답
        1.2 (구간의 하행역 != 하행 종점역)인 구간을 삭제하는 경우 -> 에러 응답
        1.3 노선의 유일한 구간을 삭제하려는 경우 -> 에러 응답
    3. 지하철 노선을 조회할 때 구간들이 순서대로 출력되어야 한다.
     */
    private StationResponse 판교역응답;
    private StationResponse 정자역응답;
    private LineResponse 신분당선응답;

    @BeforeEach
    public void before() {
        Map<String, String> 판교역 = 역_파라미터_설정("판교역");
        Map<String, String> 정자역 = 역_파라미터_설정("정자역");

        판교역응답 = StationTestUtils.역_생성_요청(판교역).as(StationResponse.class);
        정자역응답 = StationTestUtils.역_생성_요청(정자역).as(StationResponse.class);

        Map<String, String> 신분당선 = 노선_파라미터_설정("신분당선", "bg-red-600", 판교역응답.getId(), 정자역응답.getId(), 10);
        신분당선응답 = 지하철_노선_생성_요청(신분당선).as(LineResponse.class);
    }

    @DisplayName("새로운 구간의 상행역이 현재 등록 되어 있는 하행 종점역인 경우 -> 정상 응답")
    @Test
    void createSection_WhenNormalRequest_ThenReturnSuccess() {
        //given
        Map<String, String> 미금역 = 역_파라미터_설정("미금역");
        StationResponse 미금역응답 = StationTestUtils.역_생성_요청(미금역).as(StationResponse.class);

        Map<String, String> 정자미금구간 = new HashMap<>();
        정자미금구간.put("upStationId", String.valueOf(정자역응답.getId()));
        정자미금구간.put("downStationId", String.valueOf(미금역응답.getId()));
        정자미금구간.put("distance", String.valueOf(10));

        //when
        ExtractableResponse<Response> 정자미금구간생성응답 = RestAssured.given().log().all()
                .body(정자미금구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + 신분당선응답.getId() + "/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(정자미금구간생성응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("새로운 구간의 상행역이 현재 등록 되어 있는 하행 종점역이 아닌 경우 -> 에러 응답")
    @Test
    void createSection_WhenUpStationAlreadyExists_ThenReturnError() {
        //given
        Map<String, String> 미금역 = 역_파라미터_설정("미금역");
        StationResponse 미금역응답 = StationTestUtils.역_생성_요청(미금역).as(StationResponse.class);

        Map<String, String> 판교미금구간 = new HashMap<>();
        판교미금구간.put("upStationId", String.valueOf(판교역응답.getId()));
        판교미금구간.put("downStationId", String.valueOf(미금역응답.getId()));
        판교미금구간.put("distance", String.valueOf(10));

        //when
        ExtractableResponse<Response> 판교미금구간생성응답 = RestAssured.given().log().all()
                .body(판교미금구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + 신분당선응답.getId() + "/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(판교미금구간생성응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("새로운 구간의 하행역이 현재 등록 되어 있는 경우 -> 에러 응답")
    @Test
    void createSection_WhenDownStationAlreadyExists_ThenReturnError() {
        //given
        Map<String, String> 미금역 = 역_파라미터_설정("미금역");
        StationResponse 미금역응답 = StationTestUtils.역_생성_요청(미금역).as(StationResponse.class);

        Map<String, String> 정자미금구간 = new HashMap<>();
        정자미금구간.put("upStationId", String.valueOf(정자역응답.getId()));
        정자미금구간.put("downStationId", String.valueOf(미금역응답.getId()));
        정자미금구간.put("distance", String.valueOf(10));

        RestAssured.given().log().all()
                .body(정자미금구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + 신분당선응답.getId() + "/sections")
                .then().log().all()
                .extract();

        Map<String, String> 미금판교구간 = new HashMap<>();
        미금판교구간.put("upStationId", String.valueOf(미금역응답.getId()));
        미금판교구간.put("downStationId", String.valueOf(판교역응답.getId()));

        //when
        ExtractableResponse<Response> 미금판교구간생성응답 = RestAssured.given().log().all()
                .body(미금판교구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + 신분당선응답.getId() + "/sections")
                .then().log().all()
                .extract();

        //then
        assertThat(미금판교구간생성응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
