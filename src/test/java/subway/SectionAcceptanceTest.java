package subway;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.fixture.LineTestFixture;
import subway.fixture.SectionTestFixture;
import subway.fixture.StationTestFixture;
import subway.line.section.Section;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest  extends BaseTest{

    @DisplayName("구간을 등록한다.")
    @Test
    void createSection() {
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");

        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");
        long sectionId = SectionTestFixture.createSection(광교역_아이디, 사당역_아이디, 10, 노선_아이디).jsonPath().getLong("id");

        List<Long> ids = LineTestFixture.getLine(노선_아이디).response().jsonPath().getList("sections.id", Long.class);

       Assertions.assertThat(ids).contains(sectionId);
    }

    @DisplayName("상행역과 하행역은 같을 수 없다.")
    @Test
    void createSectionWithSameStations() {
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");
        int statusCode = SectionTestFixture.createSection(사당역_아이디, 사당역_아이디, 10, 노선_아이디).statusCode();


        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createSectionWithInvalidUpStation() {
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");

        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");
        long 교대역_아이디 = StationTestFixture.createStationFromName("교대역").jsonPath().getLong("id");

        SectionTestFixture.createSection(강남역_아이디, 광교역_아이디, 10, 노선_아이디);
        int statusCode = SectionTestFixture.createSection(사당역_아이디, 교대역_아이디, 10, 노선_아이디).statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    void createSectionWithInvalidDownStation() {
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");

        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");

        SectionTestFixture.createSection(강남역_아이디, 광교역_아이디, 10, 노선_아이디);
        int statusCode = SectionTestFixture.createSection(사당역_아이디, 강남역_아이디, 10, 노선_아이디).statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteSection() {

        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");

        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        SectionTestFixture.createSection(강남역_아이디, 광교역_아이디, 10, 노선_아이디);

        int statusCode = RestAssured.given().when().delete("/lines/{lineId}/sections?stationId={stationId}", 노선_아이디, 광교역_아이디).then().log().all().extract().statusCode();
        Assertions.assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.")
    @Test
    void deleteSectionWithInvalidStation() {
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green").jsonPath().getLong("id");

        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");

        SectionTestFixture.createSection(강남역_아이디, 광교역_아이디, 10, 노선_아이디);
        SectionTestFixture.createSection(광교역_아이디, 사당역_아이디, 10, 노선_아이디);

        int statusCode = RestAssured.given().when().delete("/lines/{lineId}/sections?stationId={stationId}", 노선_아이디, 광교역_아이디).then().log().all().extract().statusCode();
        Assertions.assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
