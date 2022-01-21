package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.utils.AssuredRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    @Order(1)
    void createLine() {
        LineRequest lineRequest = 노선_요청_정보_Sample1();

        ExtractableResponse<Response> response = 지하철_노선_Create(lineRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(lineRequest.getName()),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(lineRequest.getColor())
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    @Order(3)
    void getLines() {
        LineRequest lineRequest = 노선_요청_정보_Sample1();
        지하철_노선_Create(lineRequest);
        LineRequest newLineRequest = 노선_요청_정보_Sample2();
        지하철_노선_Create(newLineRequest);
        List<LineRequest> requestList = Arrays.asList(lineRequest, newLineRequest);

        ExtractableResponse<Response> response = 지하철_노선_목록_FindAll();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<LineResponse> responseList = response.jsonPath().getList(".", LineResponse.class);
                    assertThat(responseList.size()).isEqualTo(requestList.size());
                    assertThat(responseList.stream().map(it -> it.getName()).collect(Collectors.toList()))
                            .isEqualTo(requestList.stream().map(it -> it.getName()).collect(Collectors.toList()));
                }
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    @Order(5)
    void getLine() {
        LineRequest lineRequest = 노선_요청_정보_Sample1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_Create(lineRequest);
        String searchUri = givenResponse.header("Location");

        ExtractableResponse<Response> response = 지하철_노선_Find(searchUri);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(lineRequest.getName()),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(lineRequest.getColor())
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    @Order(7)
    void updateLine() {
        LineRequest lineRequest = 노선_요청_정보_Sample1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_Create(lineRequest);
        String updateUri = givenResponse.header("Location");
        LineRequest updateLineRequest = 노선_요청_정보_Modify_Sample1();

        ExtractableResponse<Response> response = 지하철_노선_Update(updateUri, updateLineRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    @Order(9)
    void deleteLine() {
        LineRequest lineRequest = 노선_요청_정보_Sample1();
        ExtractableResponse<Response> givenResponse = 지하철_노선_Create(lineRequest);
        String deleteUri = givenResponse.header("Location");

        ExtractableResponse<Response> response = 지하철_노선_Delete(deleteUri);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private LineRequest 노선_요청_정보_Sample1() {
        return new LineRequest("1호선", "파란색");
    }

    private LineRequest 노선_요청_정보_Sample2() {
        return new LineRequest("2호선", "연두색");
    }

    private LineRequest 노선_요청_정보_Modify_Sample1() {
        return new LineRequest("100호선", "무지개색");
    }

    private final static String END_POINT = "/lines";
    private ExtractableResponse<Response> 지하철_노선_Create(LineRequest lineRequest) {
        return AssuredRequest.doCreate(END_POINT, lineRequest);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_FindAll() {
        return AssuredRequest.doFind(END_POINT);
    }

    private ExtractableResponse<Response> 지하철_노선_Find(String uri) {
        return AssuredRequest.doFind(uri);
    }

    private ExtractableResponse<Response> 지하철_노선_Update(String uri, LineRequest lineRequest) {
        return AssuredRequest.doUpdate(uri, lineRequest);
    }

    private ExtractableResponse<Response> 지하철_노선_Delete(String uri) {
        return AssuredRequest.doDelete(uri);
    }
}
