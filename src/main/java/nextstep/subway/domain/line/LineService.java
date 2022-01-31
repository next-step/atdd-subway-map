package nextstep.subway.domain.line;

import nextstep.subway.domain.line.dto.LineDetailResponse;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.section.dto.SectionDetailResponse;
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

    public LineResponse saveLine(String lineName, String lineColor, Long upStationId, Long downStationId, int distance) {
        if (lineRepository.existsByName(lineName)) {
            throw new BusinessException(FOUND_DUPLICATED_NAME);
        }

        Line savedLine = new Line(lineName, lineColor);
        pushSection(savedLine, extractFirstSection(upStationId, downStationId, distance));

        lineRepository.save(savedLine);

        return LineResponse.from(savedLine);
    }

    @Transactional(readOnly = true)
    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineDetailResponse getLine(Long id) {
        return LineDetailResponse.from(findLineById(id));
    }

    public void patchLine(Long id, String lineName, String lineColor) {
        Line line = findLineById(id);
        line.modify(lineName, lineColor);
    }

    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new BusinessException(LINE_NOT_FOUND_BY_ID);
        }
        lineRepository.deleteById(id);
    }

    public SectionResponse createSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
        Section createdSection = extractSection(line, upStationId, downStationId, distance);
        pushSection(line, createdSection);

        return SectionResponse.from(createdSection);
    }

    @Transactional(readOnly = true)
    public List<SectionDetailResponse> getSections(Long id) {
        Line selectedLine = findLineById(id);

        return selectedLine.getSectionList()
                .stream()
                .map(SectionDetailResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        line.deleteSection(findStationById(stationId));
    }

    private void pushSection(Line line, Section createdSection) {
        line.addSection(createdSection);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
    }

    private Section extractSection(Line line, Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);

        line.validateSection(upStation, downStation, distance);

        return Section.of(upStation, downStation, distance);
    }

    private Section extractFirstSection(Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);

        validateFirstSection(upStation, downStation, distance);
        return Section.of(upStation, downStation, distance);
    }

    private Station findStationById(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new BusinessException(STATION_NOT_FOUND_BY_ID));
    }

    private void validateFirstSection(Station upStation, Station downStation, int distance) {
        if (sectionRepository.existsByUpStationAndDownStation(upStation, downStation)) {
            throw new BusinessException(SECTION_ALREADY_EXISTS);
        }
        SectionValidator.proper(distance);
    }
}
