package subway.line;

import org.springframework.stereotype.Service;
import subway.station.Station;
import subway.station.StationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }


    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();
        Line saveLine = lineRepository.save(Line.fromLineRequest(lineRequest));
        return LineResponse.fromLine(saveLine, upStation, downStation);
    }


    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    public LineResponse findById(Long id){
        Optional<Line> resLine = lineRepository.findById(id);
        return createLineResponse(resLine.orElseThrow());
    }

    @Transactional
    public LineResponse updateLine(Long id, LineUpdateRequest lineUpdateRequest){
        Line line = lineRepository.findById(id).orElseThrow();
        line.setColor(lineUpdateRequest.getColor());
        line.setName(lineUpdateRequest.getName());
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLine(Long id){
        Optional<Line> resLine = lineRepository.findById(id);
        lineRepository.delete(resLine.orElseThrow());
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();
        return LineResponse.fromLine(line, upStation, downStation);
    }
}
