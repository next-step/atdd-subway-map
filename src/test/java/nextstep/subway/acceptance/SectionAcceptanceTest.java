package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonLineAcceptance.지하철_노선_생성_요청;
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

        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";

        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        //given
        Map<String, String> params =
                new HashMap<>();
        params.put("distance", "10");
        params.put("downStationId", newSectionDownStationId);
        params.put("upStationId", downStationId);
        //when
        response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
                .when().post( response.header("location") + "/sections")
                .then().log().all().extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
