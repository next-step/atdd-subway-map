package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final LineConverter lineConverter;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, LineConverter lineConverter, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.lineConverter = lineConverter;
        this.stationRepository = stationRepository;

    }

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new NoSuchElementException("station is not existed by id > " + request.getUpStationId()));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new NoSuchElementException("station is not existed by id > " + request.getDownStationId()));
        Line newLine = Line.of(request.getName(), request.getColor(), request.getUpStationId(), request.getDownStationId(), request.getDistance());
        lineRepository.save(newLine);
        return lineConverter.convert(newLine, Arrays.asList(upStation, downStation));
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(line -> lineConverter.convert(line, stationRepository.findByIdIn(Arrays.asList(line.getUpStationId(), line.getDownStationId()))))
                .collect(Collectors.toList());
    }
}
