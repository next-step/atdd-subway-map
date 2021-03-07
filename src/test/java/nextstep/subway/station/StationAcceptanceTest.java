package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private enum SubwayStation {

        GangNam("강남역"),
        Yeoksam("역삼역");

        public String name;

        SubwayStation(String name) {
            this.name = name;
        }
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given & when
        ExtractableResponse<Response> response = 지하철역_생성요청(SubwayStation.GangNam.name);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철역_생성요청(SubwayStation.GangNam.name);

        // when
        ExtractableResponse<Response> response = 지하철역_생성요청(SubwayStation.GangNam.name);

        // then
        지하철역_생성실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createdResponse1 = 지하철역_생성요청(SubwayStation.GangNam.name);
        ExtractableResponse<Response> createdResponse2 = 지하철역_생성요청(SubwayStation.Yeoksam.name);

        // when
        ExtractableResponse<Response> response = 지하철역_조회요청();

        // then
        지하철역_조회_응답됨(response);
        지하철역_조회_포함됨(response, Arrays.asList(createdResponse1, createdResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성요청(SubwayStation.GangNam.name);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_제거요청(uri);

        // then
        지하철역_제거됨(response);
    }

    private ExtractableResponse<Response> 지하철역_생성요청(String name) {
        StationRequest request = new StationRequest(name);

        return RestAssured
                .given().log().all().body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all().extract();
    }

    private void 지하철역_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철역_생성실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철역_조회요청() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all().extract();
    }

    private void 지하철역_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철역_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = createdResponses.stream()
                .map(it -> it.as(StationResponse.class))
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    private ExtractableResponse<Response> 지하철역_제거요청(String uri) {
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철역_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
