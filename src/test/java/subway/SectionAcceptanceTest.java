package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.restassured.http.ContentType;
import subway.domain.LineResponse;

public class SectionAcceptanceTest extends BaseAcceptanceTest {
    @Autowired
    private LineRepository lineRepository;

    @Test
    void test_특정_노선에_구간을_등록하면_노선_조회시_등록한_구간을_확인할_수_있다() {
        //given
        Map<String, String> 역삼역 = Map.of("name", "역삼역");
        Map<String, String> 선릉역 = Map.of("name", "선릉역");
        Map<String, String> 강남역 = Map.of("name", "강남역");
        Map<String, String> 왕십리역 = Map.of("name", "왕십리역");
        createStation(역삼역);
        createStation(선릉역);
        createStation(강남역);
        createStation(왕십리역);

        LineResponse lineResponse = createLine("이호선");

        //when
        Map<String, String> sectionPostRequest = Map.of(
                                    "downStationId", "4",
                                    "upStationId", "2",
                                    "distance", "10");
        given().body(sectionPostRequest)
               .contentType(ContentType.JSON)
               .when().post("/lines/" + lineResponse.getId() + "/sections")
               .then().log().all();

        //then
        LineResponse response = when()
                                .get("/lines/" + lineResponse.getId())
                                .then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        assertAll(
            () -> assertThat(response.countSections()).hasSize(1),
            () -> assertThat(response.getUpStationId()).isEqualTo(2),
            () -> assertThat(response.getDownStationId()).isEqualTo(4),
            () -> assertThat(response.getDistance()).isEqualTo(10)
        );
    }
}
