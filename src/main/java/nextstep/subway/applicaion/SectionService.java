package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private SectionRepository sectionRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse save(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노션입니다."));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        validSectionStation(line, upStation, downStation);
        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        return createLineResponse(sectionRepository.save(section));
    }

    private void validSectionStation(Line line, Station sectionUpStation, Station sectionDownStation) {
        validSectionUpStation(line, sectionUpStation);
        validSectionDownStation(line, sectionDownStation);
    }

    private void validSectionDownStation(Line line, Station sectionDownStation) {
        Set<Long> sectionIds = new HashSet<>();
        for (Section section : line.sections()) {
            Long id = section.getUpStation().getId();
            sectionIds.add(id);
        }
        line.sections().stream().map(section -> section.getDownStation().getId()).forEach(sectionIds::add);
        if (sectionIds.contains(sectionDownStation.getId())) {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        }
    }

    private boolean isLineDownStationDuplicate(Station lineDownStation, Station sectionDownStation) {
        return Objects.equals(lineDownStation.getId(), sectionDownStation.getId());
    }

    private boolean isLineUpStationDuplicated(Station lineUpStation, Station sectionDownStation) {
        return Objects.equals(lineUpStation.getId(), sectionDownStation.getId());
    }

    private void validSectionUpStation(Line line, Station sectionUpStation) {
        if (!Objects.equals(line.downStation().getId(), sectionUpStation.getId())) {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        }
    }

    private SectionResponse createLineResponse(Section section) {
        return new SectionResponse(section.getId(), section.getLine().getId(), section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
    }

    public void delete(Long lineId, Long sectionDownStationId) {
        Set<Section> sections = sectionRepository.findAllByLine_Id(lineId);
        validSection(lineId, sectionDownStationId, sections);
        deleteSection(sectionDownStationId, sections);
    }

    private void validSection(Long lineId, Long sectionDownStationId, Set<Section> sections) {
        lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("line.not.found"));
        stationRepository.findById(sectionDownStationId).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        validIfDeleteUpStation(sectionDownStationId, sections);
        validIfExistSectionCount(sectionDownStationId, sections);
    }

    private void validIfExistSectionCount(Long sectionDownStationId, Set<Section> sections) {
        if (sections.size() < 2) {
            throw new IllegalArgumentException("section.count.less");
        }
    }

    private void validIfDeleteUpStation(Long sectionDownStationId, Set<Section> sections) {
        for (Section section : sections) {
            if (Objects.equals(section.getUpStation().getId(), sectionDownStationId)) {
                throw new IllegalArgumentException("section.upStation.not.delete");
            }
        }
    }

    private void deleteSection(Long sectionDownStationId, Set<Section> sections) {
        for (Section section : sections) {
            if (Objects.equals(section.getDownStation().getId(), sectionDownStationId)) {
                sectionRepository.deleteById(section.getId());
            }
        }
    }
}
