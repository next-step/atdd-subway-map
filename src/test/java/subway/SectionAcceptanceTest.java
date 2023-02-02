package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.BaseAcceptanceTest;
import subway.domain.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;
import subway.executor.AcceptanceExecutor;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest extends BaseAcceptanceTest {

    private Long 정자역;
    private Long 판교역;
    private Long 신분당선;

    @BeforeEach
    void setUp() {
        정자역 = createStation("정자역");
        판교역 = createStation("판교역");
        신분당선 = createLine("신분당선", "red", 정자역, 판교역).jsonPath().getLong("id");
    }

    @Test
    @DisplayName("지하철 구간 생성")
    void createSection() {
        //given 지하철 노선을 생성하고
        //when 구간을 추가한다.
        ExtractableResponse<Response> response = addSection(신분당선, 정자역, 판교역);
        //then 201을 응답받고, 노선을 조회하면 생성한 구간이 조회된다.
        LineResponse lineResponse = AcceptanceExecutor.get("/lines/" + 신분당선, LineResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineResponse.getSections().stream().
                        filter(
                                section -> section.getUpStation().getName().equals("정자역")
                                && section.getDownStation().getName().equals("판교역")).findFirst())
        );

    }


    @Test
    @DisplayName("지하철 구간 생성 - 새로운 구간의 상행역은 노선의 하행역이어야 함")
    void createSectionEqualEndStation() {
        //given 지하철 노선을 생성하고 구간을 추가한다.
        addSection(신분당선, 정자역, 판교역);

        //when 이미 추가된 하행역과 다른 상행역을 가지는 구간을 추가한다.
        Long 청계산입구역 = createStation("청계산입구역");
        Long 양재역 = createStation("양재역");
        ExtractableResponse<Response> response = addSection(신분당선, 청계산입구역, 양재역);

        //then BAD_REQUEST 400 응답을 받는다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("지하철 구간 생성 - 새로운 구간의 하행역은 등록된 역일 수 없다.")
    void createSectionWhenDuplicate() {
        //given 지하철 노선을 생성하고 구간을 추가한다.
        addSection(신분당선, 정자역, 판교역);

        //when 이미 등록된 역을 하행역으로 갖는 노선을 추가한다.
        Long 양재역 = createStation("양재역");
        ExtractableResponse<Response> response = addSection(신분당선, 양재역, 판교역);

        //then BAD_REQUEST 400 응답을 받는다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("지하철 구간 삭제")
    void deleteSection() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long 청계산입구역 = createStation("청계산입구역");

        Long section1 = addSection(신분당선, 정자역, 판교역).jsonPath().getLong("id");
        Long section2 = addSection(신분당선, 판교역, 청계산입구역).jsonPath().getLong("id");

        //when 구간 1개를 삭제한다.
        ExtractableResponse<Response> response = deleteSection(신분당선, section2);

        //then 노선 조회 시 구간이 1개만 조회된다.
        LineResponse lineResponse = AcceptanceExecutor.get("/lines/" + 신분당선, LineResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineResponse.getSections()).hasSize(1),
                () -> assertThat(lineResponse.getSections().get(0).getId()).isEqualTo(section1)
        );
    }

    @Test
    @DisplayName("지하철 구간 삭제 실패 - 마지막 구간이 아닌 경우")
    void deleteSectionWhenNotLast() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long 청계산입구역 = createStation("청계산입구역");
        Long 정자_판교_구간 = addSection(신분당선, 정자역, 판교역).jsonPath().getLong("id");
        Long 판교_청계산입구_구간 = addSection(신분당선, 판교역, 청계산입구역).jsonPath().getLong("id");

        //when 첫번째 구간을 삭제한다.
        ExtractableResponse<Response> response = deleteSection(신분당선, 정자_판교_구간);

        //then BAD_REQUEST 400 응답을 받는다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("지하철 구간 삭제 실패 - 구간이 하나만 있는 경우")
    void deleteSectionWhenOnlyOne() {
        //given 지하철 역과 노선을 생성한다. 구간 2개를 추가한다.
        Long section1 = addSection(신분당선, 정자역, 판교역).jsonPath().getLong("id");

        //when 첫번째 구간을 삭제한다.
        ExtractableResponse<Response> response = deleteSection(신분당선, section1);

        //then BAD_REQUEST 400 응답을 받는다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> deleteSection(Long 신분당선, Long sectionId) {
        return AcceptanceExecutor.delete("/lines/" + 신분당선 + "/sections?sectionId=" + sectionId);
    }

    private static ExtractableResponse<Response> addSection(Long lineId, Long upStationId, Long downStationId) {
        SectionRequest sectionRequest = new SectionRequest();
        sectionRequest.setUpStationId(upStationId);
        sectionRequest.setDownStationId(downStationId);
        sectionRequest.setDistance(10L);
        return AcceptanceExecutor.post("/lines/" + lineId + "/sections", sectionRequest);
    }

    private static ExtractableResponse<Response> createLine(String lineName, String red, Long upStation, Long downStation) {

        LineRequest lineRequest = new LineRequest();
        lineRequest.setName(lineName);
        lineRequest.setColor(red);
        lineRequest.setUpStationId(upStation);
        lineRequest.setDownStationId(downStation);
        lineRequest.setDistance(10L);

        return AcceptanceExecutor.post("/lines", lineRequest);
    }

    private static Long createStation(String name) {
        return AcceptanceExecutor.post("/stations", new StationRequest(name)).jsonPath().getLong("id");
    }

}
