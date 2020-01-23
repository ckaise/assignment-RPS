package meltwater.service;

import meltwater.domain.GameResultType;
import meltwater.domain.Score;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {
    private final Score score = new Score();

    public synchronized Score calculate(GameResultType gameResult) {
        switch (gameResult) {
            case WON: score.setWon(score.getWon() + 1); break;
            case DRAW: score.setDraw(score.getDraw() + 1); break;
            case LOST: score.setLost(score.getLost() + 1); break;
        }
        return Score.builder().won(score.getWon()).draw(score.getDraw()).lost(score.getLost()).build();
    }
}
