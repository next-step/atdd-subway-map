package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {

    @DisplayName("노선에 등록된 구간이 없으면 isNotEndDownStation은 참을 반환한다")
    @Test
    void 노선에_등록된_구간이_없고_하행_종점역이_아닌지_검증_테스트() {
        //given
        Line line = new Line();

        //when then
        assertThat(line.isNotEndDownStation(new Station())).isTrue();
    }

    @DisplayName("노선에 등록된 구간이 있고 인자로 전달된 Station이 노선의 하행종점역이 아니면 isNotEndDownStation은 참을 반환한다")
    @Test
    void 노선에_등록된_구간이_있고_하행_종점역이_아닌지_검증_테스트() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station station = 역_생성(3L);

        //when then
        assertThat(line.isNotEndDownStation(station)).isTrue();
    }

    @DisplayName("노선에 등록된 구간이 있고 인자로 전달된 Station이 노선의 하행종점역이 맞으면 isNotEndDownStation은 거짓을 반환한다")
    @Test
    void 노선에_등록된_구간이_있고_하행_종점역이_맞는지_검증_테스트() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station station = 역_생성(2L);

        //when then
        assertThat(line.isNotEndDownStation(station)).isFalse();
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, " +
            "새로 추가할 구간의 상행역이 기존 노선 하행종점역이고, " +
            "새로 추가할 구간의 하행역이 기존 노선에 등록되어 있지 않으면, " +
            "구간 추가가 예외없이 성공한다")
    @Test
    void 노선에_등록된_구간이_있고_구간을_추가할_때_정상() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station upStation = 역_생성(2L);
        Station downStation = 역_생성(3L);

        //when then
        assertDoesNotThrow(() -> line.addSection(upStation, downStation, 1000));
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, " +
            "새로 추가할 구간의 상행역이 기존 노선 하행종점역이 아니면, " +
            "구간 추가시 예외가 발생한다")
    @Test
    void 노선에_등록된_구간이_있고_구간을_추가할_때_하행종점역_예외() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station upStation = 역_생성(1L);
        Station downStation = 역_생성(3L);

        //when then
        assertThrows(IllegalArgumentException.class, () -> line.addSection(upStation, downStation, 1000));
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, " +
            "새로 추가할 구간의 하행역이 기존 노선에 등록되어있는 역이라면" +
            "구간 추가시 예외가 발생한다")
    @Test
    void 노선에_등록된_구간이_있고_구간을_추가할_때_이미_등록된역_예외() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station upStation = 역_생성(2L);
        Station downStation = 역_생성(1L);

        //when then
        assertThrows(IllegalArgumentException.class, () -> line.addSection(upStation, downStation, 1000));
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, 구간 삭제시 예외가 발생한다")
    @Test
    void 노선에_등록된_구간_삭제_종점역만_등록되어있어_예외() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);

        //when then
        assertThrows(IllegalArgumentException.class, () -> line.deleteSection(역_생성(3L)));
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, 구간 삭제시 예외가 발생한다")
    @Test
    void 노선에_등록된_구간_삭제_삭제할_구간의_하행역이_종점역이_아니면_예외() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station upStation = 역_생성(2L);
        Station downStation = 역_생성(3L);
        line.addSection(upStation, downStation, 100);

        //when then
        assertThrows(IllegalArgumentException.class, () -> line.deleteSection(역_생성(4L)));
    }

    @DisplayName("노선에 등록된 하행종점역, 상행종점역이 있고, 구간 삭제시 성공한다")
    @Test
    void 노선에_등록된_구간_삭제() {
        //given
        Line line = 상행종점역_하행종점역이_구간으로_등록된_노선_생성(1L, 2L);
        Station upStation = 역_생성(2L);
        Station downStation = 역_생성(3L);
        line.addSection(upStation, downStation, 100);

        //when then
        assertThat(line.getSections().size()).isEqualTo(2);
        assertDoesNotThrow(() -> line.deleteSection(역_생성(3L)));
        assertThat(line.getSections().size()).isEqualTo(1);
    }

    private Line 상행종점역_하행종점역이_구간으로_등록된_노선_생성(Long upStationId, Long downStationId) {
        Station upStation = 역_생성(1L);
        Station downStation = 역_생성(2L);

        return new Line("2호선", "green", upStation, downStation, 9999);
    }

    private Station 역_생성(Long id) {
        try {
            Station station = new Station();

            Field idField = station.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(station, id);

            return station;
        } catch (Exception e) {
            return null;
        }
    }
}
