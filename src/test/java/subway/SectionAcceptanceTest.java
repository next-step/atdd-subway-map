package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.LineAcceptanceTest.createLine;
import static subway.StationAcceptanceTest.createStations;

@AcceptanceTest
@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest {

    private List<Long> newStationIds;
    private Long newLineId;

    @BeforeEach
    void setUp() {
        newStationIds = createStations(4);
        newLineId = createLine("line", newStationIds.get(0), newStationIds.get(1))
                .jsonPath().getLong("id");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 성공한다.
     */
    @Test
    void createSection_success() {
        //given
        Long upStationId = newStationIds.get(1);
        Long downStationId = newStationIds.get(2);

        //when
        ExtractableResponse<Response> response = createSection(newLineId, upStationId, downStationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).contains(1L, 2L);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간의 상행역이 노선의 하행종점역이 아닐 경우
     * Then 실패한다.
     */
    @Test
    void createSection_failIfSectionUpStationIsNotLineLastDownStation() {
        //given
        Long upStationId = newStationIds.get(2);
        Long downStationId = newStationIds.get(3);

        //when
        ExtractableResponse<Response> response = createSection(newLineId, upStationId, downStationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().asString()).contains("section's up station is not line's down end station");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간의 하행역이 노선에 이미 존재하는 역일 경우
     * Then 실패한다.
     */
    @Test
    void createSection_failIfSectionDownStationIsInLine() {
        //given
        Long upStationId = newStationIds.get(1);
        Long downStationId = newStationIds.get(0);
        Long distance = 10L;

        //when
        ExtractableResponse<Response> response = createSection(newLineId, upStationId, downStationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().asString()).contains("section's down station can't be in other section's station");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 구간을 제거하면
     * Then 성공한다
     */
    @Test
    void deleteSection_success() {
        //given
        createSection(newLineId, newStationIds.get(1), newStationIds.get(2));
        Long stationId = newStationIds.get(2);

        //when
        ExtractableResponse<Response> response = deleteSection(stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 마지막이 아닌 구간을 제거하려할 경우
     * Then 실패한다
     */
    @Test
    void deleteSection_failIfSectionIsNotLast() {
        //given
        createSection(newLineId, newStationIds.get(1), newStationIds.get(2));
        Long stationId = newStationIds.get(1);

        //when
        ExtractableResponse<Response> response = deleteSection(stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().asString()).contains("it is not the last section of line");
    }

    /**
     * Given 1개 구간 지하철 노선을 생성하고
     * When 해당 구간을 제거하려고 할 경우
     * Then 실패한다
     */
    @Test
    void deleteSection_failIfLineHasOnlyOneSection() {
        //given
        Long stationId = newStationIds.get(1);

        //when
        ExtractableResponse<Response> response = deleteSection(stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().asString()).contains("The line has only one section");
    }

    private ExtractableResponse<Response> deleteSection(Long stationId) {
        ExtractableResponse<Response> response = RestAssured.given()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", newLineId, stationId)
                .then().extract();
        return response;
    }

    public static ExtractableResponse<Response> createSection(Long lineId, Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", "10");
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().extract();
        return response;
    }
}
