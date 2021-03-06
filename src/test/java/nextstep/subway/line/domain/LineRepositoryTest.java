package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    @DisplayName("지하철 노선이 존재하지 않을 경우")
    void nonExistLineName() {
        // given
        String lineName = "신분당선";

        // when
        boolean result = lineRepository.existsByName(lineName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("지하철 노선이 존재할 경우")
    void existLineName() {
        // given
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600");
        Line savedLine = lineRepository.save(lineRequest.toLine());

        // when
        boolean result = lineRepository.existsByName(savedLine.getName());

        // then
        assertThat(result).isTrue();
    }
}
