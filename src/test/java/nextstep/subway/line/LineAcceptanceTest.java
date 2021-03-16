package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.SectionHelper;
import nextstep.subway.station.StationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    Long stationId1;
    Long stationId2;
    final int DISTANCE = 5;

    @BeforeEach
    void 미리_역_생성(){
        ExtractableResponse<Response> createStation1 = StationHelper.지하철역_생성_요청("강남");
        ExtractableResponse<Response> createStation2 = StationHelper.지하철역_생성_요청("선릉");
        stationId1 = StationHelper.생성된_지하철역_ID_가져오기(createStation1);
        stationId2 = StationHelper.생성된_지하철역_ID_가져오기(createStation2);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성_요청_및_확인() {
        // when
        ExtractableResponse<Response> response =
                LineHelper.지하철_노선_생성_요청("선릉", "green darken-1", stationId1, stationId2, DISTANCE);

        // then
        지하철_노선_생성_요청_응답됨(response);
    }

    void 지하철_노선_생성_요청_응답됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        ExtractableResponse<Response> createStation3 = StationHelper.지하철역_생성_요청("대청");
        ExtractableResponse<Response> createStation4 = StationHelper.지하철역_생성_요청("일원");
        Long stationId3 = StationHelper.생성된_지하철역_ID_가져오기(createStation3);
        Long stationId4 = StationHelper.생성된_지하철역_ID_가져오기(createStation4);

        ExtractableResponse<Response> createResponse1 =
                LineHelper.지하철_노선_생성_요청("1호선", "green darken-1", stationId1, stationId2, DISTANCE);

        ExtractableResponse<Response> createResponse2 =
                LineHelper.지하철_노선_생성_요청("2호선", "green darken-2", stationId3, stationId4, DISTANCE);

        // when
        ExtractableResponse<Response> getResponses = LineHelper.지하철_노선_목록_조회_결과_요청();

        // then
        LineHelper.지하철_노선_목록_응답됨(getResponses);

        List<LineResponse> lineResponses = LineHelper.지하철_노선_목록_조회_결과_리스트(getResponses);
        LineHelper.지하철_노선_목록_리스트_사이즈_확인(lineResponses);

        List<Long> expectedLineIds = LineHelper.지하철_노선_목록_예상_아이디_리스트(createResponse1, createResponse2);
        List<Long> resultLineIds = LineHelper.지하철_노선_목록_결과_아이디_리스트(getResponses);
        LineHelper.지하철_노선_목록이_예상목록_포함하는지_확인(expectedLineIds, resultLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청("2호선", "green darken-1", stationId1, stationId2, DISTANCE);

        ExtractableResponse<Response> createStation3 = StationHelper.지하철역_생성_요청("대청");
        Long stationId3 = StationHelper.생성된_지하철역_ID_가져오기(createStation3);

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        SectionHelper.구간_추가_요청(id, stationId2, stationId3, 10);


        // when
        ExtractableResponse<Response> getResponse = LineHelper.지하철_노선_조회_요청(id);

        // then
        LineHelper.지하철_노선_응답됨(getResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청(
                        "1호선",
                        "green darken-1",
                        stationId1,
                        stationId2,
                        DISTANCE
                );

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        // when
        LineRequest lineRequest = LineHelper.파라미터_생성(
                "1호선",
                "red darken-1",
                stationId1,
                stationId2,
                DISTANCE);
        ExtractableResponse<Response> updateResponse = LineHelper.지하철_노선_수정_요청(id, lineRequest);

        // then
        LineHelper.지하철_노선_수정_확인(lineRequest, updateResponse);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        ExtractableResponse<Response> createResponse =
                LineHelper.지하철_노선_생성_요청(
                        "선릉",
                        "green darken-1",
                        stationId1,
                        stationId2,
                        DISTANCE);

        Long id = LineHelper.생성된_Entity의_ID_가져오기(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = LineHelper.지하철_노선_삭제_요청(id);

        // then
        LineHelper.지하철_노선_삭제_응답됨(deleteResponse);
    }
}
