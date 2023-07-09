package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineService;
import subway.model.section.Section;
import subway.model.station.Station;
import subway.model.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineCompositeService {

    private final LineService lineService;
    private final StationRepository stationRepository;

    public LineCompositeService(LineService lineService, StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
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

        Line savedLine = lineService.save(newLine);

        return LineResponse.from(savedLine);
    }

    @Transactional
    public LineResponse modifyLine(Long lineId, LineModifyRequest lineModifyRequest) {

        Line line = lineService.findById(lineId);

        if (lineModifyRequest.getName() != null) {
            line.setName(lineModifyRequest.getName());
        }

        if (lineModifyRequest.getColor() != null) {
            line.setColor(lineModifyRequest.getColor());
        }

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineService.findAll()
                          .stream()
                          .map(LineResponse::from)
                          .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineService.deleteById(id);
    }

    public LineResponse findById(Long id) {
        Line line = lineService.findById(id);
        return LineResponse.from(line);
    }
}
