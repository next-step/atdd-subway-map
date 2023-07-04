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
        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());
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
        Line line = getLine(id);
        List<Station> stations = stationRepository.findByIdIn(Arrays.asList(line.getUpStationId(), line.getDownStationId()));
        return lineConverter.convert(line, stations);
    }


    @Transactional
    public void update(Long id, LineRequest request) {
        if (!lineRepository.existsById(id))
            throw new NoSuchElementException("line is not existed by id > " + id);
        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());
        Line line = Line.of(id, request.getName(), request.getColor(), request.getUpStationId(), request.getDownStationId(), request.getDistance());
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(getLine(id));
    }

    private Station getStation(Long id) {
        Station upStation = stationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("station is not existed by id > " + id));
        return upStation;
    }

    private Line getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("line is not existed by id > " + id));
        return line;
    }
}
