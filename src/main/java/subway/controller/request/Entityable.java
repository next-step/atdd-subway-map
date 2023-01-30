package subway.controller.request;

public interface Entityable<E> {

    default E toEntity() {
        return null;
    }
}

