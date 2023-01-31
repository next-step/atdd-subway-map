package subway.service;

import org.springframework.stereotype.Service;
import subway.entity.Line;
import subway.entity.Section;
import subway.model.CreateSectionRequest;
import subway.model.SectionResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public SectionResponse createSection(Long lineId, CreateSectionRequest req) {
        Section section = Section.of(stationRepository.findByIdInOrderById(
            List.of(
                req.getUpStationId(),
                req.getDownStationId()
            )), req.getDistance()
        );

        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchElementException::new);
        line.addSection(section);

        sectionRepository.save(section);
        lineRepository.save(line);

        return SectionResponse.from(section);
    }
}
