package subway.subwayline;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.ApiTest;
import subway.station.StationResponse;
import subway.station.StationSteps;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationSteps.지하철생성요청_다중생성;

@DisplayName("지하철 노선 관련 기능")
class SubwayLineAcceptanceTest extends ApiTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노션을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // given
        String name = "4호선";
        String color = "bg-blue-500";
        Long upStationId = 1L;
        Long downStationId = 2L;
        int distance = 50;
        지하철생성요청_다중생성(List.of("오이도역", "이수역")).stream()
                .map(StationSteps::지하철생성요청)
                .forEach(x -> assertThat(x.statusCode()).isEqualTo(HttpStatus.CREATED.value()));
        CreateSubwayLineRequest subwayLineRequest = SubwayLineSteps.지하철노선등록요청_생성(name, color, upStationId, downStationId, distance);

        // when
        ExtractableResponse<Response> response = SubwayLineSteps.지하철노선등록요청(subwayLineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(subwayLineRequest.getName());
        assertThat(response.jsonPath().getString("color")).isEqualTo(subwayLineRequest.getColor());
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(50);
        assertThat(response.jsonPath().getList("stations", StationResponse.class)).hasSize(2)
                .extracting("name").containsExactlyInAnyOrder("오이도역", "이수역");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getSubwayLines() {
        // given
        지하철생성요청_다중생성(List.of("오이도역", "이수역", "남성역")).stream()
                .map(StationSteps::지하철생성요청)
                .forEach(x -> assertThat(x.statusCode()).isEqualTo(HttpStatus.CREATED.value()));

        CreateSubwayLineRequest subwayLineRequest = SubwayLineSteps.지하철노선등록요청_생성("4호선", "bg-blue-600", 1L, 2L, 50);
        CreateSubwayLineRequest subwayLineRequest2 = SubwayLineSteps.지하철노선등록요청_생성("7호선", "bg-red-600", 2L, 3L, 50);
        SubwayLineSteps.지하철노선등록요청(subwayLineRequest);
        SubwayLineSteps.지하철노선등록요청(subwayLineRequest2);

        // when
        ExtractableResponse<Response> response = SubwayLineSteps.지하철노선목록조회요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id")).hasSize(2).containsExactlyInAnyOrder(1, 2);
        assertThat(response.jsonPath().getList("name")).hasSize(2).containsExactlyInAnyOrder("4호선", "7호선");
        assertThat(response.jsonPath().getList("color")).hasSize(2).containsExactlyInAnyOrder("bg-blue-600", "bg-red-600");
        assertThat(response.jsonPath().getList("distance")).hasSize(2).containsExactlyInAnyOrder(50, 50);
        assertThat(response.jsonPath().getList("stations[0].name")).hasSize(2)
                .containsExactlyInAnyOrder("오이도역", "이수역");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getSubwayLine() {
        // given
        Long id = 1L;
        지하철생성요청_다중생성(List.of("오이도역", "이수역")).stream()
                .map(StationSteps::지하철생성요청)
                .forEach(x -> assertThat(x.statusCode()).isEqualTo(HttpStatus.CREATED.value()));
        SubwayLineSteps.지하철노선등록요청(SubwayLineSteps.지하철노선등록요청_생성("4호선", "bg-blue-600", 1L, 2L, 50));

        // when
        ExtractableResponse<Response> response = SubwayLineSteps.지하철노선조회요청(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("4호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-blue-600");
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(50);
        assertThat(response.jsonPath().getList("stations", StationResponse.class)).hasSize(2)
                .extracting("name").containsExactlyInAnyOrder("오이도역", "이수역");
    }

    /**
     *Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifySubwayLine() {
        // given
        Long id = 1L;
        지하철생성요청_다중생성(List.of("오이도역", "이수역")).stream()
                .map(StationSteps::지하철생성요청)
                .forEach(x -> assertThat(x.statusCode()).isEqualTo(HttpStatus.CREATED.value()));
        SubwayLineSteps.지하철노선등록요청(SubwayLineSteps.지하철노선등록요청_생성("4호선", "bg-blue-600", 1L, 2L, 50));
        ModifySubwayLineRequest request = SubwayLineSteps.지하철노선수정요청_생성("4호선", "bg-red-500");

        // when
        ExtractableResponse<Response> response = SubwayLineSteps.지하철노선수정요청(id, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        Long id = 1L;
        지하철생성요청_다중생성(List.of("오이도역", "이수역")).stream()
                .map(StationSteps::지하철생성요청)
                .forEach(x -> assertThat(x.statusCode()).isEqualTo(HttpStatus.CREATED.value()));
        SubwayLineSteps.지하철노선등록요청(SubwayLineSteps.지하철노선등록요청_생성("4호선", "bg-blue-600", 1L, 2L, 50));

        // when
        ExtractableResponse<Response> response = SubwayLineSteps.지하철노선삭제요청(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
