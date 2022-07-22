package nextstep.subway.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ObjectMapUtils() {}

    public static <D, T> List<D> mapAll(final Collection<T> collection, Class<D> outputClass) {
        return collection.stream()
                .map(entity -> map(entity, outputClass))
                .collect(Collectors.toList());
    }

    public static <D, T> List<D> mapLooseAll(final Collection<T> collection, Class<D> outputClass) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        List<D> list = new ArrayList<>();
        for (T entity : collection) {
            D map = looseMap(entity, outputClass);
            list.add(map);
        }
        return list;
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> D looseMap(final T entity, Class<D> outClass) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(entity, outClass);
    }
}
