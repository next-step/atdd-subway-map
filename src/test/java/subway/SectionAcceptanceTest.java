package subway;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.line.section.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.AcceptanceMethods.makeLine;
import static subway.AcceptanceMethods.makeStation;

@Sql(value = "/table_truncate.sql")
@DisplayName("지하철 구간 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    /**
     * given 1개의 지하철 노선을 등록하고
     * when 1개의 지하철 구간을 추가 등록하면
     * then 해당 지하철 노선의 구간 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("노선 구간 등록")
    @Test
    void createSection() {
        // given
        Long stationId1 = makeStation("gangnam").jsonPath().getLong("id");
        Long stationId2 = makeStation("yeoksam").jsonPath().getLong("id");
        Long stationId3 = makeStation("samseong").jsonPath().getLong("id");

        Long lineId = makeLine(new LineRequest("신분당선", "bg-red-600", stationId1, stationId2, 10L)).jsonPath().getLong("id");

        // when
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(2L, 3L, 13L))
                .when().log().all()
                .post("/lines/" + lineId + "/sections")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        ExtractableResponse<Response> lineSectionResponse = RestAssured
                .given().log().all()
                .when()
                .get("/lines/" + lineId + "/sections")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(lineSectionResponse.jsonPath().getList("sections.stationId", Long.class))
                .contains(2L, 3L);
    }
}
