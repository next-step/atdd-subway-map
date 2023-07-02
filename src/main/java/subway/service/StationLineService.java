package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.request.StationLineCreateRequest;
import subway.controller.request.StationLineModifyRequest;
import subway.controller.resonse.StationLineResponse;
import subway.controller.resonse.StationResponse;
import subway.domain.StationLine;
import subway.repository.StationLineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository stationLineRepository;
    private final StationService stationService;

    public StationLineService(StationLineRepository stationLineRepository, StationService stationService) {
        this.stationLineRepository = stationLineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineCreateRequest stationLineCreateRequest) {
        StationLine stationLine = StationLine.create(stationLineCreateRequest.getLineName(), stationLineCreateRequest.getColor(), stationLineCreateRequest.getUpStationId(), stationLineCreateRequest.getDownStationId(), stationLineCreateRequest.getDistance());

        StationLine satedStationLine = stationLineRepository.save(stationLine);
        StationResponse upStation = stationService.findStation(stationLine.getUpStationId());
        StationResponse downStation = stationService.findStation(stationLine.getDownStationId());

        return createStationLineResponse(satedStationLine, upStation, downStation);
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(stationLine -> {
                    StationResponse upStation = stationService.findStation(stationLine.getUpStationId());
                    StationResponse downStation = stationService.findStation(stationLine.getDownStationId());

                    return createStationLineResponse(stationLine, upStation, downStation);
                })
                .collect(Collectors.toList());
    }

    public StationLineResponse findStationLine(Long id) {
        StationLine stationLine = requireGetById(id);
        StationResponse upStation = stationService.findStation(stationLine.getUpStationId());
        StationResponse downStation = stationService.findStation(stationLine.getDownStationId());

        return createStationLineResponse(stationLine, upStation, downStation);
    }

    @Transactional
    public void modifyStationLine(Long id, StationLineModifyRequest stationLineModifyRequest) {
        StationLine stationLine = requireGetById(id);
        stationLine.modify(stationLineModifyRequest.getName(), stationLineModifyRequest.getColor());
    }

    private StationLine requireGetById(Long id) {
        return stationLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("not found station line : %d", id)));
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine, StationResponse upStationResponse, StationResponse downStationResponse) {
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                List.of(upStationResponse, downStationResponse)
        );
    }
}
