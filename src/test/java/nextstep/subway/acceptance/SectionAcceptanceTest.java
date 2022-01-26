package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineStationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 역을 생성을 요청하고
     * Given 지하철 노선 생성을 요청 하고
     * <p>
     * When 지하철 노선에 구간을 등록 요청을하면
     * Then 새로운 노선이 등록된다.
     */
    @Test
    @DisplayName("구간 추가")
    void addSection() {
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
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getId).containsExactly(Long.valueOf(강남역_id), Long.valueOf(삼성역_id), Long.valueOf(선릉역_id)),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getName).containsExactly("강남역", "삼성역", "선릉역")
        );
    }

    /**
     * Given 지하철 역을 생성을 요청 하고
     * Given 지하철 노선 생성을 요청 하고
     * <p>
     * Given 지하철 구간을 추가 요청하고
     * When 구간 삭제를 요청하면
     * Then 해당 구간이 삭제된다.
     */
    @Test
    @DisplayName("구간 제거")
    void deleteSection() {
        //given
        String 강남역_id = 지하철역_생성("강남역").jsonPath().getString("id");
        String 삼성역_id = 지하철역_생성("삼성역").jsonPath().getString("id");
        String 선릉역_id = 지하철역_생성("선릉역").jsonPath().getString("id");
        String 노선_id = 노선_생성("2호선", "갈매색", 강남역_id, 삼성역_id, "100").jsonPath().getString("id");
        구간_추가(삼성역_id, 선릉역_id, 노선_id, "100");


        //when
        ExtractableResponse<Response> deleteResponse = 구간_제거(노선_id, 선릉역_id);

        //then
        ExtractableResponse<Response> response = 노선_조회(노선_id);
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineStationResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineStationResponse.getColor()).isEqualTo("갈매색"),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getId).containsExactly(Long.valueOf(강남역_id), Long.valueOf(삼성역_id)),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getName).containsExactly("강남역", "삼성역")
        );
    }
}
