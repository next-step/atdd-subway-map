package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.commons.ErrorCode;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간관련 기능")
@Sql(scripts = {"StationInsert.sql", "LineInsert.sql", "SectionInsert.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * given: 기존에 노선과 역이 등록되어 있고,
     * when: 관리자가 구간을 등록하면
     * then: 구간의 하행 종점역이 바뀐다.
     */
    @DirtiesContext
    @Test
    @DisplayName("기존의 노선에 구간을 등록한다.")
    void 구간_등록_테스트() {
        //given
        int testingLineNumber = 1;
        String terminalStationName = "강남역";
        String upStationId = "5";
        String downStationId = "6";
        String distance = "10";
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when
        ExtractableResponse response = postAPIResponse("/lines/" + testingLineNumber + "/sections", params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        ExtractableResponse getResponse = getAPIResponse("/lines/" + testingLineNumber + "/sections");
        List<String> downStations = getResponse.jsonPath().getList("downStations.name");
        String testingName = downStations.get(downStations.size() - 1);
        assertThat(testingName).isEqualTo(terminalStationName);

    }

    /**
     * given: 기존 노선과 역이 등록되어 있고,
     * when: 관리자가 구간을 등록할 때 상행역이 구간의 하행종점역이 아니면
     * then: 잘못된 요청이라는 응답을 받는다.
     */
    @DirtiesContext
    @Test
    @DisplayName("구간 등록할 때 하행종점역이 아니면 실패한다.")
    void 구간_등록시_상행역은_하행종점역이_아니면_실패() {
        //given
        int testingLineNumber = 1;
        String terminalStationName = "강남역";
        String upStationId = "6";
        String downStationId = "7";
        String distance = "10";
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when
        ExtractableResponse response = postAPIResponse("/lines/" + testingLineNumber + "/sections", params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(ErrorCode.UP_STATION_NOT_VALID.getMessage());

    }


    /**
     * given: 기존 역이 해당노선에 등록되어 있고,
     * when: 관리자가 기존 역을 구간에 등록하면,
     * then: 잘못된 요청이라는 응답을 받는다.
     */
    @DirtiesContext
    @Test
    @DisplayName("구간안에 등록하고자 하는 역이 이미 존재하였을 때 실패한다.")
    void 이미_구간에_존재하는_역_등록() {
        //given
        int testingLineNumber = 1;
        String terminalStationName = "강남역";
        String upStationId = "5";
        String downStationId = "4";
        String distance = "10";
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when
        ExtractableResponse response = postAPIResponse("/lines/" + testingLineNumber + "/sections", params);

        //then
        String errorMessage = MessageFormat.format(ErrorCode.DOWN_STATION_NOT_VALID.getMessage(), downStationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(errorMessage);
    }

    /**
     * given: 기존 구간이 등록되어있고,
     * when: 관리자가 기존 구간(하행종점역)을 제거하면,
     * then: 구간목록에서 구간(하행종점역)이 제거된다.
     */
    @DirtiesContext
    @Test
    @DisplayName("기존 구간에서 하행 종점역을 제거하면 구간이 제거된다.")
    void 구간_제거_테스트() {
        // given
        int testingLineNumber = 2;
        int removingStationId = 7;
        String removingDownStation = "사당역";

        //when
        ExtractableResponse response = deleteAPIResponse("/lines/" + testingLineNumber + "/sections?stationId=" + removingStationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse getResponse = getAPIResponse("/lines/" + testingLineNumber + "/sections");
        List<String> downStations = getResponse.jsonPath().getList("downStations.name");
        assertThat(downStations.stream().anyMatch(station -> station.equals(removingDownStation))).isFalse();

    }

    /**
     * given: 기존 구간이 등록되어있고,
     * when: 관리자가 구간을 제거할 때, 하행 종점역이 아니면
     * then: 잘못된 요청이라는 응답을 받는다.
     */
    @DirtiesContext
    @Test
    @DisplayName("구간을 제거 할 때 상행역이 하행 종점역이 아니면 실패한다.")
    void 구간_제거시_하행종점역이_아니면_실패() {
        // given
        int testingLineNumber = 2;
        int removingStationId = 5;
        String removingStationName = "서울역";

        //when
        ExtractableResponse response = deleteAPIResponse("/lines/" + testingLineNumber + "/sections?stationId=" + removingStationId);

        //then
        String errorMessage = MessageFormat.format(ErrorCode.IS_NOT_TERMINAL_STATION.getMessage(), Integer.toString(removingStationId));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(errorMessage);
    }

    /**
     * given: 기존 구간이 등록되어있고,
     * when: 관리자가 구간을 제거할 때, 구간이 1개인 경우
     * then: 잘못된 요청이라는 응답을 받는다.
     */
    @DirtiesContext
    @Test
    @DisplayName("구간을 제거 할 때 남은 구간이 한개인 경우 실패한다.")
    void 구간_제거시_구간이_1개인경우_실패() {
        int testingLineNumber = 3;
        int removingStationId = 7;
        String removingStationName = "사당역";

        //when
        ExtractableResponse response = deleteAPIResponse("/lines/" + testingLineNumber + "/sections?stationId=" + removingStationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(ErrorCode.CANNOT_REMOVE_LAST_SECTION.getMessage());
    }

    /**
     * given: 기존 구간이 노선에 등록되어있고,
     * when: 관리자가 노선 번호로 조회할때,
     * then: 노선에 등록되어 있는 구간들이 모두 응답에 포함된다..
     */
    @DirtiesContext
    @Test
    @DisplayName("노선본호로 등록되어 있는 모든 구간을 조회한다.")
    void 노선_번호로_등록되어있는_구간_조회() {
        // given
        int testingLineNumber = 2;
        List<String> testingUpStations = List.of("수서역", "서울역");
        List<String> testingDownStations = List.of("서울역", "사당역");

        //when
        ExtractableResponse response = getAPIResponse("/lines/" + testingLineNumber + "/sections");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> upStations = response.jsonPath().getList("upStations.name");
        List<String> downStations = response.jsonPath().getList("downStations.name");
        assertThat(upStations.containsAll(testingUpStations));
        assertThat(downStations.containsAll(testingDownStations));

    }

    ExtractableResponse<Response> postAPIResponse(String url, Map<String, String> body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> getAPIResponse(String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> deleteAPIResponse(String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all()
                .extract();
    }

}
