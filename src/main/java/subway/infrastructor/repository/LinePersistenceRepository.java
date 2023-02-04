package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.service.output.LineCommandRepository;
import subway.application.service.output.LineLoadRepository;
import subway.domain.Line;
import subway.domain.NotFoundStationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class LinePersistenceRepository implements LineCommandRepository, LineLoadRepository {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineMapper lineMapper;

    public LinePersistenceRepository(LineRepository lineRepository, StationRepository stationRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineMapper = lineMapper;
    }

    @Override
    public Long createLine(Line line) {
        LineJpaEntity lineJpaEntity = lineMapper.domainToEntity(line);
        return lineRepository.save(lineJpaEntity).getId();
    }

    @Override
    public Optional<Line> loadLine(Long createdLineId) {
        return lineRepository.findById(createdLineId).map(this::buildLine);
    }

    @Override
    public List<Line> loadLines() {
        return lineRepository.findAll().stream()
            .map(this::buildLine)
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

    private Line buildLine(LineJpaEntity lineJpaEntity) {
        StationJpaEntity upStationJpaEntity = stationRepository.findById(lineJpaEntity.getUpStationId().getId())
            .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 상행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getUpStationId().getId())));

        StationJpaEntity downStationJpaEntity = stationRepository.findById(lineJpaEntity.getDownStationId().getId())
            .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 하행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getDownStationId().getId())));

        return lineMapper.entityToDomain(lineJpaEntity, upStationJpaEntity, downStationJpaEntity);
    }

}
