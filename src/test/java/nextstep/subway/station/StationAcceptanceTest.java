package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.HttpAssertions;
import org.apache.commons.lang3.StringUtils;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;

import static nextstep.subway.utils.HttpAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given & when
        ExtractableResponse<Response> response = 지하철_역_생성("강남역");

        // then
        응답_HTTP_CREATED(response);
        헤더_LOCATION_존재(response);
    }

    private ExtractableResponse<Response> 지하철_역_생성(String name) {
        Map<String, String> params = Maps.of("name", name);
        return RestAssured.given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_역_생성("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_생성("강남역");

        // then
        응답_HTTP_BAD_REQUEST(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성("강남역");
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성("역삼역");

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회();

        // then
        응답_HTTP_OK(response);
        List<Long> expectedLineIds = Arrays.asList(리소스_ID(createResponse1), 리소스_ID(createResponse2));
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철_역_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_역_생성("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_삭제(리소스_ID(createResponse));

        // then
        응답_HTTP_NO_CONTENT(response);
    }

    private ExtractableResponse<Response> 지하철_역_삭제(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

    private Long 리소스_ID(ExtractableResponse<Response> response) {
        return Optional.ofNullable(response)
                .map(this::리소스_URI)
                .map(it -> StringUtils.substringAfterLast(it, "/"))
                .map(Long::parseLong)
                .orElse(null);
    }

    private String 리소스_URI(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

}
