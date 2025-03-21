package fr.heriamc.games.oneshot.cosmetic.kill;

import fr.heriamc.games.oneshot.player.OneShotPlayer;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public interface KillEffectTask {

    void run(OneShotPlayer attacker, Location location);

    default List<Player> getPlayersAround(Location location, double x, double y, double z) {
        return location.getWorld()
                .getNearbyEntities(location, x, y, z).stream()
                .filter(entity -> entity instanceof Player)
                .map(Player.class::cast)
                .collect(Collectors.toList());
    }

    default List<PlayerConnection> getPlayersConnectionAround(Location location, double x, double y, double z) {
        return location.getWorld()
                .getNearbyEntities(location, x, y, z).stream()
                .filter(entity -> entity instanceof Player)
                .map(Player.class::cast)
                .map(player -> ((CraftPlayer) player).getHandle().playerConnection)
                .collect(Collectors.toList());
    }

}