package meltwater.rest;

import static meltwater.domain.GameResultType.UNKNOWN;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meltwater.domain.GameRequest;
import meltwater.domain.GameResponse;
import meltwater.domain.GameResultType;
import meltwater.domain.HandType;
import meltwater.domain.Score;
import meltwater.service.MatchService;
import meltwater.service.ScoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
public class RockPaperScissorController {

    public final static String GAME_PATH = "/game";
    private final MatchService matchService;
    private final ScoreService scoreService;

    @PostMapping(GAME_PATH)
    public GameResponse game(@RequestBody final GameRequest request) {
        boolean isValid = HandType.KEYS.contains(request.getHand());

        if (isValid) {
            final HandType computerHand = matchService.generateComputerHand();
            final GameResultType result = matchService.getPlayerGameResult(HandType.valueOf(request.getHand()), computerHand);
            final Score currentGameScore = scoreService.calculate(result);

            return GameResponse.builder()
                .result(result)
                .computerHand(computerHand)
                .score(currentGameScore)
                .build();
        } else {
            log.info("Invalid user input {}", request.getHand());
            return GameResponse.builder()
                .result(UNKNOWN)
                .build();
        }
    }

}
