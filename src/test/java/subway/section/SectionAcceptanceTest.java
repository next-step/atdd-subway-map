package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:SQLScripts/00.clear-database.sql", "classpath:SQLScripts/01.station-data.sql", "classpath:SQLScripts/02.section-data.sql"})
public class SectionAcceptanceTest {
    static final Long LINE_ID_1 = 1L;
    static final Long LINE_ID_2 = 2L;
    static final Long STATION_ID_1 = 1L;
    static final Long STATION_ID_2 = 2L;
    static final Long STATION_ID_3 = 3L;
    static final Long STATION_ID_4 = 4L;
    static final Long DISTANCE_10 = 10L;

    /**
     * Given 지하철 노선을 생성하고
     * When 하행 종점역을 상행역으로 구간을 등록한다
     * Then 지하철 노선등록에 성공한다
     */
    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        // when
        Response response = this.requestCreateSections(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(response.jsonPath().getLong("id")).isEqualTo(LINE_ID_1);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 등록이 된 역을 다시 등록한다
     * Then 지하철 노선등록에 실패한다
     */
    @DisplayName("등록이 된 역으로 구간을 생성하면 실패한다.")
    @Test
    void createSectionExistsStation() {
        // given
        this.requestCreateSections(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // when
        Response response = this.requestCreateSections(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 하행 종점역이 아닌 역으로 구간을 등록한다
     * Then 지하철 노선등록에 실패한다
     */
    @DisplayName("하행 종점역이 아닌 역으로 구간을 생성하면 실패한다.")
    @Test
    void createSectionNotEndStation() {
        // given
        this.requestCreateSections(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // when
        Response response = this.requestCreateSections(LINE_ID_1, STATION_ID_3, STATION_ID_4, DISTANCE_10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 등록한다
     * When 마지막 구간을 삭제한다
     * Then 구간이 삭제된다.
     */
    //TODO: 구간을 삭제한다.
    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        this.requestCreateSections(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);
        this.requestCreateSections(LINE_ID_1, STATION_ID_2, STATION_ID_3, DISTANCE_10);

        // when
        Response response = this.requestDeleteSections(LINE_ID_1, STATION_ID_3);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<Long> stationNames = this.requestSearchLine(LINE_ID_1).jsonPath().getList("stations.id", Long.class);
        assertThat(stationNames).containsAnyOf(STATION_ID_1, STATION_ID_2);
    }


    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 등록한다
     * When 등록되지 않은 역을 삭제한다
     * Then 구간 삭제에 실패한다
     */
    //TODO: 하행 종점역이 아닌 구간을 삭제하면 실패한다.


    /**
     * Given 지하철 노선을 생성하고
     * When 구간이 한개인 노선의 구간을 삭제한다
     * Then 구간 삭제에 실패한다.
     */
    //TODO: 구간이 한개일 때 삭제하면 실패한다.

    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 등록한다
     * When 하행 종점역이 아닌 구간을 삭제한다
     * Then 구간 삭제에 실패한다
     */
    //TODO: 하행 종점역이 아닌 구간을 삭제하면 실패한다.
    private Response requestCreateSections(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE).when().post("/lines/{lineId}/sections", lineId).then().log().all().extract().response();
    }

    private Response requestDeleteSections(Long lineId, Long stationId) {
        return RestAssured.given().log().all().when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId).then().log().all().extract().response();
    }

    private Response requestSearchLine(Long id) {
        return RestAssured.given().log().all().when().get("/lines/{id}", id).then().log().all().extract().response();
    }
}
