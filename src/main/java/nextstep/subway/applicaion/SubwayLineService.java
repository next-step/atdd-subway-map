package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SubwayLineModifyRequest;
import nextstep.subway.applicaion.dto.SubwayLineResponse;
import nextstep.subway.applicaion.dto.SubwayLineSaveRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.domain.SubwayLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private static final String NOT_FOUND_SUBWAY_LINE = "not found subway line by ";
    private static final String NOT_FOUND_STATION = "not found station by ";

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
        return subwayLineRepository.findById(id)
                .map(this::createSubwayLineResponse)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_SUBWAY_LINE + id));
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(SubwayLineSaveRequest subwayLineSaveRequest) {
        SubwayLine subwayLine = subwayLineSaveRequest.toEntity();
        SubwayLine save = subwayLineRepository.save(subwayLine);
        return createSubwayLineResponse(save);
    }

    @Transactional
    public void modifySubwayLine(Long id, SubwayLineModifyRequest subwayLineModifyRequest) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_SUBWAY_LINE + id));
        subwayLine.modify(subwayLineModifyRequest.getName(), subwayLineModifyRequest.getColor());
        subwayLineRepository.save(subwayLine);
    }

    @Transactional
    public void deleteSubwayLine(Long id) {
        subwayLineRepository.deleteById(id);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        StationResponse upStation = createSubwayResponse(subwayLine.getUpStationId());
        StationResponse downStation = createSubwayResponse(subwayLine.getDownStationId());
        return new SubwayLineResponse(subwayLine, upStation, downStation);
    }

    private StationResponse createSubwayResponse(Long id) {
        return stationRepository.findById(id)
                .map(StationResponse::new)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_STATION + id));
    }
}
