package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineSaveResponse saveLine(final LineSaveRequest lineRequest) {
        validateDuplicatedLineName(lineRequest.getName());
        validateDuplicatedLineColor(lineRequest.getColor());
        final Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));

        final Station upStation = findByStationById(lineRequest.getUpStationId());
        final Station downStation = findByStationById(lineRequest.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, lineRequest.getDistance()));
        return new LineSaveResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineReadAllResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> new LineReadAllResponse(line, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineReadResponse findLine(final Long lineId) {
        final Line line = findLineById(lineId);
        return new LineReadResponse(line, Collections.EMPTY_LIST);
    }

    public void updateLine(final Long lineId, final LineUpdateRequest lineUpdateRequest) {
        validateDuplicatedLineName(lineUpdateRequest.getName());
        validateDuplicatedLineColor(lineUpdateRequest.getColor());
        final Line line = findLineById(lineId);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void delete(final Long id) {
        lineRepository.deleteById(id);
    }

    private void validateDuplicatedLineName(final String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new EntityExistsException("duplicate line name occurred");
        }
    }

    private void validateDuplicatedLineColor(final String lineColor) {
        if (lineRepository.existsByColor(lineColor)) {
            throw new EntityExistsException("duplicate line color occurred");
        }
    }

    public SectionSaveResponse addSection(final Long lineId, final SectionAddRequest sectionAddRequest) {
        final Line line = findLineById(lineId);
        final Station downStation = findByStationById(sectionAddRequest.getDownStationId());
        final Station upStation = findByStationById(sectionAddRequest.getUpStationId());
        final Section section = new Section(line, upStation, downStation, sectionAddRequest.getDistance());
        line.addSection(section);
        return new SectionSaveResponse(section);
    }

    private Line findLineById(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("empty line occurred"));
    }

    private Station findByStationById(final Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new EntityNotFoundException("empty station occurred"));
    }
}
