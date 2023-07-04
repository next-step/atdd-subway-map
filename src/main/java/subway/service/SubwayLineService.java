package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.NotFoundSubwayLineException;
import subway.controller.resonse.StationResponse;
import subway.controller.resonse.SubwayLineResponse;
import subway.domain.SubwayLine;
import subway.repository.SubwayLineRepository;
import subway.service.command.SubwayLineCreateCommand;
import subway.service.command.SubwayLineModifyCommand;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;
    private final StationService stationService;

    public SubwayLineService(SubwayLineRepository subwayLineRepository, StationService stationService) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public SubwayLineResponse saveStationLine(SubwayLineCreateCommand createCommand) {
        SubwayLine subwayLine = SubwayLine.create(createCommand);

        SubwayLine satedSubwayLine = subwayLineRepository.save(subwayLine);
        StationResponse upStation = stationService.findStation(subwayLine.getUpStationId());
        StationResponse downStation = stationService.findStation(subwayLine.getDownStationId());

        return createSubwayLineResponse(satedSubwayLine, upStation, downStation);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(subwayLine -> {
                    StationResponse upStation = stationService.findStation(subwayLine.getUpStationId());
                    StationResponse downStation = stationService.findStation(subwayLine.getDownStationId());

                    return createSubwayLineResponse(subwayLine, upStation, downStation);
                })
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        SubwayLine subwayLine = requireGetById(id);
        StationResponse upStation = stationService.findStation(subwayLine.getUpStationId());
        StationResponse downStation = stationService.findStation(subwayLine.getDownStationId());

        return createSubwayLineResponse(subwayLine, upStation, downStation);
    }

    @Transactional
    public void modifySubwayLine(Long id, SubwayLineModifyCommand modifyCommand) {
        SubwayLine subwayLine = requireGetById(id);
        subwayLine.modify(modifyCommand.getName(), modifyCommand.getColor());
    }

    @Transactional
    public void deleteSubwayLineById(Long id) {
        SubwayLine subwayLine = requireGetById(id);
        subwayLineRepository.delete(subwayLine);
    }

    private SubwayLine requireGetById(Long id) {
        return subwayLineRepository.findById(id)
                .orElseThrow(() -> new NotFoundSubwayLineException(id));
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine, StationResponse upStationResponse, StationResponse downStationResponse) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                List.of(upStationResponse, downStationResponse)
        );
    }
}
