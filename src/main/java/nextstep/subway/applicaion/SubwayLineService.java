package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.domain.SubwayLineRepository;
import nextstep.subway.exception.SubwayLineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {
    private final StationRepository stationRepository;
    private final SubwayLineRepository subwayLineRepository;

    public SubwayLineService(StationRepository stationRepository, SubwayLineRepository subwayLineRepository) {
        this.stationRepository = stationRepository;
        this.subwayLineRepository = subwayLineRepository;
    }

    public List<SubwayLineResponse> getSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(this::createSubwayLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse getSubwayLineById(Long id) {
        return createSubwayLineResponse(findSubwayLineById(id));
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(SubwayLineSaveRequest subwayLineSaveRequest) {
        SubwayLine subwayLine = subwayLineSaveRequest.toEntity();
        subwayLineRepository.save(subwayLine);
        return createSubwayLineResponse(subwayLine);
    }

    @Transactional
    public void modifySubwayLine(Long id, SubwayLineModifyRequest subwayLineModifyRequest) {
        SubwayLine subwayLine = findSubwayLineById(id);
        subwayLine.modify(subwayLineModifyRequest.getName(), subwayLineModifyRequest.getColor());
    }

    @Transactional
    public void deleteSubwayLine(Long id) {
        subwayLineRepository.deleteById(id);
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        SubwayLine subwayLine = findSubwayLineById(lineId);
        Section newSection = sectionRequest.toEntity();
        subwayLine.addSection(newSection);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        SubwayLine subwayLine = findSubwayLineById(lineId);
        subwayLine.removeStation(stationId);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(subwayLine, createStationResponseList(subwayLine.getStationIds()));
    }

    private List<StationResponse> createStationResponseList(Set<Long> ids) {
        return stationRepository.findAllById(ids).stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    private SubwayLine findSubwayLineById(Long id) {
        return subwayLineRepository.findById(id)
                .orElseThrow(() -> new SubwayLineNotFoundException(id));
    }

}
