package fr.heriamc.games.oneshot.cosmetic.kill;

import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.Location;

public interface KillCosmetic extends Cosmetic {

    void play(OneShotPlayer attacker, Location location);

    KillEffectTask getTask();

}