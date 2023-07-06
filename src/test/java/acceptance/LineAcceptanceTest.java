package acceptance;

import static api.StationLineApiRequest.지하철역_노선_단건_조회;
import static api.StationLineApiRequest.지하철역_노선_등록_요청;
import static api.StationLineApiRequest.지하철역_노선_등록_요청_후_id_추출;
import static api.StationLineApiRequest.지하철역_노선_목록_조회_요청;
import static api.StationLineApiRequest.지하철역_노선_삭제;
import static api.StationLineApiRequest.지하철역_노선_수정;
import static fixture.StationLineRequestFixture.distance;
import static fixture.StationLineRequestFixture.green;
import static fixture.StationLineRequestFixture.red;
import static fixture.StationLineRequestFixture.노선등록요청_데이터_생성;
import static fixture.StationLineRequestFixture.다른분당선;
import static fixture.StationLineRequestFixture.또다른지하철역_id;
import static fixture.StationLineRequestFixture.분당선;
import static fixture.StationLineRequestFixture.새로운지하철역_id;
import static fixture.StationLineRequestFixture.신분당선;
import static fixture.StationLineRequestFixture.지하철역_id;
import static fixture.StationModifyRequestFixture.노선수정요청_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;

import config.AcceptanceTestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTestConfig {

    /**
     * When 지하철 노선을 생성하면 <br>
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        //when
        ExtractableResponse<Response> response = 지하철역_노선_등록_요청(
                노선등록요청_데이터_생성(신분당선, red, 지하철역_id, 새로운지하철역_id, distance)
        );

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red),
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("stations.id")).contains(
                Long.valueOf(지하철역_id).intValue(),
                Long.valueOf(새로운지하철역_id).intValue()
            )
        );

        //Then
        assertThat(지하철역_노선_목록_조회_요청().jsonPath().getList("id"))
            .contains(Long.valueOf(response.jsonPath().getLong("id")).intValue());
    }


    /**
     * Given 2개의 지하철 노선을 생성하고 <br>
     * When 지하철 노선 목록을 조회하면 <br>
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getStationLines() {

        //given
        지하철역_노선_등록_요청(노선등록요청_데이터_생성(신분당선, red, 지하철역_id, 새로운지하철역_id, distance));
        지하철역_노선_등록_요청(노선등록요청_데이터_생성(분당선, green, 지하철역_id, 또다른지하철역_id, distance));

        //when
        ExtractableResponse<Response> response = 지하철역_노선_목록_조회_요청();

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("name")).containsOnly(신분당선, 분당선)
        );
    }

    /**
     * Given 지하철 노선을 생성하고 <br>
     * When 생성한 지하철 노선을 조회하면
     * <br> Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철 노선 조회")
    @Test
    void getStationLine() {

        //given
        long 신규등록_노선_id = 지하철역_노선_등록_요청_후_id_추출(노선등록요청_데이터_생성(신분당선, red, 지하철역_id, 새로운지하철역_id, distance));

        //when
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규등록_노선_id);

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red),
            () -> assertThat(response.jsonPath().getList("stations")).hasSize(2),
            () -> assertThat(response.jsonPath().getList("stations.id")).contains(
                Long.valueOf(지하철역_id).intValue(),
                Long.valueOf(새로운지하철역_id).intValue()
            )
        );

    }

    /**
     * Given 지하철 노선을 생성하고 <br>
     * When 생성한 지하철 노선을 수정하면 <br>
     * Then 해당 지하철 노선 정보는 수정된다 <br>
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyStationLine() {

        //given
        long 신규등록_노선_id = 지하철역_노선_등록_요청_후_id_추출(노선등록요청_데이터_생성(신분당선, red, 지하철역_id, 새로운지하철역_id, distance));

        //when
        지하철역_노선_수정(신규등록_노선_id, 노선수정요청_데이터_생성(다른분당선, red));

        //then
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규등록_노선_id);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getString("name")).isEqualTo(다른분당선),
            () -> assertThat(response.jsonPath().getString("color")).isEqualTo(red)
        );
    }

    /**
     * Given 지하철 노선을 생성하고 <br>
     * When 생성한 지하철 노선을 삭제하면 <br>
     * Then 해당 지하철 노선 정보는 삭제된다 <br>
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteStationLine() {

        //given
        long 신규등록_노선_id = 지하철역_노선_등록_요청_후_id_추출(노선등록요청_데이터_생성(신분당선, red, 지하철역_id, 새로운지하철역_id, distance));

        //when
        지하철역_노선_삭제(신규등록_노선_id);

        //then
        ExtractableResponse<Response> response = 지하철역_노선_단건_조회(신규등록_노선_id);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("해당하는 id 에 맞는 지하철 노선이 존재하지 않습니다.")
        );

    }

}
