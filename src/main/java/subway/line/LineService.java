package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;
    private final LineConverter lineConverter;

    public LineService(LineRepository lineRepository, LineConverter lineConverter, StationService stationService) {
        this.lineRepository = lineRepository;
        this.lineConverter = lineConverter;
        this.stationService = stationService;

    }

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.getStation(request.getUpStationId());
        Station downStation = stationService.getStation(request.getDownStationId());
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

        Map<Long, Station> stationMap = stationService.findByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        return list.stream()
                .map(line -> lineConverter.convert(line, Arrays.asList(stationMap.get(line.getUpStationId()), stationMap.get(line.getDownStationId()))))
                .collect(Collectors.toList());
    }

    public LineResponse getById(Long id) {
        Line line = getLine(id);
        List<Station> stations = stationService.findByIds(Arrays.asList(line.getUpStationId(), line.getDownStationId()));
        return lineConverter.convert(line, stations);
    }


    @Transactional
    public void update(Long id, LineRequest request) {
        if (!lineRepository.existsById(id))
            throw new NoSuchElementException("line is not existed by id > " + id);
        stationService.getStation(request.getUpStationId());
        stationService.getStation(request.getDownStationId());
        Line line = Line.of(id, request.getName(), request.getColor(), request.getUpStationId(), request.getDownStationId(), request.getDistance());
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(getLine(id));
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("line is not existed by id > " + id));
    }
}
