package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static config.fixtures.subway.StationLineMockData.호남선_생성_상행_하행_설정;
import static config.fixtures.subway.StationMockData.역_10개;
import static io.restassured.RestAssured.given;
import static subway.StationLineSteps.지하철_노선_생성_요청;
import static subway.StationSteps.지하철_역_생성_요청;
import static utils.HttpResponseUtils.getCreatedLocationId;

@DisplayName("지하철 구간 관리")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationSectionAcceptanceTest {

    @BeforeEach
    void 초기_지하철_역_설정() {
        지하철_역_생성_요청(역_10개);
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때, 상행역 ID를 1보다 작은 숫자로 요청하면
     * Then  지하철 구간 생성에 실패한다.
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void 상행역_ID가_1보다_작은_숫자일_경우_등록_실패(int upStationId) {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", upStationId);
        section.put("downStationId", "2");
        section.put("distance", 1);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때, 하행역 ID를 1보다 작은 숫자로 요청하면
     * Then  지하철 구간 생성에 실패한다.
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void 하행역_ID가_1보다_작은_숫자일_경우_등록_실패(int downStationId) {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "1");
        section.put("downStationId", downStationId);
        section.put("distance", 1);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때, 거리를 1보다 작은 숫자로 요청하면
     * Then  지하철 구간 생성에 실패한다.
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void 거리가_1보다_작은_숫자일_경우_등록_실패(int distance) {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> section = new HashMap<>();
        section.put("downStationId", "1");
        section.put("upStationId", "2");
        section.put("distance", distance);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 상행역이 하행 종점역으로 등록되어 있으면서
     * When  요청한 하행역이 구간으로 등록되어 있지 않을 경우
     * Then  지하철 구간 등록에 성공한다.
     */
    @Test
    void 상행역이_하행_종점역으로_등록되어_있고_하행역이_구간으로_등록되지_않은_역일_경우_등록_성공() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "2"); // 호남선에서 하행 종점역으로 설정된 역 ID
        section.put("downStationId", "4"); // 구간으로 등록되지 않은 역 ID
        section.put("distance", 10);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 상행역이 하행 종점역으로 등록되어 있지 않을 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 상행역이_하행_종점역으로_등록되어_있지_않을_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 4L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "5"); // 상행역이 하행 종점역으로 등록되어 있지 않은 역 ID
        section.put("downStationId", "10");
        section.put("distance", 10);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 상행역이 역으로 등록되어 있지 않을 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 상행역으로_역_등록되어_있지_않은_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 10L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "11");
        section.put("downStationId", "3");
        section.put("distance", 10);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 상행역이 이미 구간으로 등록되어 있는 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 상행역이_이미_구간에_등록되어_있는_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> 등록_성공_할_구간 = new HashMap<>();
        등록_성공_할_구간.put("upStationId", "2");
        등록_성공_할_구간.put("downStationId", "3");
        등록_성공_할_구간.put("distance", 10);

        // when
        given().log().all()
                .body(등록_성공_할_구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        Map<String, Object> 등록_실패_할_구간 = new HashMap<>();
        등록_실패_할_구간.put("upStationId", "2"); // 이미 구간으로 등록된 역 ID
        등록_실패_할_구간.put("downStationId", "4");
        등록_실패_할_구간.put("distance", 10);

        given().log().all()
                .body(등록_실패_할_구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 하행역이 이미 하행 종점역으로 등록되어 있는 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 하행역이_이미_하행_종점역으로_등록되어_있는_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 4L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "4");
        section.put("downStationId", "4"); // 이미 하행 종점역으로 등록되어 있는 역 ID
        section.put("distance", 10);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 하행역이 역으로 등록되어 있지 않은 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 하행역이_역으로_등록되어_있지_않은_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 10L));

        Map<String, Object> section = new HashMap<>();
        section.put("upStationId", "10");
        section.put("downStationId", "11"); // 역으로 등록되지 않은 역 ID
        section.put("distance", 10);

        // when
        given().log().all()
                .body(section)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선이 생성되고
     * When  지하철 구간을 생성할 때,
     * When  요청한 하행역이 이미 구간으로 등록되어 있는 경우
     * Then  지하철 구간 등록에 실패한다.
     */
    @Test
    void 하행역이_이미_구간에_등록되어_있는_경우_등록_실패() {
        // given
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청(호남선_생성_상행_하행_설정(1L, 2L));

        Map<String, Object> 등록_성공_할_구간 = new HashMap<>();
        등록_성공_할_구간.put("upStationId", "2");
        등록_성공_할_구간.put("downStationId", "3");
        등록_성공_할_구간.put("distance", 10);

        // when
        given().log().all()
                .body(등록_성공_할_구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        Map<String, Object> 등록_실패_할_구간 = new HashMap<>();
        등록_실패_할_구간.put("upStationId", "3");
        등록_실패_할_구간.put("downStationId", "2"); // 이미 구간에 등록되어 있는 역 ID
        등록_실패_할_구간.put("distance", 10);

        given().log().all()
                .body(등록_실패_할_구간)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%d/sections", getCreatedLocationId(response)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}