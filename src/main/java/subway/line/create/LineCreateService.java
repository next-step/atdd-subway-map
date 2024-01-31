package subway.line.create;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

@Transactional
@Service
public class LineCreateService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineCreateService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineCreatedResponse createLine(LineCreateRequest request) {
        Station upStation = findStationByStationId(request.getUpStationId());
        Station downStation = findStationByStationId(request.getDownStationId());
        Line line = getSavedLine(request.getName(), request.getColor());
        saveSection(line, upStation, downStation, request.getDistance());
        return mapToResponse(line, upStation, downStation);
    }

    private Station findStationByStationId(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다. stationId: " + stationId));
    }

    private Line getSavedLine(String name, String color) {
        Line line = new Line(name, color);
        return lineRepository.save(line);
    }

    private void saveSection(Line line, Station upStation, Station downStation, Long distance) {
        Section section = new Section(line.getId(), upStation.getId(), downStation.getId(), distance);
        sectionRepository.save(section);
    }

    private LineCreatedResponse mapToResponse(Line savedLine, Station upStation, Station downStation) {
        LineCreatedStationResponse upStationResponse = new LineCreatedStationResponse(upStation.getId(), upStation.getName());
        LineCreatedStationResponse downStationResponse = new LineCreatedStationResponse(downStation.getId(), downStation.getName());
        return new LineCreatedResponse(
                savedLine.getId(),
                savedLine.getName(),
                savedLine.getColor(),
                List.of(upStationResponse, downStationResponse)
        );
    }
}
