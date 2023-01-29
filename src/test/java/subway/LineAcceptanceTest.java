package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관리")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    Station 강남역;
    Station 잠실역;
    Station 천호역;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        잠실역 = stationRepository.save(new Station("잠실역"));
        천호역 = stationRepository.save(new Station("천호역"));
    }

    @DisplayName("지하철노선 생성 성공")
    @Test
    void createLine() {
        지하철_노선_생성("이호선", "green", 강남역.getId(), 잠실역.getId(), 5L);
        List<String> lineNames = 지하철_노선_조회();
        assertThat(lineNames)
                .hasSize(1)
                .containsAnyOf("이호선");
    }

    @DisplayName("지하철노선 2개 생성 성공")
    @Test
    void create_2_Line() {
        지하철_노선_생성("이호선", "green", 강남역.getId(), 잠실역.getId(), 10L);
        지하철_노선_생성("팔호선", "green", 잠실역.getId(), 천호역.getId(), 15L);

        List<String> lineNames = 지하철_노선_조회();
        assertThat(lineNames)
                .hasSize(2)
                .contains("이호선", "팔호선");
    }

    @DisplayName("지하철노선 조회 by id 성공")
    @Test
    void find_Line() {
        Long 이호선Id = 지하철_노선_생성("이호선", "green", 강남역.getId(), 잠실역.getId(), 5L);
        String name = 지하철_노선_조회_by_id(이호선Id);
        assertThat(name).isEqualTo("이호선");
    }

    private Long 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract().jsonPath().getLong("id");
    }

    List<String> 지하철_노선_조회() {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().jsonPath().getList("name", String.class);
    }

    String 지하철_노선_조회_by_id(Long id) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", id)
                .when()
                    .get("/lines/{id}")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().jsonPath().get("name");
    }
}
