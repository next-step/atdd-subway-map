package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station.Station;
import subway.Station.StationRepository;
import subway.Station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.getById(lineRequest.getUpStationId());
        Station downStation = stationRepository.getById(lineRequest.getDownStationId());

        Line line = lineRepository.save(
                new Line(
                        lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()
                )
        );

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return createLineResponse(lineRepository.findById(id).get());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        new StationResponse(line.getUpStation().getId(), line.getUpStation().getName()),
                        new StationResponse(line.getDownStation().getId(), line.getDownStation().getName())
                ),
                line.getDistance()
        );
    }

}
