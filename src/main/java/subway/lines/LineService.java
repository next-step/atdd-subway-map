package subway.lines;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionAddRequest;
import subway.section.SectionDeleteRequest;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        final Line line = lineRepository.save(lineCreateRequest.getLine());
        line.addSection(
            new Section(
                line,
                lineCreateRequest.getUpStationId(),
                lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance()
            )
        );

        return createLineResponse(line);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return createLineResponse(line);
    }

    @Transactional
    public void updateLines(Long id, LineUpdateRequest lineUpdateRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLines(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(Long id, SectionAddRequest sectionAddRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        sectionAddRequest.validateSectionToAdd(line);

        line.addSection(sectionAddRequest.getSection(line));

        return createLineResponse(line);
    }

    @Transactional
    public void deleteSection(Long id, SectionDeleteRequest sectionDeleteRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        sectionDeleteRequest.validateToDelete(line);

        final Section sectionToDelete = sectionRepository.findByLineIdAndDownStationId(id, sectionDeleteRequest.getStationId()).orElseThrow(EntityNotFoundException::new);
        line.deleteSection(sectionToDelete);
    }

    private LineResponse createLineResponse(Line line) {
        final Set<Long> stationIdSet = new HashSet<>();
        line.getSections().forEach(section -> {
            stationIdSet.addAll(
                Arrays.asList(
                    section.getUpStationId(),
                    section.getDownStationId()
                )
            );
        });

        final List<Station> stations = stationRepository.findAllById(stationIdSet);

        return new LineResponse(line, stations);
    }

}
