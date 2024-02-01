package subway;

import static subway.domain.LineResponse.createLineResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import subway.domain.Line;
import subway.domain.LineRequest;
import subway.domain.LineResponse;
import subway.domain.Station;
import subway.domain.Stations;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        Stations stations = Stations.of(upStation, downStation);
        Line line = Line.of(lineRequest, stations);
        stations.addLine(line);
        lineRepository.save(line);
        return createLineResponse(line);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new NoStationException(stationId + "에 해당하는 지하철 역이 존재하지 않습니다."));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::createLineResponse)
                             .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoStationException(id + "에 해당하는 지하철 역이 존재하지 않습니다."));
        return LineResponse.createLineResponse(line);
    }
}