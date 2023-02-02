package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.section.SectionCreateRequest;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {

    Long lineId;
    Long firstStationId;
    Long secondStationId;
    Long thirdStationId;

    @BeforeEach
    public void init() {
        firstStationId = StationRestAssuredTest.createStation("지하철역1");
        secondStationId = StationRestAssuredTest.createStation("지하철역2");
        thirdStationId = StationRestAssuredTest.createStation("지하철역3");
        LineCreateRequest lineCreateRequest = new LineCreateRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10L);
        lineId = LineRestAssuredTest.createLine(lineCreateRequest);
    }
    /**
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선에 구간이 등록된다.
     * Then 지하철 노선을 조회하면 추가된 구간을 확인할 수 있다.
     */
    @DisplayName("지하철 노선에 구간을 등록하고 그 구간을 조회할 수 있다.")
    @Test
    public void sectionCreateTest() {
        var param = new SectionCreateRequest(secondStationId, thirdStationId, 10L);
        SectionRestAssuredTest.createSection(param);

        LineResponse line = LineRestAssuredTest.getLine(lineId);
        List<Long> ids = line.getStationResponseList().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(ids).containsAnyOf(thirdStationId);
    }


}
