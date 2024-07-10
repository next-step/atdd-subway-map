package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.SubwayLine;
import subway.dto.SubwayLineRequest;
import subway.dto.SubwayLineResponse;
import subway.dto.SubwayLineUpdateRequest;
import subway.repository.SubwayLineRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class SubwayLineService {
    private final SubwayLineRepository subwayLineRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(SubwayLineRequest request) {
        var subwayLine = subwayLineRepository.save(request.toSubwayLine());
        return SubwayLineResponse.from(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(SubwayLineResponse::from)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        var subwayLine = findSubwayLineOrElseThrow(id);
        return SubwayLineResponse.from(subwayLine);
    }

    @Transactional
    public void updateSubwayLine(Long id, SubwayLineUpdateRequest request) {
        var subwayLine = findSubwayLineOrElseThrow(id);
        subwayLine.updateBasicInfo(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteSubwayLine(Long id) {
        findSubwayLineOrElseThrow(id);
        subwayLineRepository.deleteById(id);
    }

    private SubwayLine findSubwayLineOrElseThrow(Long id) {
        return subwayLineRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
