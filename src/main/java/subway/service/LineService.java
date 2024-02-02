package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.service.dto.LineDto;
import subway.service.dto.SaveLineDto;
import subway.service.dto.StationDto;
import subway.service.dto.UpdateLineDto;

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
    public LineDto saveLine(SaveLineDto saveLineDto) {
        Station upStation = stationService.findStationById(saveLineDto.getUpStationId());
        Station downStation = stationService.findStationById(saveLineDto.getDownStationId());
        Line line = lineRepository.save(Line.create(
                saveLineDto.getName(),
                saveLineDto.getColor()
        ));
        Section section = Section.create(upStation, downStation, line, saveLineDto.getDistance());
        line.addSection(section);
        return this.createLineDto(line);
    }

    public List<LineDto> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineDto).collect(Collectors.toList());
    }

    public LineDto getLineByIdOrFail(Long id) {
        Line line = this.findLineByIdOrFail(id);
        return this.createLineDto(line);
    }

    @Transactional
    public void updateLine(UpdateLineDto updateLineDto) {
        Line line = this.findLineByIdOrFail(updateLineDto.getTargetId());
        line.update(updateLineDto.getName(), updateLineDto.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findLineByIdOrFail(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private LineDto createLineDto(Line line) {
        return new LineDto(
                line.getId(),
                line.getName(),
                line.getColor(),
                createStationDto(line.getAllStations())
        );
    }

    private List<StationDto> createStationDto(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
