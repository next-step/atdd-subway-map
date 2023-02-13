package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.domain.LineRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.utils.LineTestHelpers.지하철_노선_목록_조회;
import static subway.utils.LineTestHelpers.지하철_노선_삭제;
import static subway.utils.LineTestHelpers.지하철_노선_생성;
import static subway.utils.LineTestHelpers.지하철_노선_수정;
import static subway.utils.LineTestHelpers.지하철_노선_조회;
import static subway.utils.LineTestHelpers.지하철역_생성;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static final String SINBUNDANG_LINE = "신분당선";
    private static final String SUINBUNDANG_LINE = "수인분당선";
    private static Long GANGNAM_ID;
    private static Long GWANGKYO_ID;
    private static Long SUNGSOO_ID;
    private static Long DOKOK_ID;

    @Autowired
    private LineRepository lineRepository;

    public static ExtractableResponse<Response> 지하철_신분당선_생성() {
        return 지하철_노선_생성(SINBUNDANG_LINE, "bg-red-600", GANGNAM_ID, GWANGKYO_ID, 10);
    }

    public static ExtractableResponse<Response> 지하철_수인분당선_생성() {
        return 지하철_노선_생성(SUINBUNDANG_LINE, "bg-yellow-600", SUNGSOO_ID, DOKOK_ID, 10);
    }

    @BeforeEach
    void setUp() {
        GANGNAM_ID = 지하철역_생성("강남역");
        GWANGKYO_ID = 지하철역_생성("광교역");
        SUNGSOO_ID = 지하철역_생성("성수역");
        DOKOK_ID = 지하철역_생성("도곡역");
    }

    @AfterEach
    public void cleanup() {
        lineRepository.deleteAll();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void saveLine() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames).containsAnyOf(SINBUNDANG_LINE);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLines() {
        지하철_신분당선_생성();
        지하철_수인분당선_생성();

        List<String> lineNames = 지하철_노선_목록_조회();

        assertThat(lineNames).hasSize(2);
        assertThat(lineNames).containsExactly(SINBUNDANG_LINE, SUINBUNDANG_LINE);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationById() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();
        Long line_id = createResponse.jsonPath().getLong("id");

        String lineName = 지하철_노선_조회(line_id);
        assertThat(lineName).isEqualTo(SINBUNDANG_LINE);
    }

    @DisplayName("지하철 노선 정보를 수정한다.")
    @Test
    void updateStation() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();
        Long line_id = createResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> updateResponse = 지하철_노선_수정(line_id, "구분당선", "be-red-200");
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        String lineName = 지하철_노선_조회(line_id);
        assertThat(lineName).isEqualTo("구분당선");
    }

    @DisplayName("지하철 노선 정보를 삭제한다.")
    @Test
    void deleteStationById() {
        ExtractableResponse<Response> createResponse = 지하철_신분당선_생성();
        Long line_id = createResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제(line_id);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames).doesNotContain(SINBUNDANG_LINE);
    }

}
