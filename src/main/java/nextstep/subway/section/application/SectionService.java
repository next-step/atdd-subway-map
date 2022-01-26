package nextstep.subway.section.application;

import nextstep.subway.common.exception.LineNotFoundException;
import nextstep.subway.common.exception.StationNotFoundException;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.domain.manager.LineData;
import nextstep.subway.section.domain.manager.SectionLineManager;
import nextstep.subway.section.domain.manager.SectionStationManager;
import nextstep.subway.section.domain.manager.StationData;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.domain.Sections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
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
    public List<LineData> findAllStations(List<LineData> lineDataList) {
        return lineDataList.stream().map(this::createStationData)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineData findStations(LineData lineData) {
        return createStationData(lineData);
    }

    public void addSection(Long lineId, SectionRequest request) {
        Assert.isTrue(!Objects.equals(request.getUpStationId(), request.getDownStationId()),
                "상행역과 하행역은 동일할 수 없습니다.");

        if (!sectionLineManager.isExistsByLine(lineId))
            throw new LineNotFoundException();

        if (!sectionStationManager.isExistInStations(request.getUpStationId(), request.getDownStationId()))
            throw new StationNotFoundException();

        Sections sections = getLineSections(lineId);
        sections.checkAddValidation(request.getUpStationId(), request.getDownStationId());

        sectionRepository.save(request.toSection(lineId));
    }

    public void removeLineById(Long lineId) {
        sectionRepository.deleteByLineId(lineId);
    }

    public void removeSectionById(Long lineId, Long id) {
        if (!sectionLineManager.isExistsByLine(lineId))
            throw new LineNotFoundException();

        Sections sections = getLineSections(lineId);
        sections.checkRemoveValidation(id);

        sectionRepository.deleteByDownStationIdAndLineId(lineId, id);
    }

    @Transactional(readOnly = true)
    public boolean isExistStation(Long stationId) {
        return sectionRepository.existsByUpStationIdOrDownStationId(stationId, stationId);
    }

    private LineData createStationData(LineData line) {
        Sections sections = getLineSections(line.getId());
        if (!sections.isEmpty()) {
            List<StationData> stations = sectionStationManager.getAllInStations(sections.getSectionIds());
            line.setStations(stations);
        }
        return line;
    }

    private Sections getLineSections(Long lineId) {
        return new Sections(sectionRepository.findAllByLineIdOrderById(lineId));
    }
}
