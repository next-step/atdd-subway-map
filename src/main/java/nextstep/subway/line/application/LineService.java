package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.*;
import nextstep.subway.line.exception.*;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public CreatedLineResponse saveLine(LineRequest request) {
        try {
            Line newLine = request.toLine();
            newLine.addSection(getStationById(request.getUpStationId()),
                    getStationById(request.getDownStationId()), request.getDistance());
            Line persistLine = lineRepository.save(newLine);
            return CreatedLineResponse.of(persistLine);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateLineException("Duplicated Line");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long lineId) {
        return lineRepository.findById(lineId)
                .map(LineResponse::of)
                .orElseThrow(() -> new NoSuchLineException("Not found lineId"+lineId));
    }

    public void updateLine(final Long lineId, LineRequest lineRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(()-> new NoSuchLineException("No such line"));
        line.update(lineRequest.toLine());
    }

    public void deleteLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        if(line.isPresent()){
            lineRepository.delete(line.get());
        }
    }

    @Transactional
    public SectionResponse saveSection(final Long lineId, SectionRequest sectionRequest) {
        final Line line = getLineById(lineId);
        final Station upStation = getStationById(sectionRequest.getUpStationId());
        final Station downStation = getStationById(sectionRequest.getDownStationId());
        line.addSection(upStation, downStation, sectionRequest.getDistance());
        return SectionResponse.of(line.getLastSection());
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = getLineById(lineId);
        line.deleteStation(getStationById(stationId));
    }

    private Line getLineById(final Long lineId) {
        return  lineRepository.findById(lineId)
                .orElseThrow(()-> new NoSuchLineException("No such lineId: "+ lineId));
    }

    private Station getStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(()-> new NoSuchStationException("No such up station Id: " + stationId));
    }

}
