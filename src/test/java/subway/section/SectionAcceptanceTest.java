package subway.section;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.RestAssuredValidationUtils;
import subway.common.TestFixture;
import subway.dto.SectionRequest;
import subway.line.LineApiClient;
import subway.station.StationApiClient;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/init.sql")
public class SectionAcceptanceTest {
    private final StationApiClient stationApiClient = new StationApiClient();
    private final LineApiClient lineApiClient = new LineApiClient();
    private final SectionApiClient sectionApiClient = new SectionApiClient();
    private Long lineId;

    @BeforeEach
    void setup() {
        lineId = lineApiClient.createLine(TestFixture.SinBunDangLine).jsonPath().getLong("id");
    }

    @Test
    @DisplayName("지하철 구간을 생성한다.")
    void createSection() {
        // when
        sectionApiClient.createSection(lineId, new SectionRequest(3L, 2L, 10L));

        // then
        ExtractableResponse<Response> response = lineApiClient.findLineById(lineId);
        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);

        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.OK);
        RestAssuredValidationUtils.validateFieldContainsExactly(stationIds, 1L, 2L, 3L);
    }

    @Test
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아닐 경우 생성에 실패한다.")
    void createSectionWhenNewUpStationIsNotDownStationOfLine() {
        // when
        stationApiClient.createStation("지하철역4");
        ExtractableResponse<Response> response = sectionApiClient.createSection(lineId, new SectionRequest(3L, 4L, 10L));

        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록된 역일 수 없다.")
    void createSectionWhenNewDownStationExistsInLine() {
        // when
        ExtractableResponse<Response> response = sectionApiClient.createSection(lineId, new SectionRequest(2L, 3L, 10L));

        // then
        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("특정 지하철 구간을 삭제한다.")
    void deleteSection() {
        // given
        sectionApiClient.createSection(lineId, new SectionRequest(3L, 2L, 10L));

        // when
        sectionApiClient.deleteSection(1L, 3L);

        // then
        ExtractableResponse<Response> response = lineApiClient.findLineById(1L);
        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);
        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.OK);
        RestAssuredValidationUtils.validateFieldBodyHasSize(stationIds, 2);
    }

    @Test
    @DisplayName("노선에 등록된 하행 종점역이 아닐 경우, 해당 노선을 삭제할 수 없다.")
    void deleteSectionWhenGivenStationIsNotDownStationOfLine() {
        // when
        ExtractableResponse<Response> response = sectionApiClient.deleteSection(1L, 3L);

        // then
        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("노선에 구간이 1개일 경우, 구간을 삭제할 수 없다.")
    void deleteSectionWhenThereIsOnlyOneSectionInLine() {
        // when
        ExtractableResponse<Response> response = sectionApiClient.deleteSection(1L, 2L);

        // then
        RestAssuredValidationUtils.validateStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
