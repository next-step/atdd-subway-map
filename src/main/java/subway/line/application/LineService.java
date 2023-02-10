package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationResponse;

@Service
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
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));

        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            upStation,
            downStation,
            lineRequest.getDistance())
        );
        line.addStation(upStation);
        line.addStation(downStation);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return createLineResponse(
            lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.LINE_NOT_FOUND))
        );
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.LINE_NOT_FOUND));

        line.changeLine(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            List.of(new StationResponse(line.getUpStation().getId(), line.getUpStation().getName()),
                new StationResponse(line.getDownStation().getId(), line.getDownStation().getName()))
        );
    }
}
