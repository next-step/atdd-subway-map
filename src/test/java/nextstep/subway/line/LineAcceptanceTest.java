package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private StationResponse 잠실역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        강남역 = 지하철역_등록("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록("선릉역").as(StationResponse.class);
        잠실역 = 지하철역_등록("잠실역").as(StationResponse.class);
    }

    private Map<String, String> getLineCreateParams(String lineName, String color, StationResponse upStation, StationResponse downStation, String distance){
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", lineName);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation.getId().toString());
        lineCreateParams.put("downStationId", downStation.getId().toString());
        lineCreateParams.put("distance", distance);

        return lineCreateParams;
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        Map<String,String> lineCreateParams =
                getLineCreateParams("2호선","bg-green-600",강남역, 역삼역, "10");

        ExtractableResponse<Response> 이호선 = 지하철_노선_등록(lineCreateParams);

        // then
        지하철_노선_생성_성공(이호선);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String,String> lineCreateParams =
                getLineCreateParams("2호선","bg-green-600",강남역,역삼역,"10");
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록(lineCreateParams);

        lineCreateParams =
                getLineCreateParams("신분당선","bg-red-600",선릉역, 잠실역, "4");
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록(lineCreateParams);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_전체노선_조회_요청(createResponse2);

        // then
        // 지하철_노선_목록_응답됨
        지하철_노선_응답_성공(response);

        // 지하철_노선_목록_포함됨
        지하철_노선_포함_확인(Arrays.asList(createResponse1, createResponse2), response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String,String> lineCreateParams =
                getLineCreateParams("2호선","bg-green-600",강남역,역삼역,"10");
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록(lineCreateParams);
        LineResponse 이호선 = createResponse1.as(LineResponse.class);


        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(이호선.getId());

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답_성공(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String,String> lineCreateParams =
                getLineCreateParams("신분당","bg-red-600",강남역,역삼역,"10");
        ExtractableResponse<Response> createResponse = 지하철_노선_등록(lineCreateParams);

        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("color","bg-blue-600");
        updateParam.put("name", "구분당선");

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse,updateParam);

        // then
        // 지하철_노선_수정됨
        지하철_노선_응답_성공(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String,String> lineCreateParams =
                getLineCreateParams("신분당","bg-red-600",강남역,역삼역,"10");
        ExtractableResponse<Response> createResponse = 지하철_노선_등록(lineCreateParams);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        // 지하철_노선_삭제됨
        지하철_노선_삭제_성공(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        //given
        Map<String,String> lineCreateParams =
                getLineCreateParams("2호선","bg-green-600",강남역,역삼역,"10");
        ExtractableResponse<Response> createResponse = 지하철_노선_등록(lineCreateParams);

         // when
        ExtractableResponse<Response> response = 지하철_노선_등록("2호선","bg-green-600");

        // then
        지하철_노선_생성_실패(response);
    }

    /** 중복코드 리팩터링 */
    private void 지하철_노선_응답_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_생성_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선_생성_실패(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 지하철_노선_삭제_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철_노선_포함_확인(List<ExtractableResponse<Response>> expectedList,
                              ExtractableResponse<Response> realList){

        List<Long> expectedLineIds = Arrays.asList(expectedList.get(0),expectedList.get(1)).stream()
                .map(list -> Long.parseLong(list.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> responseLineIds = realList.jsonPath().getList(".", LineResponse.class).stream()
                .map(list -> list.getId())
                .collect(Collectors.toList());

        assertThat(responseLineIds).containsAll(expectedLineIds);
    }
}