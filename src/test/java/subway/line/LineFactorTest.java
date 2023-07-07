package subway.line;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.line.LineRequester.*;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineFactorTest {

    private static final String GANGNAM_STATION_NAME = "강남역";
    private static final String SEOLLEUNG_STATION_NAME = "선릉역";
    private static final String SUWON_STATION_NAME = "수원역";

    private static final String SHINBUNDANG_LINE_NAME = "신분당선";
    private static final String SHINBUNDANG_LINE_COLOR = "bg-red-600";
    private static final String BUNDANG_LINE_NAME = "분당선";
    private static final String BUNDANG_LINE_COLOR = "bg-green-600";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void 지하철노선생성() {
        // when
        Long id = createLineThenReturnId(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME, 10);
        JsonPath jsonPath = findLine(id).jsonPath();

        // then
        assertThat(jsonPath.getObject("name", String.class)).isEqualTo(SHINBUNDANG_LINE_NAME);
        assertThat(jsonPath.getList("stations.name", String.class)).containsExactly(GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선목록을 조회시 등록된 전체 지하철 노선이 조회되어야 한다.")
    @Test
    void 지하철노선목록조회() {
        // given
        Long shinbundangLineId = createLineThenReturnId(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME, 10);
        Long bundangLineId = createLineThenReturnId(BUNDANG_LINE_NAME, BUNDANG_LINE_COLOR, SEOLLEUNG_STATION_NAME, SUWON_STATION_NAME, 5);

        // when
        JsonPath jsonPath = findLines().jsonPath();

        // then
        assertThat(jsonPath.getList("id", Long.class)).containsExactly(shinbundangLineId, bundangLineId);
        assertThat(jsonPath.getList("stations.name")).contains(List.of(GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME), List.of(SEOLLEUNG_STATION_NAME, SUWON_STATION_NAME));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회시 등록된 지하철 노선정보가 조회되어야 한다.")
    @Test
    void 지하철노선조회() {
        // given
        Long id = createLineThenReturnId(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME, 10);

        // when
        JsonPath jsonPath = findLine(id).jsonPath();

        // then
        assertThat(jsonPath.getObject("id", Long.class)).isEqualTo(id);
        assertThat(jsonPath.getList("stations.name")).contains(GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정시 등록된 지하철 노선정보가 수정되어야 한다.")
    @Test
    void 지하철노선수정() {
        // given
        Long id = createLineThenReturnId(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME, 10);

        // when
        modifyLine(id, BUNDANG_LINE_NAME, BUNDANG_LINE_COLOR);

        // then
        JsonPath jsonPath = findLine(id).jsonPath();
        assertThat(jsonPath.getObject("name", String.class)).isEqualTo(BUNDANG_LINE_NAME);
        assertThat(jsonPath.getObject("color", String.class)).isEqualTo(BUNDANG_LINE_COLOR);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제시 등록된 지하철 노선정보가 삭제되어야 한다.")
    @Test
    void 지하철노선삭제() {
        // given
        Long id = createLineThenReturnId(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION_NAME, SEOLLEUNG_STATION_NAME, 10);

        // when
        deleteLine(id);

        // then
        assertThat(findLine(id).response().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
