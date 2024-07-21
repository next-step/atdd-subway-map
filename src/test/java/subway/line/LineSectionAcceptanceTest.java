package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.SubwayUtils.responseToId;
import static subway.line.LineUtils.지하철노선_생성;
import static subway.station.StationUtils.*;
import static subway.line.SectionUtils.*;
import static subway.line.LineUtils.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("구간 관리 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineSectionAcceptanceTest {

    private static final Map<String, Long> stationIds = new HashMap<>();

    @BeforeEach
    void setUp() {
        stationIds.put("신사역", 지하철역_생성_후_id_추출("신사역"));
        stationIds.put("논현역", 지하철역_생성_후_id_추출("논현역"));
        stationIds.put("신논현역", 지하철역_생성_후_id_추출("신논현역"));
        stationIds.put("역삼역", 지하철역_생성_후_id_추출("역삼역"));
    }

    /**
     * Given 지하철 노선이 주어지고
     * When 새로운 구간을 등록하면
     * Then 구간이 등록된다.
     */
    @DisplayName("새로운 구간을 생성한다.")
    @Test
    void createSection() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철구간_생성_응답 = 지하철구간_생성(생성된노선_id, 지하철역_id("논현역"), 지하철역_id("신논현역"), 10L);

        // then
        assertThat(지하철구간_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given 지하철 노선이 주어지고
     * When 새로운 구간을 생성할 때 새로운 구간의 상행역은 노선에 등록되어 있는 하행 종점역이 아니면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역이 노선에 등록되어 있는 하행 종점이 아니면 예외가 발생한다.")
    @Test
    void createSectionException() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철구간_생성_응답 = 지하철구간_생성(생성된노선_id, 지하철역_id("역삼역"), 지하철역_id("신논현역"), 10L);

        // then
        assertThat(지하철구간_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 지하철 노선이 주어지고
     * When 새로운 구간을 생성할 때 새로운 구간의 하행역이 이미 노선에 등록되어 있으면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역이 이미 노선에 등록되어 있으면 예외가 발생한다.")
    @Test
    void createSectionException2() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철구간_생성_응답 = 지하철구간_생성(생성된노선_id, 지하철역_id("논현역"), 지하철역_id("신사역"), 10L);

        // then
        assertThat(지하철구간_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 지하철 구간이 주어지고
     * When 지하철 구간을 삭제하면
     * Then 지하철 구간이 삭제된다.
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        ExtractableResponse<Response> 지하철구간_생성_응답 = 지하철구간_생성(생성된노선_id, 지하철역_id("논현역"), 지하철역_id("신논현역"), 10L);
        assertThat(지하철구간_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        지하철구간_삭제(생성된노선_id, 지하철역_id("신논현역"));

        // then
        ExtractableResponse<Response> 지하철노선_조회_응답 = 지하철노선_조회(생성된노선_id);
        assertThat(responseToStationNames(지하철노선_조회_응답)).doesNotContain("신논현역");
    }

    /**
     * Given 지하철 구간이 주어지고
     * When 지하철 구간을 삭제할 때 하행 종점역(마지막 구간)이 아닌 경우
     * Then 예외가 발생한다.
     */
    @DisplayName("삭제할 구간이 하행 종점역이 아닌 경우 예외가 발생한다.")
    @Test
    void deleteSectionException() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        ExtractableResponse<Response> 지하철구간_생성_응답 = 지하철구간_생성(생성된노선_id, 지하철역_id("논현역"), 지하철역_id("신논현역"), 10L);
        assertThat(지하철구간_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> 지하철구간_삭제_응답 = 지하철구간_삭제(생성된노선_id, 지하철역_id("논현역"));

        // then
        assertThat(지하철구간_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 지하철 구간이 주어지고
     * When 지하철 구간을 삭제할 때 상행 종점역과 하행 종점역만 있는 경우(구간이 1개)
     * Then 예외가 발생한다.
     */
    @DisplayName("삭제할 구간이 1개인 경우 예외가 발생한다.")
    @Test
    void deleteSectionException2() {
        //given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철노선_생성("신분당선", "bg-red-600", 지하철역_id("신사역"), 지하철역_id("논현역"), 10L);
        assertThat(신분당선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long 생성된노선_id = responseToId(신분당선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철구간_삭제_응답 = 지하철구간_삭제(생성된노선_id, 지하철역_id("논현역"));

        // then
        assertThat(지하철구간_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

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
