package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicatedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class LineService {
    public static final int MIN_SECTION_COUNT = 2;
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        validateDuplicatedLine(request);
        Station upStation =
                getStationById(request.getUpStationId());
        Station downStation =
                getStationById(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor());
        Section section = new Section(upStation, downStation, request.getDistance());
        line.setSection(section);

        Line saveLine = lineRepository.save(line);
        return new LineResponse(saveLine);
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(toList());
    }

    public LineResponse getLine(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);
        return new LineResponse(findLine);
    }

    public void modifyLine(Long lineId, LineRequest lineRequest) {
        Line findLine = lineRepository.findLineById(lineId);
        findLine.changeLine(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);
        lineRepository.delete(findLine);
    }


    public void addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findLineWithSectionsById(lineId);
        List<Section> sections = line.getSections();
        int sectionLastIndex = sections.size() - 1;

        Section section =
                sections.get(sectionLastIndex);
        Station upStation =
                getStationById(request.getUpStationId());
        Station downStation =
                getStationById(request.getDownStationId());

        if (section.downStationNotSameAs(upStation)) {
            throw new IllegalArgumentException("구간을 추가할 수 없습니다.");
        }

        if (line.stationNoneMach(downStation)){
            throw new IllegalArgumentException("구간을 추가할 수 없습니다.");
        }

        line
            .setSection(Section.of(upStation, downStation, request.getDistance()));
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findLineWithSectionsById(lineId);
        List<Section> sections = line.getSections();
        int size = sections.size();

        if(size < MIN_SECTION_COUNT){
            throw new IllegalArgumentException("삭제할 수 있는 구간이 존재하지 않습니다");
        }

        Section lastSection = sections.get(size - 1);
        if(lastSection.getDownStation().getId() == stationId){
            sections.remove(lastSection);
        }
    }



    private void validateDuplicatedLine(LineRequest request) {
        boolean existsLine = lineRepository.existsLineByName(request.getName());
        if(existsLine) {
            throw new DuplicatedException("중복된 라인을 생성할 수 없습니다.");
        }
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역 역니다."));
    }

}
