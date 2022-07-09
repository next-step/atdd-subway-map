package nextstep.subway.domain.line;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.domain.line.Fixture.강남역;
import static nextstep.subway.domain.line.Fixture.분당역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StationsTest {
    @Test
    @DisplayName("역들 안에 들어가는 역이 두 개 이상이 아니면 IllegalArgumentException 예외를 발생한다.")
    void test2() {
        // given
        List<Station> stationList = List.of(강남역);

        // when & then
        assertThatThrownBy(
            () -> new Stations(stationList)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("역들 안에는 상행 종점역과 하행 종점역이 포함된다.")
    void test3() {
        // given
        Station 상행종점역 = 강남역;
        Station 하행종점역 = 분당역;

        // when
        Stations stations = new Stations(Arrays.asList(상행종점역, 하행종점역));

        // then
        assertAll(
            () -> assertThat(stations.getStations()).hasSize(2),
            () -> assertThat(stations.getStations()).contains(상행종점역),
            () -> assertThat(stations.getStations()).contains(하행종점역)
        );
    }
}
