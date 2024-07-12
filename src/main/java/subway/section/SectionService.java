package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.commons.ErrorCode;
import subway.commons.HttpException;
import subway.line.Line;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long createSection(SectionCreateDTO dto, Line line) {

        Section section = Section.builder()
                .upStationId(dto.getUpStationId())
                .downStationId(dto.getDownStationId())
                .distance(dto.getDistance())
                .line(line)
                .build();
        line.addSection(section);
        return section.getId();
    }

    public List<Section> readSections(Long lineID) {
        return sectionRepository.findAllByLineIdOrderById(lineID);
    }

    @Transactional
    public void deleteSection(SectionDeleteDTO dto) {

        Section deletingSection = sectionRepository.findByLineIdAndDownStationId(dto.getLineId(), dto.getStationId())
                .orElseThrow(() -> new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION, dto.getStationId().toString()));
        Line line = deletingSection.getLine();
        line.deleteSection(dto.getStationId());
    }
}
