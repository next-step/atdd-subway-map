package subway.unit.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.line.Line;
import subway.domain.entity.line.LineSection;
import subway.domain.repository.LineRepository;
import subway.fixtures.LineFixture;

import javax.persistence.EntityManager;

@Component
public class LineDbUtil {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Line insertLine(Long upStationId, Long downStationId) {
        Line line = LineFixture.prepareRandom(upStationId, downStationId);
        return lineRepository.save(line);
    }

    @Transactional
    public LineSection insertSection(Line line, Long upStationId, Long downStationId, Long distance) {
        LineSection section = new LineSection(line, upStationId, downStationId, distance);
        entityManager.persist(section);
        return section;
    }
}
