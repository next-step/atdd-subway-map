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
import subway.line.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineAcceptanceTest.parseCreateLineResponse;
import static subway.LineAcceptanceTest.requestCreateLine;


@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@Sql(value = {"/LineSelectionAcceptance.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineSelectionAcceptanceTest {

    @DisplayName("상행-하행만 있는 노선에 구간을 등록하는 기능")
    @Test
    void appendLineSelection1() {
        //when 지하철 노선을 생성하면.
        LineRequest createLineReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        LineResponse createLineRes = parseCreateLineResponse(requestCreateLine(createLineReq));


        //then 상행-하행 2가지 역만 있는 노선에 구간을 등록하면
        LineAppendRequest lineAppendRequest = new LineAppendRequest("2", "4", 10L);
        LineAppendResponse lineAppendResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineAppendRequest)
                .when().post("/lines/"  + createLineRes.getId() + "/selections")
                .then().log().all()
                .extract().jsonPath().getObject("$", LineAppendResponse.class);


        LineAppendResponse checkLineAppendResponse = new LineAppendResponse(createLineReq.getName(), createLineReq.getUpStationId(), lineAppendResponse.getDownStationId(), createLineReq.getDistance() + lineAppendRequest.getDistance(), List.of(1L,2L,4L));


        checkLineAppendResponse(lineAppendResponse, checkLineAppendResponse);
    }

    private void checkLineAppendResponse(LineAppendResponse actual, LineAppendResponse matcher) {
        assertThat(actual.getLineName()).isEqualTo(matcher.getLineName());
        assertThat(actual.getUpStationId()).isEqualTo(matcher.getUpStationId());
        assertThat(actual.getDownStationId()).isEqualTo(matcher.getDownStationId());
        assertThat(actual.getStations()).isEqualTo(matcher.getStations());
        assertThat(actual.getDistance()).isEqualTo(matcher.getDistance());
    }
}
