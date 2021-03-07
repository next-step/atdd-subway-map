package nextstep.subway.station;

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

import static nextstep.subway.station.StationStep.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private Map<String, String> 강남역;
    private Map<String, String> 역삼역;

    @BeforeEach
    void setup() {
        강남역 = new HashMap<String, String>() {{
            put("name", "강남역");
        }};
        역삼역 = new HashMap<String, String>() {{
            put("name", "역삼역");
        }};
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        응답_결과_확인(response, HttpStatus.CREATED);
        지하철역_위치_확인(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(강남역);

        // then
        응답_결과_확인(createResponse2, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(강남역);
        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_전체_조회_요청();

        // then
        응답_결과_확인(response, HttpStatus.OK);
        List<Long> expectedLineIds = 지하철역_위치_아이디_추출(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철역_아이디_추출(response);
        지하철역_목록_포함됨(expectedLineIds, resultLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // when
        Long targetId = 지하철역_위치_아이디_추출(createResponse);
        ExtractableResponse<Response> response = 지하철역_제거_요청(targetId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
