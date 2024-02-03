package subway.application.line;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.application.line.section.SectionRequest;
import subway.domain.SectionRegister;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.application.station.StationResponse;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseThrow(() -> new IllegalArgumentException("지하철을 찾을 수 없습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseThrow(() -> new IllegalArgumentException("지하철을 찾을 수 없습니다."));
        Line createdLine = new Line(request.getName(), request.getColor(), request.getDistance(),
            upStation, downStation);
        lineRepository.save(createdLine);
        return new LineResponse(
            createdLine.getId(),
            createdLine.getName(),
            createdLine.getColor(),
            List.of(
                new StationResponse(upStation.getId(), upStation.getName()),
                new StationResponse(downStation.getId(), downStation.getName())
            )
        );
    }

    private String newStationName(Long id) {
        return "지하철역" + id;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream().map(line -> new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stationRepository.findAllByLineId(line.getId()).stream()
                .map(station -> new StationResponse(
                    station.getId(),
                    station.getName()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<StationResponse> stations = stationRepository.findAllByLineId(id).stream()
            .map(station -> new StationResponse(
                station.getId(),
                station.getName()
            )).collect(Collectors.toList());
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations
        );
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void registSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException("노선이 존재하지 않습니다."));
        Section section = new Section(line, request.getUpStationId(), request.getDownStationId(),
            request.getDistance());
        Set<Station> stations = new HashSet<>(stationRepository.findAllByLineId(
            lineId));
        SectionRegister sectionRegister = new SectionRegister();
        sectionRegister.registSectionInLine(stations, line, section);
    }
}
