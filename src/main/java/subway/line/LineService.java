package subway.line;

import org.springframework.stereotype.Service;
import subway.error.ErrorMessage;
import subway.station.Station;
import subway.station.StationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Line newLine = Line.createLine(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance());
        Line saveLine = lineRepository.save(newLine);
        return LineResponse.fromLine(saveLine, upStation, downStation);
    }


    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Optional<Line> resLine = lineRepository.findById(id);
        return createLineResponse(resLine.orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_LINE.message))
        );
    }

    @Transactional
    public LineResponse updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        Optional<Line> resLine = lineRepository.findById(id);
        lineRepository.delete(resLine.orElseThrow());
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
        return LineResponse.fromLine(line, upStation, downStation);
    }

    @Transactional
    public LineAppendResponse appendLine(Long id, LineAppendRequest lineAppendRequest) {
        Line line = fineLineById(id);

        Long appendUpStationId = Long.parseLong(lineAppendRequest.getUpStationId());
        findStationById(appendUpStationId);

        Long appendDownStationsId = Long.parseLong(lineAppendRequest.getDownStationId());
        findStationById(appendDownStationsId);

        validateAppendLine(line, appendUpStationId, appendDownStationsId);

        Long appendDistance = lineAppendRequest.getDistance();
        line.appendSelection(appendUpStationId, appendDownStationsId, appendDistance);
        return LineAppendResponse.fromLine(line);
    }

    @Transactional
    public LineRemoveResponse removeLine(Long id, Long stationId) {
        Line line = fineLineById(id);
        findStationById(stationId);

        validateRemoveLine(line, stationId);

        line.removeSelection(stationId);
        return LineRemoveResponse.fromLine(line);
    }

    private Line fineLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_DATA_LINE.message));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.NO_DATA_STATION.message)
        );
    }

    private void validateAppendLine(Line line, Long upStationId, Long downStationId){
        if(!upStationId.equals(line.getDownStationId())){
            throw new IllegalStateException("추가되는 상행은, 기존 하행과 같아야 됩니다.");
        }

        if(line.getStations().contains(downStationId)){
            throw new IllegalStateException("추가되는 하행은, 기존 노선에 없어야 됩니다. ");
        }
    }

    private void validateRemoveLine(Line line, Long removeStationId) {
        if(!line.getStations().contains(removeStationId)){
            throw new IllegalStateException("삭제될 구간은 노선에 포함되어 있어야 합니다.");
        }

        if (line.getStations().size() == 2) {
            throw new IllegalStateException("구간이 1개인 경우 역을 삭제 할 수 없습니다.");
        }
    }

}
