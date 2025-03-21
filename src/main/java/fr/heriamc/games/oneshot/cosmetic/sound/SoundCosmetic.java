package fr.heriamc.games.oneshot.cosmetic.sound;

import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;

public interface SoundCosmetic extends Cosmetic {

    Sound getSound();

    void play(OneShotPlayer gamePlayer, Location location);

}