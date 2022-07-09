package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    public LineDto createLine(LineCreateDto lineDto) {

        return LineDto.builder().build();
    }
}
