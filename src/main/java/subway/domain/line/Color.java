package subway.domain.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Color {
    RED("bg-red-600"),
    BLUE("bg-blue-600"),
    GREEN("bg-green-600"),
    ;

    private String name;

    public static Color findByName(String name) {
        return Arrays.asList(values())
                .stream()
                .filter(color -> color.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 색깔 이름 입니다."));
    }

}
