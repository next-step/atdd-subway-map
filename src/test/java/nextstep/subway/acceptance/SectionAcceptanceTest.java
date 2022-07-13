package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.ExceptionMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@Sql(scripts = {
        "classpath:sql/truncate.sql",
        "classpath:sql/createStations.sql",
        "classpath:sql/createLine.sql",
        "classpath:sql/createSection.sql"})
public class SectionAcceptanceTest extends BaseAcceptanceTest{

    /**
     * When     새로운 구간을 지하철 노선의 하행 종점역에 등록하면
     * Then     지하철 노선의 구간 목록에서 생성된 구간을 찾을 수 있다.
     * Then     지하철 노선의 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역에 구간을 등록한다.")
    void registerSectionTest() {
        //when
        Map<String, Object> requestBody = getRequestBody("2","3",10);
        ExtractableResponse<Response> createdSection = createSection(requestBody);

        //then
        ExtractableResponse<Response> sections = getSections(1);
        int sectionId = createdSection.jsonPath().getInt("id");
        assertThat(sections.jsonPath().getList("id",Integer.class)).contains(sectionId);

        //then
        ExtractableResponse<Response> line = getLine(1);
        assertThat(line.jsonPath().getInt("stations[1].id")).isEqualTo(3);
    }

    private ExtractableResponse<Response> getSections(long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}/sections",lineId)
                .then().log().all()
                .extract();
    }

    /**
     * When     새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이 아니면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역에 구간을 등록한다.")
    void registerIllegalUpStationSectionTest() {
        //when
        Map<String, Object> requestBody = getRequestBody("1","3",10);
        ExtractableResponse<Response> response = createSection(requestBody);

        //then
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(ExceptionMessages.getNotEndpointInputExceptionMessage(1,2));
    }

    /**
     * Given    지하철 노선의 하행 종점역에 새로운 구간을 등록하고
     * When     해당 노선의 하행 종점역의 구간을 제거하면
     * Then     지하철 노선의 구간 목록에서 제거한 구간을 찾을 수 없다.
     * Then     지하철 노선의 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역의 구간을 제거한다.")
    void removeSectionTest() {
        //given
        Map<String, Object> requestBody = getRequestBody("2","3",10);
        ExtractableResponse<Response> createdSection = createSection(requestBody);
        int createdSectionId = createdSection.jsonPath().getInt("id");

        //when
        deleteSection(1,3);

        //then
        ExtractableResponse<Response> lines = getLines();
        assertThat(lines.jsonPath().getList("id",Integer.class)).doesNotContain(createdSectionId);

        //then
        ExtractableResponse<Response> line = getLine(1);
        assertThat(line.jsonPath().getInt("stations[1].id")).isEqualTo(2);

    }

    /**
     * Given    지하철 노선의 상행 종점역에 새로운 구간을 등록하고
     * When     삭제할 구간이 하행 종점역이 아니면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역의 구간을 제거한다.")
    void removeIllegalDownStationSectionTest() {
        //given
        Map<String, Object> requestBody = getRequestBody("2","3",10);
        createSection(requestBody);

        //when
        ExtractableResponse<Response> response = deleteSection(1, 2);
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(ExceptionMessages.getNotEndpointInputExceptionMessage(2,3));
    }

    /**
     * Then     삭제할 지하철 노선의 구간이 1개이면 에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 구간이 1개일 때 구간을 제거한다.")
    void remainAtLeastOneSectionTest() {
        //then
        ExtractableResponse<Response> response = deleteSection(1, 2);
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(ExceptionMessages.getNeedAtLeastOneSectionExceptionMessage());
    }

    private ExtractableResponse<Response> createSection(Map<String, Object> requestBody) {
        return RestAssured
            .given().log().all()
            .body(requestBody)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections",1)
            .then().log().all()
            .extract();
    }

    private Map<String, Object> getRequestBody(String upStationId, String downStationId, long distance) {
        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("upStationId", upStationId);
        requestBody.put("downStationId",downStationId);
        requestBody.put("distance", distance);
        return requestBody;
    }

    private ExtractableResponse<Response> getLine(long lineId) {
        return RestAssured
            .given().log().all()
            .when().get("/lines/{id}",lineId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getLines() {
        return RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> deleteSection(long lineId, long stationId) {
        return RestAssured
            .given().log().all()
            .param("stationId", stationId)
            .when().delete("/lines/{id}/sections", lineId)
            .then().log().all()
            .extract();
    }

}
