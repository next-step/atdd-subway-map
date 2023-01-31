package subway.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.exception.LineNotFoundException;
import subway.model.Line;
import subway.repository.LineRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.unit.UnitTestFixture.BUN_DANG_LINE;
import static subway.unit.UnitTestFixture.SHIN_BUN_DANG_LINE;

@DataJpaTest
class LineRepositoryTest {

    private static Line shinBunDangLine;
    private static Line bunDangLine;

    @Autowired
    LineRepository lineRepository;

    @BeforeEach
    void save() {
        lineRepository.deleteAllInBatch();
        shinBunDangLine = lineRepository.save(SHIN_BUN_DANG_LINE);
        bunDangLine = lineRepository.save(BUN_DANG_LINE);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findAll() {
        // when
        List<Line> lines = lineRepository.findAll();

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines).containsAll(List.of(shinBunDangLine, bunDangLine));
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void findById() {
        // when
        Line line = lineRepository.findById(shinBunDangLine.getId())
                .orElseThrow(LineNotFoundException::new);

        // then
        assertThat(line).isEqualTo(shinBunDangLine);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void modify() {
        // given
        Line line = lineRepository.findById(shinBunDangLine.getId())
                .orElseThrow(LineNotFoundException::new);

        // when
        line.modifyLine("당당선", "bg-red-700");
        Line modifiedLine = lineRepository.save(line);

        // then
        assertThat(modifiedLine.getName()).isEqualTo("당당선");
        assertThat(modifiedLine.getColor()).isEqualTo("bg-red-700");
        assertThat(line.getName()).isEqualTo("당당선");
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteById() {
        // given
        Line line = lineRepository.findById(shinBunDangLine.getId())
                .orElseThrow(LineNotFoundException::new);

        // when
        lineRepository.deleteById(line.getId());

        // then
        List<Line> lines = lineRepository.findAll();
        assertThat(lines).doesNotContain(shinBunDangLine);
    }


}
