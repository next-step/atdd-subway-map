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
import subway.line.section.CannotAddSectionException;
import subway.line.section.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.AcceptanceMethods.*;

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
        ExtractableResponse<Response> response = makeSection(lineId, new SectionRequest(stationId2, stationId3, 13L));

        // then
        assertThat(response.jsonPath().getList("sections.stationId", Long.class))
                .containsExactly(stationId1, stationId2, stationId3);
    }

    /**
     * given 지하철 노선을 생성 후 1개의 구간을 더 등록하고
     * when 이미 등록된 구간을 등록하면
     * then 구간 등록 에러가 발생한다.
     */
    @DisplayName("에러_지하철 노선 등록_이미 존재하는 역")
    @Test
    void addSectionError_duplicated() {
        // given
        Long stationId1 = makeStation("gangnam").jsonPath().getLong("id");
        Long stationId2 = makeStation("yeoksam").jsonPath().getLong("id");
        Long stationId3 = makeStation("samseong").jsonPath().getLong("id");

        Long lineId = makeLine(new LineRequest("신분당선", "bg-red-600", stationId1, stationId2, 10L)).jsonPath().getLong("id");
        makeSection(lineId, new SectionRequest(stationId2, stationId3, 13L));
        // when
        // then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(stationId3, stationId2, 16L))
                .when().log().all()
                .post("/lines/" + lineId + "/sections")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given 지하철 노선을 생성 후
     * when 하행 종점역이 아닌 역을 상행선으로 등록하려고 하면
     * then 구간 등록 에러가 발생한다.
     */
    @DisplayName("에러_지하철 노선 등록_하행 종점역이 일치하지 않음")
    @Test
    void addSectionError_isNotCurrentDownStation() {
        // given
        Long stationId1 = makeStation("gangnam").jsonPath().getLong("id");
        Long stationId2 = makeStation("yeoksam").jsonPath().getLong("id");
        Long stationId3 = makeStation("samseong").jsonPath().getLong("id");

        Long lineId = makeLine(new LineRequest("신분당선", "bg-red-600", stationId1, stationId2, 10L)).jsonPath().getLong("id");

        // when
        // then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(stationId1, stationId3, 13L))
                .when().log().all()
                .post("/lines/" + lineId + "/sections")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
