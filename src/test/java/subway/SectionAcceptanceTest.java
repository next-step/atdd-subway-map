package subway;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;

@DisplayName("구간 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    String 서귀포역 = LineAcceptanceTest.서귀포역;
    String 한라산역 = LineAcceptanceTest.한라산역;
    String 백두산역 = LineAcceptanceTest.백두산역;
    String 금강산역 = "금강산역";
    String 러시아역 = "러시아역";
    LineRequest 제주선 = LineAcceptanceTest.제주선_생성(); // upStation : 1, downStation : 2
    LineRequest 반도선 = LineAcceptanceTest.반도선_생성(); // upStation : 2, downStation : 3

    long 한라산역ID = 1L;
    long 백두산역ID = 2L;
    long 서귀포역ID = 3L;
    long 금강산역ID = 4L;
    long 러시아역ID = 5L;

    @BeforeEach
    void setUp() {
        StationAcceptanceTest.createOneStation(한라산역);
        StationAcceptanceTest.createOneStation(백두산역);
        StationAcceptanceTest.createOneStation(서귀포역);
        StationAcceptanceTest.createOneStation(금강산역);
        StationAcceptanceTest.createOneStation(러시아역);
        LineAcceptanceTest.노선_생성(제주선);
    }

    @Test
    void 구간_제거_테스트() {
        long lineId = 한라산역ID;
        SectionRequest sectionRequest = new SectionRequest(백두산역ID, 서귀포역ID, 10);
        createSection(lineId, sectionRequest);
        LineAcceptanceTest.노선_조회(lineId);

        ExtractableResponse<Response> extract = RestAssured
            .given().log().all()
            .pathParam("id", lineId)
            .params("stationId", 서귀포역ID)
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .delete("/lines/{id}/sections")
            .then()
            .log().all()
            .extract();

        assertThat(extract.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @Test
    void 구간_등록_테스트() {
        long id = 한라산역ID;
        SectionRequest sectionRequest = new SectionRequest(백두산역ID, 서귀포역ID, 10);
        assertEquals(createSection(id, sectionRequest).statusCode(), HttpStatus.CREATED.value());
    }

    @Test
    void 하행종점_상행역_다르면_예외를_던진다() {
        long id = 한라산역ID;
        SectionRequest sectionRequest = new SectionRequest(금강산역ID, 러시아역ID, 10);
        assertAll(
            () -> assertEquals(createSection(id, sectionRequest).statusCode(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())
            // () -> assertThrows(IllegalSectionException.class, () -> createSection(id, sectionRequest))
            //TODO : 왜 실패하지..?
        );
    }

    public static ExtractableResponse<Response> createSection(long id, SectionRequest sectionRequest) {
        return RestAssured
            .given().log().all()
            .pathParam("id", id)
            .body(sectionRequest)
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/{id}/sections")
            .then()
            .log().all()
            .extract();
    }

    public static void assertFailBadRequest(ExtractableResponse<Response> response, String message) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(message);
    }

    @AfterEach
    void after() {
        long lineId = 1L;
        LineAcceptanceTest.노선_삭제(lineId);
    }

}
