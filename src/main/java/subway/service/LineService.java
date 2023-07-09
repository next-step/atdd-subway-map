package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineRepository;
import subway.model.section.Section;
import subway.model.section.SectionRepository;
import subway.model.station.Station;
import subway.model.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineSaveRequest lineSaveRequest) {
        Station upStation = stationRepository.findById(lineSaveRequest.getUpStationId())
                                             .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));
        Station downStation = stationRepository.findById(lineSaveRequest.getDownStationId())
                                               .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));

        Section section = Section.builder()
                                 .upStation(upStation)
                                 .downStation(downStation)
                                 .distance(lineSaveRequest.getDistance())
                                 .build();

        Line newLine = Line.builder()
                           .name(lineSaveRequest.getName())
                           .color(lineSaveRequest.getColor())
                           .sections(List.of(section))
                           .distance(lineSaveRequest.getDistance())
                           .build();

        section.setLine(newLine);

        Line savedLine = lineRepository.save(newLine);

        return LineResponse.from(savedLine);
    }

    @Transactional
    public LineResponse modifyLine(Long lineId, LineModifyRequest lineModifyRequest) {
        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));

        if (lineModifyRequest.getName() != null) {
            line.setName(lineModifyRequest.getName());
        }

        if (lineModifyRequest.getColor() != null) {
            line.setColor(lineModifyRequest.getColor());
        }

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));
        return LineResponse.from(line);
    }
}
