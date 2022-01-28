package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.exception.AlreadyRegisteredLineException;
import nextstep.subway.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private LineVerificationService lineVerificationService;
    private StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, LineVerificationService lineVerificationService, StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.lineVerificationService = lineVerificationService;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        String lineName = request.getName();
        if (lineVerificationService.isExistByName(lineName)) {
            throw new AlreadyRegisteredLineException(lineName);
        }

        PairedStations pairedStations = stationService.createPairedStations(request.getUpStationId(), request.getDownStationId());

        Line line = lineRepository.save(new Line(request.getName(),
                request.getColor(),
                pairedStations.getUpStation(),
                pairedStations.getDownStation(),
                request.getDistance()));

        return LineResponse.fromEntity(line);
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return LineResponse.fromEntity(findLineById(id));
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void update(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.save(line.update(lineRequest.toEntity()));
    }

    public void delete(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }

    public Section saveSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        Section section = line.addSection(upStation, downStation, request.getDistance());

        return sectionRepository.save(section);
    }

    public void deleteSectionByEndDownStationId(Long lineId, Long endDownStationId) {
        Line line = findLineById(lineId);
        line.deleteSection(stationService.findStationById(endDownStationId));
    }
}
