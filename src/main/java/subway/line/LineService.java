package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException());
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException());

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return line.createLineResponse();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(Line::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException())
                .createLineResponse();
    }
}
