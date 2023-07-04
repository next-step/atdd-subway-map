package subway.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.vo.LineRequest;
import subway.vo.LineResponse;
import subway.vo.StationResponse;

import java.util.ArrayList;


@Service
@Transactional(readOnly = true)
public class LineService {

    public LineResponse saveLine(LineRequest lineRequest) {
        return null;
    }
}
