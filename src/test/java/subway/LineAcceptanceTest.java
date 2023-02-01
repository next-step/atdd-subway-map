package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.BaseAcceptanceTest;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;
import subway.executor.AcceptanceExecutor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest extends BaseAcceptanceTest {

    private Long 정자역;
    private Long 판교역;

    @BeforeEach
    void setUp() {
        정자역 = createStation("정자역");
        판교역 = createStation("판교역");
    }

    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        //when 지하철 노선을 생성하면
        ExtractableResponse<Response> response = createLine("신분당선", "red", 정자역, 판교역);

        //then  지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
                , () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선")
                , () -> assertThat(response.jsonPath().getList("stations", Station.class)).hasSize(2));

    }

    @Test
    @DisplayName("지하철 노선 목록 조회")
    void showLines() {
        //given 2개의 지하철 노선을 생성하고
        createLine("신분당선", "red", 정자역, 판교역);
        createLine("5호선", "purple", "광화문", "서대문");

        //when 지하철 노선 목록을 조회하면
        List<LineResponse> response = List.of(AcceptanceExecutor.get("lines", LineResponse[].class));

        //then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertAll(
                () -> assertThat(response).hasSize(2)
                , () -> assertThat(response.stream().map(LineResponse::getName)).containsAnyOf("신분당선", "5호선"));

    }


    @Test
    @DisplayName("지하철 노선 조회")
    void showLine() {
        //given 지하철 노선을 생성하고
        Long 신분당선 = createLine("신분당선", "red", 정자역, 판교역).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 조회하면

        LineResponse response = AcceptanceExecutor.get("/lines/" + 신분당선, LineResponse.class);

        //then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("신분당선")
                , () -> assertThat(response.getColor()).isEqualTo("red")
                , () -> assertThat(response.getId()).isEqualTo(신분당선));

    }

    @Test
    @DisplayName("지하철 노선 수정")
    void updateLine() {
        //given 지하철 노선을 생성하고
        Long 신분당선 = createLine("신분당선", "red", 정자역, 판교역).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 수정하면
        LineRequest updateRequest = new LineRequest();
        updateRequest.setName("경강선");
        updateRequest.setColor("blue");
        ExtractableResponse<Response> response = AcceptanceExecutor.put("/lines/" + 신분당선, updateRequest);

        //then 해당 지하철 노선 정보는 수정된다
        LineResponse updatedLine = AcceptanceExecutor.get("/lines/" + 신분당선, LineResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName())
                , () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor()));

    }

    @Test
    @DisplayName("지하철 노선 삭제")
    void deleteLine() {
        //given 지하철 노선을 생성하고
        Long 신분당선 = createLine("신분당선", "red", 정자역, 판교역).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> deleteLineResponse = AcceptanceExecutor.delete("/lines/" + 신분당선);

        //then 해당 지하철 노선 정보는 삭제된다
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Long createLine(String name, String color, String upStation, String downStation) {
        Long upStationId = createStation(upStation);
        Long downStationId = createStation(downStation);
        return createLine(name, color, upStationId, downStationId).jsonPath().getLong("id");
    }

    private static ExtractableResponse<Response> createLine(String lineName, String red, Long upStation, Long downStation) {

        LineRequest lineRequest = new LineRequest();
        lineRequest.setName(lineName);
        lineRequest.setColor(red);
        lineRequest.setUpStationId(upStation);
        lineRequest.setDownStationId(downStation);
        lineRequest.setDistance(10L);

        return AcceptanceExecutor.post("/lines", lineRequest);
    }

    private static Long createStation(String name) {
        return AcceptanceExecutor.post("/stations", new StationRequest(name)).jsonPath().getLong("id");
    }

}
