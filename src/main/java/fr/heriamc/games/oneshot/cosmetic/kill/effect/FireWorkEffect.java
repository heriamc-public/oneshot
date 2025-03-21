package fr.heriamc.games.oneshot.cosmetic.kill.effect;

import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import fr.heriamc.games.oneshot.cosmetic.kill.KillEffectTask;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.concurrent.TimeUnit;

public class FireWorkEffect implements KillEffectTask {

    @Override
    public void run(OneShotPlayer gamePlayer, Location location) {
        var firework = new ItemStack(org.bukkit.Material.FIREWORK);
        var fireworkMeta = (FireworkMeta) firework.getItemMeta();
        var effect = FireworkEffect.builder().withColor(Color.RED).withFade(Color.WHITE).with(FireworkEffect.Type.BALL).trail(true).flicker(true).build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setItemMeta(fireworkMeta);

        var world = ((CraftWorld) location.getWorld()).getHandle();
        var entityFireworks = new EntityFireworks(world, location.getX(), location.getY(), location.getZ(), CraftItemStack.asNMSCopy(firework));

        var spawnPacket = new PacketPlayOutSpawnEntity(entityFireworks, 76);
        var metaPacket = new PacketPlayOutEntityMetadata(entityFireworks.getId(), entityFireworks.getDataWatcher(), true);
        var explodePacket = new PacketPlayOutEntityStatus(entityFireworks, (byte) 17);
        var destroyPacket = new PacketPlayOutEntityDestroy(entityFireworks.getId());

        var players = getPlayersConnectionAround(location, 20, 20, 20);
        var gamePlayerConnection = gamePlayer.getCraftPlayer().getHandle().playerConnection;

        if (!players.contains(gamePlayerConnection))
            players.add(gamePlayerConnection);

        players.forEach(connection -> {
            connection.sendPacket(spawnPacket);
            connection.sendPacket(metaPacket);
        });

        VirtualThreading.schedule(() -> players.forEach(connection -> {
            connection.sendPacket(explodePacket);
            connection.sendPacket(destroyPacket);
        }), 1, TimeUnit.SECONDS);
    }

}