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
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(StationRepository stationRepository,
                          LineRepository lineRepository) {
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

    public void deleteSection(long lineId, long stationId) {
        final Line foundLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", lineId)));

        final Station foundStation = stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", stationId)));

        foundLine.deleteLastSection(foundStation);
    }
}
