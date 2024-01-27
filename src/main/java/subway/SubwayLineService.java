package subway;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private SubwayLineRepository subwayLineRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse saveLine(SubwayLineRequest subwayLineRequest) {
        SubwayLine line = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName()));

        return createSubwayLineResponse(line);
    }

    public List<SubwayLineResponse> findAllLines() {
        return subwayLineRepository.findAll().stream().map(this::createSubwayLineResponse).collect(
            Collectors.toList());
    }

    public SubwayLineResponse findLine(Long id) {
        final SubwayLine line = subwayLineRepository.findById(id).orElse(null);
        if(line == null) {
            return null;
        }

        return createSubwayLineResponse(line);
    }

    @Transactional
    public SubwayLineResponse updateLine(Long id, SubwayLineRequest subwayLineRequest) {
        SubwayLine line = subwayLineRepository.findById(id).orElse(null);
        if(line == null) {
            throw new EntityNotFoundException();
        }

        line.setName(subwayLineRequest.getName());

        return createSubwayLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        subwayLineRepository.deleteById(id);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine line) {
        return new SubwayLineResponse(
            line.getId(),
            line.getName()
        );
    }
}
