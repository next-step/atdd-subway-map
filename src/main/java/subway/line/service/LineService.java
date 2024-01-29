package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.LineRepository;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    private final SectionRepository sectionRepository;
    private final LineDataService lineDataService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, LineDataService lineDataService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.lineDataService = lineDataService;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        Station upStation = lineDataService.findStation(request.getUpStationId());
        Station downStation = lineDataService.findStation(request.getDownStationId());

        Line line = new Line(
                request.getName(),
                request.getColor(),
                request.getDistance(),
                upStation,
                downStation
        );
        Line savedLine = lineRepository.save(line);

        Section section = Section.of(request.getDistance(), upStation, downStation, savedLine);
        sectionRepository.save(section);

        return lineDataService.mappingToLineResponse(savedLine);
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream().map(lineDataService::mappingToLineResponse).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineDataService.findLine(id);

        return lineDataService.mappingToLineResponse(line);
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineDataService.findLine(id);

        line.updateLine(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
