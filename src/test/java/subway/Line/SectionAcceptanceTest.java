package subway.Line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 구간 관련 기능")
@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest extends LineAcceptanceFixture {
    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 새로운 구간의 상행역이 기존 노선의 하행 종점역이 아니면,
     * Then: 예외가 발생한다.
     */
    @Test
    @DisplayName("상행역이 기존 노선의 하행 종점역이 아니면 예외 발생")
    void createSection_상행역이_기존_노선의_하행_종점역이_아니면_예외발생() {
        // Given & When
        ExtractableResponse<Response> response = createSection(신분당선, 신사역, 양재역, 10);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 기존 노선의 하행 종점역이 새로운 구간의 하행역이 되면,
     * Then: 예외가 발생한다.
     */
    @Test
    @DisplayName("기존 노선의 하행 종점역이 새로운 구간의 하행역이 되면 예외 발생")
    void createSection_기존_노선의_하행_종점역이_새로운_구간의_하행역이_되면_예외_발생() {
        // Given & When
        ExtractableResponse<Response> response = createSection(신분당선, 강남역, 논현역, 10);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 새로운 구간이 등록이 되면,
     * Then: 노선의 길이는 새로운 구간 길이 만큼 증가한다.
     */
    @Test
    @DisplayName("노선의 길이는 새로운 구간 길이 만큼 증가한다")
    void createSection_구간_길이() {
        // Given
        var sectionParam = createSectionParam(논현역, 양재역, 10);

        // When
        createSection(신분당선, 논현역, 양재역, 10);

        // Then
        int 신분당선_길이 = getStationsId(신분당선).jsonPath().getInt("distance");

        assertThat(신분당선_길이).isEqualTo(17);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 새로운 구간이 등록이 되면,
     * Then: 노선의 하행 종점역은 새로운 구간의 하행역이 된다.
     */
    @Test
    @DisplayName("노선 하행 종점역은 새로운 구간의 하행역이 된다")
    void createSection_노선_하행_종점역은_새로운_구간의_하행역이_된다() {
        // Given & When
        createSection(신분당선, 논현역, 양재역, 10);

        // Then
        var ids = getStationsId(신분당선).jsonPath().getList("stations.id");

        var stationIds = stationIdsToList(ids);
        var 노선_하행_종점역 = stationIds.get(stationIds.size() - 1);

        assertThat(노선_하행_종점역).isEqualTo(양재역);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 노선의 마지막 구간이 아닌 구간을 제거하면,
     * Then: 예외가 발생한다.
     */
    @Test
    @DisplayName("노선의 마지막 구간이 아닌 구간을 제거하면 예외가 발생한다")
    void deleteSection_노선의_마지막_구간이_아닌_구간을_제거하면_예외_발생() {
        // Given
        createSection(신분당선, 논현역, 양재역, 10);

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all().when()
                .queryParam("stationId", 논현역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("/lines/" + 신분당선 + "/sections")
                .then().log().all().extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 구간이 1개일때 삭제를 하면,
     * Then: 예외가 발생한다.
     */
    @Test
    @DisplayName("노선의 마지막 구간이 아닌 구간을 제거하면 예외가 발생한다")
    void deleteSection_구간이_한개일때_구간을_제거하면_예외_발생() {
        // Given & When
        ExtractableResponse<Response> response = RestAssured.given().log().all().when()
                .queryParam("stationId", 논현역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("/lines/" + 신분당선 + "/sections")
                .then().log().all().extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 마지막 구간을 삭제를 하면,
     * Then: 기존의 하행 종점역은 삭제가 되고, 삭제된 구간의 상행역이 하행 종점역이 된다.
     */
    @Test
    @DisplayName("구간을_삭제하면_삭제된_구간_상행역이_하행_종점역이_된다")
    void deleteSection_구간을_삭제하면_삭제된_구간_상행역이_하행_종점역이_된다() {
        // Given
        createSection(신분당선, 논현역, 양재역, 10);

        // When
        deleteSection();

        // Then
        var ids = getStationsId(신분당선).jsonPath().getList("stations.id");
        var stationIds = stationIdsToList(ids);
        var 노선_하행_종점역 = stationIds.get(stationIds.size() - 1);

        assertThat(노선_하행_종점역).isEqualTo(논현역);
    }

    private ExtractableResponse<Response> getStationsId(Long lineId) {
        return RestAssured.given().log().all().when().get("/lines/" + lineId).then()
                .log().all().extract();
    }

    private List<Long> stationIdsToList(List<Object> ids) {
        return ids.stream().map(id -> Long.valueOf((int)id)).collect(Collectors.toList());
    }

    private ExtractableResponse<Response> deleteSection() {
        return RestAssured.given().log().all().when()
                .queryParam("stationId", 양재역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("/lines/" + 신분당선 + "/sections")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> createSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        return RestAssured.given().log().all().when()
                .body(createSectionParam(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + lineId + "/sections")
                .then().log().all().extract();
    }

    private HashMap<String, Object> createSectionParam(Long upStationId, Long downStationId, int distance) {
        var sectionParam = new HashMap<String, Object>();
        sectionParam.put("upStationId", upStationId);
        sectionParam.put("downStationId", downStationId);
        sectionParam.put("distance", distance);
        return sectionParam;
    }
}
