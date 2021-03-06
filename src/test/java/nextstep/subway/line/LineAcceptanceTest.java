package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.line.LineStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private Map<String, String> 신분당선;
    private Map<String, String> 이호선;

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<String, String>() {{
            put("name", "신분당선");
            put("color", "bg-red-600");
        }};
        이호선 = new HashMap<String, String>() {{
            put("name", "2호선");
            put("color", "bg-green-600");
        }};
    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_등록(신분당선);

        // then
        응답_결과_확인(response, HttpStatus.CREATED);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록(신분당선);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록(이호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        응답_결과_확인(response, HttpStatus.OK);

        List<Long> createdLineIds = 지하철_노선_아이디_추출(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_노선_객체_리스트_반환(response);

        지하철_노선_목록_포함됨(createdLineIds, resultLineIds);
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);

        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdId);

        // then
        응답_결과_확인(response, HttpStatus.OK);
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);

        // when
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "구분당선");
        params.put("color", "bg-blue-600");
        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        ExtractableResponse<Response> response1 = 지하철_노선_수정_요청(createdId, params);
        ExtractableResponse<Response> response2 = 지하철_노선_조회_요청(createdId);

        // then
        지하철_노선_수정됨(response1);
        지하철_노선_수정_확인(response2, params);
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createdResponse1 = 지하철_노선_등록(신분당선);
        Long createdId = 지하철_노선_아이디_추출(createdResponse1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdId);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철_노선_등록(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_등록(신분당선);

        // then
        지하철역_생성_실패됨(response);
    }
}
