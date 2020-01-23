package meltwater.domain;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HandType {
    ROCK,
    PAPER,
    SCISSORS,
    LIZARD,
    SPOCK;

    public static final ImmutableSet<String> KEYS = ImmutableSet.copyOf(
        Arrays.stream(HandType.values()).map(Enum::name).toArray(String[]::new));
}
