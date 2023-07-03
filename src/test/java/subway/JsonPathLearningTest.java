package subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

public class JsonPathLearningTest {

    @Test
    void getFirstDepthKey() {
        String json = "{\n"
                + "  \"name\": \"owen\"\n"
                + "}";

        String name = JsonPath.read(json, "$.name");

        assertThat(name).isEqualTo("owen");
    }

    @Test
    void parseArrayField() {
        String json = "{\n"
                + "  \"arr\": [\n"
                + "    1,2,3\n"
                + "  ]\n"
                + "}";


        Integer firstElement = JsonPath.read(json, "$.arr[0]");
        assertThat(firstElement).isEqualTo(1);
    }

    @Test
    void parseJsonArray() {
        String json = "[\n"
                + "  1,2,3\n"
                + "]";

        Integer secondElement = JsonPath.read(json, "$.[1]");

        assertThat(secondElement).isEqualTo(2);
    }
}
