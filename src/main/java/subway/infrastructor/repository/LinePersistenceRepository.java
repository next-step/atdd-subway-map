package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.service.output.LineCommandRepository;
import subway.application.service.output.LineLoadRepository;
import subway.domain.Line;
import subway.domain.exception.NotFoundLineException;

import java.util.List;
import java.util.stream.Collectors;

@Component
class LinePersistenceRepository implements LineCommandRepository, LineLoadRepository {

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;

    public LinePersistenceRepository(LineRepository lineRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
    }

    @Override
    public Long createLine(Line line) {
        LineJpaEntity lineJpaEntity = lineMapper.domainToEntity(line);
        return lineRepository.save(lineJpaEntity).getId();
    }

    @Override
    public Line loadLine(Long createdLineId) {
        return lineRepository.findById(createdLineId).map(lineMapper::entityToDomain).orElseThrow(NotFoundLineException::new);
    }

    @Override
    public List<Line> loadLines() {
        return lineRepository.findAll().stream()
            .map(lineMapper::entityToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void updateLine(Line line) {
        lineRepository.save(lineMapper.domainToEntity(line));
    }

    @Override
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

}
