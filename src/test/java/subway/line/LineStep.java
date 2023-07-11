package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationStep.강남역;
import static subway.station.StationStep.역삼역;
import static subway.station.StationStep.판교역;
import static subway.util.Extractor.getId;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import subway.common.exception.ErrorMessage;
import subway.line.dto.LineResponse;
import subway.station.StationStep;
import subway.station.dto.StationResponse;
import subway.util.Extractor;

import java.util.List;
import java.util.Map;

public class LineStep {

    private static final String LINE_URL = "/lines";
    public static Map<String, String> 신분당선 = getLineParams("신분당선", "bg-red");
    public static Map<String, String> 분당선 = getLineParams("분당선", "bg-yellow");

    public static ExtractableResponse<Response> 지하철노선을_추가(List<Map<String, String>> 노선목록) {

        return 노선목록.stream().map(params -> Extractor.post(LINE_URL, params))
                .collect(Collectors.toList()).get(0);
    }

    public static ExtractableResponse<Response> 지하철노선을_수정(Long id, Map<String, String> params) {

        return Extractor.put(LINE_URL + "/" + id, params);
    }

    public static void 지하철노선을_확인(List<Map<String, String>> 노선목록) {

        List<String> lines = 전체_지하철노선의_이름을_조회();
        assertThat(lines).hasSize(노선목록.size());
        for (Map<String, String> 대상 : 노선목록) {
            assertThat(lines).containsAnyOf(대상.get("name"));
        }
    }

    public static void 지하철노선을_확인(Long id, Map<String, String> 대상) {

        LineResponse lineResponse = 특정_지하철노선_조회(id).jsonPath()
            .getObject("", LineResponse.class);

        assertThat(lineResponse.getName()).isEqualTo(대상.get("name"));
        assertThat(lineResponse.getColor()).isEqualTo(대상.get("color"));
    }


    public static ExtractableResponse<Response> 지하철노선을_삭제(Long id) {

        return Extractor.delete(LINE_URL + "/" + id);
    }

    public static void 지하철노선_삭제확인(Long id) {

        ExtractableResponse<Response> response = 특정_지하철노선_조회(id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static Map<String, String> 지하철역_및_지하철_노선을_생성(String upStationName,
        String downStationName, Map<String, String> 대상) {

        Long lineId = getId(지하철노선을_추가(Arrays.asList(대상)));
        Long upStationId = getId(StationStep.지하철역을_저장(upStationName));
        Long downStationId = getId(StationStep.지하철역을_저장(downStationName));

        return getSectionParams(lineId, upStationId, upStationName,
            downStationId, downStationName, 10);

    }

    public static ExtractableResponse<Response> 지하철노선의_구간을_추가(Map<String, String> params) {

        return Extractor.post(LINE_URL + "/" + params.get("lineId") + "/sections", params);
    }

    public static void 지하철노선의_구간_확인(Map<String, String> params) {

        LineResponse lineResponse = 특정_지하철노선_조회(Long.valueOf(params.get("lineId"))).jsonPath()
            .getObject("", LineResponse.class);
        int lastIndex = lineResponse.getStations().size() - 1;
        StationResponse upStation = lineResponse.getStations().get(lastIndex - 1);
        StationResponse downStation = lineResponse.getStations().get(lastIndex);

        assertThat(upStation.getId()).isEqualTo(Long.valueOf(params.get("upStationId")));
        assertThat(upStation.getName()).isEqualTo(params.get("upStationName"));

        assertThat(downStation.getId()).isEqualTo(Long.valueOf(params.get("downStationId")));
        assertThat(downStation.getName()).isEqualTo(params.get("downStationName"));
    }

    public static Map<String, String> 신분당선_등록() {

        Map<String, String> params = 지하철역_및_지하철_노선을_생성(강남역, 판교역, 신분당선);
        지하철노선의_구간을_추가(params);

        return params;
    }

    public static void 지하철노선의_구간을_연장(Map<String, String> params, String downStationName) {

        Long downStationId = getId(StationStep.지하철역을_저장(downStationName));
        changeSectionParams(params, downStationId, downStationName, 15);

        지하철노선의_구간을_추가(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_하행_종점역과_상행역이_다른_구간_생성(Map<String, String> params) {

        Long upStationId = getId(StationStep.지하철역을_저장(역삼역));
        params.put("upStationId", String.valueOf(upStationId));

        return 지하철노선의_구간을_추가(params);
    }

    public static ExtractableResponse<Response> 등록되어_있는_하행역_생성(Map<String, String> params) {

        return 지하철노선의_구간을_추가(params);
    }

    public static void BAD_REQUEST_확인(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(errorMessage);
    }

    private static List<String> 전체_지하철노선의_이름을_조회() {

        return Extractor.get(LINE_URL).jsonPath().getList("name", String.class);
    }

    private static ExtractableResponse<Response> 특정_지하철노선_조회(Long id) {

        return Extractor.get(LINE_URL + "/" + id);
    }

    private static Map<String, String> getSectionParams(Long lineId, Long upStationId,
        String upStationName, Long downStationId, String downStationName, int distance) {

        Map<String, String> params = new HashMap<>();
        params.put("lineId", String.valueOf(lineId));
        params.put("upStationId", String.valueOf(upStationId));
        params.put("upStationName", upStationName);
        params.put("downStationId", String.valueOf(downStationId));
        params.put("downStationName", downStationName);
        params.put("distance", String.valueOf(distance));

        return params;
    }

    private static void changeSectionParams(Map<String, String> params, Long downStationId, String downStationName, int distance) {

        params.put("upStationId", params.get("upStationId"));
        params.put("upStationName", params.get("upStationName"));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("downStationName", downStationName);
        params.put("distance", String.valueOf(distance));

    }
    private static Map<String, String> getLineParams(String name, String color) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }
    public static ExtractableResponse<Response> 지하철구간을_축소(Long lineId, Long stationId) {

        return Extractor.delete(LINE_URL + "/" + lineId + "/sections?stationId=" + stationId);
    }
}
