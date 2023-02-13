package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId()).toEntity();
        Station downStation = stationService.findStationById(request.getDownStationId()).toEntity();

        Line line = lineRepository.save(request.toEntity(upStation, downStation));

        sectionRepository.save(new Section(line, upStation, downStation,
            request.getDistance()));

        return LineResponse.of(
            line.getId(),
            line.getName(),
            line.getColor(),
            List.of(line.getUpStation(), line.getUpStation()), line.getUpStation(),
            line.getDownStation());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
            .stream().map(line -> LineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                findAllStations(line), line.getUpStation(), line.getDownStation())
            )
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = getLineById(id);
        List<Station> stations = findAllStations(line);

        return LineResponse.of(line.getId(), line.getName(), line.getColor(), stations,
            line.getUpStation(), line.getDownStation());
    }

    private List<Station> findAllStations(Line line){
        List<Station> stations = line.getSections().stream().map(Section::getUpStation).collect(
            Collectors.toList());
        stations.add(line.getDownStation());
        return stations;
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    @Transactional(readOnly = true)
    public Line getLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
