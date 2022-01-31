package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.requests.LineRequests.*;
import static nextstep.subway.acceptance.type.GeneralNameType.*;
import static nextstep.subway.acceptance.type.LineNameType.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor());
        lineCreateRequest(SECOND_LINE.lineName(), SECOND_LINE.lineColor());

        // when
        ExtractableResponse<Response> response = readLineListRequest();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList(NAME.getType());
        assertThat(stationNames).contains(NEW_BUN_DANG_LINE.lineName(), SECOND_LINE.lineName());
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        Long lineId =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor())
                        .jsonPath()
                        .getLong(ID.getType());

        // when
        ExtractableResponse<Response> readLineResponse = specificLineReadRequest(lineId);

        // then
        assertThat(readLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String responseLineName = readLineResponse.jsonPath().getString(NAME.getType());
        String readUpdatedLineColor = readLineResponse.jsonPath().getString(COLOR.getType());
        assertThat(responseLineName).isEqualTo(NEW_BUN_DANG_LINE.lineName());
        assertThat(readUpdatedLineColor).isEqualTo(NEW_BUN_DANG_LINE.lineColor());
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        String uri =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor())
                        .header(LOCATION);

        // when
        String updateLineName = "구분당선";
        String updateLineColor = "bg-blue-600";
        ExtractableResponse<Response> updateResponse =
                lineUpdateRequest(uri, updateLineName, updateLineColor);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        String uri =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor())
                        .header(LOCATION);

        // when
        ExtractableResponse<Response> deleteResponse =
                RestAssured.given().log().all().when().delete(uri).then().log().all().extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        // addition: to verify deleted contents
    }

    @DisplayName("중복된 이름으로 노선을 생성할 수 없다.")
    @Test
    void duplicateNameCreationTest() {
        // given
        String uri =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor())
                        .header(LOCATION);

        // when
        ExtractableResponse<Response> duplicateCreationResponse =
                lineCreateRequest(NEW_BUN_DANG_LINE.lineName(), NEW_BUN_DANG_LINE.lineColor());

        // then
        assertThat(duplicateCreationResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
