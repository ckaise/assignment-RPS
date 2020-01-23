package meltwater.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import meltwater.domain.GameResultType;
import meltwater.domain.HandType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchService {

    public static final ImmutableMap<HandType, Set<HandType>> WIN_LOSE_MAP =
        ImmutableMap.of(HandType.ROCK, ImmutableSet.of(HandType.SCISSORS, HandType.LIZARD),
                        HandType.PAPER, ImmutableSet.of(HandType.ROCK, HandType.SPOCK),
                        HandType.SCISSORS, ImmutableSet.of(HandType.PAPER, HandType.LIZARD),
                        HandType.LIZARD, ImmutableSet.of(HandType.PAPER, HandType.SPOCK),
                        HandType.SPOCK, ImmutableSet.of(HandType.ROCK, HandType.SCISSORS));

    public HandType generateComputerHand() {
        int num = ThreadLocalRandom.current().nextInt(HandType.KEYS.size());
        return HandType.values()[num];
    }

    public GameResultType getPlayerGameResult(final HandType player, final HandType computer) {
        if (player == computer) {
            return GameResultType.DRAW;
        } else if (WIN_LOSE_MAP.get(player).contains(computer)) {
            return GameResultType.WON;
        } else {
            return GameResultType.LOST;
        }
    }
}
