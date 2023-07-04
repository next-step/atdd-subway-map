package subway;

import static api.StationApiRequest.*;
import static fixture.StationRequestFixture.지하철역_등록_요청_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;

import fixture.StationRequestFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.service.response.StationResponse;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    private static final String 지하철역이름 = "지하철역이름";
    private static final String 새로운지하철역이름 = "새로운지하철역이름";
    private static final String 또다른지하철역이 = "또다른지하철역이";
    private static final String 강남역 = "강남역";


    /**
     * When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(지하철역_등록_요청_데이터_생성(강남역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_리스트_조회()
                .jsonPath()
                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고 When 지하철역 목록을 조회하면 Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void showStations() {

        //given
        지하철역_생성_요청(지하철역_등록_요청_데이터_생성(지하철역이름));
        지하철역_생성_요청(지하철역_등록_요청_데이터_생성(새로운지하철역이름));
        지하철역_생성_요청(지하철역_등록_요청_데이터_생성(또다른지하철역이));

        //when
        ExtractableResponse<Response> response = 지하철역_리스트_조회();

        //then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("", StationResponse.class)).hasSize(3),
            () -> assertThat(response.jsonPath().getString("[0].name")).isEqualTo(지하철역이름),
            () -> assertThat(response.jsonPath().getString("[1].name")).isEqualTo(새로운지하철역이름),
            () -> assertThat(response.jsonPath().getString("[2].name")).isEqualTo(또다른지하철역이)
        );
    }

    /**
     * Given 지하철역을 생성하고 When 그 지하철역을 삭제하면 Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역 삭제")
    void removeStation() {

        //given
        long 신규생성_지하철역_id = 지하철역_생성_요청(지하철역_등록_요청_데이터_생성(새로운지하철역이름))
                .jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 지하철역_삭제(신규생성_지하철역_id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> stationsList = 지하철역_리스트_조회();
        assertThat(stationsList.jsonPath().getList("")).isEmpty();

    }
}