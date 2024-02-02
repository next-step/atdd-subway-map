package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.request.LineRequest;
import subway.util.StationTestUtil;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.SubwayLineUtil.createSubwayLine;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DirtiesContext
@Sql("/truncate.sql")
public class SectionAcceptanceTest {

    long stationId1, stationId2, stationId3;
    @BeforeEach
    void setUp() {
        stationId1 = StationTestUtil.createStation("지하철역").jsonPath().getLong("id");
        stationId2 = StationTestUtil.createStation("새로운지하철역").jsonPath().getLong("id");
        stationId3 = StationTestUtil.createStation("또다른지하철역").jsonPath().getLong("id");
    }

    /**
     * 구간 등록 기능
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        //given
        Long lineId = createSubwayLine(new LineRequest("2호선", "green", stationId1, stationId2, 10)).jsonPath().getLong("id");

        //when
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", stationId2);
        params.put("downStationId", stationId3);
        params.put("distance", 10);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}