package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    @Autowired
    StationRepository stationRepository;
    @Autowired
    LineRepository lineRepository;
    @Autowired
    SectionRepository sectionRepository;

    /**
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 노선에 지하철 구간이 등록된다
     * Then 지하철 노선 정보 조회 시 생성한 지하철 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운_지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(지하철역, 새로운_지하철역, 10L, "신분당선", "bg-red-600");

        Station 또다른_지하철역 = stationRepository.save(new Station("또다른지하철역"));

        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", 새로운_지하철역.getId());
        params.put("downStationId", 또다른_지하철역.getId());
        params.put("distance", 3L);

        // when
        지하철노선에_지하철구간을_등록한다(신분당선, params);

        // then
        List<String> stationNames = LineAcceptanceTest.지하철노선_한개를_조회한다(신분당선.getId())
                .jsonPath().getList("stations.name", String.class);

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
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운_지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(지하철역, 새로운_지하철역, 10L, "신분당선", "bg-red-600");

        Station 다른_지하철역 = stationRepository.save(new Station("다른지하철역"));
        Station 또다른_지하철역 = stationRepository.save(new Station("또다른지하철역"));

        // given
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", 다른_지하철역.getId());
        params.put("downStationId", 또다른_지하철역.getId());
        params.put("distance", 3L);

        // when, then
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_등록한다(신분당선, params);
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
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운_지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(지하철역, 새로운_지하철역, 10L, "신분당선", "bg-red-600");

        Station 다른_지하철역 = stationRepository.save(new Station("다른지하철역"));
        Station 등록된_지하철역 = stationRepository.save(new Station("지하철역"));

        // given
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", 다른_지하철역.getId());
        params.put("downStationId", 등록된_지하철역.getId());
        params.put("distance", 3L);

        // when, then
        ExtractableResponse<Response> response = 지하철노선에_지하철구간을_등록한다(신분당선, params);
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
        Station upStation = stationRepository.save(new Station("지하철역"));
        Station downStation = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(upStation, downStation, 10L, "신분당선", "bg-red-600");

        Station 새로운_지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Station 또다른_지하철역 = stationRepository.save(new Station("또다른지하철역"));

        Section registSection = sectionRepository.save(new Section(새로운_지하철역, 또다른_지하철역, 3L));
        신분당선.registerSection(registSection);

        // when
        지하철노선에_구간을_제거한다(신분당선, 또다른_지하철역);
        List<String> stationNames = LineAcceptanceTest.지하철노선_한개를_조회한다(신분당선.getId())
                .jsonPath().getList("stations.name", String.class);
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
        Station upStation = stationRepository.save(new Station("지하철역"));
        Station downStation = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(upStation, downStation, 10L, "신분당선", "bg-red-600");

        Station 새로운_지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Station 또다른_지하철역 = stationRepository.save(new Station("또다른지하철역"));
        Section registSection = sectionRepository.save(new Section(새로운_지하철역, 또다른_지하철역, 3L));
        신분당선.registerSection(registSection);

        // given
        Station 다른_지하철역 = stationRepository.save(new Station("다른지하철역"));

        // when
        ExtractableResponse<Response> response = 지하철노선에_구간을_제거한다(신분당선, 다른_지하철역);

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
        Station 지하철역 = stationRepository.save(new Station("지하철역"));
        Station 새로운지하철역 = stationRepository.save(new Station("새로운지하철역"));
        Line 신분당선 = 지하철노선_생성(지하철역, 새로운지하철역, 10L, "신분당선", "bg-red-600");

        // when
        ExtractableResponse<Response> response = 지하철노선에_구간을_제거한다(신분당선, 새로운지하철역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철노선에_지하철구간을_등록한다(Line line, Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + line.getId() + "/sections")
                .then().log().all()
                .extract();

        return response;
    }

    private static ExtractableResponse<Response> 지하철노선에_구간을_제거한다(Line 신분당선, Station 또다른_지하철역) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + 신분당선.getId() + "/sections?stationId=" + 또다른_지하철역.getId())
                .then().log().all()
                .extract();

        return response;
    }

    private Line 지하철노선_생성(Station upStation, Station downStation
            , long distance, String lineName, String lineColor) {
        Section section = sectionRepository.save(new Section(upStation, downStation, distance));

        return lineRepository.save(new Line(lineName, lineColor, section));
    }
}
