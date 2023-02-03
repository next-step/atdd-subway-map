package subway.stationline;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;
import subway.stationline.dto.*;

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
        StationLine stationLine = stationLineRepository.save(request.convertToEntity());
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

    public StationLineReadResponse readStationLine(Long stationLineId) {
        StationLine stationLine = stationLineRepository.findById(stationLineId).orElseThrow();
        Station upStation = stationRepository.findById(stationLine.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(stationLine.getDownStationId()).orElseThrow();
        return new StationLineReadResponse(stationLine, List.of(upStation, downStation));
    }

    @Transactional
    public void updateStationLine(Long stationLineId, StationLineUpdateRequest request) {
        StationLine stationLine = stationLineRepository.findById(stationLineId).orElseThrow();
        stationLine.updateNameAndColor(request.convertToEntity());
    }

    @Transactional
    public void deleteStationLine(Long stationLineId) {
        stationLineRepository.deleteById(stationLineId);
    }

    @Transactional
    public StationLineExtendResponse extendStationLine(StationLineExtendRequest request) {
        return null;
    }
}
