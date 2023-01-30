package subway.stationline;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;
import subway.stationline.dto.StationLineCreateRequest;
import subway.stationline.dto.StationLineCreateResponse;
import subway.stationline.dto.StationLineReadListResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationLineCreateResponse createStationLine(StationLineCreateRequest request) {
        StationLine stationLine = stationLineRepository.save(new StationLine(request));
        Station upStation = stationRepository.findById(stationLine.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(stationLine.getDownStationId()).orElseThrow();
        return new StationLineCreateResponse(stationLine, List.of(upStation, downStation));
    }

    public List<StationLineReadListResponse> readStationLineList() {
        List<StationLine> stationLines = stationLineRepository.findAll();
        List<StationLineReadListResponse> response = new ArrayList<>();
        for (StationLine stationLine : stationLines) {
            Station upStation = stationRepository.findById(stationLine.getUpStationId()).orElseThrow();
            Station downStation = stationRepository.findById(stationLine.getDownStationId()).orElseThrow();
            response.add(new StationLineReadListResponse(stationLine, List.of(upStation, downStation)));
        }
        return response;
    }


}
