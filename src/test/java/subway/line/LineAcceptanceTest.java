package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import setting.RandomPortSetting;
import subway.common.util.CommonApi;
import subway.common.util.CommonValidationUtils;
import subway.line.util.ValidationUtils;

import static subway.line.MockLine.분당선;
import static subway.line.MockLine.신분당선;

@DisplayName("노선 관련 기능")
@Sql("/stations.sql")
public class LineAcceptanceTest extends RandomPortSetting {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철노선 생성")
    void createLine() {
        // When
        LineApi.createLine(신분당선);

        // Then
        ExtractableResponse<Response> responseOfShowLines = LineApi.showLines();
        ValidationUtils.checkLineExistenceInList(responseOfShowLines, 신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void showLines() {
        // Given
        LineApi.createLine(신분당선);
        LineApi.createLine(분당선);

        // When
        ExtractableResponse<Response> responseOfShowLines = LineApi.showLines();

        // Then
        CommonValidationUtils.checkResponseCount(responseOfShowLines, 2);
        ValidationUtils.checkLinesExistenceInList(responseOfShowLines, 신분당선, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void showLine() {
        // Given
        ExtractableResponse<Response> responseOfCreateLine = LineApi.createLine(신분당선);
        CommonValidationUtils.checkRequestCreated(responseOfCreateLine);

        // When
        ExtractableResponse<Response> responseOfShowLine = CommonApi.showResource(responseOfCreateLine);

        // Then
        ValidationUtils.checkLineExistence(responseOfShowLine, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철노선 수정")
    void updateLine() {
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine() {
    }
}
