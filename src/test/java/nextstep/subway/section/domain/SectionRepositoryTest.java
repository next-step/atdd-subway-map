package nextstep.subway.section.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static nextstep.subway.line.LineTestSource.lineId;
import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SectionRepositoryTest {

    @Autowired
    private SectionRepository target;

    @Test
    void lineId로조회() {
        target.save(section(1L));
        target.save(section(1L));

        final List<Section> result = target.findAllByLineId(lineId);

        assertThat(result).hasSize(2);
    }

}