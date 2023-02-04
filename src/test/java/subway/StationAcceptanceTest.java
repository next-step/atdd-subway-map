package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.station.StationRequest;

import java.util.List;

import static org.hamcrest.Matchers.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AbstractAcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 교대역 = "교대역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다.
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다.
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        역_생성(강남역).statusCode(HttpStatus.CREATED.value());
        역_목록_조회()
                .body("name", hasItems(강남역));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다.
     */
    @DisplayName("지하철역 2개를 생성하고 목록을 조회한다.")
    @Test
    void createStations() {
        for (String station : List.of(강남역, 교대역)) {
            역_생성(station);
        }

        역_목록_조회()
                .body("size()", equalTo(2))
                .body("name", contains(강남역, 교대역));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 지하철역 목록 조회 시 앞서 생성한 역을 찾을 수 없다.
     */
    @DisplayName("지하철역 2개를 생성하고 그중 1개를 삭제한다.")
    @Test
    void deleteStation() {
        역_생성(교대역);
        var createResponse = 역_생성(강남역);

        given().
        when()
                .delete(리소스_경로_추출(createResponse)).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        역_목록_조회()
                .body("size()", equalTo(1))
                .body("name", not(contains(강남역)));
    }

    public static ValidatableResponse 역_생성(String stationName) {
        StationRequest request = new StationRequest(stationName);
        return post("/stations", request);
    }

    private ValidatableResponse 역_목록_조회() {
        return get("/stations");
    }
}
