package subway.acceptance;

import autoparams.AutoSource;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;
import subway.controller.dto.CreateLineRequest;
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
    @AutoSource
    void createLine(CreateLineRequest request) {
        // when
        ExtractableResponse<Response> response = LineTestApi.createLine(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = LineTestApi.showLines().jsonPath().getList("name");
        assertThat(lineNames).containsAnyOf(request.getName());
    }
}
