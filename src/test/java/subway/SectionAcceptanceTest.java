package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.common.DomainExceptionType;

@DirtiesContext
@Sql(
        scripts = "/sql/insert-line-with-oneSection.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired private DataBaseCleanUp dataBaseCleanUp;

    private Long 노선ID = 1L;
    private Long 강남역ID = 1L;
    private Long 역삼역ID = 2L;
    private Long 선릉역ID = 3L;
    private String 선릉역 = "선릉역";
    private Long 삼성역ID = 4L;

    @AfterEach
    void setUp() {
        dataBaseCleanUp.excute();
    }

    /**
     * Senario - 노선의 하행 종점역에 새로운 구간을 추가한다
     * Given - 구간을 1개 가진 노선을 생성하고
     * When - 해당 노선에 새로운 구간(노선의 하행선 = 구간의 상행선)을 추가하면
     * Then - 구간의 하행역이 노선에 추가된다.
     */
    @DisplayName("구간 추가 - 노선의 하행 종점역에 새로운 구간을 추가한다.")
    @Test
    void success_createSectionTest() {
        // given - SQL로 대체

        // when
        구간추가(노선ID, 역삼역ID, 선릉역ID, 7);

        // then
        List<String> stationList = 노선조회(노선ID).jsonPath().getList("stations.name");
        assertThat(stationList).contains(선릉역);
    }

    /**
     * Senario - 추가 구간의 상행역이 노선의 하행종점역이 아니라면 예외를 발생시킨다.
     * Given - 구간을 1개 가진 노선을 생성하고
     * When - 노선의 하행 종점역과 구간의 상행역이 다른 구간을 추가하면
     * Then - 예외를 발생시킨다. */
    @DisplayName("구간 추가 - 추가 구간의 상행역이 노선의 하행종점역이 아니라면 예외를 발생시킨다.")
    @Test
    void fail_createSectionTest_missMatch() {
        // given - SQL로 대체

        // when
        String errorCode = 구간추가(노선ID, 선릉역ID, 삼성역ID, 7).jsonPath().getString("errorCode");

        // then
        assertEquals(DomainExceptionType.UPDOWN_STATION_MISS_MATCH.getCode(), errorCode);
    }

    /**
     * Senario - 추가 구간의 하행역이 노선에 포함되어 있다면 예외를 발생시킨다.
     * Given - 구간을 1개 가진 노선을 생성하고
     * When - 하행역이 노선에 등록되어 있는 구간을 추가한다면
     * Then - 예외를 발생시킨다. */
    @DisplayName("구간 추가 - 추가 구간의 하행역이 노선에 포함되어 있다면 예외를 발생시킨다.")
    @Test
    void fail_createSectionTest_existStation() {
        // given - SQL로 대체

        // when
        String errorCode = 구간추가(노선ID, 역삼역ID, 강남역ID, 7).jsonPath().getString("errorCode");

        // then
        assertEquals(DomainExceptionType.DOWN_STATION_EXIST_IN_LINE.getCode(), errorCode);
    }

    /**
     * Senario - 노선의 하행 종점역을 하행역으로 가진 구간을 삭제한다.
     * Given - 구간을 2개 가진 노선을 생성하고
     * When - 노선의 하행 종점역을 하행역으로 가진 구간을 삭제하면
     * Then - 하행역이 삭제된다. */
    @DisplayName("구간 삭제 - 노선의 하행 종점역을 하행역으로 가진 구간을 삭제한다.")
    @Test
    @Sql(scripts = "/sql/insert-line-with-twoSection.sql")
    void success_deleteSectionTest() {
        // given - SQL 대체

        // when
        var response = 구간삭제(노선ID, 선릉역ID);

        // then
        assertEquals(response.statusCode(), HttpStatus.NO_CONTENT.value());
    }

    /**
     * Senaril - 노선의 하행 종점역이 포함되지 않은 역을 삭제한다면 예외랄 발생시킨다.
     * Given - 구간을 2개 가진 노선을 생성하고
     * When - 노선의 하행 종점역과 다른 하행역을 가지는 구간을 삭제하려 하면
     * Then - 예외를 발생시킨다. */
    @DisplayName("구간 삭제 - 노선의 하행 종점역이 포함되지 않은 역을 삭제한다면 예외랄 발생시킨다.")
    @Test
    @Sql(scripts = "/sql/insert-line-with-twoSection.sql")
    void fail_deleteSectionTest_deleteNotDownSataion() {
        // given - SQL 대체

        // when
        String errorCode = 구간삭제(노선ID, 강남역ID).jsonPath().getString("errorCode");

        // then
        assertEquals(DomainExceptionType.NOT_DOWN_STATION.getCode(), errorCode);
    }

    /**
     * Senario - 구간을 1개 가진 노선의 구간을 삭제한다면 예외를 발생시킨다.
     * Given - 구간을 1개 가진 노선을 생성하고
     * When - 노선 구간을 삭제하려 하면
     * Then - 예외를 발생시킨다. */
    @DisplayName("구간 삭제 - 구간을 1개 가진 노선의 구간을 삭제한다면 예외를 발생시킨다.")
    @Test
    void fail_deleteSectionTest_hasOneSectionLine() {
        // given - SQL 대체

        // when
        String errorCode = 구간삭제(노선ID, 역삼역ID).jsonPath().getString("errorCode");

        // then
        assertEquals(DomainExceptionType.CANT_DELETE_SECTION.getCode(), errorCode);
    }

    private ExtractableResponse<Response> 구간추가(
            Long lineId, Long upStationId, Long downStationId, Integer distance) {

        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .post("/lines/{id}/sections", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }

    private ExtractableResponse<Response> 노선조회(Long lineId) {
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .get("/lines/{id}", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }

    private ExtractableResponse<Response> 구간삭제(Long lineId, Long stationId) {
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .param("stationId", stationId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .delete("/lines/{id}/sections", lineId)
                        .then()
                        .log()
                        .all()
                        .extract();

        return response;
    }
}
