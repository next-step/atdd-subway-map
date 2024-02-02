package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;
import subway.entity.StationLine;
import subway.entity.StationLineRepository;
import subway.entity.StationSection;
import subway.entity.StationSectionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository stationLineRepository;

    private final StationSectionRepository stationSectionRepository;

    public StationLineService(StationLineRepository stationLineRepository,
                              StationSectionRepository stationSectionRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationSectionRepository = stationSectionRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest request) {
        StationLine stationLine = stationLineRepository.save(convertToStationLineEntity(request));
        stationSectionRepository.save(convertToStationSectionEntity(stationLine));
        return convertToResponse(stationLine);
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public StationLineResponse findStationLineById(Long stationLineId) {
        return convertToResponse(stationLineRepository.findById(stationLineId)
                .orElseThrow(EntityNotFoundException::new)); // TODO: Throw Custom Exception?
    }

    @Transactional
    public void updateStationLine(Long stationLineId, StationLineRequest request) {
        StationLine stationLine = stationLineRepository.findById(stationLineId)
                .orElseThrow(EntityNotFoundException::new);
        convertToResponse(stationLineRepository.save(updateStationLine(request, stationLine)));
    }

    @Transactional
    public void deleteStationLine(Long stationLineId) {
        stationLineRepository.deleteById(stationLineId);
    }

    private StationLine updateStationLine(StationLineRequest request, StationLine stationLine) {
        return stationLine.update(
                request.getName(),
                request.getColor()
        );
    }

    private StationLine convertToStationLineEntity(StationLineRequest request) {
        return new StationLine(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
    }

    private StationLineResponse convertToResponse(StationLine stationLine) {
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stationLine.getUpStationId(),
                stationLine.getDownStationId(),
                stationLine.getDistance());
    }

    private static StationSection convertToStationSectionEntity(StationLine stationLine) {
        return new StationSection(
                stationLine.getUpStationId(),
                stationLine.getDownStationId(),
                stationLine.getDistance(),
                stationLine);
    }
}
