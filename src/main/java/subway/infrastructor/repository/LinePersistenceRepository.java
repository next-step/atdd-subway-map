package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.repository.LineRepositoryPort;
import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;
import subway.domain.NotFoundStationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class LinePersistenceRepository implements LineRepositoryPort {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineMapper lineMapper;

    public LinePersistenceRepository(LineRepository lineRepository, StationRepository stationRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineMapper = lineMapper;
    }

    @Override
    public Long createLine(LineCreateDomain lineCreateDomain) {
        LineJpaEntity lineJpaEntity = lineMapper.domainToEntity(lineCreateDomain);
        return lineRepository.save(lineJpaEntity).getId();
    }

    @Override
    public Optional<LineDomain> loadLine(Long createdLineId) {
        return lineRepository.findById(createdLineId).map(lineJpaEntity -> {
            Station upStation = stationRepository.findById(lineJpaEntity.getUpStationId().getId())
                .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 상행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getUpStationId().getId())));

            Station downStation = stationRepository.findById(lineJpaEntity.getDownStationId().getId())
                .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 하행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getDownStationId().getId())));

            return lineMapper.entityToDomain(lineJpaEntity, upStation, downStation);
        });
    }

    @Override
    public List<LineDomain> loadLines() {
        return lineRepository.findAll().stream().map(lineJpaEntity -> {
            Station upStation = stationRepository.findById(lineJpaEntity.getUpStationId().getId())
                .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 상행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getUpStationId().getId())));

            Station downStation = stationRepository.findById(lineJpaEntity.getDownStationId().getId())
                .orElseThrow(() -> new NotFoundStationException(String.format("해당하는 Line 에 하행 Station 을 찾을 수 없습니다. Requested LineId: %d StationId: %d", lineJpaEntity.getId(), lineJpaEntity.getDownStationId().getId())));

            return lineMapper.entityToDomain(lineJpaEntity, upStation, downStation);
        }).collect(Collectors.toList());
    }

}
