package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.exception.*;
import subway.domain.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationService stationService;
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

        line.setUpStation(upStation);
        line.setDownStation(downStation);

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

        Section saveSection = new Section(upStation, downStation, distance);

        if (line.canAddSection(upStation)) {
            saveSection = sectionRepository.save(saveSection);
            line.addSection(saveSection);
            return saveSection;
        }
        throw new CannotAddSectionException();
    }

    @Transactional
    public void deleteSection(Long lineId, Long sectionId){
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        Section section = sectionRepository.findById(sectionId).orElseThrow(SectionNotFoundException::new);
        if(line.canDeleteSection(section)){
            line.deleteSection(section);
            return;
        }
        throw new CannotDeleteSectionException();
    }
}
