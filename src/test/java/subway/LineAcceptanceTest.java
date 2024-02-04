package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends CommonAcceptanceTest{
    private static Long 강남역Id;
    private static Long 양재역Id;

    @BeforeEach
    void createDefaultStations() {
        강남역Id = extractResponseId(StationRestAssuredCRUD.createStation("강남역"));
        양재역Id = extractResponseId(StationRestAssuredCRUD.createStation("양재역"));
    }

    Long extractResponseId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    List<String> extractResponseNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createLine() {

        //when
        String 신분당선 = "신분당선";
        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation(신분당선, "bg-red-600", 강남역Id, 양재역Id, 10);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = extractResponseNames(LineRestAssuredCRUD.showStationList());
        assertThat(lineNames).contains(신분당선);

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createAndShowTwoLineList() {

        String 서현역 = "서현역";
        String 신분당선 = "신분당선";
        String 수인분당선 = "수인분당선";

        //given
        Long 서현역Id = extractResponseId(StationRestAssuredCRUD.createStation(서현역));

        LineRestAssuredCRUD.createStation(신분당선, "bg-red-600", 강남역Id, 양재역Id, 10);
        LineRestAssuredCRUD.createStation(수인분당선, "bg-yellow-600", 강남역Id, 서현역Id, 10);

        //when
        List<String> names = extractResponseNames(LineRestAssuredCRUD.showStationList());

        // then
        assertThat(names).containsAll(List.of(신분당선, 수인분당선));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 노선을 조회한다")
    @Test
    void createAndShowLine() {

        //given
        String 신분당선 = "신분당선";
        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation(신분당선, "bg-red-600", 강남역Id, 양재역Id, 10);

        Long createdId = extractResponseId(createResponse);

        //when
        String name = LineRestAssuredCRUD.showStation(createdId).jsonPath().getString("name");

        // then
        assertThat(name).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void createAndModifyLine() {

        //given
        String createStationName = "신분당선";
        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation(createStationName, "bg-red-600", 강남역Id, 양재역Id, 10);

        Long createdId = extractResponseId(createResponse);

        //when
        String modifyStationName = "수인분당선";
        ExtractableResponse<Response> modifyResponse = LineRestAssuredCRUD.modifyStation(createdId, modifyStationName, "bg-yellow-600");

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        String name = LineRestAssuredCRUD.showStation(createdId).jsonPath().getString("name");
        assertThat(name).isEqualTo(modifyStationName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 생성하고 삭제한다")
    @Test
    void createAndDeleteLine() {

        //given
        String 신분당선 = "신분당선";
        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 강남역Id, 양재역Id, 10);

        Long createdId = extractResponseId(createResponse);

        //when
        ExtractableResponse<Response> deleteResponse = LineRestAssuredCRUD.deleteStation(createdId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> names = extractResponseNames(LineRestAssuredCRUD.showStationList());
        assertThat(names).doesNotContain(신분당선);
    }
}
