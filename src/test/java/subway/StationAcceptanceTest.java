package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.CommonApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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
        ExtractableResponse<Response> response = CommonApi.Station.createStationBy("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(CommonApi.Station.listStationName()).containsExactly("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("모든 지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        final String 강남역 = "강남역";
        final String 역삼역 = "역삼역";
        CommonApi.Station.createStationBy(강남역);
        CommonApi.Station.createStationBy(역삼역);

        // when
        ExtractableResponse<Response> response = CommonApi.Station.listStation();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsExactly(강남역, 역삼역);
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 목록을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        final String 강남역 = "강남역";
        final Long 강남역_ID = CommonApi.Station.createStationBy(강남역).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = CommonApi.Station.deleteStationBy(강남역_ID);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        assertThat(CommonApi.Station.listStationName()).doesNotContain(강남역);
    }


}