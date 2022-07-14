package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationSectionRequest;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationSection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StationSectionService {

    private final StationSectionRepository stationSectionRepository;
    private final StationLineService stationLineService;
    private final StationService stationService;
    private final StationSectionMapper stationSectionMapper;

    public StationSectionService(StationSectionRepository stationSectionRepository,
                                 StationLineService stationLineService,
                                 StationService stationService,
                                 StationSectionMapper stationSectionMapper) {
        this.stationSectionRepository = stationSectionRepository;
        this.stationLineService = stationLineService;
        this.stationService = stationService;
        this.stationSectionMapper = stationSectionMapper;
    }

    @Transactional
    public StationSectionResponse createStationSection(StationSectionRequest request, Long lineId) {
        validateNoneExistStation(request);
        StationSection stationSection = stationSectionMapper.of(request);
        StationLine stationLine = stationLineService.findLineById(lineId);
        stationLine.addSection(stationSection);

        return stationSectionMapper.of(stationSection);
    }

    @Transactional
    public void deleteStationSection(Long lineId, Long stationId) {
        StationLine stationLine = stationLineService.findLineById(lineId);
        StationSection section = stationLine.findSectionByDownStationId(stationId);
        stationLine.deleteSection(section);
        stationSectionRepository.delete(section);
    }

    private void validateNoneExistStation(StationSectionRequest request) {
        stationService.findStationById(request.getDownStationId());
        stationService.findStationById(request.getUpStationId());
    }
}
