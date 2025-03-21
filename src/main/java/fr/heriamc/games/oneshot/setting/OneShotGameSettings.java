package fr.heriamc.games.oneshot.setting;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.games.engine.GameSettings;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.board.GameBoard;
import fr.heriamc.games.engine.board.GameBoardManager;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.board.OneShotGameBoard;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneShotGameSettings extends GameSettings<OneShotMapManager> {

    private int buildMaxY;

    public OneShotGameSettings(GameSize gameSize) {
        super(gameSize, new GameBoardManager());
        this.buildMaxY = 30;
    }

    @Override
    public GameBoard<?, ?> defaultBoard(MiniGame game, BaseGamePlayer gamePlayer) {
        return new OneShotGameBoard((OneShotGame) game, (OneShotPlayer) gamePlayer);
    }

}