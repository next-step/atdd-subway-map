package nextstep.subway.domain;

import nextstep.subway.domain.exception.InvalidUpDownStationException;
import nextstep.subway.domain.exception.OutOfBoundDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @Test
    @DisplayName("동일한 지하철역으로 구간을 생성 시 에러를 반환한다.")
    void invalidCreate() {
        Station upStation = StationTest.GANGNAM_STATION;
        Station downStation = upStation;

        assertThatThrownBy(() -> Section.create(upStation, downStation, 10))
                .isInstanceOf(InvalidUpDownStationException.class);
    }

    @Test
    @DisplayName("양수가 아닌 거리로 구간을 생성 시 에러를 반환한다.")
    void invalidCreate_distance() {
        Station upStation = StationTest.GANGNAM_STATION;
        Station downStation = StationTest.YEOKSAM_STATION;

        assertThatThrownBy(() -> Section.create(upStation, downStation, 0))
                .isInstanceOf(OutOfBoundDistanceException.class)
                .hasMessage("1 보다 작을 수 없습니다. 입력한 값=0");
    }

    @Test
    @DisplayName("구간의 하행역과 요청한 지하철 역이 같으면 True 를 리턴한다.")
    void isMatchDownStation() {
        final Station upStation = StationTest.GANGNAM_STATION;
        final Station downStation = StationTest.YEOKSAM_STATION;
        Section section = Section.create(upStation, downStation, 10);

        assertThat(section.isMatchDownStation(StationTest.GANGNAM_STATION)).isFalse();
        assertThat(section.isMatchDownStation(StationTest.SEOLLEUNG_STATION)).isFalse();
        assertThat(section.isMatchDownStation(StationTest.YEOKSAM_STATION)).isTrue();
    }

    @Test
    @DisplayName("구간의 하행역 혹은 상행역 중 같은 지하철역을 가지고 있으면 True 를 리턴한다.")
    void hasStation() {
        final Station upStation = StationTest.GANGNAM_STATION;
        final Station downStation = StationTest.YEOKSAM_STATION;
        Section section = Section.create(upStation, downStation, 10);

        assertThat(section.hasStation(StationTest.GANGNAM_STATION)).isTrue();
        assertThat(section.hasStation(StationTest.YEOKSAM_STATION)).isTrue();
        assertThat(section.hasStation(StationTest.SEOLLEUNG_STATION)).isFalse();
    }
}