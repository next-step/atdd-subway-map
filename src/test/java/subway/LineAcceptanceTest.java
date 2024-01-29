package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.fixture.LineTestFixture;
import subway.fixture.StationTestFixture;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private final String 노선이름_1 = "1호선";
    private final String 노선이름_2 = "2호선";

    private final String 노선색_1 = "RED";
    private final String 노선색_2 = "GREEN";

    private long 상행종점_아이디;
    private long 하행종점_아이디;

    @BeforeEach
    @Sql({"/sql/delete-station.sql","/sql/delete-line.sql"})
    void setUp() {
        상행종점_아이디 =StationTestFixture.createStationFromName("강남역").jsonPath().getLong("id");
        하행종점_아이디 =StationTestFixture.createStationFromName("역삼역").jsonPath().getLong("id");;
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        LineTestFixture.createLine(노선이름_1, 노선색_1, 상행종점_아이디, 하행종점_아이디);
        List<String> names = LineTestFixture.allLines().jsonPath().getList("name", String.class);

        //then
        assertThat(names).contains(노선이름_1);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        LineTestFixture.createLine(노선이름_1, 노선색_1, 상행종점_아이디, 하행종점_아이디);
        LineTestFixture.createLine(노선이름_2, 노선색_2, 상행종점_아이디, 하행종점_아이디);

        // when
        List<String> names = LineTestFixture.allLines().jsonPath().getList("name", String.class);

        //then
        assertThat(names).hasSize(2);
        assertThat(names).containsExactly(노선이름_1, 노선이름_2);
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void showLine() {
        // given
        long 노선_아이디 = 1l;

        // when
        LineTestFixture.createLine(노선이름_1, 노선색_1, 상행종점_아이디, 하행종점_아이디);
        ExtractableResponse<Response> response = LineTestFixture.showLine(노선_아이디);

        //then
        assertThat(response.jsonPath().getLong("id")).isEqualTo(노선_아이디);
        assertThat(response.jsonPath().getString("name")).isEqualTo(노선이름_1);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        long 노선_아이디 = 1l;
        LineTestFixture.createLine(노선이름_1, 노선색_1, 상행종점_아이디, 하행종점_아이디);

        String 변경할_노선이름 = "3호선";
        String 변경할_노선색 = "ORANGE";
        HashMap<String, String> params = new HashMap<>();
        params.put("name", 변경할_노선이름);
        params.put("color", 변경할_노선색);

        // when
        LineTestFixture.updateLine(노선_아이디, params);
        ExtractableResponse<Response> response = LineTestFixture.showLine(노선_아이디);

        //then
        assertThat(response.jsonPath().getString("name")).isEqualTo(변경할_노선이름);
        assertThat(response.jsonPath().getString("color")).isEqualTo(변경할_노선색);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        long 노선_아이디 = 1l;
        LineTestFixture.createLine(노선이름_1, 노선색_1, 상행종점_아이디, 하행종점_아이디);

        // when
        LineTestFixture.deleteLine(노선_아이디);
        List<Long> ids = LineTestFixture.allLines().jsonPath().getList("id", Long.class);

        //then
        assertThat(ids).doesNotContain(노선_아이디);
    }


}
