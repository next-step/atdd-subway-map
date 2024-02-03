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
    public StationSectionResponse createStationSection(StationSectionRequest request) {
        StationLine stationLine = findStationLineById(request.getStationLineId());
        StationSection stationSection = convertToStationSectionEntity(request);

        if (!existStation(stationSection)) {
            throw new IllegalArgumentException("요청한 역은 존재하지 않습니다.");
        }
        if (!stationLine.canSave(stationSection)) {
            throw new IllegalArgumentException("요청한 구간을 저장할 수 없습니다.");
        }
        return convertToResponse(saveStationSection(stationSection.setStationLine(stationLine)));
    }

    @Transactional
    public void deleteStationSection(Long stationLineId, Long stationIdToDelete) {
        StationLine stationLine = findStationLineById(stationLineId);

        if (!stationLine.canDelete(stationIdToDelete)) {
            throw new IllegalArgumentException("요청한 구간(혹은 역)을 삭제할 수 없습니다.");
        }
    }

    private boolean existStation(StationSection stationSection) {
        boolean upStationExists = stationRepository.findById(stationSection.getUpStationId()).isPresent();
        boolean downStationExists = stationRepository.findById(stationSection.getDownStationId()).isPresent();
        return upStationExists && downStationExists;
    }

    private StationSection saveStationSection(StationSection stationSection) {
        stationSection.updateDownStationOfLine();
        return stationSectionRepository.save(stationSection);
    }

    private StationLine findStationLineById(Long stationLineId) {
        return stationLineRepository.findById(stationLineId).orElseThrow(EntityNotFoundException::new);
    }

    private static StationSection convertToStationSectionEntity(StationSectionRequest request) {
        return new StationSection(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
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
