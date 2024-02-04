package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;
import subway.dto.StationResponse;
import subway.entity.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationRepository stationRepository;

    private final StationLineRepository stationLineRepository;

    private final StationSectionRepository stationSectionRepository;

    public StationLineService(StationRepository stationRepository, StationLineRepository stationLineRepository,
                              StationSectionRepository stationSectionRepository) {
        this.stationRepository = stationRepository;
        this.stationLineRepository = stationLineRepository;
        this.stationSectionRepository = stationSectionRepository;
    }

    @Transactional
    public StationLineResponse createStationLine(StationLineRequest request) {
        StationLine stationLine = stationLineRepository.save(convertToStationLineEntity(request));
        StationSection createdStationSection = stationSectionRepository.save(convertToSectionEntity(stationLine));
        return convertToLineResponse(stationLine.setStationSection(createdStationSection));
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(this::convertToLineResponse)
                .collect(Collectors.toList());
    }

    public StationLineResponse findStationLineById(Long stationLineId) {
        return convertToLineResponse(stationLineRepository.findById(stationLineId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public void updateStationLine(Long stationLineId, StationLineRequest request) {
        StationLine stationLine = stationLineRepository.findById(stationLineId)
                .orElseThrow(EntityNotFoundException::new);
        convertToLineResponse(stationLineRepository.save(updateStationLine(request, stationLine)));
    }

    @Transactional
    public void deleteStationLine(Long stationLineId) {
        stationLineRepository.deleteById(stationLineId);
    }

    private static Long findFirstStation(List<StationSection> stationSections) {
        return stationSections.get(0).getUpStationId();
    }

    private StationResponse findStation(Long stationId) {
        return convertToStationResponse(stationRepository.findById(stationId).orElseThrow(IllegalArgumentException::new));
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

    private StationLineResponse convertToLineResponse(StationLine stationLine) {
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                convertToStationResponses(stationLine.getSections()));
    }

    private StationSection convertToSectionEntity(StationLine stationLine) {
        return new StationSection(
                stationLine.getUpStationId(),
                stationLine.getDownStationId(),
                stationLine.getDistance(),
                stationLine);
    }

    private StationResponse convertToStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    private List<StationResponse> convertToStationResponses(List<StationSection> stationSections) {
        List<StationResponse> stationResponses = new ArrayList<>();
        stationResponses.add(findStation(findFirstStation(stationSections)));
        stationSections.forEach(stationSection -> stationResponses.add(findStation(stationSection.getDownStationId())));

        return stationResponses;
    }
}
