package nextstep.subway.line.application;

import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private LineRepository lineRepository;

    private LineResponse created;
    private List<LineResponse> createdLines = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        created = lineService.saveLine(new LineRequest("1호선", "blue"));;
        createdLines.add(created);
        createdLines.add(lineService.saveLine(new LineRequest("2호선", "green")));
        createdLines.add(lineService.saveLine(new LineRequest("3호선", "brown")));
    }

    @AfterEach
    public void clean(){
        lineRepository.deleteAll();
    }

    @DisplayName("노선을 등록한다.")
    @Test
    public void findLineById() {
        //Given
        LineResponse createdResponse = lineService.saveLine(new LineRequest("5호선", "purple"));

        //When
        LineResponse response = lineService.findLineById(createdResponse.getId());

        //Then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(createdResponse.getId()),
                () -> assertThat(response.getName()).isEqualTo(createdResponse.getName()),
                () -> assertThat(response.getCreatedDate()).isEqualTo(createdResponse.getCreatedDate()),
                () -> assertThat(response.getModifiedDate()).isEqualTo(createdResponse.getModifiedDate())
        );
    }

    @Test
    public void notFoundLineException() {
        assertThatThrownBy(() -> {
                 Long id = lineService.saveLine(new LineRequest("6호선", "brown")).getId();
                 lineService.findLineById(id + 1);
        }).isInstanceOf(NotFoundLineException.class);
    }

    @Test
    public void updateLine() {
        //Given
        String postFixColor= "-600";
        LineRequest lineRequest = new LineRequest(created.getName(), created.getColor()+postFixColor);

        //When
        lineService.updateLine(created.getId(), lineRequest);
        LineResponse updatedResponse = lineService.findLineById(created.getId());

        //Then
        assertThat(updatedResponse.getColor()).isEqualTo(created.getColor()+postFixColor);
    }

    @Test
    public void deleteLine() {
        assertThatThrownBy(() -> {
            lineService.deleteLineById(created.getId());
            lineService.findLineById(created.getId());
        }).isInstanceOf(NotFoundLineException.class);
    }

    @Test
    public void findAllLines() {
        //Given & When
        List<LineResponse> lines = lineService.findAllLines();

        //Then
        assertThat(lines.size()).isEqualTo(createdLines.size());
    }
}
