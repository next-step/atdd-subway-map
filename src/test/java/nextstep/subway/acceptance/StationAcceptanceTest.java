package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.step.StationRequest.PATH_PREFIX;
import static nextstep.subway.acceptance.step.StationRequest.stationCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String LOCATION = "Location";
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = stationCreateRequest(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = stationCreateRequest(강남역);

        ExtractableResponse<Response> createResponse2 = stationCreateRequest(역삼역);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get(PATH_PREFIX)
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
    }

    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = stationCreateRequest(강남역);

        // when
        String uri = createResponse.header(LOCATION);
        ExtractableResponse<Response> response =
                RestAssured.given().log().all().when().delete(uri).then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복된 이름으로 역을 생성할 수 없다.")
    @Test
    void duplicateNameCreationTest() {
        // given
        ExtractableResponse<Response> creationResponse = stationCreateRequest(강남역);

        // when
        ExtractableResponse<Response> duplicateCreationResponse = stationCreateRequest(강남역);

        // then
        assertThat(duplicateCreationResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
