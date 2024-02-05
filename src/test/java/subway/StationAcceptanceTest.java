package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationAcceptanceTest {

    @Autowired
    private HibernateUtil hibernateUtil;

    @BeforeEach
    void setUp() {
        hibernateUtil.clear();
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
        ExtractableResponse<Response> response = RequestFixtures.지하철역_생성_요청하기(
            Fixtures.getCreateStationParams("강남역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = RequestFixtures.지하철역_목록_조회_요청하기().jsonPath()
            .getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철 역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"));

        RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("역삼역"));

        // when
        List<String> stationNames = RequestFixtures.지하철역_목록_조회_요청하기().jsonPath()
            .getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철 역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"));

        // when
        RequestFixtures.지하철역_삭제_요청하기(1L);

        // then
        List<String> stationNames = RequestFixtures.지하철역_목록_조회_요청하기().jsonPath()
            .getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }
}
