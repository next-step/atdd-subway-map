package nextstep.subway.section.application;

import nextstep.subway.common.exception.LineNotFoundException;
import nextstep.subway.common.exception.StationNotFoundException;
import nextstep.subway.section.application.dto.SectionLineResponse;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.application.manager.LineData;
import nextstep.subway.section.application.manager.SectionLineManager;
import nextstep.subway.section.application.manager.SectionStationManager;
import nextstep.subway.section.application.manager.StationData;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SectionLineManager sectionLineManager;
    private final SectionStationManager sectionStationManager;

    public SectionService(SectionRepository sectionRepository, SectionLineManager sectionLineManager, SectionStationManager sectionStationManager) {
        this.sectionRepository = sectionRepository;
        this.sectionLineManager = sectionLineManager;
        this.sectionStationManager = sectionStationManager;
    }

    @Transactional(readOnly = true)
    public List<SectionLineResponse> findAllLinesStations() {
        return sectionLineManager.getAllLines().map(this::createStationData)
                .collect(Collectors.toList());
    }

    public void addSection(Long lineId, SectionRequest request) {
        if (!sectionLineManager.existsByLine(lineId))
            throw new LineNotFoundException();

        if (!sectionStationManager.existInStations(request.getUpStationId(), request.getDownStationId()))
            throw new StationNotFoundException();

        List<Section> sections = sectionRepository.findAllByLineId(lineId);

        if (!sections.isEmpty()) {
            Assert.isTrue(sections.stream().noneMatch(section ->
                    section.getUpStationId() == request.getUpStationId()
            ), "새로운 구간의 상행역은 현재 등록되어있는 상행역일 수 없다.");

            Assert.isTrue(sections.stream().noneMatch(section ->
                    section.getUpStationId() == request.getDownStationId() || section.getDownStationId() == request.getDownStationId()
            ), "새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");

            Assert.isTrue(sections.stream().anyMatch(section ->
                    section.getDownStationId() == request.getUpStationId()
            ), "구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        sectionRepository.save(request.toSection(lineId));
    }

    public void removeSectionById(Long lineId, Long id) {
        if (!sectionLineManager.existsByLine(lineId))
            throw new LineNotFoundException();

        List<Section> sections = sectionRepository.findAllByLineId(lineId);

//        if (!sectionRepository.existsById(id))
//            throw new SectionNotFoundException();

        //lineId downStationId
        sectionRepository.deleteByDownStationIdAndLineId(lineId, id);
    }

    private SectionLineResponse createStationData(LineData line) {
        List<Section> sections = sectionRepository.findAllByLineId(line.getId());

        Set<Long> ids = new HashSet<>(sections.stream().map(section -> section.getId()).collect(Collectors.toList()));
        List<StationData> stations = sectionStationManager.getAllInStations(ids);
        return SectionLineResponse.of(line, stations);
    }
}
