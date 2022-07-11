package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.domain.SubwayLineRepository;
import nextstep.subway.exception.StationNotFoundException;
import nextstep.subway.exception.SubwayLineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        List<SubwayLine> all = subwayLineRepository.findAll();
        return all.stream()
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
    public SubwayLineResponse saveSubwaySection(Long lineId, SubwaySectionRequest subwaySectionRequest) {
        return null;
    }

    @Transactional
    public void deleteSubwaySection(Long lineId, Long stationId) {

    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        StationResponse upStation = createStationResponse(subwayLine.getUpStationId());
        StationResponse downStation = createStationResponse(subwayLine.getDownStationId());
        return new SubwayLineResponse(subwayLine, upStation, downStation);
    }

    private StationResponse createStationResponse(Long id) {
        return stationRepository.findById(id)
                .map(StationResponse::new)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    private SubwayLine findSubwayLineById(Long id) {
        return subwayLineRepository.findById(id)
                .orElseThrow(() -> new SubwayLineNotFoundException(id));
    }

}
