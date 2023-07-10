package subway.facade;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.StationLine;
import subway.entity.StationLineSection;
import subway.entity.group.StationLineSectionGroup;
import subway.service.StationLineSectionService;
import subway.service.StationLineService;
import subway.service.StationService;
import subway.service.request.StationLineModifyRequest;
import subway.service.request.StationLineRequest;
import subway.service.response.StationLineResponse;
import subway.service.response.StationResponse;

@Service
@Transactional
public class StationLineFacade {

    private final StationService stationService;
    private final StationLineService stationLineService;
    private final StationLineSectionService stationLineSectionService;

    public StationLineFacade(StationService stationService, StationLineService stationLineService,
        StationLineSectionService stationLineSectionService) {
        this.stationService = stationService;
        this.stationLineService = stationLineService;
        this.stationLineSectionService = stationLineSectionService;
    }

    public StationLineResponse lineCreate(StationLineRequest request) {

        final StationLine stationLine = stationLineService.create(request);
        final StationLineSection stationLineSection = stationLineSectionService.create(request,
            stationLine.getId());

        final List<StationResponse> stationResponses = stationService.findAllIn(
            stationLineSection.getStationIdList());

        return StationLineResponse.of(stationLine, stationResponses);
    }


    public StationLineResponse lineFindById(long id) {

        final StationLine stationLine = stationLineService.findById(id);
        return getStationLineResponse(stationLine);
    }

    private StationLineResponse getStationLineResponse(StationLine stationLine) {

        final StationLineSectionGroup sectionGroup = StationLineSectionGroup.of(
            stationLineSectionService.findAllByLineId(stationLine.getId())
        );

        final List<StationResponse> stationResponses = stationService.findAllIn(
            sectionGroup.getStationsId()
        );

        return StationLineResponse.of(stationLine, stationResponses);
    }

    public List<StationLineResponse> findAllStationLines() {

        return stationLineService.findAllLine()
            .stream()
            .map(this::getStationLineResponse)
            .collect(Collectors.toList());

    }

    public void modifyLine(long id, StationLineModifyRequest request) {
        stationLineService.modify(id, request.getName(), request.getColor());
    }

    public void deleteLine(long id) {
        stationLineService.delete(id);
    }
}
