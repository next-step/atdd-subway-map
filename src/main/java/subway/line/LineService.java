package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.LineNotFoundException;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance(), upStation, downStation);
        Line newLine = lineRepository.save(line);
        return new LineResponse(newLine.getId(), newLine.getName(), newLine.getColor(),
                makeStationResponses(newLine.getStations()));
    }

    @Transactional
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> {
            return new LineResponse(line.getId(), line.getName(), line.getColor(),
                    makeStationResponses(line.getStations()));
        }).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), makeStationResponses(line.getStations()));
    }

    public Line findOneById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        line.changeNameAndColor(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        line.breakAllStationRelation();
        lineRepository.deleteById(id);
    }

    private static List<StationResponse> makeStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
