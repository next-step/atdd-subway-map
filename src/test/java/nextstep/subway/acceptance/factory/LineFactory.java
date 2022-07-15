package nextstep.subway.acceptance.factory;

import io.restassured.response.ValidatableResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineFactory {
    public static Line mock(String name, String color) {
        return new Line(name, color);
    }

    public static Line 신분당선() {
        return new Line("신분당선", "red");
    }

    public static Line 분당선() {
        return new Line("분당선", "green");
    }

    public static Line 경춘선() {
        return new Line("경춘선", "blue");
    }

    public static Line 우이신설() {
        return new Line("우이신설", "blue");
    }

    public static Line 경의중앙선() {
        return new Line("경의중앙선", "blue");
    }

}


