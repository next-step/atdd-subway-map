package nextstep.subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static nextstep.subway.domain.factory.EntityFactory.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lineRepository;

    @DisplayName("중복된 이름의 노선은 존재할 수 없다.")
    @Test
    void saveAlreadyExistName() {
        // given
        Line 이호선 = lineRepository.save(createLine("2호선", "green"));

        // when / then
        assertThatThrownBy(() -> lineRepository.save(createLine("2호선", "orange")))
                .isInstanceOf(RuntimeException.class);
    }
}