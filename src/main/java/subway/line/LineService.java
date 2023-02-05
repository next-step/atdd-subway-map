package subway.line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.StationsDto;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.dto.StationResponse;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            lineRequest.getUpStationId(),
            lineRequest.getDownStationId(),
            lineRequest.getDistance())
        );
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
        Station upStation = stationRepository.findById(line.getUpStationId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));
        Station downStation = stationRepository.findById(line.getDownStationId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));

        List<StationResponse> stationResponse = new ArrayList<>();
        stationResponse.add(new StationResponse(upStation.getId(), upStation.getName()));
        stationResponse.add(new StationResponse(downStation.getId(), downStation.getName()));
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            new StationsDto(stationResponse)
        );
    }
}
