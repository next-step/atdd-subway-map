package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long createSection(SectionCreateDTO dto) {
        validateCreate(dto);
        Section section = Section.builder()
                .upStationId(dto.getUpStationId())
                .downStationId(dto.getDownStationId())
                .distance(dto.getDistance())
                .lineId(dto.getLineId())
                .build();
        sectionRepository.save(section);
        return section.getId();
    }

    public List<Section> readSections(Long lineID) {
        return sectionRepository.findAllByLineIdOrderById(lineID);
    }

    @Transactional
    public void deleteSection(SectionDeleteDTO dto) {
        validateDelete(dto);
        Section deletingSection = sectionRepository.findByLineIdAndDownStationId(dto.getLineId(), dto.getStationId())
                .orElseThrow(() -> new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION));
        sectionRepository.delete(deletingSection);
    }

    private void validateCreate(SectionCreateDTO dto) {
        List<Section> currentSections = readSections(dto.getLineId());
        // 하행 종점이 같은 것이 존재하면 x
        boolean isExist = currentSections.stream().anyMatch(section -> section.getDownStationId() == dto.getDownStationId());
        if(isExist) {
            throw new HttpException(ErrorCode.DOWN_STATION_NOT_VALID);
        }
        // 상행 종점이 같은 것이 존재하면 x
        isExist = currentSections.stream().anyMatch(section -> section.getUpStationId() == dto.getUpStationId());
        if(isExist) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }
        // 등록하는 상행역이 기존 하행종점과 같지 않으면 x
        Section lastSection = currentSections.get(currentSections.size() -1);
        if(lastSection.getDownStationId() != dto.getUpStationId()) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }


    }

    private void validateDelete(SectionDeleteDTO dto) {
        List<Section> currentSections = readSections(dto.getLineId());
        if(currentSections.size() == 1) {
            throw new HttpException(ErrorCode.CANNOT_REMOVE_LAST_SECTION);
        }
        Section lastSection = currentSections.get(currentSections.size() -1);
        if(lastSection.getDownStationId() != dto.getStationId()) {
            throw new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION);
        }
    }
}
