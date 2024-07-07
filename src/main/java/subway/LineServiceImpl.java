package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineServiceImpl implements LineService{

    @Transactional
    @Override
    public void createLine(LineCreateRequest lineCreateRequest) {

    }


    public LineResponse readLine(Long id) {
        return null;
    }


    public List<LineResponse> readLines() {
        return null;
    }

    @Transactional
    @Override
    public void updateLine(LineUpdateDTO lineUpdateDTO) {

    }

    @Transactional
    @Override
    public void deleteLine(Long id) {

    }
}
