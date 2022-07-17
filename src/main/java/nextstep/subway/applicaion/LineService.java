package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NoSuchLineException;
import nextstep.subway.exception.NoSuchStationException;
import nextstep.subway.repository.DistanceRepository;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final DistanceRepository distanceRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineResponse saveLine(LineCreationRequest lineRequest) {
        Pair<Station, Station> stationPair = fetchStations(lineRequest);
        Pair<Distance, Distance> distancePair = createDistances(lineRequest);
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        createSections(stationPair, distancePair, line);
        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchLineException(lineId));
        return LineResponse.from(line);
    }

    @Transactional
    public void modifyLine(Long lineId, LineModificationRequest lineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchLineException(lineId));
        line.updateNameAndColor(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Pair<Station, Station> fetchStations(LineCreationRequest lineRequest) {
        Map<Long, Station> stationMap = stationRepository.findAllById(lineRequest.getStationIds())
                .stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
        validateStationsBeforeCreateLine(stationMap, lineRequest);
        return Pair.of(stationMap.get(lineRequest.getUpStationId()), stationMap.get(lineRequest.getDownStationId()));
    }

    private void validateStationsBeforeCreateLine(Map<Long, Station> stationMap, LineCreationRequest lineRequest) {
        if (!stationMap.containsKey(lineRequest.getUpStationId())) {
            throw new NoSuchStationException(lineRequest.getUpStationId());
        }
        if (!stationMap.containsKey(lineRequest.getDownStationId())) {
            throw new NoSuchStationException(lineRequest.getDownStationId());
        }
    }

    private Pair<Distance, Distance> createDistances(LineCreationRequest lineRequest) {
        Distance upStationDistance = new Distance(0, lineRequest.getDistance());
        Distance downStationDistance = new Distance(lineRequest.getDistance(), 0);
        distanceRepository.saveAll(List.of(upStationDistance, downStationDistance));
        return Pair.of(upStationDistance, downStationDistance);
    }

    private void createSections(Pair<Station, Station> stationPair, Pair<Distance, Distance> distancePair, Line line) {
        Section upStationSection  = new Section(stationPair.getFirst(), line, distancePair.getFirst());
        Section downStationSection  = new Section(stationPair.getSecond(), line, distancePair.getSecond());
        sectionRepository.saveAll(List.of(upStationSection, downStationSection));
        line.addSections(upStationSection, downStationSection);
    }

}
