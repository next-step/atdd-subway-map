package subway.line;

import common.JpaRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

@DisplayName("지하철 노선 관련 Repository 단위 테스트")

class LineRepositoryTest extends JpaRepositoryTest<Line, Long> {

    @Autowired
    private LineRepository lineRepository;


    @Override
    protected JpaRepository<Line, Long> repository() {
        return lineRepository;
    }

    @Override
    protected Line createTestInstance() {
        return new Line("line01", "bg-red-009", 100);
    }
}
