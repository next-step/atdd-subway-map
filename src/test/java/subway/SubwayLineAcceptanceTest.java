package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.test.AcceptanceTestBase;
import subway.test.LineRequestBuilder;
import subway.test.StationRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.test.Constants.*;


@DisplayName("지하철노선 관련 기능")
public class SubwayLineAcceptanceTest extends AcceptanceTestBase {

    LineRequestBuilder.Builder defaultBuilder;

    @BeforeEach
    void beforeEach() {
        var upStationResponse = new StationRequestBuilder.Builder()
                .name(GANGNAM_STATION)
                .build()
                .requestCreate();

        var downStationResponse =
                new StationRequestBuilder.Builder()
                        .name(SEOUL_STATION)
                        .build()
                        .requestCreate();
        this.defaultBuilder = new LineRequestBuilder.Builder()
                .name(LINE_SINBUNDANG)
                .color(COLOR_RED)
                .distance(10L)
                .upStationId(upStationResponse.extractId())
                .downStationId(downStationResponse.extractId());
    }

    /*
     * given 지하철 정보를 입력하고
     * when 지하철역 노선을 생성하면
     * /then 지하철역 노선이 생성된다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createSubwayLine() {
        //when
        var createdResponse = defaultBuilder.build().requestCreate();

        //then
        assertTrue(createdResponse.isCreated());
        assertThat(createdResponse.extractName()).isEqualTo(LINE_SINBUNDANG);
    }

    /**
     * given 지하철노선이 3개일때
     * when 지하철역 노선목록을 조회하면
     * then 지하철역 노선3개가 조회된다
     */

    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void getAllSubwayLine() {
        //given
        defaultBuilder.name(LINE_SINBUNDANG).build().requestCreate();
        defaultBuilder.name(LINE_ONE).build().requestCreate();
        defaultBuilder.name(LINE_TWO).build().requestCreate();

        //when
        var getAllResponse = LineRequestBuilder.requestGetAll();

        //then
        assertTrue(getAllResponse.isOk());
        assertThat(getAllResponse.extractIds().size()).isEqualTo(3);
    }

    /**
     * given 지하철역 노선이 등록되어있는 경우
     * when 해당 지하철역 노선을 조회한다
     * then: 지하철역 노선이 조회된다
     */

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void getSubwayLine() {
        //given
        var createdResponse = defaultBuilder.build().requestCreate();
        var createdId = createdResponse.extractId();

        //when
        var getResponse = LineRequestBuilder.requestGet(createdId);

        //then
        assertTrue(getResponse.isOk());
        assertThat(getResponse.extractName()).isEqualTo(LINE_SINBUNDANG);
    }

    /**
     * given: 노선이 등록된 경우
     * when: 노선의 이름과 색을 수정한다
     * then: 이름과 색이 수정된다
     */
    @DisplayName("지하철 노선의 이름과 색을 수정한다")
    @Test
    void updateSubwayLine() {
        //given
        var createdResponse = defaultBuilder.build().requestCreate();
        var createdId = createdResponse.extractId();

        //when
        var updatedResponse = new LineRequestBuilder.Builder()
                .name(LINE_ONE)
                .color(COLOR_BLUE)
                .build()
                .requestUpdate(createdId);

        //then
        assertTrue(updatedResponse.isNoContent());

        var getResponse = LineRequestBuilder.requestGet(createdId);
        assertThat(getResponse.extractName()).isEqualTo(LINE_ONE);
        assertThat(getResponse.extractColor()).isEqualTo(COLOR_BLUE);
    }

    /**
     * given: 노선이 등록된 경우
     * when: 노선을 삭제한다
     * then: 노선이 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteSubwayLine() {
        //given
        var createdResponse = defaultBuilder.build().requestCreate();
        var createdId = createdResponse.extractId();

        //when
        var deletedResponse = LineRequestBuilder.requestDelete(createdId);

        //then
        assertTrue(deletedResponse.isNoContent());

        var getAllResponse = LineRequestBuilder.requestGetAll();
        assertThat(getAllResponse.extractIds()).doesNotContain(createdId);
    }
}
