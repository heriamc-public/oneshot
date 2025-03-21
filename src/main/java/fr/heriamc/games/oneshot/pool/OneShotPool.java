package fr.heriamc.games.oneshot.pool;

import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.data.OneShotDataManager;
import lombok.Setter;

import java.util.function.Supplier;

@Setter
public class OneShotPool extends GamePool<OneShotGame> {

    private OneShotDataManager dataManager;

    public OneShotPool() {
        super(OneShotGame.class, "OneShot Pool", HeriaServerType.ONESHOT, 0, 5, DirectConnectStrategy.FILL_GAME);
    }

    @Override
    public Supplier<OneShotGame> newGame() {
        // USELESS ???
        if (dataManager == null) throw new NullPointerException("DATA MANAGER NOT INITIALIZED");

        return () -> new OneShotGame(dataManager);
    }

}