package subway.line.domain;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

    default Line getById(final Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("지하철 노선을 찾을 수 없습니다 : id=" + id));
    }
}
