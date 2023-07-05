package subway.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    @Autowired
    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    protected LineService() {

    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) throws Exception {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(
                () -> new Exception("check up-station")
        );

        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(
                () -> new Exception("check down-station")
        );

        Line line = Line.builder().name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();

        lineRepository.save(line);
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getUpStation(), line.getDownStation(), line.getDistance());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);

        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());

        lineRepository.save(line);
    }
}
