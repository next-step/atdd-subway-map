package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationRepository;
import subway.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StationLineService
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Transactional(readOnly = true)
@Service
public class StationLineService {

    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationLineResponse createStationLines(StationLineCreateRequest stationLineCreateRequest) {
        StationLine stationLine = stationLineRepository.save(stationLineCreateRequest.toEntity());
        return getStationsInLine(stationLine);
    }

    public List<StationLineResponse> findAllStations() {
        List<StationLine> stationLines = stationLineRepository.findAll();
        List<StationLineResponse> stationLineResponses = new ArrayList<>();
        for (StationLine stationLine : stationLines) {
            List<Station> stations = stationRepository.findAllByIdIn(List.of(stationLine.getDownStationId(), stationLine.getUpStationId()));
            stationLineResponses.add(StationLineResponse.from(
                    stationLine,
                    stations.stream()
                            .map(StationResponse::from)
                            .collect(Collectors.toList())
            ));
        }

        return stationLineResponses;
    }

    public StationLineResponse findOneStation(Long id) {
        StationLine stationLine = stationLineRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return getStationsInLine(stationLine);
    }

    private StationLineResponse getStationsInLine(StationLine stationLine) {
        List<Station> stations = stationRepository.findAllByIdIn(List.of(stationLine.getDownStationId(), stationLine.getUpStationId()));
        return StationLineResponse.from(
                stationLine,
                stations.stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteStationLineById(Long id) {
        stationLineRepository.deleteById(id);
    }
}
