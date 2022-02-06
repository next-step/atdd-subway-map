package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
        final Integer distance = sectionRequest.getDistance();

        final Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", downStationId)));
        final Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", upStationId)));
        final Line foundLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", lineId)));

        foundLine.addSection(upStation, downStation, distance);

        return createSectionResponse(toSection(foundLine, upStation, downStation, distance));
    }

    private Section toSection(Line foundLine, Station upStation, Station downStation, Integer distance) {
        return new Section(foundLine, upStation, downStation, distance);
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

        foundLine.deleteLastSection();
    }
}
