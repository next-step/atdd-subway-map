package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.fixture.StationFixture;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 생성_지하철역(StationFixture.선릉);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> 등록된_지하철역_이름_목록 =
                조회_지하철역_목록().jsonPath().getList("name", String.class);

        assertThat(등록된_지하철역_이름_목록).containsAnyOf(StationFixture.선릉.name());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 생성 후 목록을 조회한다.")
    @Test
    void createAndReadStations() {
        // given
        생성_지하철역(StationFixture.연신내);
        생성_지하철역(StationFixture.충무로);

        // when
        List<Map> 등록된_지하철역_목록 = 조회_지하철역_목록().jsonPath().get();

        // then
        assertThat(등록된_지하철역_목록).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 생성 후 삭제한다.")
    @Test
    void createAndDeleteStation() {
        // given
        ExtractableResponse<Response> response = 생성_지하철역(StationFixture.교대);
        Long stationId = response.body().jsonPath().getLong("id");

        // when
        삭제_지하철역(stationId);

        // then
        List<Long> 등록된_지하철역_고유번호_목록 =
                조회_지하철역_목록().jsonPath().getList("id", Long.class);

        assertThat(등록된_지하철역_고유번호_목록).doesNotContain(stationId);
    }

    private ExtractableResponse<Response> 생성_지하철역(StationFixture station) {
        return RestAssured.given().log().all()
                .body(station.toMap())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 삭제_지하철역(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 조회_지하철역_목록() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

}