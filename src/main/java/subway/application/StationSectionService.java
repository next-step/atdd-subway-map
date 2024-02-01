package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationSectionRequest;
import subway.dto.StationSectionResponse;
import subway.entity.*;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class StationSectionService {

    private final StationRepository stationRepository;
    private final StationLineRepository stationLineRepository;
    private final StationSectionRepository stationSectionRepository;

    public StationSectionService(StationRepository stationRepository,
                                 StationLineRepository stationLineRepository,
                                 StationSectionRepository stationSectionRepository) {
        this.stationRepository = stationRepository;
        this.stationLineRepository = stationLineRepository;
        this.stationSectionRepository = stationSectionRepository;
    }

    @Transactional
    public StationSectionResponse saveStationSection(StationSectionRequest request) {
        StationLine stationLine = findStationLineById(request.getStationLineId());
        StationSection stationSection = convertToStationSectionEntity(request);
        hasStation(stationSection);

        if (!stationSection.canSave(stationLine)) {
            throw new IllegalArgumentException("해당 역은 저장할 수 없습니다.");
        }
        return convertToResponse(canStationSectionSave(stationSection, stationLine));
    }

    private void hasStation(StationSection stationSection) {
        stationRepository.findById(stationSection.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        stationRepository.findById(stationSection.getDownStationId()).orElseThrow(EntityNotFoundException::new);
    }

    private StationSection canStationSectionSave(StationSection stationSection, StationLine stationLine) {
        stationSection.setStationLine(stationLine);
        stationSection.updateLineDownStationId();
        stationSection.canSave(stationLine);
        return stationSectionRepository.save(stationSection);
    }

    private static StationSection convertToStationSectionEntity(StationSectionRequest request) {
        return new StationSection(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
    }

    private StationLine findStationLineById(Long stationLineId) {
        return stationLineRepository.findById(stationLineId).orElseThrow(EntityNotFoundException::new);
    }

    private StationSectionResponse convertToResponse(StationSection stationSection) {
        return new StationSectionResponse(
            stationSection.getId(),
            stationSection.getUpStationId(),
            stationSection.getDownStationId(),
            stationSection.getDistance()
        );
    }
}
