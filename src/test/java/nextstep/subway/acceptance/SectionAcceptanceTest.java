package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceTest.노선_생성;
import static nextstep.subway.acceptance.LineAcceptanceTest.노선_조회;
import static nextstep.subway.acceptance.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineStationResponse;
import nextstep.subway.utils.CustomRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 노선 생성을 요청 하고
     * <p>
     * When 지하철 노선에 구간을 등록하면
     * Then 새로운 노선이 등록된다.
     */
    @Test
    @DisplayName("노선 구간 추가")
    void uizuewiqr() {
        //given
        String 강남역_id = 지하철역_생성("강남역").jsonPath().getString("id");
        String 삼성역_id = 지하철역_생성("삼성역").jsonPath().getString("id");
        String 선릉역_id = 지하철역_생성("선릉역").jsonPath().getString("id");
        String 노선_id = 노선_생성("2호선", "갈매색", 강남역_id, 삼성역_id, "100").jsonPath().getString("id");

        //when
        ExtractableResponse<Response> sectionAddResponse = 구간_추가(삼성역_id, 선릉역_id, 노선_id, "100");

        //then
        ExtractableResponse<Response> response = 노선_조회(노선_id);
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);
        assertAll(
                () -> assertThat(sectionAddResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineStationResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineStationResponse.getColor()).isEqualTo("갈매색"),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getId).containsExactly(1L, 2L, 3L),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getName).containsExactly("강남역", "삼성역", "선릉역")
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * <p>
     * When 지하철 노선에 구간을 등록하면
     * Then 새로운 노선이 등록된다.
     */
    @Test
    @DisplayName("구간 제거")
    void uizu123ewiqr() {
        //given
        String 강남역_id = 지하철역_생성("강남역").jsonPath().getString("id");
        String 삼성역_id = 지하철역_생성("삼성역").jsonPath().getString("id");
        String 선릉역_id = 지하철역_생성("선릉역").jsonPath().getString("id");
        String 노선_id = 노선_생성("2호선", "갈매색", 강남역_id, 삼성역_id, "100").jsonPath().getString("id");
        ExtractableResponse<Response> sectionAddResponse = 구간_추가(삼성역_id, 선릉역_id, 노선_id, "100");


        //when
        ExtractableResponse<Response> deleteResponse = 구간_제거(노선_id, 선릉역_id);

        //then
        ExtractableResponse<Response> response = 노선_조회(노선_id);
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineStationResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineStationResponse.getColor()).isEqualTo("갈매색"),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getId).containsExactly(1L, 2L),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getName).containsExactly("강남역", "삼성역")
        );
    }

    private ExtractableResponse<Response> 구간_추가(final String upStationId, final String downStationId, final String lineId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("distance", distance);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);

        return CustomRestAssured.post("/lines/" + lineId + "/sections", params);
    }

    private ExtractableResponse<Response> 구간_제거(final String lineId, String stationId) {
        return CustomRestAssured.delete("/lines/" + lineId + "/sections?stationId=" + stationId);
    }
}
