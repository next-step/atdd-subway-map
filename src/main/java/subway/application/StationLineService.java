package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationResponse;
import subway.entity.StationLine;
import subway.entity.StationLineRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository stationLineRepository;

    public StationLineService(StationLineRepository stationLineRepository) {
        this.stationLineRepository = stationLineRepository;
    }

    @Transactional
    public StationLine saveStationLine(Map<String, String> stationLineMap) {
        return stationLineRepository.save(
                new StationLine(
                        stationLineMap.get("name"),
                        stationLineMap.get("color"),
                        (long) Integer.parseInt(stationLineMap.get("upStationId")),
                        (long) Integer.parseInt(stationLineMap.get("downStationId")),
                        (long) Integer.parseInt(stationLineMap.get("distance"))));
    }

    public List<StationLine> findAllStationLines() {
        return stationLineRepository.findAll();
    }
}
