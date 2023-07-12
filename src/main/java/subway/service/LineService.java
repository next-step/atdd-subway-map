package subway.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(StationService stationService,LineRepository lineRepository,StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toEntity());
        return LineResponse.fromEntity(line);
    }


    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .map(LineResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));

        lineRepository.delete(line);
    }

    @Transactional
    public void saveSection(Long lineId,SectionRequest sectionRequest) {
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException("upStation not found"));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException("downStation not found"));
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));

        validateCreateSectionRequest(line, upStation.getId(), downStation.getId());

        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionRequest.getDistance())
                .build();

        line.getSections().add(section);
    }

    private void validateCreateSectionRequest(Line line,Long newUpStationId,Long newDownStationId) {
        List<Station> stationList = line.getStationList();
        //기존에 등록된 구간이 없을 경우
        if(stationList.isEmpty()){
            return;
        }
        Station lastStation = stationList.get(stationList.size()-1);
        if(!Objects.equals(newUpStationId, lastStation.getId())){
            throw new IllegalArgumentException("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역과 다릅니다");
        }

        boolean isNewDownStationIdAlreadyExists = stationList.stream()
                .anyMatch(station-> station.getId().equals(newDownStationId));

        if(isNewDownStationIdAlreadyExists){
            throw new IllegalArgumentException("새로운 구간의 하행역이 해당 노선에 이미 등록되어 있습니다.");
        }
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(IllegalArgumentException::new);;
        validateDeleteSectionRequest(line, station);

        line.getSections().remove(line.getSections().size() - 1);
    }

    private void validateDeleteSectionRequest(Line line,Station station){
        List<Section> sections = line.getSections();

        if (!sections.get(sections.size()-1).getUpStation().getId().equals(station.getId())){
            throw new IllegalArgumentException();
        }
        if(sections.size()<=1){
            throw new IllegalArgumentException();
        }

    }






}
