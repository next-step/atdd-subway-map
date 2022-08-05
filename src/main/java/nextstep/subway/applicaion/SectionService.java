package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.section.CreateSectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.ValidationException;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long create(Long lineId, CreateSectionRequest createSectionRequest) {
        final Line findLine = getLineIfExists(lineId);
        final Station findUpStation = getStationIfExists(createSectionRequest.getUpStationId());
        final Station findDownStation = getStationIfExists(createSectionRequest.getDownStationId());
        validateSectionDataBeforeCreate(findLine, findUpStation, findDownStation);
        final Section section = new Section(findLine, findUpStation, findDownStation, createSectionRequest.getDistance());
        final Section savedSection = sectionRepository.save(section);
        findLine.updateDownStation(savedSection.getDownStation());

        return savedSection.getId();
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        final Line findLine = getLineIfExists(lineId);
        final Station findStation = getStationIfExists(stationId);
        validateSectionDataBeforeDelete(findLine, findStation);
        final Section findSection = getSectionByLineAndDownStation(findLine, findStation);
        findLine.updateDownStation(findSection.getUpStation());
        sectionRepository.delete(findSection);
    }

    public Section findOne(Long sectionId) {
        return getSectionIfExists(sectionId);
    }

    private void validateSectionDataBeforeCreate(Line line, Station upStation, Station downStation) {
        if (!line.getDownStation().equals(upStation)) {
            throw new ValidationException("새로운 구간의 상행역은 기존 노선의 하행 종점역이어야만 합니다.");
        }

        if (line.getAllStations().contains(downStation)) {
            throw new ValidationException("이미 해당 노선의 구간에 등록되어있는 하행역입니다.");
        }
    }

    private void validateSectionDataBeforeDelete(Line line, Station downStation) {
        final Optional<Section> findSection = sectionRepository.findByLineAndUpStation(line, downStation);
        if (findSection.isPresent()) {
            throw new ValidationException("마지막 구간만 삭제할 수 있습니다.");
        }
        final List<Section> findSections = sectionRepository.findAllByLine(line);
        if (findSections.size() <= 1) {
            throw new ValidationException("남은 구간이 한 개일 경우, 삭제할 수 없습니다.");
        }
    }

    private Line getLineIfExists(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 노선입니다."));
    }

    private Section getSectionIfExists(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 구간입니다."));
    }

    private Section getSectionByLineAndDownStation(Line line, Station station) {
        return sectionRepository.findByLineAndDownStation(line, station)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 구간입니다."));
    }

    private Station getStationIfExists(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 역입니다."));
    }
}
