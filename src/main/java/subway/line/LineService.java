package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.*;
import java.util.function.Function;
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
        List<Line> list = lineRepository.findAll();
        List<Long> stationIds = list.stream()
                .map(e -> Arrays.asList(e.getUpStationId(), e.getDownStationId()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Station> stationMap = stationRepository.findByIdIn(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        return list.stream()
                .map(line -> lineConverter.convert(line, Arrays.asList(stationMap.get(line.getUpStationId()), stationMap.get(line.getDownStationId()))))
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
        getStation(request.getUpStationId());
        getStation(request.getDownStationId());
        Line line = Line.of(id, request.getName(), request.getColor(), request.getUpStationId(), request.getDownStationId(), request.getDistance());
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(getLine(id));
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("station is not existed by id > " + id));
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("line is not existed by id > " + id));
    }
}
