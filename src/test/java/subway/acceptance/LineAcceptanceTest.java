package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import subway.internal.BaseTestSetup;
import subway.internal.LineTestApi;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends BaseTestSetup {
    /**
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("노선을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선,남색,1,1,5", "2호선,초록색,2,3,10"})
    void createLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        // when
        ExtractableResponse<Response> response = LineTestApi.createLine(name, color, upStationId, downStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = LineTestApi.showLines().jsonPath().getList("name");
        assertThat(lineNames).containsAnyOf(name);
    }
}
