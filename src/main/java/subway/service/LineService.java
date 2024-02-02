package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.service.dto.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineDto saveLine(SaveLineCommand command) {
        Station upStation = stationService.findStationById(command.getUpStationId());
        Station downStation = stationService.findStationById(command.getDownStationId());
        Line line = lineRepository.save(Line.create(
                command.getName(),
                command.getColor(),
                upStation,
                downStation,
                command.getDistance()
        ));
        return LineDto.from(line);
    }

    public List<LineDto> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineDto::from).collect(Collectors.toList());
    }

    public LineDto getLineByIdOrFail(Long id) {
        Line line = this.findLineByIdOrFail(id);
        return LineDto.from(line);
    }

    @Transactional
    public void updateLine(UpdateLineCommand command) {
        Line line = this.findLineByIdOrFail(command.getTargetId());
        line.update(command.getName(), command.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineSectionDto saveLineSection(SaveLineSectionCommand command) {
        Station upStation = stationService.findStationById(command.getUpStationId());
        Station downStation = stationService.findStationById(command.getDownStationId());

        Line line = findLineByIdOrFail(command.getLineId());

        Section section = Section.create(upStation, downStation, line, command.getDistance());
        line.addSection(section);

        return LineSectionDto.from(section);
    }

    @Transactional
    public void deleteLineSection(Long lineId, Long stationId) {
        Line line = findLineByIdOrFail(lineId);
        line.deleteStation(stationId);
    }

    private Line findLineByIdOrFail(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
