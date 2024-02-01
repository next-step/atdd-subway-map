package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

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
    public StationSection saveStationSection(Long stationLineId, Map<String, Object> request) {
        StationLine stationLine = findStationLineById(stationLineId);

        StationSection stationSection = convertToStationSectionEntity(request);

        hasStation(stationSection);

        if (!stationSection.canSave(stationLine)) {
            throw new IllegalArgumentException("해당 역은 저장할 수 없습니다.");
        }

        stationSection.setStationLine(stationLine);
        return canStationSectionSave(stationSection, stationLine);
    }

    private void hasStation(StationSection stationSection) {
        stationRepository.findById(stationSection.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        stationRepository.findById(stationSection.getDownStationId()).orElseThrow(EntityNotFoundException::new);
    }

    private StationSection canStationSectionSave(StationSection stationSection, StationLine stationLine) {
        stationSection.updateLineDownStationId();
        stationSection.canSave(stationLine);
        return stationSectionRepository.save(stationSection);
    }

    private static StationSection convertToStationSectionEntity(Map<String, Object> request) {
        return new StationSection(
                Long.parseLong(request.get("upStationId").toString()),
                Long.parseLong(request.get("downStationId").toString()),
                Integer.parseInt(request.get("distance").toString())
        );
    }

    private StationLine findStationLineById(Long stationLineId) {
        return stationLineRepository.findById(stationLineId).orElseThrow(EntityNotFoundException::new);
    }
}
