package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.ResponseCode;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import nextstep.subway.ui.dto.section.SectionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SectionService {
    public static final int INVALID_REMOVE_SIZE = 1;

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionResponse createSection(long lineId, CreateSectionRequest request) {
        Line line = findLine(lineId);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Section section = Section.builder()
                                 .upStation(upStation)
                                 .downStation(downStation)
                                 .distance(request.getDistance()).build();
        line.addSection(section);
        sectionRepository.save(section);

        return SectionResponse.of(section);
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        Sections sections = findLine(lineId).getSections();

        Section removeSection = sections.removeLastSection(stationId);

        sectionRepository.delete(removeSection);
    }

    private Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new CustomException(ResponseCode.STATION_NOT_FOUND));
    }

    private Line findLine(final Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new CustomException(ResponseCode.LINE_NOT_FOUND));
    }
}
