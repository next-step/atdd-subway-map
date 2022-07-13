package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.mapper.domain.SectionMapper;
import nextstep.subway.applicaion.mapper.response.SectionResponseMapper;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.exception.AlreadyRegisteredException;
import nextstep.subway.exception.NotDownStationException;
import nextstep.subway.exception.OnlyOneSectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final SectionResponseMapper sectionResponseMapper;

    @Transactional
    public void createSection(Long lineId, SectionRequest sectionRequest) {
        if (isNotDownStationId(lineId, sectionRequest.getUpStationId())) {
            throw new NotDownStationException(String.format("%d번 노선의 %d이/가 하행 종점역이 아닙니다.", lineId, sectionRequest.getUpStationId()));
        }

        if (isAlreadyExistsStation(lineId, sectionRequest.getDownStationId())) {
            throw new AlreadyRegisteredException(String.format("이미 %d번 노선에 등록된 역입니다.", lineId));
        }

        sectionRepository.save(sectionMapper.map(lineId, sectionRequest));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        if (isNotDownStationId(lineId, stationId)) {
            throw new NotDownStationException(String.format("%d번 노선의 %d가 하행 종점역이 아닙니다.", lineId, stationId));
        }

        if (sectionRepository.countByLineId(lineId) == 1) {
            throw new OnlyOneSectionException();
        }

        sectionRepository.deleteByLineIdAndDownStationId(lineId, stationId);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        return sectionRepository.findAllByLineId(lineId).stream()
                .map(sectionResponseMapper::map)
                .collect(Collectors.toList());
    }

    private boolean isNotDownStationId(Long lineId, Long downStationId) {
        List<SectionResponse> sections = findSectionsByLineId(lineId);

        List<Long> upStationIds = sections.stream().map(SectionResponse::getUpStationId).collect(Collectors.toList());
        List<Long> downStationIds = sections.stream().map(SectionResponse::getDownStationId).collect(Collectors.toList());

        Long currentDownStationId = downStationIds.stream()
                .filter(down -> !upStationIds.contains(down))
                .findAny()
                .orElse(downStationId);

        return !Objects.equals(currentDownStationId, downStationId);
    }

    private boolean isAlreadyExistsStation(Long lineId, Long stationId) {
        List<SectionResponse> sections = findSectionsByLineId(lineId);

        List<Long> upStationIds = sections.stream().map(SectionResponse::getUpStationId).collect(Collectors.toList());
        List<Long> downStationIds = sections.stream().map(SectionResponse::getDownStationId).collect(Collectors.toList());

        return upStationIds.contains(stationId) || downStationIds.contains(stationId);
    }
}
