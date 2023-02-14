package subway;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import java.util.List;

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
    LineRequest 제주선 = LineAcceptanceTest.제주선_생성();

    long 제주선ID = 1L;
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

    /**
     * given 백두산역과 서귀포역(하행 종점) 을 구간으로 갖는 호선이 존재할 때,
     * when 하행선인 서귀포역을 제거하면
     * then 서귀포역이 삭제된다.
     */
    @Test
    void 구간_제거_테스트() {
        제주선_구간_추가(제주선ID, new SectionRequest(백두산역ID, 서귀포역ID, 10));

        ExtractableResponse<Response> extract = 하행선_제거(제주선ID);

        assertThat(extract.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    /**
     * given 2개의 구간이 존재할 때
     * when 하행선을 제거하면
     * then 호선의 Station 수는 하나 줄어든다.
     */
    @Test
    void 구간_제거하면_길이가_줄어든다() {
        제주선_구간_추가(제주선ID, new SectionRequest(백두산역ID, 서귀포역ID, 10));
        List<?> 제거_전_Stations = 제주선_노선_조회();

        하행선_제거(제주선ID);
        List<?> 제거_후_Stations = 제주선_노선_조회();

        assertEquals(제거_전_Stations.size() - 1, 제거_후_Stations.size());
    }

    @Test
    void 구간_등록_테스트() {
        long id = 제주선ID;
        SectionRequest sectionRequest = new SectionRequest(백두산역ID, 서귀포역ID, 10);
        assertEquals(제주선_구간_추가(id, sectionRequest).statusCode(), HttpStatus.CREATED.value());
    }

    /**
     * given 기존 구간 <한라산역(상행) - 백두산역(하행)> 존재
     * when 새로운 구간의 상행선(금강산역)이 기존 구간의 하행선(백두산역)과 다르면
     * then 예외를 던진다.
     */
    @Test
    void 하행종점_상행역_다르면_예외를_던진다() {
        long id = 제주선ID;
        SectionRequest sectionRequest = new SectionRequest(금강산역ID, 러시아역ID, 10);
        assertEquals(제주선_구간_추가(id, sectionRequest).statusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 제주선_구간_추가(long id, SectionRequest sectionRequest) {
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

    private List<?> 제주선_노선_조회() {
        return LineAcceptanceTest.노선_조회(제주선ID).jsonPath().get("stations");
    }

    private ExtractableResponse<Response> 하행선_제거(long lineId) {
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
        return extract;
    }

}
