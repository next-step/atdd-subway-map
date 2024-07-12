package subway.domain.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.line.Line;
import subway.domain.entity.station.Station;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LineCommander {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Long createLine(LineCommand.CreateLine command) {
        verifyStationExist(command.getUpStationId(), command.getDownStationId());
        Line line = lineRepository.save(Line.init(command));
        return line.getId();
    }

    @Transactional
    public void updateLine(LineCommand.UpdateLine command) {
        Line line = lineRepository.findByIdOrThrow(command.getId());
        line.update(command);
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = lineRepository.findByIdOrThrow(id);
        lineRepository.delete(line);
    }

    private void verifyStationExist(Long upStationId, Long downStationId) {
        this.stationRepository.findByIdOrThrow(upStationId);
        this.stationRepository.findByIdOrThrow(downStationId);
    }
}
