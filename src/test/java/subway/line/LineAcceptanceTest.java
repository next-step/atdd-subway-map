package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.dto.LineResponse;
import subway.util.Extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/sql/table-line-truncate.sql")
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private static Map<String, String> 신분당선;
    private static Map<String, String> 분당선;

    private static final String LINE_URL = "/line";

    @BeforeEach
    void init() {
        신분당선 = getParams("신분당선", "bg-red");
        분당선 = getParams("분당선", "bg-yellow");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        //given
        //when
        saveLine(신분당선);
        //then
        List<String> lines = Extractor.get(LINE_URL).jsonPath().getList("name", String.class);
        assertThat(lines).containsAnyOf(신분당선.get("name"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void searchLines() {

        //given
        saveLine(신분당선);
        saveLine(분당선);
        //when
        List<String> lines = Extractor.get(LINE_URL).jsonPath().getList("name", String.class);
        //then
        assertThat(lines).hasSize(2);
        assertThat(lines).containsExactly(신분당선.get("name"), 분당선.get("name"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void searchLine() {

        //given
        long id = saveLine(신분당선).jsonPath().getLong("id");
        //when
        String name = Extractor.get(LINE_URL + "/" + id).jsonPath().get("name");
        //then
        assertThat(name).isEqualTo(신분당선.get("name"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void modifyLine() {

        //given
        long id = saveLine(신분당선).jsonPath().getLong("id");
        //when
        LineResponse lineResponse = Extractor.put(LINE_URL + "/" + id, 분당선)
            .jsonPath().getObject("", LineResponse.class);
        //then
        assertThat(lineResponse.getName()).isEqualTo(분당선.get("name"));
        assertThat(lineResponse.getColor()).isEqualTo(분당선.get("color"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {

        //given
        long id = saveLine(신분당선).jsonPath().getLong("id");
        //when
        Extractor.delete(LINE_URL + "/" + id);
        ExtractableResponse< Response> response = Extractor.get(LINE_URL + "/" + id);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> saveLine(Map<String, String> params) {

        return Extractor.post(LINE_URL, params);
    }

    private Map<String, String> getParams(String name, String color) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }
}
