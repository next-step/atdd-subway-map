package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.SubwaySection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse createLine(SubwayLineRequest subwayLineRequest) {
        SubwayLine subwayLine = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName()
                , subwayLineRequest.getColor()));

        return createLineResponse(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        return createLineResponse(subwayLine);
    }

    public SubwayLine findSubwayLineEntity(Long id) {
        return subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public void updateSubwayLine(Long id, SubwayLineUpdateRequest subwayLineUpdateRequest) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        subwayLine.updateName(subwayLineUpdateRequest.getName());
        subwayLine.updateColor(subwayLineUpdateRequest.getColor());
    }

    @Transactional
    public void delete(Long id) { subwayLineRepository.deleteById(id); }

    @Transactional
    public void updateSections(Long id, SubwaySection section) {
        SubwayLine line = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        List<SubwaySection> sections = line.getSections();
        sections.add(section);
    }

    private SubwayLineResponse createLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getSections()
        );
    }
}