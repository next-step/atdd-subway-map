package subway.service;

import org.springframework.stereotype.Service;
import subway.dto.LineRequest;
import subway.dto.LineUpdateRequest;
import subway.exception.ErrorMessage;
import subway.exception.SubwayException;
import subway.model.Line;
import subway.model.Section;
import subway.model.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, StationService sectionService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Line create(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Line newLine = new Line.Builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();
        Line line = lineRepository.save(newLine);

        Section section = new Section.Builder()
                .lineId(line.getId())
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();
        sectionRepository.save(section);
        return line;
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayException(ErrorMessage.NOT_FOUND_SUBWAY_LINE_ID));
    }

    @Transactional
    public void update(Long id, LineUpdateRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new SubwayException(ErrorMessage.NOT_FOUND_SUBWAY_LINE_ID));
        line.update(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void save(Line line) {
        lineRepository.save(line);
    }

    @Transactional
    public void addSectionAndSave(Long lineId, Section section) {
        Line line = findLineById(lineId);
        line.addSection(section);
        lineRepository.save(line);
    }
}
