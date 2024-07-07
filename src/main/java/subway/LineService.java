package subway;

import java.util.List;

public interface LineService {

    void createLine(LineCreateRequest lineCreateRequest);
    void updateLine(LineUpdateDTO lineUpdateDTO);
    void deleteLine(Long id);
}
