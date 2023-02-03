package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.SectionException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.StationFixture.*;

@DisplayName("구간 도메인")
class SectionTest {

    @DisplayName("상행역과 하행역이 동일한 경우 예외가 발생한다.")
    @Test
    void createExceptionTest() {
        assertThatThrownBy(() -> new Section(강남역, 강남역, 10))
                .isInstanceOf(SectionException.class);
    }
}
