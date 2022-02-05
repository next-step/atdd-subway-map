package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository,
                          StationRepository stationRepository,
                          LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public SectionResponse saveSection(SectionRequest sectionRequest, long lineId) {
        final long downStationId = sectionRequest.getDownStationId();
        final long upStationId = sectionRequest.getUpStationId();
        checkExistingStation(downStationId);
        checkExistingStation(upStationId);

        final Station downStation = stationRepository.getById(downStationId);
        final Station upStation = stationRepository.getById(upStationId);
        final Line foundLine = lineRepository.getById(lineId);
        validateStationInSection(downStation, upStation, foundLine);

        final Section section = sectionRepository.save(
                toSection(sectionRequest, upStation, downStation, foundLine));

        return createSectionResponse(section);
    }

    private Section toSection(SectionRequest sectionRequest, Station upStation, Station downStation, Line foundLine) {
        return new Section(foundLine, upStation, downStation, sectionRequest.getDistance());
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId(),
                section.getLine().getId(),
                section.getDownStation().getId(),
                section.getUpStation().getId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate()
        );
    }

    private void validateStationInSection(Station downStation, Station upStation, Line foundLine) {
        final List<Section> sections = foundLine.getSections();
        if (!sections.isEmpty()) {
            checkUpStation(upStation, sections);
            checkDownStation(downStation, sections);
        }
    }

    private void checkDownStation(Station downStation, List<Section> sections) {
        final Section lastSection = sections.get(sections.size() - 1);
        if (lastSection.getDownStation().equals(downStation) ||
                lastSection.getUpStation().equals(downStation)) {
            throw new IllegalArgumentException("등록할 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }

    private void checkUpStation(Station upStation, List<Section> sections) {
        final Section lastSection = sections.get(sections.size() - 1);
        if (!upStation.equals(lastSection.getDownStation())) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkExistingStation(long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", stationId));
        }
    }

    public void deleteSection(long lineId, long stationId) {
        checkExistingStation(stationId);

        final Line foundLine = lineRepository.getById(lineId);
        final List<Section> sections = foundLine.getSections();
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("지하철 구간이 1개인 경우 구간을 제거할 수 없습니다.");
        }

        final Station foundStation = stationRepository.getById(stationId);
        final Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.getDownStation().equals(foundStation)) {
            throw new IllegalArgumentException("노선에 등록된 역(하행종점역)만 제거 가능합니다.");
        }

        sectionRepository.delete(lastSection);
    }
}
