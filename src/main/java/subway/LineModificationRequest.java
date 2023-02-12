package subway;

import javax.validation.constraints.Size;

public class LineModificationRequest {
    @Size(min = 0, max = 20)
    private String name;
    @Size(min = 0, max = 20)
    private String color;


    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
