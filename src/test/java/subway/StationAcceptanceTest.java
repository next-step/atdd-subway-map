package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.requests.StationRequests.*;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
        ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성_요청하기("강남역");

        // then
        assertThat(지하철역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_목록_조회_요청하기().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void fetchStations() {
        // given
        var stationNames = List.of("강남역", "신사역");
        stationNames.forEach(it -> 지하철역_생성_요청하기(it));

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회_요청하기();

        // then
        assertThat(지하철역_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(지하철역_목록_조회_응답.jsonPath().getList("name", String.class))
                .hasSize(2)
                .containsExactlyElementsOf(stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String 지하철역_이름 = "강남역";
        String 지하철역_ID = 지하철역_생성_요청하기(지하철역_이름).jsonPath().getString("id");

        // when
        ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철역_삭제_요청하기(지하철역_ID);

        // then
        assertThat(지하철역_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(지하철역_목록_조회_요청하기().jsonPath().getList("name", String.class)).doesNotContain(지하철역_이름);
    }
}
