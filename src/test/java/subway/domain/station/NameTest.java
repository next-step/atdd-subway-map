package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 역 이름 테스트")
public class NameTest {
    @Test
    @DisplayName("지하철 역 이름 길이 규칙 위반 : 20자 초과")
    void test1() {
        String name = "123456789012345678901234567890";
        assertThatThrownBy(() -> {
            new Name(name);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지하철 역 이름 길이 규칙 위반 : 0자")
    void test2() {
        String name = "";
        assertThatThrownBy(() -> {
            new Name(name);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}