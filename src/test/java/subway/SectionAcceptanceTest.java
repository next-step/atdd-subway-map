package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subway.fixture.LineTestFixture;
import subway.fixture.SectionTestFixture;
import subway.fixture.StationTestFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@ActiveProfiles("AcceptanceTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest  extends BaseTest{

    @DisplayName("구간을 등록한다.")
    @Test
    void createSection() {
        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green", 강남역_아이디, 광교역_아이디).jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");

        long sectionId = SectionTestFixture.createSection(광교역_아이디, 사당역_아이디, 10, 노선_아이디).jsonPath().getLong("id");
        long 조회한_구간_아이디 = RestAssured.given().when().get("/sections/{id}", 노선_아이디).then().log().all().extract().jsonPath().getLong("id");

        Assertions.assertThat(sectionId).isEqualTo(조회한_구간_아이디);
    }

    @DisplayName("노선이 하나뿐인 구간을 삭제하는 경우 에러 처리")
    @Test
    void deleteSection() {
        long 강남역_아이디 = StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        long 광교역_아이디 = StationTestFixture.createStationFromName("광교역").jsonPath().getLong("id");
        long 노선_아이디 = LineTestFixture.createLine("2호선", "green", 강남역_아이디, 광교역_아이디).jsonPath().getLong("id");
        long 사당역_아이디 = StationTestFixture.createStationFromName("사당역").jsonPath().getLong("id");
        long 교대역_아이디 = StationTestFixture.createStationFromName("교대역").jsonPath().getLong("id");


        long sectionId_1 = SectionTestFixture.createSection(광교역_아이디, 사당역_아이디, 10, 노선_아이디).jsonPath().getLong("id");
        long sectionId_2 = SectionTestFixture.createSection(사당역_아이디, 교대역_아이디, 10, 노선_아이디).jsonPath().getLong("id");


        int statusCode = RestAssured.given().when().delete("/lines/{lineId}/sections?stationId={stationId}", 노선_아이디, 교대역_아이디).then().log().all().extract().statusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value());

        int status = RestAssured.given().when().get("/sections/{sectionId}", sectionId_2).then().log().all().extract().statusCode();
        Assertions.assertThat(status).isEqualTo(500);
    }
}
