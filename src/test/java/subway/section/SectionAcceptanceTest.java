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
     * Then 생성한 구간의 정보를 응답 받을 수 있고, 지하철 노선의 하행역이 새로운 지하철역으로 바뀌고, 지하철 노선의 거리가 바뀐다
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

        String sectionId = createSectionResponse.response().getHeaders().get("location").getValue().split("/sections/")[1];
        ExtractableResponse<Response> showSectionResponse = 지하철_구간_조회_요청(sectionId);
        SectionResponse sectionResponse = ObjectMapperHolder.instance.readValue(showSectionResponse.response().body().asString(), SectionResponse.class);

        assertThat(sectionResponse.getId()).isEqualTo(2L);
        assertThat(sectionResponse.getUpStation().getId()).isEqualTo(downStationId);
        assertThat(sectionResponse.getDownStation().getId()).isEqualTo(newDownStationId);

        // then
        ExtractableResponse<Response> showLineResponse = 지하철_노선_요청(lineResponse.getId());
        LineResponse changedLineResponse = ObjectMapperHolder.instance.readValue(showLineResponse.response().body().asString(), LineResponse.class);

        Long sumOfDistance = SINBUNDANG_LINE_DISTANCE + SINBUNDANG_NEW_DISTANCE;
        List<StationResponse> stationsOfResponse = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(3L, SINBUNDANG_NEW_DOWN_STATION_NAME));
        assertThat(changedLineResponse).isEqualTo(new LineResponse(changedLineResponse.getId(), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse, sumOfDistance));
    }

    /**
     * Given 지하철 노선과 노선에 속하지 않은 새로운 지하철역을 생성하고
     * When 지하철 노선의 하행역이 아닌 다른 역과 새로운 지하철역을 구간으로 등록하면
     * Then 에러가 발생한다
     */
    @Test
    void 지하철_구간_생성시_노선의_하행역이_아닌_역을_구간의_상행역으로_등록하면_예러_발생() throws JsonProcessingException {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(request);
        LineResponse lineResponse = ObjectMapperHolder.instance.readValue(createLineResponse.response().body().asString(), LineResponse.class);
        Long downStationId = lineResponse.getStations().get(0).getId();
        Long newDownStationId = 지하철역_생성_요청_및_아이디_추출(SINBUNDANG_NEW_DOWN_STATION_NAME);

        // when
        ExtractableResponse<Response> createSectionResponse = 지하철_구간_생성_요청(lineResponse.getId(), new SectionRequest(downStationId, newDownStationId, SINBUNDANG_NEW_DISTANCE));

        // then
        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 속한 지하철역을 구간으로 등록하면
     * Then 예러가 발생한다
     */
    @Test
    void 지하철_구간_생성시_노선에_속한_역을_구간으로_등록하면_에러_발생() throws JsonProcessingException {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(request);
        LineResponse lineResponse = ObjectMapperHolder.instance.readValue(createLineResponse.response().body().asString(), LineResponse.class);
        Long downStationId = lineResponse.getStations().get(1).getId();
        Long newDownStationId = lineResponse.getStations().get(0).getId();

        // when
        ExtractableResponse<Response> createSectionResponse = 지하철_구간_생성_요청(lineResponse.getId(), new SectionRequest(downStationId, newDownStationId, SINBUNDANG_NEW_DISTANCE));

        // then
        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선과 노선에 속하지 않은 새로운 지하철역을 생성 후, 지하철 노선의 하행역과 새로운 지하철역을 구간으로 등록하고
     * When 지하철 노선의 하행 종점역을 제거하면
     * Then 구간이 정상적으로 제거되고, 지하철 노선의 하행 종점역과 거리가 바뀌고, 제거된 구간 조회시 에러가 발생한다
     */
    @Test
    void 지하철_구간_제거() throws JsonProcessingException {
        // given
        LineRequest request = 지하철역_생성_및_지하철_노선_요청_객체_생성(SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, SINBUNDANG_UP_STATION_NAME, SINBUNDANG_DOWN_STATION_NAME, SINBUNDANG_LINE_DISTANCE);
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(request);
        LineResponse lineResponse = ObjectMapperHolder.instance.readValue(createLineResponse.response().body().asString(), LineResponse.class);
        Long downStationId = lineResponse.getStations().get(1).getId();
        Long newDownStationId = 지하철역_생성_요청_및_아이디_추출(SINBUNDANG_NEW_DOWN_STATION_NAME);

        ExtractableResponse<Response> createSectionResponse = 지하철_구간_생성_요청(lineResponse.getId(), new SectionRequest(downStationId, newDownStationId, SINBUNDANG_NEW_DISTANCE));

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_구간_제거_요청(lineResponse.getId(), newDownStationId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> showLineResponse = 지하철_노선_요청(lineResponse.getId());
        LineResponse changedLineResponse = ObjectMapperHolder.instance.readValue(showLineResponse.response().body().asString(), LineResponse.class);
        List<StationResponse> stationsOfResponse = List.of(new StationResponse(1L, SINBUNDANG_UP_STATION_NAME), new StationResponse(2L, SINBUNDANG_DOWN_STATION_NAME));
        assertThat(changedLineResponse).isEqualTo(new LineResponse(changedLineResponse.getId(), SINBUNDANG_LINE_NAME, SINBUNDANG_LINE_COLOR, stationsOfResponse, SINBUNDANG_LINE_DISTANCE));

        // then
        String sectionId = createSectionResponse.response().getHeaders().get("location").getValue().split("/sections/")[1];
        ExtractableResponse<Response> showSectionResponse = 지하철_구간_조회_요청(sectionId);
        assertThat(showSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    public static ExtractableResponse<Response> 지하철_구간_조회_요청(String sectionId) {
        return RestAssured
                .given().log().all()
                .when()
                .get("/sections/" + sectionId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }
}
