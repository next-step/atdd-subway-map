package subway;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.common.Comment;
import subway.controller.request.LineRequest;
import subway.controller.response.LineResponse;
import subway.util.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/insert-station.sql")
class LineAcceptanceTest {

    private static final String LINE_PATH = "/lines";

    private static final String NAME = "신분당선";
    private static final String NAME2 = "이호선";

    private Map<String, Object> 신분당선;
    private Map<String, Object> 이호선;

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<>();
        신분당선.put("name", NAME);
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", 1);
        신분당선.put("downStationId", 2);
        신분당선.put("distance", 10);

        이호선 = new HashMap<>();
        이호선.put("name", NAME2);
        이호선.put("color", "bg-green-600");
        이호선.put("upStationId", 3);
        이호선.put("downStationId", 4);
        이호선.put("distance", 10);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine()  {
        // given
        // 지하철 노선을 생성하면
        LineResponse createdLine = 지하철노선_생성(신분당선);

        // when
        // 지하철 노선 목록 조회 시
        List<LineResponse> selectedLines = 지하철노선_목록조회();

        Optional<LineResponse> maybeLine = selectedLines.stream()
                .filter(line -> line.getId().equals(createdLine.getId()))
                .findFirst();

        // then
        // 생성한 노선이 존재한다
        assertThat(maybeLine).isNotEmpty();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLine() {
        // given
        // 2개의 지하철 노선을 생성하고
        지하철노선_생성(신분당선);
        지하철노선_생성(이호선);

        // when
        // 지하철 노선 목록을 조회하면
        final List<String> lineNames = 지하철노선목록의_이름_조회();

        // then
        // 2개의 노선이 존재한다
        assertThat(lineNames).hasSize(2);
    }


    @DisplayName("지하철 노선 단건을 조회한다")
    @Test
    void getLineTest() {
        // given
        // 지하철 노선을 생성하고
        LineResponse createdLine = 지하철노선_생성(신분당선);

        // when
        // 생성한 노선을 조회하면
        LineResponse selectedLine = 지하철노선_단건조회(createdLine.getId());

        // then
        // 생성한 노선이 존재한다
        assertThat(selectedLine.getId()).isEqualTo(createdLine.getId());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철 노선을 생성하고
        LineResponse createdLine = 지하철노선_생성(신분당선);

        String updateName = "개정된 신분당선";
        String updateColor = "bg-red-500";

        // when
        // 생성한 노선을 수정하면
        지하철_노선을_수정한다(updateName, updateColor);

        // then
        // 생성한 노선 정보가 수정되어있다
        LineResponse selectedLine = 지하철노선_단건조회(createdLine.getId());
        assertThat(selectedLine.getName()).isEqualTo(updateName);
        assertThat(selectedLine.getColor()).isEqualTo(updateColor);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철 노선을 생성하고
        LineResponse createdLine = 지하철노선_생성(신분당선);

        // when
        // 생성한 노선을 삭제하면
        지하철_노선을_삭제한다(createdLine.getId());

        // then
        // 노선 목록 조회 시 삭제된 노선이 존재하지 않는다
        List<LineResponse> selectedLines = 지하철노선_목록조회();

        Optional<LineResponse> maybeLine = selectedLines.stream()
                .filter(line -> line.getId().equals(createdLine.getId()))
                .findFirst();

        assertThat(maybeLine).isEmpty();
    }

    @Comment("지하철 노선을 생성하는 메서드")
    private LineResponse 지하철노선_생성(final Map<String, Object> line) {
        final LineRequest param = MapHelper.readValue(line, LineRequest.class);

        return RestAssured.given().log().all()
                .contentType(JSON)
                .body(param)
                .when().post(LINE_PATH)
                .then().log().all()
                .extract().jsonPath().getObject("", LineResponse.class);
    }


    @Comment("지하철 노선 목록의 이름을 반환하는 함수")
    private List<String> 지하철노선목록의_이름_조회() {
        return 지하철노선_목록조회().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
    }

    @Comment("지하철 노선 목록을 반환하는 함수")
    private List<LineResponse> 지하철노선_목록조회() {
        return RestAssured
                .given().accept(APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH)
                .then().statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getList("", LineResponse.class);
    }

    @Comment("지하철 노선을 수정하는 메서드")
    private void 지하철_노선을_수정한다(final String newLineName, final String newLinColor) {
        RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(Map.of("name", newLineName, "color", newLinColor))
                .when()
                .put("/lines/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Comment("지하철 노선을 조회하는 메서드")
    private LineResponse 지하철노선_단건조회(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}",id)
                .then().log().all()
                .extract().jsonPath().getObject("",LineResponse.class);
    }

    @Comment("지하철 노선을 삭제하는 메서드")
    private void 지하철_노선을_삭제한다(Long id) {
        RestAssured
                .given()
                .when()
                .delete("/lines/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
