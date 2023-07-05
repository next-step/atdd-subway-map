package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineSaveRequest;
import subway.utils.StationApiHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.utils.LineApiHelper.*;

@Sql("truncate_tables.sql")
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;

    @BeforeEach
    public void init() {
        stationId1 = saveStationAndGetId("강남역");
        stationId2 = saveStationAndGetId("사당역");
        stationId3 = saveStationAndGetId("역삼역");
        stationId4 = saveStationAndGetId("삼성역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // given
        String lineName = "line이름";

        LineSaveRequest request = makeLineSaveRequest(lineName, stationId1, stationId2);

        // when
        ExtractableResponse<Response> creationResponse = callApiToCreateLine(request);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> names = callApiToGetLines().jsonPath()
                                                .getList("name", String.class);
        assertThat(names.size()).isEqualTo(1);
        assertThat(names.get(0)).contains(lineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        String lineName1 = "line이름1";
        String lineName2 = "line이름2";

        callApiToCreateLine(makeLineSaveRequest(lineName1, stationId1, stationId2));
        callApiToCreateLine(makeLineSaveRequest(lineName2, stationId3, stationId4));

        // when
        ExtractableResponse<Response> linesResponse = callApiToGetLines(); // 지하철 노선 목록 조회하면

        // then
        assertThat(linesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> names = linesResponse.jsonPath()
                                          .getList("name", String.class); // 2개의 노선을 조회할 수 있다.
        assertThat(names.size()).isEqualTo(2);
        assertThat(names).contains(lineName1, lineName2);
    }

    private LineSaveRequest makeLineSaveRequest(String lineName1, Long stationId1, Long stationId2) {
        return LineSaveRequest.builder()
                              .name(lineName1)
                              .color("bg-red-600")
                              .upStationId(stationId1)
                              .downStationId(stationId2)
                              .distance(10L)
                              .build();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 단건 조회한다.")
    @Test
    void getSingleLine() {

        // given
        String lineName = "line이름";
        long lineId = callApiToCreateLine(makeLineSaveRequest(lineName, stationId1, stationId2)).jsonPath()
                                                                                                .getLong("id");

        // when
        ExtractableResponse<Response> lineResponse = callApiToGetSingleLine(lineId);

        // then
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.jsonPath()
                               .getString("name")).isEqualTo(lineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 정보를 수정한다.")
    @Test
    void modifyLine() {

        // given
        String lineNameBefore = "line이름_before";
        String lineNameAfter = "line이름_after";

        ExtractableResponse<Response> creationResponse = callApiToCreateLine(makeLineSaveRequest(lineNameBefore, stationId1, stationId2));
        Long lineId = creationResponse.jsonPath()
                                      .getLong("id");
        String color = creationResponse.jsonPath()
                                       .getString("color");

        // when
        ExtractableResponse<Response> modificationResponse = callApiToModifyLine(lineId, LineModifyRequest.builder()
                                                                                                  .name(lineNameAfter)
                                                                                                  .color(color)
                                                                                                  .build());

        // then
        assertThat(modificationResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> lineResponse = callApiToGetSingleLine(lineId);
        assertThat(lineResponse.jsonPath().getString("name")).isEqualTo(lineNameAfter);

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 정보를 삭제한다.")
    @Test
    void deleteLine() {

        // given
        ExtractableResponse<Response> creationResponse = callApiToCreateLine(makeLineSaveRequest("line이름", stationId1, stationId2));
        Long lineId = creationResponse.jsonPath()
                                      .getLong("id");

        // when
        ExtractableResponse<Response> deletionResponse = callApiToDeleteLine(lineId);

        // then
        assertThat(deletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> lineResponse = callApiToGetSingleLine(lineId);
        assertThat(lineResponse.jsonPath().getList("id", Long.class).size()).isEqualTo(0);
    }

    private static long saveStationAndGetId(String stationName) {
        return StationApiHelper.callApiToCreateStation(stationName)
                               .jsonPath()
                               .getLong("id");
    }
}
