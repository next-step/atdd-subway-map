package subway.line;

import org.springframework.stereotype.Service;
import subway.error.ErrorMessage;
import subway.station.Station;
import subway.station.StationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
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
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Line newLine = Line.createLine(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance());
        Line saveLine = lineRepository.save(newLine);
        return LineResponse.fromLine(saveLine, upStation, downStation);
    }


    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Optional<Line> resLine = lineRepository.findById(id);
        return createLineResponse(resLine.orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_LINE.message))
        );
    }

    @Transactional
    public LineResponse updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        Optional<Line> resLine = lineRepository.findById(id);
        lineRepository.delete(resLine.orElseThrow());
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        return LineResponse.fromLine(line, upStation, downStation);
    }

}
