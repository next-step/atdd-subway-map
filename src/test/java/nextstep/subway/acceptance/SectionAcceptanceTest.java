package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.testsupport.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @Nested
    class 구간등록기능 {
        /**
         * Given A,B,C 라는 이름을 가진 3개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(B역은 상행역 C역은 하행역)을 노선에 등록하고
         * Then 등록된 노선의 상행역, 하행역을 확인한다.
         */
        @Test
        void 새로운_구간_등록(){
            // given
            long aId = 지하철역_생성_요청("A").jsonPath().getLong("id");
            long bId = 지하철역_생성_요청("B").jsonPath().getLong("id");
            long cId = 지하철역_생성_요청("C").jsonPath().getLong("id");

            // given
            long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", aId, bId, 10).jsonPath().getLong("id");

            // when
            final ExtractableResponse<Response> response = 구간_등록_요청(lineId, bId, cId, 10);

            // then
            assertAll(
                ()->assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                ()->assertThat(response.jsonPath().getLong("upStationId")).isEqualTo(aId),
                ()->assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(cId)
                     );
        }

        /**
         * Given A,B 라는 이름을 가진 2개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(B역은 상행역 A역은 하행역)을 노선에 등록한다.
         * Then 노선에 새로운 구간을 등록할 수 없다.
         */
        @Test
        void 새로운_구간의_하행역이_노선에_이미_등록되어있는경우() {
            // given
            long aId = 지하철역_생성_요청("A").jsonPath().getLong("id");
            long bId = 지하철역_생성_요청("B").jsonPath().getLong("id");

            // given
            long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", aId, bId, 10).jsonPath().getLong("id");

            // when
            final ExtractableResponse<Response> response = 구간_등록_요청(lineId, bId, aId, 10);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        /**
         * Given A,B,C,D 라는 이름을 가진 4개의 역을 생성한다.
         * Given 노선(A역은 상행역, B역은 하행역)을 생성한다.
         * When 새로운 구간(C역은 상행역 D역은 하행역)을 노선에 등록한다.
         * Then 노선에 새로운 구간을 등록할 수 없다.
         */
        @Test
        void 새로운_구간의_상행역이_노선의_하행역이_아닌_경우() {
            // given
            long aId = 지하철역_생성_요청("A").jsonPath().getLong("id");
            long bId = 지하철역_생성_요청("B").jsonPath().getLong("id");
            long cId = 지하철역_생성_요청("C").jsonPath().getLong("id");
            long dId = 지하철역_생성_요청("D").jsonPath().getLong("id");

            // given
            long lineId = 지하철노선_생성_요청("신분당선", "bg-red-600", aId, bId, 10).jsonPath().getLong("id");

            // when
            final ExtractableResponse<Response> response = 구간_등록_요청(lineId, cId, dId, 10);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    private ExtractableResponse<Response> 구간_등록_요청(long registLineId, long downStationId, long upStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("downStationId", String.valueOf(downStationId));
        params.put("upStationId", String.valueOf(upStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post(String.format("/lines/%s/sections", registLineId))
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 지하철노선_생성_요청(final String lineName,
                                                      final String lineColor,
                                                      final long upStationId,
                                                      final long downStationId,
                                                      final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all()
                          .extract();
    }
}
