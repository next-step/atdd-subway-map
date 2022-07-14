package nextstep.subway.applicaion;

import javax.transaction.Transactional;
import nextstep.subway.applicaion.dto.StationSectionRequest;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationSection;
import org.springframework.stereotype.Service;

@Service
public class StationSectionService {

    private final StationSectionRepository stationSectionRepository;
    private final StationLineService stationLineService;
    private final StationSectionMapper stationSectionMapper;

    public StationSectionService(StationSectionRepository stationSectionRepository,
                                 StationLineService stationLineService,
                                 StationSectionMapper stationSectionMapper) {
        this.stationSectionRepository = stationSectionRepository;
        this.stationLineService = stationLineService;
        this.stationSectionMapper = stationSectionMapper;
    }

    @Transactional
    public StationSectionResponse createStationSection(StationSectionRequest request, Long lineId) {
        StationSection stationSection = stationSectionMapper.of(request);
        StationLine stationLine = stationLineService.findLineById(lineId);
        stationLine.addSection(stationSection);

        return stationSectionMapper.of(stationSection);
    }
}
