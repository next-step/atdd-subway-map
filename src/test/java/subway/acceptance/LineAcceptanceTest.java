package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.domain.Station;


import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.StationApiRequest.*;
import static subway.util.LineApiRequest.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Station upStation = 지하철역_생성_요청(강남역).response().as(Station.class);
        Station downStation = 지하철역_생성_요청(양재역).response().as(Station.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상,
            upStation.getId(), downStation.getId(), 10);

        // then
        지하철_노선_생성됨(response, 신분당선_이름, 신분당선_색상, upStation.getName(), downStation.getName());
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response, String name, String color,
        String upStationName, String downStationName) {
        List<String> stationNames = response.jsonPath().getList("stations.name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAnyOf(upStationName, downStationName);
    }
}
