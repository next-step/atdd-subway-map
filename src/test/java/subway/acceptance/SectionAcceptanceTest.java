package subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.line.presentaion.request.CreateLineRequest;
import subway.station.presentaion.request.CreateStationRequest;

import static subway.acceptance.extractableResponse.LineApiExtractableResponse.createLine;
import static subway.acceptance.extractableResponse.StationApiExtractableResponse.createStation;

@DisplayName("지하철 구간 관련 기능")
// @Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given 지하철 구간을 등록하고
     * When 지하철 노선을 조회하면
     * Then 등록한 구간을 조회할 수 있다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void 지하철_구간을_등록() {
        // given
        String 신분당선 = "신분당선";
        String 강남역 = "강남역";
        String 신논현역 = "신논현역";
        String 논현역 = "논현역";

        Long upStationId = createStation(CreateStationRequest.from(강남역)).jsonPath().getLong("id");
        Long downStationId = createStation(CreateStationRequest.from(신논현역)).jsonPath().getLong("id");
        CreateLineRequest createLineRequest = CreateLineRequest.of(신분당선, "bg-red-600", upStationId, downStationId, 10);
        Long lineId = createLine(createLineRequest).jsonPath().getLong("id");

        // 지하철 구간 등록
//        SectionRequest sectionRequest =
//
//
//        // when
//        assertThat(createLine(lineRequest).statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // createSection


    }


}
