package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import subway.Station.Station;
import subway.Station.StationRepository;
import subway.Station.StationResponse;
import subway.code.UseStatus;

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
        return lineRepository.findAllByUseStatus(UseStatus.Y).stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        final Line line = lineRepository.findByIdAndUseStatus(id, UseStatus.Y)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 지하철 노선입니다."));

        return createLineResponse(line);
    }

    @Transactional
    public LineResponse modifyLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.getReferenceById(id);
        Station upStation = stationRepository.getById(lineRequest.getUpStationId());
        Station downStation = stationRepository.getById(lineRequest.getDownStationId());

        line.modifyLine(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());

        return createLineResponse(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineRepository.getById(id);
        line.delete();
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
