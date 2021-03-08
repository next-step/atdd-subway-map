package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.line.exception.LineNameDuplicatedException;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.line.exception.WrongUpStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final LineMapper lineMapper;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.lineMapper = lineMapper;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        if (lineRepository.existsByName(lineRequest.getName())) {
            throw new LineNameDuplicatedException(lineRequest.getName());
        }

        Line persistLine = lineRepository.save(lineMapper.toLine(lineRequest));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse toLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
        line.update(lineMapper.toLine(lineRequest));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Section lastSection = sectionRepository.findLastSectionByLineId(id);

        if (isWrongUpStation(sectionRequest, lastSection)) {
            throw new WrongUpStationException(lastSection.getDownStation());
        }

        Section persistSection = sectionRepository.save(lineMapper.toSection(id, sectionRequest));
        return SectionResponse.of(persistSection);
    }

    private boolean isWrongUpStation(SectionRequest sectionRequest, Section lastSection) {
        return lastSection != null && lastSection.isDownStationOfSectionNotEqualToUpStation(sectionRequest.getUpStationId());
    }
}
