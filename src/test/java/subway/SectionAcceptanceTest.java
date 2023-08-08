package subway;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.SectionResponse;
import subway.enums.SubwayMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.SubwayUtils.*;

@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    private Long lineId;

    private Long upStationId;
    private Long downStationId;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    void setUp() {
        upStationId = createStation("사당역").jsonPath().getLong("id");
        downStationId = createStation("범계역").jsonPath().getLong("id");
        lineId = createLine(createLineRequest("4호선", "파랑", upStationId, downStationId, 10)).jsonPath().getLong("id");
    }

    /**
     * When 새로운 지하철 구간을 등록하면
     * Then 지하철 노선 조회시 등록 된 구간이 조회된다
     */
    @DirtiesContext
    @DisplayName("지하철 구간 생성")
    @Test
    void createSectionSuccess() {
        // given
        long newStationId = createStation("인덕원역").jsonPath().getLong("id");

        // when
        createSection(lineId, createSectionAddRequest(downStationId, newStationId, 3));

        //then
        List<String> lines = getLine(lineId).jsonPath().getList("stations.name", String.class);

        assertThat(lines).containsExactlyInAnyOrder("사당역","범계역","인덕원역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 이미 있는 역을 구간으로 등록하려하면
     * Then 지하철 구간이 등록되지 않는다
     */
    @DirtiesContext
    @DisplayName("지하철 구간 생성 실패 - 이미 있는 역")
    @Test
    void addSectionAlreadyExisted() {

        //given
        long newStationId = createStation("인덕원역").jsonPath().getLong("id");
        createSection(lineId, createSectionAddRequest(downStationId, newStationId, 2));

        //when
        Response response = createSection(lineId, createSectionAddRequest(newStationId, downStationId, 2));


        //then
        assertThat(response.getBody().asString()).isEqualTo(SubwayMessage.NEW_SECTION_NEW_STATION_ERROR);

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 구간의 상행역이 하행종점역과 다르면
     * Then 지하철 구간이 등록되지 않는다
     */
    @DirtiesContext
    @DisplayName("지하철 구간 생성 실패 - 하행종점역이 상행역이 아님")
    @Test
    void addSectionNotEqualDownStation() {

        //given
        long newStationId = createStation("인덕원역").jsonPath().getLong("id");

        //when
        Response response = createSection(lineId, createSectionAddRequest(upStationId, newStationId, 2));

        //then
        assertThat(response.getBody().asString()).isEqualTo(SubwayMessage.NEW_SECTION_UP_STATION_ERROR);

    }

    /**
     * Given 지하철 노선을 생성하고 구간을 하나 더 생성하고
     * When 마지막 역을 제거하면
     * Then 마지막 구간이 제거된다
     */
    @DirtiesContext
    @DisplayName("지하철 구간을 제거")
    @Test
    void removeSectionSuccess() {

        //given
        long newStationId = createStation("인덕원역").jsonPath().getLong("id");
        createSection(lineId, createSectionAddRequest(downStationId, newStationId, 3));

        //when
        deleteSections(lineId, createSectionDeleteRequest(newStationId));
        List<SectionResponse> response = getSections(lineId).jsonPath().getList("");

        //then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response).doesNotHaveToString("인덕원역");


    }

    /**
     * Given 지하철 노선을 생성하고
     * When 마지막 역을 제거하면
     * Then 구간이 제거되지 않는다
     */
    @DirtiesContext
    @DisplayName("지하철 구간 제거 실패 - 구간은 하나 이상")
    @Test
    void removeSectionOneSection() {

        //when
        Response response = deleteSections(lineId, createSectionDeleteRequest(downStationId));

        //then
        assertThat(response.getBody().asString()).isEqualTo(SubwayMessage.SECTION_MORE_THAN_TWO);

    }

    /**
     * Given 지하철 노선을 생성하고 구간을 하나 더 생성하고
     * When 가운데 역을 제거하면
     * Then 구간이 제거되지 않는다
     */
    @DirtiesContext
    @DisplayName("지하철 구간을 제거 실패 - 하행종점역만 제거 가능")
    @Test
    void removeSectionNotLast() {

        //given
        long newStationId = createStation("인덕원역").jsonPath().getLong("id");
        createSection(lineId, createSectionAddRequest(downStationId, newStationId, 2));

        //when
        Response response = deleteSections(lineId, createSectionDeleteRequest(downStationId));

        //then
        assertThat(response.getBody().asString()).isEqualTo(SubwayMessage.DELETE_STATION_MUST_END);

    }
    private Map<String, String> createSectionAddRequest(Long upStationId, Long downStationId, int distance){
        Map<String, String> map = new HashMap<>();
        map.put("upStationId", String.valueOf(upStationId));
        map.put("downStationId", String.valueOf(downStationId));
        map.put("distance", String.valueOf(distance));

        return map;
    }
    
    private Map<String, String> createSectionDeleteRequest(Long stationId) {
        Map<String, String> map = new HashMap<>();
        map.put("stationId", String.valueOf(stationId));
        return map;
    }
}