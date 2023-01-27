package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.DataBaseCleanUp;
import subway.line.LineAcceptanceTest;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.station.StationAcceptanceTest;
import subway.station.dto.StationResponse;
import subway.station.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.line.LineAcceptanceTest.*;
import static subway.station.StationAcceptanceTest.*;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    void tearDown() {
        dataBaseCleanUp.cleanUp();
    }

    /**
     * Given : 1개의 역, 1개의 노선을 등록
     * When  : 구간을 추가 하면
     * Then  : 구간을 추가 된다
     */
    @Test
    void 지하철_노선_구간_등록_성공() {
        //given
        Long newStationId = createStation("역1");
        LineCreateRequest oldLineCreateRequest = createLineRequestFixture("노선1");
        LineResponse lineResponse = requestCreateLine(oldLineCreateRequest);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("downStationId", newStationId);
        paramMap.put("upStationId", oldLineCreateRequest.getDownStationId());
        paramMap.put("distance", 10);

        //when
        ExtractableResponse<Response> response = given()
                .pathParam("lineId", lineResponse.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(paramMap)
                .when()
                .post(RESOURCE_PATH + "/{lineId}/sections")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given : 3개의 역, 1개의 노선을 등록
     * When  : 새로운 구간의 상행역이 등록된 노선의 하행 종점이 아닌 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_상행이_등록된_노선의_하행종점과_다르면_등록_불가() {
    }

    /**
     * Given : 3개의 역, 1개의 노선을 등록
     * When  : 새로운 구간의 하행역이 등록된 노선의 역인 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_하행역이_등록된_노선의_역_등록_불가() {

    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 구간을 제거 하면
     * Then  : 구간이 제거 된다
     */
    @Test
    void 지하철_노선_구간_제거_성공() {
    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 마지막 이 아닌 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_마지막이_아닌_구간_제거_불가() {
    }

    /**
     * Given : 3개의 역, 1개의 노선, 1개의 구간을 등록
     * When  : 구간이 하나인 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_구간이_하나인_구간_제거_불가() {
    }
}
