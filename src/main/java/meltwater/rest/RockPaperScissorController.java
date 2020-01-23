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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
public class RockPaperScissorController {

    public final static String GAME_PATH = "/game";
    private final MatchService service;
    private final Score score = new Score();

    @PostMapping(GAME_PATH)
    public GameResponse game(@RequestBody final GameRequest request) {
        boolean isValid = HandType.KEYS.contains(request.getHand());

        if (isValid) {
            final HandType computerHand = service.generateComputerHand();
            final GameResultType result = service.getPlayerGameResult(HandType.valueOf(request.getHand()), computerHand);

            final Score currentGameScore;
            synchronized (score) {
                switch(result) {
                    case WON: score.setWon(score.getWon() + 1); break;
                    case DRAW: score.setDraw(score.getDraw() + 1); break;
                    case LOST: score.setLost(score.getLost() + 1); break;
                }
                currentGameScore = Score.builder().won(score.getWon()).draw(score.getDraw()).lost(score.getLost()).build();
            }

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
