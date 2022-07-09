package nextstep.subway.domain;

import nextstep.subway.domain.exception.InvalidUpDownStationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @Test
    @DisplayName("동일한 지하철역으로 구간을 생성 시 에러를 반환한다.")
    void invalidCreate() {
        Station upStation = new Station("강남역");
        Station downStation = upStation;

        assertThatThrownBy(() -> Section.create(upStation, downStation, 10))
                .isInstanceOf(InvalidUpDownStationException.class);
    }
}