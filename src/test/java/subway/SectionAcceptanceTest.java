package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.LineAcceptanceTest.createLine;
import static subway.StationAcceptanceTest.createStations;

@AcceptanceTest
@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest {

    private List<Long> newStationIds;
    private Long newLineId;

    @BeforeEach
    void setUp() {
        newStationIds = createStations(4);
        newLineId = createLine("line", newStationIds.get(0), newStationIds.get(1))
                .jsonPath().getLong("id");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 성공한다.
     */
    @Test
    void createSection_success() {
        //given
        Long upStationId = newStationIds.get(1);
        Long downStationId = newStationIds.get(2);
        Long distance = 10L;

        //when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", newLineId)
                .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간의 상행역이 노선의 하행종점역이 아닐 경우
     * Then 실패한다.
     */
    @Test
    @Disabled
    void createSection_failIfSectionUpStationIsNotLineLastDownStation() {


    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간의 하행역이 노선에 이미 존재하는 역일 경우
     * Then 실패한다.
     */
    @Test
    @Disabled
    void createSection_failIfSectionDownStationIsInLine() {

    }
}
