package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.StationLineSection;
import subway.repository.StationLineSectionRepository;
import subway.service.request.StationLineRequest;

@Service
@Transactional
public class StationLineSectionService {

    private final StationLineSectionRepository stationLineSectionRepository;

    public StationLineSectionService(StationLineSectionRepository stationLineSectionRepository) {
        this.stationLineSectionRepository = stationLineSectionRepository;
    }

    public StationLineSection create(final StationLineRequest request, final Long stationLineId) {
        return stationLineSectionRepository.save(
            new StationLineSection(
                stationLineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
            )
        );
    }

    public List<StationLineSection> findAllByLineId(Long lineId) {
        return stationLineSectionRepository.findAllByStationLineId(lineId);
    }
}
