package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private StationRequest 일호선역1;
    private StationRequest 일호선역2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        일호선역1 = StationRequest.of("일호선역1");
        일호선역2 = StationRequest.of("일호선역2");
    }

    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 역등록결과 = 지하철_역_을_등록한다(일호선역1);

        // then
        지하철_역_등록성공(역등록결과);
    }

    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        지하철_역_을_등록한다(일호선역1);
        지하철_역_을_등록한다(일호선역2);

        // when
        ExtractableResponse<Response> 역목록조회결과 = 지하철_역_목록을_조회한다();

        지하철_역_목록조회_결과에_원하는_역들이_있다(역목록조회결과, Arrays.asList(일호선역1, 일호선역2));
    }

    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> 역등록결과 = 지하철_역_을_등록한다(일호선역1);

        // when
        ExtractableResponse<Response> 역삭제결과 = 지하철_역을_삭제한다(
            역등록결과.as(StationResponse.class).getId());

        // then
        지하철_역_삭제_성공한다(역삭제결과);
    }

    @DisplayName("중복된 이름으로 지하철역 생성 요청하면 실패한다.")
    @Test
    void duplicatedLine() {
        // given
        지하철_역_을_등록한다(일호선역1);

        // when
        ExtractableResponse<Response> 역등록결과 = 지하철_역_을_등록한다(일호선역1);

        // then
        지하철_역_등록_실패한다(역등록결과);
    }

    public static ExtractableResponse<Response> 지하철_역_을_등록한다(StationRequest request) {
        return RestAssuredCRUD.postRequest("/stations", request);
    }

    public static ExtractableResponse<Response> 지하철_역_목록을_조회한다() {
        return RestAssuredCRUD.get("/stations");
    }

    public static ExtractableResponse<Response> 지하철_역을_삭제한다(Long stationId) {
        return RestAssuredCRUD.delete("/stations/"+stationId);
    }

    public static void 지하철_역_등록성공(ExtractableResponse<Response> 역등록결과) {
        assertThat(역등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(역등록결과.header("Location")).isNotBlank();
    }

    public static void 지하철_역_목록조회_결과에_원하는_역들이_있다(ExtractableResponse<Response> 역목록조회결과, List<StationRequest> 역요청들) {
        assertThat(역목록조회결과.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = 역목록조회결과.jsonPath().getList("name");
        List<String> names = 역요청들.stream()
            .map(StationRequest::getName)
            .collect(Collectors.toList());
        assertThat(lineNames).containsAll(names);
    }

    public static void 지하철_역_등록_실패한다(ExtractableResponse<Response> 역등록결과) {
        assertThat(역등록결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_역_삭제_성공한다(ExtractableResponse<Response> 역삭제결과) {
        assertThat(역삭제결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
