package subway.section;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.station.StationResponse;
import subway.util.ObjectMapperHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceTest.*;
import static subway.station.StationAcceptanceTest.지하철역_생성_요청_및_아이디_추출;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private static final String SINBUNDANG_LINE_NAME = "신분당선";
    private static final String SINBUNDANG_LINE_COLOR = "bg-red-600";
    private static final String SINBUNDANG_UP_STATION_NAME = "신사역";
    private static final String SINBUNDANG_DOWN_STATION_NAME = "광교역";
    private static final Long SINBUNDANG_LINE_DISTANCE = 10L;

    private static final String SINBUNDANG_NEW_DOWN_STATION_NAME = "새로운 하행역";
    private static final Long SINBUNDANG_NEW_DISTANCE = 3L;

    /**
     * Given 지하철 노선과 노선에 속하지 않은 새로운 지하철역을 생성하고
     * When 지하철 노선의 하행역과 새로운 지하철역을 구간으로 등록하면
     * Then 새로운 지하철 구간이 생성되고, 지하철 노선의 하행역이 새로운 지하철역으로 바뀌고, 지하철 노선의 거리가 바뀐다
     */
    @Test
    void 지하철_구간_생성() throws JsonProcessingException {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(request);
        LineResponse lineResponse = ObjectMapperHolder.instance.readValue(createLineResponse.response().body().asString(), LineResponse.class);

        Long downStationId = lineResponse.getStations().get(1).getId();
        Long newDownStationId = 지하철역_생성_요청_및_아이디_추출(SINBUNDANG_NEW_DOWN_STATION_NAME);

        // when
        ExtractableResponse<Response> createSectionResponse = 지하철_구간_생성_요청(lineResponse.getId(), new SectionRequest(downStationId, newDownStationId, SINBUNDANG_NEW_DISTANCE));

        // then
        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> showResponse = 지하철_노선_요청(lineResponse.getId());
        LineResponse showLineResponse = ObjectMapperHolder.instance.readValue(showResponse.response().body().asString(), LineResponse.class);

        Long sumOfDistance = SINBUNDANG_LINE_DISTANCE + SINBUNDANG_NEW_DISTANCE;
        List<StationResponse> stationsOfResponse = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(3L, SINBUNDANG_NEW_DOWN_STATION_NAME));
        assertThat(showLineResponse).isEqualTo(new LineResponse(lineResponse.getId(), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse, sumOfDistance));
    }

    private ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, SectionRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
