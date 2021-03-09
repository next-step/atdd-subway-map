package nextstep.subway.section.application;

import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.section.exception.DownStationDuplicatedException;
import nextstep.subway.section.exception.WrongUpStationException;
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
            throw new WrongUpStationException();
        }
        if (idDownStationDuplicated(lineId, sectionRequest)) {
            throw new DownStationDuplicatedException();
        }

        Section persistSection = sectionRepository.save(sectionMapper.toSection(lineId, sectionRequest));
        return SectionResponse.of(persistSection);
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

    public void deleteSectionById(Long id) {
        sectionRepository.deleteById(id);
    }
}
