package nextstep.subway.domain.line;

import nextstep.subway.domain.line.dto.LineDetailResponse;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.section.dto.SectionDetailResponse;
import nextstep.subway.domain.section.dto.SectionRequest;
import nextstep.subway.domain.section.dto.SectionResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.handler.error.custom.BusinessException;
import nextstep.subway.handler.validator.SectionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.handler.error.custom.ErrorCode.*;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    /* 노선 생성 */
    public LineResponse saveLine(LineRequest request) {
        Line savedLine = Line.of(request.getName(), request.getColor());
        pushSection(savedLine, createFirstSection(request.getUpStationId(), request.getDownStationId(),
                request.getDistance()));

        lineRepository.save(savedLine);

        return LineResponse.from(savedLine);
    }

    private Section createFirstSection(Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);

        validateSectionFirst(upStation, downStation, distance);
        return Section.of(upStation, downStation, distance);
    }

    private void validateSectionFirst(Station upStation, Station downStation, int distance) {
        if (sectionRepository.existsByUpStationAndDownStation(upStation, downStation)) {
            throw new BusinessException(SECTION_ALREADY_EXISTS);
        }
        SectionValidator.validDistance(distance);
    }

    /* 노선 목록 조회 */
    @Transactional(readOnly = true)
    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    /* 단일 노선 조회 */
    @Transactional(readOnly = true)
    public LineDetailResponse getLine(Long id) {
        return LineDetailResponse.from(findLineById(id));
    }

    /* 단일 노선 정보 수정 */
    public void patchLine(Long id, String lineName, String lineColor) {
        Line line = findLineById(id);
        line.modify(lineName, lineColor);
    }

    /* 단일 노선 삭제 */
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    /* 노선에 구간 추가 */
    public SectionResponse insertSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
        Section createdSection =
                createSection(line, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        pushSection(line, createdSection);

        return SectionResponse.from(createdSection);
    }

    private Section createSection(Line line, Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);

        SectionValidator.existsOnlyOneStation(line, upStation, downStation);
        SectionValidator.validDistance(distance);

        return Section.of(upStation, downStation, distance);
    }

    /* 노선의 구간 목록 조회 */
    @Transactional(readOnly = true)
    public List<SectionDetailResponse> getSections(Long id) {
        Line selectedLine = findLineById(id);

        return selectedLine.getSectionList()
                .stream()
                .map(SectionDetailResponse::of)
                .collect(Collectors.toList());
    }

    /* 노선의 단일 구간 삭제 */
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station downStation = stationRepository.findById(stationId)
                .orElseThrow(() -> new BusinessException(STATION_NOT_FOUND_BY_ID));

        line.getSectionList().stream()
                .filter(section -> section.hasDownStation(downStation))
                .findFirst()
                .ifPresent(section -> line.deleteSection(section));
    }

    private void pushSection(Line line, Section createdSection) {
        line.addSection(createdSection);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new BusinessException(STATION_NOT_FOUND_BY_ID));
    }
}
