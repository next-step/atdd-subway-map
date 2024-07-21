package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.SubwayUtils.*;
import static subway.line.LineUtils.*;
import static subway.station.StationUtils.지하철역_생성;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("노선 관리 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    private static Map<String, Long> stationIds = new HashMap<>();
    @BeforeEach
    void setUp() {
        stationIds.put("신사역", 지하철역_생성_후_id_추출("신사역"));
        stationIds.put("논현역", 지하철역_생성_후_id_추출("논현역"));
        stationIds.put("역삼역", 지하철역_생성_후_id_추출("역삼역"));
        stationIds.put("강남역", 지하철역_생성_후_id_추출("강남역"));
    }

    /**
     * When 새로운 지하철 노선을 입력하고, 관리자가 노선을 생성하면
     * Then 해당 노선이 생성된다.
     * Then 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        String 생성된_노선명 = responseToName(신분당선_생성_응답);
        assertThat(생성된_노선명).isEqualTo("신분당선");
    }

    /**
     * Given 여러개의 지하철 노선이 등록되어 있고
     * When 지하철 노선 목록을 조회하면
     * Then 모든 지하철 노선 목록이 조회된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> 이호선_생성_응답 = 지하철노선_생성("2호선", "bg-red-600", 지하철역_id("역삼역"), 지하철역_id("강남역"), 10L);
        assertThat(이호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<String> 지하철노선명_목록 = responseToNames(지하철노선_목록조회());

        //then
        assertThat(지하철노선명_목록).containsExactlyInAnyOrder("신분당선", "2호선");

    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 조회하면
     * Then 해당 지하철 노선 목록이 조회된다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        //given
        ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(지하철노선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> 지하철노선_조회_응답 = 지하철노선_조회(responseToId(지하철노선_생성_응답));

        //then
        String 지하철노선_이름 = responseToName(지하철노선_조회_응답);
        assertThat(지하철노선_이름).isEqualTo("신분당선");

        List<String> 지하철노선의_역이름_목록 = responseToStationNames(지하철노선_조회_응답);
        assertThat(지하철노선의_역이름_목록).containsExactlyInAnyOrder("신사역", "논현역");
    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 수정하면
     * Then 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        //when
        ExtractableResponse<Response> 지하철노선_수정_응답 = 지하철노선_수정(생성된노선_id, "1호선", "bg-blue-600");

        //then
        assertThat(지하철노선_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        String 수정된노선_이름 = responseToName(지하철노선_조회(생성된노선_id));
        assertThat(수정된노선_이름).isEqualTo("1호선");
    }

    /**
     * Given 특정 지하철 노선이 등록되어 있고
     * When 해당 노선을 삭제하면
     * Then 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(지하철노선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> 지하철노선_삭제_응답 = 지하철노선_삭제(responseToLocation(지하철노선_생성_응답));

        //then
        assertThat(지하철노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> 지하철노선_목록 = responseToNames(지하철노선_목록조회());
        assertThat(지하철노선_목록).doesNotContain("신분당선");

    }

    private static Long 지하철역_생성_후_id_추출(String name) {
        ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성(name);

        assertThat(지하철역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return responseToId(지하철역_생성_응답);
    }

    private static Long 지하철역_id(String name) {
        return stationIds.get(name);
    }

}
