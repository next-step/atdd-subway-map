package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.test.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static subway.test.Constants.COLOR_BLUE;
import static subway.test.Constants.LINE_SINBUNDANG;

public class SectionAcceptanceTest extends AcceptanceTestBase {
    LineRequestBuilder.Builder defaultLineBuilder;
    SectionRequestBuilder.Builder defaultSectionBuilder;

    @BeforeEach
    void beforeEach() {
        defaultLineBuilder = new LineRequestBuilder.Builder()
                .distance(10L)
                .name(LINE_SINBUNDANG)
                .color(COLOR_BLUE);

        defaultSectionBuilder = new SectionRequestBuilder.Builder()
                .distance(10L);

    }


    //given: 지하철 노선이 등록되어 있다
    //when: 노선의 하행 종점역을 상행역으로 하는 구간을 등록한다
    //then: 새로운 구간이 등록된다
    //then: 노선의 하행 종점역이 변경된다
    @DisplayName("지하철 노선 구간을 등록한다")
    @Test
    void createSection() {
        //given
        var data = createLineWithOneSection();
        var newDownStationId = createStation();

        //when
        var response = defaultSectionBuilder
                .upStationId(data.downStationId)
                .downStationId(newDownStationId)
                .build()
                .requestCreate(data.subwayLineId);

        //then
        assertTrue(response.isCreated());
        //then
        var changedDownStationId = LineRequestBuilder
                .requestGet(data.subwayLineId)
                .extractDownStationId();
        assertThat(changedDownStationId).isEqualTo(newDownStationId);
    }

    //given: 지하철 노선이 등록되어 있다
    //when 노선의 하행 종점역이 아닌 역을 상행역으로 하는 구간을 등록한다
    //then 구간 등록에 실패한다
    @DisplayName("구간의 상행역이 노선의 하행 종점역이 아닌 경우 구간을 등록할 수 없다")
    @Test
    void failToCreateSection() {
        //given
        var data = createLineWithTwoSection();
        var newDownStationId = createStation();

        //when
        var response = defaultSectionBuilder
                .upStationId(data.middleStationId)
                .downStationId(newDownStationId)
                .build()
                .requestCreate(data.subwayLineId);

        //then
        assertTrue(response.isBadRequest());
    }

    //given: 지하철 노선이 등록되어 있다
    //when: 새로운 구간을 등록할 때, 이미 등록된 역을 하행역으로 사용한다
    //then 구간 등록에 실패한다
    @DisplayName("구간의 하행역이 이미 노선에 등록된 경우 구간을 등록할 수 없다")
    @Test
    void failToCreateSection2() {
        //given
        var data = createLineWithTwoSection();

        //when
        var response = defaultSectionBuilder
                .upStationId(data.downStationId)
                .downStationId(data.middleStationId)
                .build()
                .requestCreate(data.subwayLineId);

        //then
        assertTrue(response.isBadRequest());
    }

    //given: 구간이 2개 이상인 지하철 노선이 등록되어 있다
    //when: 지하철 노선의 하행 종점역을 제거한다
    //then: 구간 제거에 성공한다
    //then: 노선의 하행 종점역이 변경된다.
    @DisplayName("지하철 구간 제거에 성공한다")
    @Test
    void deleteSection() {
        //given
        var data = createLineWithTwoSection();

        //when
        var response = SectionRequestBuilder.requestDelete(data.subwayLineId, data.downStationId);
        //then
        assertTrue(response.isNoContent());

        //then
        var changedDownStationId = LineRequestBuilder.requestGet(data.subwayLineId).extractDownStationId();
        assertThat(changedDownStationId).isNotEqualTo(data.downStationId);
    }

    //given: 구간이 2개 이상인 지하철 노선이 등록되어 있다
    //when: 지하철 노선의 히헹 종점역이 아닌 역을 제거한다
    //then: 구간 제거에 실패한다
    @DisplayName("노선의 하행 종점역 이외의 역은 삭제할 수 없다")
    @Test
    void failToDeleteSection() {
        //given
        var data = createLineWithTwoSection();

        //when
        var response = SectionRequestBuilder.requestDelete(data.subwayLineId, data.middleStationId);

        //then
        assertTrue(response.isBadRequest());
    }

    //given: 구간이 1개인 지하철 노선이 등록되어있다
    //when: 지하철 노선의 하행 종점역을 제거한다
    //then: 구간 제거에 실패한다
    @DisplayName("노선의 구간이 1개인 경우 역을 삭제할 수 없다")
    @Test
    void failToDeleteSection2() {
        //given
        var data = createLineWithOneSection();

        //when
        var response = SectionRequestBuilder.requestDelete(data.subwayLineId, data.downStationId);

        //then
        assertTrue(response.isBadRequest());
    }

    private Data createLineWithOneSection() {
        var upStationId = createStation();
        var downStationId = createStation();
        var subwayLineId = defaultLineBuilder
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build()
                .requestCreate()
                .extractId();
        return new Data(upStationId, downStationId, subwayLineId);
    }

    private Data createLineWithTwoSection() {
        var upStationId = createStation();
        var downStationId = createStation();
        var middleStationId = createStation();
        var subwayLineId = defaultLineBuilder
                .upStationId(upStationId)
                .downStationId(middleStationId)
                .build()
                .requestCreate()
                .extractId();

        defaultSectionBuilder
                .upStationId(middleStationId)
                .downStationId(downStationId)
                .build()
                .requestCreate(subwayLineId);

        return new Data(upStationId, downStationId, middleStationId, subwayLineId);
    }

    private Long createStation() {
        return new StationRequestBuilder.Builder().name(RandomGenerator.generateString(5)).build().requestCreate().extractId();
    }

    class Data {
        Long upStationId;
        Long downStationId;
        Long subwayLineId;
        Long middleStationId;

        public Data(Long upStationId, Long downStationId, Long middleStationId, Long subwayLineId) {
            this(upStationId, downStationId, subwayLineId);
            this.middleStationId = middleStationId;
        }

        public Data(Long upStationId, Long downStationId, Long subwayLineId) {
            this.upStationId = upStationId;
            this.downStationId = downStationId;
            this.subwayLineId = subwayLineId;
        }
    }
}
