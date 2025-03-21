package fr.heriamc.games.oneshot.setting;

import fr.heriamc.api.game.GameState;
import fr.heriamc.games.engine.map.GameMapManager;
import fr.heriamc.games.engine.map.slime.SlimeGameMap;
import fr.heriamc.games.engine.map.slime.SlimeMap;
import fr.heriamc.games.engine.map.slime.SlimeWorldLoader;
import fr.heriamc.games.engine.point.MultiplePoint;
import fr.heriamc.games.engine.utils.json.ConfigLoader;
import fr.heriamc.games.oneshot.OneShotGame;
import lombok.Getter;

@Getter
public class OneShotMapManager extends GameMapManager<OneShotGame, SlimeMap, SlimeWorldLoader> {

    private final SlimeMap arenaMap;

    private MultiplePoint spawnPoints;

    public OneShotMapManager(OneShotGame game) {
        super(game, new SlimeWorldLoader());
        this.arenaMap = new SlimeGameMap(getFormattedTypeMapName("arena"), getFormattedMapTemplateName("nuketown2025"));
        this.spawnPoints = new MultiplePoint("random");
    }

    @Override
    public void setup() {
        addMap(arenaMap);

        getMapLoader().load(arenaMap).whenComplete((slimeMap, throwable) -> {
            var configuration = ConfigLoader.loadConfig("OneShot", slimeMap.getTemplateName(), OneShotGameConfiguration.class);

            game.getSettings().setBuildMaxY(configuration.getBuildYMax());
            game.getLobby().setSpawnPoint(configuration.getSpawn().setWorld(slimeMap));
            slimeMap.setSpawn(configuration.getSpawn().setWorld(slimeMap));

            configuration.getRandom().getPoints().forEach(point -> point.setWorld(slimeMap));

            slimeMap.getWorld().setGameRuleValue("doFireTick", "false");

            this.spawnPoints = configuration.getRandom();
            game.setState(GameState.ALWAYS_PLAYING);
        });
    }

    @Override
    public void end() {
        delete(arenaMap);
    }

}