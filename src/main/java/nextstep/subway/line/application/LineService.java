package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.line.exception.DuplicateLineException;
import nextstep.subway.line.exception.IllegalSectionArgument;
import nextstep.subway.line.exception.NoSuchLineException;
import nextstep.subway.line.exception.NoSuchStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public LineResponse saveLine(LineRequest request) {
        try {
            Line newLine = request.toLine();
            newLine.addSection(makeSectionHelper(request));
            Line persistLine = lineRepository.save(newLine);
            return LineResponse.of(persistLine);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateLineException("Duplicated Line");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        return line.map(LineResponse::of)
                .orElseThrow(() -> new NoSuchLineException("Not found lineId"+lineId));
    }

    public void updateLine(final Long lineId, LineRequest lineRequest) {
        Optional< Line > optionalLine = lineRepository.findById(lineId);
        if(!optionalLine.isPresent()) {
            throw new NoSuchLineException("No such line");
        }
        optionalLine.get().update(lineRequest.toLine());
    }

    public void deleteLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        if(line.isPresent()){
            lineRepository.delete(line.get());
        }
    }

    public SectionResponse saveSection(final Long lineId, SectionRequest sectionRequest) {
        final Line line = getLineById(lineId);
        final Station upStation = getStationById(sectionRequest.getUpStationId());
        final Station downStation = getStationById(sectionRequest.getDownStationId());

        line.addSection(new Section(upStation, downStation, sectionRequest.getDistance()));
        lineRepository.save(line);
        return SectionResponse.of(line.getLastSection());
    }

    private Line getLineById(final Long lineId) {
        Optional<Line> optionalLine = lineRepository.findById(lineId);
        return optionalLine.orElseThrow(()-> new NoSuchLineException("No such lineId: "+ lineId));
    }

    private Station getStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(()-> new NoSuchStationException("No such up station Id: " + stationId));
    }

    private Section makeSectionHelper(LineRequest request){
        try{
            Station upStation = getStationById(request.getUpStationId());
            Station downStation = getStationById(request.getDownStationId());
            return Section.of(upStation, downStation, request.getDistance());
        } catch (IllegalArgumentException e) {
            throw new IllegalSectionArgument(e.getMessage());
        }
    }
}
