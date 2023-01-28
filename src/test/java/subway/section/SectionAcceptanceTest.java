package subway.section;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.DataBaseCleanUp;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.line.LineAcceptanceTest.createLineRequestFixture;
import static subway.line.LineAcceptanceTest.requestCreateLine;
import static subway.station.StationAcceptanceTest.createStation;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    void tearDown() {
        dataBaseCleanUp.cleanUp();
    }

    /**
     * Given : 노선을 생성한다
     * When  : 구간을 추가 하면
     * Then  : 구간이 추가 되고 추가된 구간을 응답한다.
     * And   : 응답한 구간은 생성요청한 구간의 정보와 같다.
     */
    @Test
    void 지하철_노선_구간_등록_성공() {
        //given
        Long oldLineUpStationId = createStation("노선_상행역");
        Long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        Long sectionDownStationId = createStation("추가하는_구간_하행역");

        //when
        SectionCreateRequest request = createSectionCreateRequest(lineId, sectionDownStationId, oldLineDownStationId);
        SectionResponse sectionResponse = requestCreateSection(request);

        //then
        assertRequestAndResponseEquals(request, sectionResponse);
    }

    private void assertRequestAndResponseEquals(SectionCreateRequest request, SectionResponse sectionResponse) {
        assertThat(sectionResponse.getSectionId()).isNotNull();
        assertThat(sectionResponse.getLineResponse().getId()).isEqualTo(request.getLineId());
        assertThat(sectionResponse.getDistance()).isEqualTo(request.getDistance());
        assertThat(sectionResponse.getUpStation().getId()).isEqualTo(request.getUpStationId());
        assertThat(sectionResponse.getDownStation().getId()).isEqualTo(request.getDownStationId());
    }

    private SectionCreateRequest createSectionCreateRequest(Long lineId, Long downStationId, Long upStationId) {

        SectionCreateRequest request = SectionCreateRequest.builder()
                .lineId(lineId)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(10)
                .build();

        return request;
    }

    private SectionResponse requestCreateSection(SectionCreateRequest request) {
        return given()
                .pathParam("lineId", request.getLineId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/lines/{lineId}/sections")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(SectionResponse.class);
    }

    /**
     * Given : 노선을 생성한다.
     * When  : 새로운 구간의 상행역이 등록된 노선의 하행 종점이 아닌 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_상행이_등록된_노선의_하행종점과_다르면_등록_불가() {
        //given
        Long oldLineUpStationId = createStation("역1");
        Long oldLineDownStationId = createStation("역1");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        Long sectionDownStationId = createStation("역1");

        //when
        SectionCreateRequest request = createSectionCreateRequest(lineId, sectionDownStationId, oldLineUpStationId);
        SectionResponse sectionResponse = requestCreateSection(request);

        //then
        assertRequestAndResponseEquals(request, sectionResponse);
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
