package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;
import subway.entity.StationLine;
import subway.entity.StationLineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository stationLineRepository;

    public StationLineService(StationLineRepository stationLineRepository) {
        this.stationLineRepository = stationLineRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest request) {
        return convertToResponse(stationLineRepository.save(convertToEntity(request)));
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private StationLine convertToEntity(StationLineRequest request) {
        return new StationLine(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDownStationId());
    }

    private StationLineResponse convertToResponse(StationLine stationLine) {
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stationLine.getUpStationId(),
                stationLine.getDownStationId(),
                stationLine.getDownStationId());
    }
}
