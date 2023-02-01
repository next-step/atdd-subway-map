package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.exception.*;
import subway.exception.statusmessage.SubwayExceptionStatus;

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
        return findLineById(id);
    }

    @Transactional
    public void updateLine(Long id, String color, String name) {
        Line line = findLineById(id);
        line.changeName(name);
        line.changeColor(color);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Section addSection(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStation(upStationId);
        Station downStation = stationService.findStation(downStationId);

        Section saveSection = new Section(line, upStation, downStation, distance);
        line.addSection(saveSection);
        return saveSection;

    }

    @Transactional
    public void deleteSection(Long lineId, Long sectionId) {
        Line line = findLineById(lineId);
        Section section = findSectionById(sectionId);
        if (line.canDeleteSection(section)) {
            line.deleteSection(section);
            return;
        }
        throw new SubwayException(SubwayExceptionStatus.SECTION_NOT_DELETE);
    }

    private Section findSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId).orElseThrow(
                () -> new SubwayException(SubwayExceptionStatus.SECTION_NOT_FOUND)
        );
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(
                () -> new SubwayException(SubwayExceptionStatus.LINE_NOT_FOUND)
        );
    }
}
