package subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.presentation.request.AddSectionRequest;
import subway.line.presentation.request.CreateLineRequest;
import subway.station.presentation.request.CreateStationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.extractableResponse.LineApiExtractableResponse.*;
import static subway.acceptance.extractableResponse.StationApiExtractableResponse.*;

@DisplayName("지하철 구간 관련 기능")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given 지하철 구간을 등록하고
     * When 지하철 노선을 조회하면
     * Then 등록한 구간의 역을 조회할 수 있다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void 지하철_구간을_등록() {
        // given
        String 신분당선 = "신분당선";
        String 논현역 = "논현역";
        String 신논현역 = "신논현역";
        String 강남역 = "강남역";
        String 양재역 = "양재역";

        Long 논현역_ID = createStation(CreateStationRequest.from(논현역)).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from(신논현역)).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 신논현역_ID, 논현역_ID, 10);
        Long 신분당선_ID = createLine(신분당선_생성_정보).jsonPath().getLong("id");

        // when
        Long 강남역_ID = createStation(CreateStationRequest.from(강남역)).jsonPath().getLong("id");
        Long 양재역_ID = createStation(CreateStationRequest.from(양재역)).jsonPath().getLong("id");
        AddSectionRequest 구간_생성_정보 = AddSectionRequest.of(강남역_ID, 양재역_ID, 15);
        assertThat(createSection(구간_생성_정보, 신분당선_ID).statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> responseLineName = selectLine(신분당선_ID).jsonPath().get("stations.name");
        assertThat(responseLineName).contains(논현역, 신논현역, 강남역, 양재역);
    }

}
