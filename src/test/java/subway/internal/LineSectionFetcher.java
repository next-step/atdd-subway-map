package subway.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import subway.domain.entity.line.Line;
import subway.domain.entity.line.LineSection;

import java.util.List;

@Component
public interface LineSectionFetcher extends JpaRepository<LineSection, Long> {
    List<LineSection> findAllByLine(Line line);
}
