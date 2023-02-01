package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Line saveLine(Line line, Long upStationId, Long downStationId) {

        Station upStation = stationService.findStation(upStationId);
        Station downStation = stationService.findStation(downStationId);

        line.changeFirstAndLastStation(upStation, downStation);

        return lineRepository.save(line);

    }

    public List<Line> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, String color, String name) {
        Line line = lineRepository.findById(id).orElseThrow(StationNotFoundException::new);
        line.changeName(name);
        line.changeColor(color);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Section addSection(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        Station upStation = stationService.findStation(upStationId);
        Station downStation = stationService.findStation(downStationId);

        if (!line.canAddSection(upStation)) {
            throw new CannotAddSectionException();
        }

        Section saveSection = new Section(line, upStation, downStation, distance);
        line.addSection(saveSection);
        return saveSection;

    }

    @Transactional
    public void deleteSection(Long lineId, Long sectionId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        Section section = sectionRepository.findById(sectionId).orElseThrow(SectionNotFoundException::new);
        if (line.canDeleteSection(section)) {
            line.deleteSection(section);
            return;
        }
        throw new CannotDeleteSectionException();
    }
}
