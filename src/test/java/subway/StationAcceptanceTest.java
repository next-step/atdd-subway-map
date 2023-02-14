package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import subway.common.DatabaseCleanup;

import static subway.common.ResponseUtils.ID_추출;
import static subway.common.ResponseUtils.적절한_응답_코드를_받을_수_있다;
import static subway.common.ResponseUtils.n개의_이름_목록을_조회할_수_있다;
import static subway.common.ResponseUtils.목록에서_이름을_찾을_수_없다;
import static subway.fixtures.StationFixtures.강남역;
import static subway.fixtures.StationFixtures.방배역;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 강남역_생성_응답 = 지하철역_생성(강남역);

        // then
        적절한_응답_코드를_받을_수_있다(강남역_생성_응답, HttpStatus.CREATED);

        ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회();
        적절한_응답_코드를_받을_수_있다(지하철역_목록_조회_응답, HttpStatus.OK);
        n개의_이름_목록을_조회할_수_있다(지하철역_목록_조회_응답, new String[]{강남역});
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 2개의 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        String[] stations = {강남역, 방배역};
        Arrays.stream(stations).forEach(StationAcceptanceTest::지하철역_생성);

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회();

        // then
        적절한_응답_코드를_받을_수_있다(지하철역_목록_조회_응답, HttpStatus.OK);
        n개의_이름_목록을_조회할_수_있다(지하철역_목록_조회_응답, stations);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        long 강남역_ID = ID_추출(지하철역_생성(강남역));

        // when
        ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철역_삭제(강남역_ID);

        // then
        적절한_응답_코드를_받을_수_있다(지하철역_삭제_응답, HttpStatus.NO_CONTENT);
        목록에서_이름을_찾을_수_없다(지하철역_목록_조회(), 강남역);
    }

    public static ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제(long stationId) {
        return RestAssured.given().log().all()
            .when().delete("/stations/{id}", stationId)
            .then().log().all()
            .extract();
    }
}