package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.*;
import subway.linesection.LineSection;
import subway.linesection.LineSectionService;
import subway.station.Station;
import subway.station.StationService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LineFacadeService {

    private final LineService lineService;
    private final LineSectionService lineSectionService;
    private final StationService stationService;
    private final LineConverter lineConverter;

    public LineFacadeService(LineService lineService, LineSectionService lineSectionService, StationService stationService, LineConverter lineConverter) {
        this.lineService = lineService;
        this.lineSectionService = lineSectionService;
        this.stationService = stationService;
        this.lineConverter = lineConverter;
    }

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.getStation(request.getUpStationId());
        Station downStation = stationService.getStation(request.getDownStationId());
        Line line = lineService.create(request);
        lineSectionService.createSection(line.getId(), upStation.getId(), downStation.getId(), request.getDistance());
        return lineConverter.convert(line, Arrays.asList(upStation, downStation));
    }

    public LineResponse getById(Long id) {
        Line line = lineService.getLine(id);
        List<Long> stationIds = findStationIdsByLineId(id);
        List<Station> stations = stationService.findByIds(stationIds);
        return lineConverter.convert(line, stations);
    }

    @Transactional
    public void update(Long id, LineRequest request) {
        stationService.getStation(request.getUpStationId());
        stationService.getStation(request.getDownStationId());
        lineService.update(id, request);
    }

    @Transactional
    public void delete(Long id) {
        lineService.delete(id);
    }

    public List<LineResponse> getList() {
        List<Line> list = lineService.getList();

        Map<Long, List<Long>> lineStationMap = list.stream()
                .map(e -> getLineSectionsByLineId(e.getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        LineSection::getLineId,
                        Collectors.mapping(LineSection::getCurrentStationId, Collectors.toList())
                ));

        List<Long> stationIds = list.stream()
                .map(e -> lineStationMap.get(e.getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Map<Long, Station> stationMap = stationService.findByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        return list.stream()
                .map(line -> {
                    List<Station> collect = lineStationMap.get(line.getId())
                            .stream()
                            .map(stationMap::get)
                            .collect(Collectors.toList());
                    return lineConverter.convert(line, collect);
                })
                .collect(Collectors.toList());
    }

    private List<Long> findStationIdsByLineId(Long lindId) {
        return getLineSectionsByLineId(lindId)
                .stream()
                .map(LineSection::getCurrentStationId)
                .collect(Collectors.toList());
    }

    private List<LineSection> getLineSectionsByLineId(Long lindId) {
        List<LineSection> lineSections = lineSectionService.findByLineId(lindId);
        return lineSections;
    }
}
