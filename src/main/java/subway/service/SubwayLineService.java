package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.NotFoundSubwayLineException;
import subway.controller.request.SubwayLineSectionAddRequest;
import subway.controller.resonse.StationResponse;
import subway.controller.resonse.SubwayLineResponse;
import subway.controller.resonse.SubwayLineSectionResponse;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayLine;
import subway.repository.StationRepository;
import subway.repository.StationSectionRepository;
import subway.repository.SubwayLineRepository;
import subway.service.command.SubwayLineCreateCommand;
import subway.service.command.SubwayLineModifyCommand;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;
    private final StationRepository stationRepository;
    private final StationSectionRepository stationSectionRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository, StationRepository stationRepository, StationSectionRepository stationSectionRepository) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationRepository = stationRepository;
        this.stationSectionRepository = stationSectionRepository;
    }

    @Transactional
    public SubwayLineResponse saveStationLine(SubwayLineCreateCommand createCommand) {
        Station upStation = stationRepository.getReferenceById(createCommand.getUpStationId());
        Station downStation = stationRepository.getReferenceById(createCommand.getDownStationId());

        SubwayLine subwayLine = SubwayLine.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(createCommand.getDistance())
                .name(createCommand.getName())
                .color(createCommand.getColor())
                .build();

        SubwayLine satedSubwayLine = subwayLineRepository.save(subwayLine);

        return createSubwayLineResponse(satedSubwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(this::createSubwayLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        SubwayLine subwayLine = requireGetById(id);
        return createSubwayLineResponse(subwayLine);
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

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                List.of(new StationResponse(subwayLine.getUpStation()),
                        new StationResponse(subwayLine.getDownStation())),
                subwayLine.getDistance());
    }

    @Transactional
    public SubwayLineSectionResponse addStationSection(Long subwayLineId, SubwayLineSectionAddRequest subwayLineCreateRequest) {
        SubwayLine subwayLine = requireGetById(subwayLineId);

        Station upStation = stationRepository.getReferenceById(subwayLineCreateRequest.getUpStationId());
        Station downStation = stationRepository.getReferenceById(subwayLineCreateRequest.getDownStationId());

        Section savedSection = stationSectionRepository.save(Section.of(upStation, downStation, subwayLineCreateRequest.getDistance()));
        subwayLine.expandLine(savedSection);

        return SubwayLineSectionResponse.of(savedSection);
    }

    @Transactional
    public void deleteStationAtLineSection(Long subwayLineId, Long stationId) {
        SubwayLine subwayLine = requireGetById(subwayLineId);

        Station targetStation = stationRepository.getReferenceById(stationId);

        subwayLine.pop(targetStation);
    }
}
