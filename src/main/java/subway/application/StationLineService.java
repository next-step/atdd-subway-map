package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;
import subway.dto.StationSectionResponse;
import subway.entity.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
        StationSection createdStationSection = stationSectionRepository.save(convertToStationSectionEntity(stationLine));
        return convertToResponse(stationLine.addStationSection(createdStationSection));
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
                stationLine.getDistance(),
                convertToStationSectionResponse(stationLine.getSections()));
    }

    private StationSection convertToStationSectionEntity(StationLine stationLine) {
        return new StationSection(
                stationLine.getUpStationId(),
                stationLine.getDownStationId(),
                stationLine.getDistance(),
                stationLine);
    }

    private List<StationSectionResponse> convertToStationSectionResponse(List<StationSection> stationSections) {
        return stationSections.stream()
                .map(stationSection -> new StationSectionResponse(
                        stationSection.getId(),
                        findStationById(stationSection).getName(),
                        stationSection.getUpStationId(),
                        stationSection.getDownStationId(),
                        stationSection.getDistance()))
                .collect(Collectors.toList());
    }

    private Station findStationById(StationSection stationSection) {
        return stationRepository.findById(stationSection.getUpStationId()).orElseThrow(EntityNotFoundException::new);
    }
}
