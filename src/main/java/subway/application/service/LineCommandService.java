package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineCommandUseCase;
import subway.application.service.output.LineCommandRepository;
import subway.application.service.input.LineLoadUseCase;
import subway.domain.*;
import subway.infrastructor.repository.StationRepository;

@Service
@Transactional
class LineCommandService implements LineCommandUseCase {

    private final LineCommandRepository lineCommandRepository;
    private final LineLoadUseCase lineLoadUseCase;
    private final StationRepository stationRepository;

    LineCommandService(LineCommandRepository lineCommandRepository, LineLoadUseCase lineLoadUseCase, StationRepository stationRepository) {
        this.lineCommandRepository = lineCommandRepository;
        this.lineLoadUseCase = lineLoadUseCase;
        this.stationRepository = stationRepository;
    }

    @Override
    public Long createLine(LineCreateDto lineCreateDto) {
        Station upStation = stationRepository.findById(lineCreateDto.getUpStationId())
            .map(stationJpaEntity -> new Station(stationJpaEntity.getId(), stationJpaEntity.getName()))
            .orElseThrow(NotFoundStationException::new);
        Station downStation = stationRepository.findById(lineCreateDto.getDownStationId())
            .map(stationJpaEntity -> new Station(stationJpaEntity.getId(), stationJpaEntity.getName()))
            .orElseThrow(NotFoundStationException::new);

        Line line = Line.withoutId(lineCreateDto.getName(), lineCreateDto.getColor(), upStation, downStation, lineCreateDto.getDistance());
        return lineCommandRepository.createLine(line);
    }

    @Override
    public void updateLine(LineUpdateDto lineUpdateDto) {
        Line line = lineLoadUseCase.loadLine(lineUpdateDto.getLineId());
        line.updateLine(lineUpdateDto.getName(), lineUpdateDto.getColor());
        lineCommandRepository.updateLine(line);
    }

    @Override
    public void deleteLine(Long lineId) {
        lineCommandRepository.deleteLine(lineId);
    }

}
