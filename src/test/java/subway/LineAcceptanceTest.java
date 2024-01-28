package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.websocket.OnClose;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extractableResponse.StationApiExtractableResponse.createStationByStationName;
import static subway.extractableResponse.LineApiExtractableResponse.createLine;
import static subway.extractableResponse.LineApiExtractableResponse.selectLines;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성() {
        // given
        String lineName = "신분당선";
        Long upStationId = createStationByStationName("강남역").jsonPath().getLong("id");
        Long downStationId = createStationByStationName("신논현역").jsonPath().getLong("id");
        Map<String, Object> requestBody = createRequestBody(lineName, "bg-red-600", upStationId, downStationId, 10);

        // when
        createLine(requestBody);

        // then
        List<String> lineNames =
                selectLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(lineName);
    }

    private Map<String, Object> createRequestBody(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("color", color);
        requestBody.put("upStationId", upStationId);
        requestBody.put("downStationId", downStationId);
        requestBody.put("distance", distance);

        return requestBody;
    }

}
