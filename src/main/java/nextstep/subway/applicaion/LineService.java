package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    @Transactional
    public LineResponse saveStationLine(LineRequest lineRequest) {
        return null;
    }

    public List<LineResponse> findAllStationsLines() {
        return null;
    }

    public LineResponse findStationLine(Long id) {
        return null;
    }

    @Transactional
    public void updateStationLine(LineUpdateRequest request, Long id) {

    }

    public void deleteStationLine(Long id) {
    }
}
