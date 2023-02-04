package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.util.BaseAcceptanceTest;
import subway.controller.response.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql("/insert-station.sql")
class LineAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<>();
        신분당선.put("name", SHIN_BUN_DANG);
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", 1);
        신분당선.put("downStationId", 2);
        신분당선.put("distance", 10);

        이호선 = new HashMap<>();
        이호선.put("name", LEE_HO_SEON);
        이호선.put("color", "bg-green-600");
        이호선.put("upStationId", 3);
        이호선.put("downStationId", 4);
        이호선.put("distance", 10);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철 노선을 생성하면
        ExtractableResponse<Response> response = 지하철노선_생성(신분당선);
        long id = response.jsonPath().getLong("id");

        // 지하철 노선 목록 조회 시
        List<LineResponse> selectedLines = 지하철노선_목록조회();

        Optional<LineResponse> maybeLine = selectedLines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst();

        // then
        // 생성한 노선이 존재한다
        assertThat(maybeLine).isNotEmpty();
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
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
        List<String> lineNames = 지하철노선목록의_이름_조회();

        // then
        // 2개의 노선이 존재한다
        assertThat(lineNames).hasSize(2);
    }


    @DisplayName("지하철 노선 단건을 조회한다")
    @Test
    void getLineTest() {
        // given
        // 지하철 노선을 생성하고
        ExtractableResponse<Response> response = 지하철노선_생성(신분당선);
        long id = response.jsonPath().getLong("id");

        // when
        // 생성한 노선을 조회하면
        LineResponse selectedLine = 지하철노선_단건조회(id);

        // then
        // 생성한 노선이 존재한다
        assertThat(selectedLine.getId()).isEqualTo(id);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철 노선을 생성하고
        ExtractableResponse<Response> response = 지하철노선_생성(신분당선);

        long id = response.jsonPath().getLong("id");

        String updateName = "개명된 신분당선";
        String updateColor = "bg-red-500";

        // when
        // 생성한 노선을 수정하면
        지하철_노선을_수정한다(id, updateName, updateColor);

        // then
        // 생성한 노선 정보가 수정되어있다
        LineResponse selectedLine = 지하철노선_단건조회(id);
        assertThat(selectedLine.getName()).isEqualTo(updateName);
        assertThat(selectedLine.getColor()).isEqualTo(updateColor);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철 노선을 생성하고
        ExtractableResponse<Response> createdResponse = 지하철노선_생성(신분당선);
        long id = createdResponse.jsonPath().getLong("id");

        // when
        // 생성한 노선을 삭제하면
        ExtractableResponse<Response> deleteResponse = 지하철_노선을_삭제한다(id);

        // then
        // 노선 목록 조회 시 삭제된 노선이 존재하지 않는다
        List<LineResponse> selectedLines = 지하철노선_목록조회();

        Optional<LineResponse> maybeLine = selectedLines.stream()
                .filter(line -> line.getId().equals(id))
                .findFirst();

        assertThat(deleteResponse.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(maybeLine).isEmpty();
    }

}
