package nextstep.subway.section.application;

import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.section.exception.CreateSectionWithWrongUpStationException;
import nextstep.subway.section.exception.DeleteSectionWithNotLastException;
import nextstep.subway.section.exception.DeleteSectionWithOnlyOneException;
import nextstep.subway.section.exception.DownStationDuplicatedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    public SectionService(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        if (isWrongUpStation(lineId, sectionRequest)) {
            throw new CreateSectionWithWrongUpStationException();
        }
        if (idDownStationDuplicated(lineId, sectionRequest)) {
            throw new DownStationDuplicatedException();
        }

        Section persistSection = sectionRepository.save(sectionMapper.toSection(lineId, sectionRequest));
        return SectionResponse.of(persistSection);
    }

    public void deleteSectionById(Long lineId, Long sectionId) {
        if (isNotLastSection(lineId, sectionId)) {
            throw new DeleteSectionWithNotLastException();
        }
        if (isOnlyOneSection(lineId)) {
            throw new DeleteSectionWithOnlyOneException();
        }

        sectionRepository.deleteById(sectionId);
    }

    private boolean isWrongUpStation(Long lineId, SectionRequest sectionRequest) {
        Section lastSection = sectionRepository.findLastSectionByLineId(lineId);
        return lastSection != null && lastSection.isDownStationOfSectionNotEqualToUpStation(sectionRequest.getUpStationId());
    }

    private boolean idDownStationDuplicated(Long lineId, SectionRequest sectionRequest) {
        List<Long> downStationIds = sectionRepository.findAllByLineId(lineId).stream()
                .map(it -> it.getDownStation().getId())
                .collect(Collectors.toList());
        return downStationIds.contains(sectionRequest.getDownStationId());
    }

    private boolean isNotLastSection(Long lineId, Long sectionId) {
        Section lastSection = sectionRepository.findLastSectionByLineId(lineId);
        return lastSection.isNotEqualToSection(sectionId);
    }

    private boolean isOnlyOneSection(Long lineId) {
        long count = sectionRepository.countByLineId(lineId);
        return count == 1;
    }
}
