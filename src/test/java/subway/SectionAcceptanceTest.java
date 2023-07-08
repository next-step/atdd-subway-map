package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.SubwayClient.*;

@Sql("/truncate.sql")
@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 노선에 지하철 구간이 등록된다
     * Then 지하철 노선 정보 조회 시 생성한 지하철 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        Station 또다른_지하철역 = 지하철역을_생성한다("또다른지하철역");
        Map<String, Object> params = createSectionParams(새로운_지하철역.getId(), 또다른_지하철역.getId(), 3L);

        // when
        지하철노선에_지하철구간을_등록한다(id, params);

        // then
        List<String> stationNames = 지하철노선_한개를_조회한다(id).jsonPath().getList("stations.name", String.class);
        assertThat(stationNames).contains(또다른_지하철역.getName());
    }

    /**
     * Given 지하철 노선의 하행 종점역과 다른 상행역을 가진 지하철 구간을 생성하고
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 구간 등록시 에러가 발생한다.
     */
    @DisplayName("지하철 노선의 하행종점역과 다른 상행역을 가진 지하철 구간을 등록할때 에러가 발생한다.")
    @Test
    void createSectionErrorCaseOne() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        Long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        Station 다른_지하철역 = 지하철역을_생성한다("다른지하철역");
        Station 또다른_지하철역 = 지하철역을_생성한다("또다른지하철역");

        // given
        Map<String, Object> params = createSectionParams(다른_지하철역.getId(), 또다른_지하철역.getId(), 3L);

        // when, then
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_등록한다(id, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 구간의 하행역을 지하철 노선에 등록된 역으로 생성하고
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 구간 등록시 에러가 발생한다.
     */
    @DisplayName("지하철 구간의 하행역을 노선에 등록된 역으로 생성하고 구간을 등록할때 에러가 발생한다.")
    @Test
    void createSectionErrorCaseTwo() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        Long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        Station 다른_지하철역 = 지하철역을_생성한다("다른지하철역");
        Station 등록된_지하철역 = 지하철역을_생성한다("지하철역");

        // given
        Map<String, Object> params = createSectionParams(다른_지하철역.getId(), 등록된_지하철역.getId(), 3L);

        // when, then
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_등록한다(id, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 지하철 노선에 지하철 구간을 제거하면
     * Then 지하철 노선에 지하철 구간이 제거된다
     * Then 지하철 노선 정보 조회 시 제거한 지하철 구간을 찾을 수 없다
     */
    @DisplayName("지하철 노선에 구간을 제거한다")
    @Test
    void deleteSection() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        Long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        Station 또다른_지하철역 = 지하철역을_생성한다("또다른지하철역");
        지하철노선에_지하철구간을_등록한다(id, createSectionParams(새로운_지하철역.getId(), 또다른_지하철역.getId(), 3L));

        // when
        지하철노선에_지하철구간을_제거한다(id, 또다른_지하철역);

        // then
        List<String> stationNames = 지하철노선_한개를_조회한다(id).jsonPath().getList("stations.name", String.class);
        assertThat(stationNames).doesNotContain(또다른_지하철역.getName());
    }

    /**
     * Given 지하철 노선에 등록된 하행종점역이 아닌 역을 생성하고
     * When 지하철 노선에 지하철 구간을 제거하면
     * Then 지하철 구간 제거시 에러가 발생한다.
     */
    @DisplayName("지하철 노선에 등록된 하행종점역이 아닌 역을 생성하고 구간을 제거할때 에러가 발생한다")
    @Test
    void deleteSectionErrorCaseOne() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        Long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        Station 또다른_지하철역 = 지하철역을_생성한다("또다른지하철역");
        지하철노선에_지하철구간을_등록한다(id, createSectionParams(새로운_지하철역.getId(), 또다른_지하철역.getId(), 3L));

        // given
        Station 다른_지하철역 = 지하철역을_생성한다("다른지하철역");

        // when
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_제거한다(id, 다른_지하철역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 지하철 노선에 지하철 구간을 제거할 때 지하철 노선에 구간이 1개라면
     * Then 지하철 구간 제거시 에러가 발생한다.
     */
    @DisplayName("지하철 노선에 지하철 구간이 1개인 경우 지하철 구간을 제거할때 에러가 발생한다")
    @Test
    void deleteSectionErrorCaseTwo() {
        Station 지하철역 = 지하철역을_생성한다("지하철역");
        Station 새로운_지하철역 = 지하철역을_생성한다("새로운지하철역");
        Long id = 지하철노선_생성(지하철역, 새로운_지하철역, "신분당선", "bg-red-600", 10L);

        // when
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_제거한다(id, 새로운_지하철역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public Long 지하철노선_생성(Station upStation, Station downStation
            , String lineName, String lineColor, Long lineDistance) {
        Map<String, Object> lineParams = createLineParams(lineName, lineColor
                , upStation.getId()
                , downStation.getId()
                , lineDistance);

        return 지하철노선을_생성한다(lineParams).jsonPath().getLong("id");
    }
}
