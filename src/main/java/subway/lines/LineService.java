package subway.lines;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionAddRequest;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        final Line line = lineRepository.save(
            new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                lineCreateRequest.getUpStationId(),
                lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance()
            )
        );

        final Section section = sectionRepository.save(
            new Section(
                lineCreateRequest.getUpStationId(),
                lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance()
            )
        );

        section.updateLine(line);

        return createLineResponse(line);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        final Line line = lineRepository.findById(id).orElse(null);
        if (line == null) {
            return null;
        }

        return createLineResponse(line);
    }

    @Transactional
    public void updateLines(Long id, LineUpdateRequest lineUpdateRequest) {
        final Line line = lineRepository.findById(id).orElse(null);
        if (line == null) {
            throw new EntityNotFoundException();
        }

        line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLines(Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        sectionRepository.deleteAllById(
            line.getSections().stream().map(Section::getId).collect(Collectors.toSet())
        );
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(Long id, SectionAddRequest sectionAddRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        validateAddSectionCondition(sectionAddRequest, line);

        final Section section = sectionRepository.save(
            new Section(
                sectionAddRequest.getUpStationId(),
                sectionAddRequest.getDownStationId(),
                sectionAddRequest.getDistance()
            )
        );
        section.updateLine(line);
        line.updatesSection(section.getDownStationId(), line.getDistance() + section.getDistance());

        return createLineResponse(line);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        validateDeleteSectionCondition(id, stationId, line);

        final Section section = sectionRepository.findByLineIdAndDownStationId(id, stationId);
        if(section == null) {
            throw new EntityNotFoundException();
        }

        line.updatesSection(section.getUpStationId(), line.getDistance() - section.getDistance());
        sectionRepository.deleteById(section.getId());
    }

    private void validateDeleteSectionCondition(Long id, Long stationId, Line line) {
        final int numberOfSection = sectionRepository.countByLineId(id);
        if(numberOfSection == 1) {
            throw new IllegalArgumentException();
        }

        if(line.getDownStationId() != stationId) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateAddSectionCondition(SectionAddRequest sectionAddRequest, Line line) {
        if (!Objects.equals(line.getDownStationId(), sectionAddRequest.getUpStationId())) {
            throw new IllegalArgumentException();
        }
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
