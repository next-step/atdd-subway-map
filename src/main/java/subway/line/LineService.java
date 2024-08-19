package subway.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.section.Section;
import subway.section.SectionRepository;
import subway.section.Sections;
import subway.station.Station;
import subway.station.StationRepository;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(IllegalArgumentException::new);
        Section newSection = sectionRepository.save(new Section(upStation, downStation, lineRequest.getDistance()));
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), new Sections(newSection)));
        return new LineResponse(line);
    }

    public List<LineResponse> getAllLines() {
        List<Line> allLines = lineRepository.findAll();
        return allLines.stream()
                       .map(LineResponse::new)
                       .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return new LineResponse(line);
    }

    public void updateLine(LineRequest lineRequest, Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(lineRequest);
        lineRepository.save(line);
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        lineRepository.delete(line);
    }
}
