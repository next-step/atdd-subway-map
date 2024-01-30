package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        List<Station> stations = new ArrayList<>();
        stations.add(stationRepository.findById(lineRequest.getUpStationId()).orElseThrow());
        stations.add(stationRepository.findById(lineRequest.getDownStationId()).orElseThrow());
        Line line = lineRepository.save(new Line(lineRequest));
        return createLineResponse(line, stations);
    }

    public List<LineResponse> findAllLines() {
        List<LineResponse> lineResponses = new ArrayList<>();

        List<Line> lines = lineRepository.findAll();
        for(Line line : lines) {
            List<Station> stations = new ArrayList<>();
            stations.add(stationRepository.findById(line.getUpStationId()).orElseThrow());
            stations.add(stationRepository.findById(line.getDownStationId()).orElseThrow());

            lineResponses.add(createLineResponse(line, stations));
        }

        return lineResponses;
    }

    private LineResponse createLineResponse(Line line, List<Station> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations
        );
    }

}
