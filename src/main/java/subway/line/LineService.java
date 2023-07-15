package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateSectionRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.UpdateLineRequest;
import subway.section.Section;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(Line.builder().name(request.getName()).color(request.getColor()).build());
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        line.addSection(section);

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::from).collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow();
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(Long lineId, CreateSectionRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        line.addSection(section);
        return LineResponse.from(line);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        Station station = stationRepository.findById(stationId).orElseThrow();
        line.deleteSection(station);
    }
}
