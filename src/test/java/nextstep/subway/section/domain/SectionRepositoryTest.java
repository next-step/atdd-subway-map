package nextstep.subway.section.domain;

import nextstep.subway.line.LineTestSource;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SectionRepositoryTest {

    @Autowired
    private SectionRepository target;

    @Autowired
    private LineRepository lineRepository;

    @Test
    void lineId로조회() {
        final Line savedLine = lineRepository.save(LineTestSource.lineWithSection());

        final List<Section> result = target.findAllByLine_Id(savedLine.getId());

        assertThat(result).isNotEmpty();
    }

}