package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        //when 지하철 노선을 생성하면
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        ExtractableResponse<Response> response = createLine("신분당선", "red", 정자역Id, 판교역Id);

        //then  지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
                , () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선")
                , () -> assertThat(response.jsonPath().getList("stations", Station.class)).hasSize(2));

    }

    @Test
    @DisplayName("지하철 노선 목록 조회")
    void showLines() {
        //given 2개의 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        createLine("신분당선", "red", 정자역Id, 판교역Id);

        Long 광화문역Id = createStation("광화문");
        Long 서대문역Id = createStation("서대문");
        createLine("5호선", "purple", 광화문역Id, 서대문역Id);

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
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 조회하면
        LineResponse 신분당선 = AcceptanceExecutor.get("/lines/" + 신분당선Id, LineResponse.class);

        //then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertAll(
                () -> assertThat(신분당선.getName()).isEqualTo("신분당선")
                , () -> assertThat(신분당선.getColor()).isEqualTo("red")
                , () -> assertThat(신분당선.getId()).isEqualTo(신분당선Id));

    }

    @Test
    @DisplayName("지하철 노선 수정")
    void updateLine() {
        //given 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 수정하면
        LineRequest updateRequest = new LineRequest();
        updateRequest.setName("경강선");
        updateRequest.setColor("blue");
        ExtractableResponse<Response> response = AcceptanceExecutor.put("/lines/" + 신분당선Id, updateRequest);

        //then 해당 지하철 노선 정보는 수정된다
        LineResponse updatedLine = AcceptanceExecutor.get("/lines/" + 신분당선Id, LineResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName())
                , () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor()));

    }

    @Test
    @DisplayName("지하철 노선 삭제")
    void deleteLine() {
        //given 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        //when 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> deleteLineResponse = AcceptanceExecutor.delete("/lines/" + 신분당선Id);

        //then 해당 지하철 노선 정보는 삭제된다
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철 구간 생성")
    void createSection() {
        //when 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        //then 구간을 추가한다.
        ExtractableResponse<Response> response = addSection(신분당선Id, 정자역Id, 판교역Id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("지하철 구간 생성 - 새로운 구간의 상행역은 노선의 하행역이어야 함")
    void createSectionEqualEndStation() {
        //when 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");
        addSection(신분당선Id, 정자역Id, 판교역Id);

        //then 구간을 추가한다.
        Long 청계산입구역Id = createStation("청계산입구역");
        Long 양재역Id = createStation("양재역");
        ExtractableResponse<Response> response = addSection(신분당선Id, 청계산입구역Id, 양재역Id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("지하철 구간 생성 - 새로운 구간의 하행역은 등록된 역일 수 없다.")
    void createSectionWhenDuplicate() {
        //when 지하철 노선을 생성하고
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");
        addSection(신분당선Id, 정자역Id, 판교역Id);

        //then 구간을 추가한다.
        Long 양재역Id = createStation("양재역");
        ExtractableResponse<Response> response = addSection(신분당선Id, 양재역Id, 판교역Id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("지하철 구간 삭제")
    void deleteSection() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        Long 청계산입구역Id = createStation("청계산입구역");

        Long section1 = addSection(신분당선Id, 정자역Id, 판교역Id).jsonPath().getLong("id");
        Long section2 = addSection(신분당선Id, 판교역Id, 청계산입구역Id).jsonPath().getLong("id");

        //when 구간 1개를 삭제한다.
        ExtractableResponse<Response> response  = deleteSection(신분당선Id, section2);

        //then 노선 조회 시 구간이 1개만 조회된다.
        LineResponse lineResponse = AcceptanceExecutor.get("/lines/" + 신분당선Id, LineResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineResponse.getSections()).hasSize(1),
                () -> assertThat(lineResponse.getSections().get(0).getId()).isEqualTo(section1)
        );
    }

    @Test
    @DisplayName("지하철 구간 삭제 실패 - 마지막 구간이 아닌 경우")
    void deleteSectionWhenNotLast() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");
        Long 청계산입구역Id = createStation("청계산입구역");

        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");

        Long section1 = addSection(신분당선Id, 정자역Id, 판교역Id).jsonPath().getLong("id");
        Long section2 = addSection(신분당선Id, 판교역Id, 청계산입구역Id).jsonPath().getLong("id");
        //when 첫번째 구간을 삭제한다.
        ExtractableResponse<Response> response = deleteSection(신분당선Id, section1);

        //then 400 반환
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("지하철 구간 삭제 실패 - 구간이 하나만 있는 경우")
    void deleteSectionWhenOnlyOne() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long 정자역Id = createStation("정자역");
        Long 판교역Id = createStation("판교역");

        Long 신분당선Id = createLine("신분당선", "red", 정자역Id, 판교역Id).jsonPath().getLong("id");
        Long section1 = addSection(신분당선Id, 정자역Id, 판교역Id).jsonPath().getLong("id");

        //when 첫번째 구간을 삭제한다.
        ExtractableResponse<Response> response = deleteSection(신분당선Id, section1);

        //then 400 반환
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static ExtractableResponse<Response> deleteSection(Long 신분당선Id, Long sectionId) {
        return AcceptanceExecutor.delete("/lines/" + 신분당선Id + "/sections?sectionId=" + sectionId);
    }


    private static ExtractableResponse<Response> addSection(Long lineId, Long upStationId, Long downStationId) {
        SectionRequest sectionRequest = new SectionRequest();
        sectionRequest.setUpStationId(upStationId);
        sectionRequest.setDownStationId(downStationId);
        sectionRequest.setDistance(10L);
        return AcceptanceExecutor.post("/lines/" + lineId + "/sections", sectionRequest);
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
