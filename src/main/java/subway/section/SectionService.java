package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.InvalidInputException;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public Section init(Line line) {
        Section section = Section.builder()
                .line(line)
                .upstation(line.getUpstation())
                .downstation(line.getDownstation())
                .distance(line.getDistance())
                .build();

        sectionRepository.save(section);
        return section;
    }

    @Transactional
    public SectionResponse add(Long lineId, SectionAddRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Station lastDownstation = getLastDownstation(line);

        if (!Objects.equals(lastDownstation.getId(), request.getUpstationId())) {
            throw new InvalidInputException("해당 노선의 하행 종점역과 새로운 구간의 상행역이 일치해야 합니다.");
        }

        if (line.getSections().stream().anyMatch(section ->
                section.getDownstation().getId().equals(request.getDownstationId()) ||
                        section.getUpstation().getId().equals(request.getDownstationId()))) {
            throw new InvalidInputException("새로운 구간의 하행역은 이미 노선에 존재하는 역이면 안 됩니다.");
        }

        Station upstation = stationRepository.findById(request.getUpstationId()).orElseThrow(EntityNotFoundException::new);
        Station downstation = stationRepository.findById(request.getDownstationId()).orElseThrow(EntityNotFoundException::new);

        Section newSection = Section.builder()
                .line(line)
                .upstation(upstation)
                .downstation(downstation)
                .distance(request.getDistance())
                .build();

        sectionRepository.save(newSection);
        line.addSection(newSection);

        return SectionResponse.from(newSection);
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Station lastDownstation = getLastDownstation(line);

        if (stationId != lastDownstation.getId()) {
            throw new InvalidInputException("노선에 등록된 하행 종점역만 제거할 수 있습니다.");
        }
        if (line.getSections().size() == 1) {
            throw new InvalidInputException("노선에 상행 종점역과 하행 종점역만 있는 경우에는 제거할 수 없습니다.");
        }

        Section section = line.popSection();
        sectionRepository.deleteById(section.getId());
    }

    private Station getLastDownstation(Line line) {
        return line.getSections().isEmpty() ? line.getDownstation() : line.getSections().get(line.getSections().size() - 1).getDownstation();
    }
}
