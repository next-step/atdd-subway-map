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
    private final StationRepository stationRepository;
    private final LineConverter lineConverter;

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

    public List<LineResponse> getList() {
        return lineRepository.findAll()
                .stream()
                .map(line -> {
                    List<Station> stations = stationRepository.findByIdIn(Arrays.asList(line.getUpStationId(), line.getDownStationId()));
                    return lineConverter.convert(line, stations);
                })
                .collect(Collectors.toList());
    }

    public LineResponse getById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("line is not existed by id > " + id));
        List<Station> stations = stationRepository.findByIdIn(Arrays.asList(line.getUpStationId(), line.getDownStationId()));
        return lineConverter.convert(line, stations);
    }
}
