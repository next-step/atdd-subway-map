package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
import subway.section.repository.SectionRepository;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.line.LineAcceptanceTest.createLineRequestFixture;
import static subway.line.LineAcceptanceTest.requestCreateLine;
import static subway.station.StationAcceptanceTest.createStation;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private SectionRepository sectionRepository;

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
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        long sectionDownStationId = createStation("추가하는_구간_하행역");

        //when
        SectionCreateRequest request = createSectionRequestFixture(lineId, sectionDownStationId, oldLineDownStationId);
        ExtractableResponse<Response> response = requestCreateSection(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertRequestAndResponseEquals(request, response.as(SectionResponse.class));
    }

    private void assertRequestAndResponseEquals(SectionCreateRequest request, SectionResponse sectionResponse) {
        assertThat(sectionResponse.getSectionId()).isNotNull();
        assertThat(sectionResponse.getLineResponse().getId()).isEqualTo(request.getLineId());
        assertThat(sectionResponse.getDistance()).isEqualTo(request.getDistance());
        assertThat(sectionResponse.getUpStation().getId()).isEqualTo(request.getUpStationId());
        assertThat(sectionResponse.getDownStation().getId()).isEqualTo(request.getDownStationId());
    }

    private SectionCreateRequest createSectionRequestFixture(Long lineId, Long downStationId, Long upStationId) {
        int distance = 10;

        return SectionCreateRequest.builder()
                .lineId(lineId)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    private ExtractableResponse<Response> requestCreateSection(SectionCreateRequest request) {
        return given()
                .pathParam("lineId", request.getLineId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request).log().all()
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }

    /**
     * Given : 노선을 생성한다.
     * When  : 새로운 구간의 상행역이 등록된 노선의 하행 종점이 아닌 구간을 추가 하면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_상행이_등록된_노선의_하행종점과_다르면_등록_불가() {
        //given
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        long sectionDownStationId = createStation("추가하는_구간_하행역");

        //when
        SectionCreateRequest request = createSectionRequestFixture(lineId, sectionDownStationId, oldLineUpStationId);
        ExtractableResponse<Response> response = requestCreateSection(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given : 노선을 생성한다
     * When  : 새로운 구간의 하행역이 이미 노선에 등록된 역이면
     * Then  : 구간 등록에 실패 한다
     */
    @Test
    void 지하철_노선_구간_등록_실패_새로운_구간의_하행역이_등록된_노선의_역_등록_불가() {
        //given
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();

        //when
        SectionCreateRequest request = createSectionRequestFixture(lineId, oldLineUpStationId, oldLineDownStationId);
        ExtractableResponse<Response> response = requestCreateSection(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given : 노선을 등록한다
     * And   : 구간을 등록한다
     * When  : 구간을 제거 하면
     * Then  : 구간이 제거 된다
     */
    @Test
    void 지하철_노선_구간_제거_성공() {
        //given
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        long newSectionDownStationId = createStation("추가하는_구간_하행역");

        SectionCreateRequest request = createSectionRequestFixture(lineId, newSectionDownStationId, oldLineDownStationId);
        ExtractableResponse<Response> createSectionResponse = requestCreateSection(request);
        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()); // then에서 검증하는게 좋을까?

        //when
        ExtractableResponse<Response> deleteResponse = requestDeleteSection(lineId, newSectionDownStationId);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Long sectionId = createSectionResponse.as(SectionResponse.class).getSectionId();
        sectionRepository.findById(sectionId).ifPresent(section -> fail("구간이 제거 되지 않았습니다"));
    }

    private ExtractableResponse<Response> requestDeleteSection(long lineId, long newSectionDownStationId) {
        return given()
                .pathParam("id", lineId)
                .param("stationId", newSectionDownStationId)
                .when().log().all()
                .delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    /**
     * Given : 노선을 등록한다
     * And   : 구간을 등록한다
     * When  : 마지막 이 아닌 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_마지막이_아닌_구간_제거_불가() {
        //given
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();
        long newSectionDownStationId = createStation("추가하는_구간_하행역");

        SectionCreateRequest request = createSectionRequestFixture(lineId, newSectionDownStationId, oldLineDownStationId);
        ExtractableResponse<Response> createSectionResponse = requestCreateSection(request);
        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()); // then에서 검증하는게 좋을까?

        //when
        ExtractableResponse<Response> deleteResponse = requestDeleteSection(lineId, oldLineDownStationId);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(deleteResponse.asString()).isEqualTo("마지막 구간의 하행역이 아닙니다."); // 메시지를 바꾸면 테스트가 꺠지는데 상수화 하는게 좋을까?
    }

    /**
     * Given : 노선을 등록한다
     * When  : 구간이 하나인 구간을 제거 하면
     * Then  : 구간 제거에 실패 한다
     */
    @Test
    void 지하철_노선_구간_실패_구간이_하나인_구간_제거_불가() {
        //given
        long oldLineUpStationId = createStation("노선_상행역");
        long oldLineDownStationId = createStation("노선_하행역");
        long lineId = requestCreateLine(createLineRequestFixture("노선1", oldLineUpStationId, oldLineDownStationId)).getId();

        //when
        ExtractableResponse<Response> deleteResponse = requestDeleteSection(lineId, oldLineDownStationId);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(deleteResponse.asString()).isEqualTo("노선에 구간이 하나 입니다."); // 메시지를 바꾸면 테스트가 꺠지는데 상수화 하는게 좋을까?
    }
}
