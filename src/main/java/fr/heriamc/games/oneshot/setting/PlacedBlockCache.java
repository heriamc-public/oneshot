package fr.heriamc.games.oneshot.setting;

import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Getter
public class PlacedBlockCache {

    private final ConcurrentMap<Location, Block> placedBlock;

    /*
        NEED TO CHANGE THE WHOLE CLASS !!!!!!!!!!
     */

    public PlacedBlockCache() {
        this.placedBlock = new ConcurrentHashMap<>();
    }

    public void putBlock(Location location, Block block, long duration, TimeUnit timeUnit) {
        placedBlock.put(location, block);
        VirtualThreading.schedule(() -> removeBlock(location), duration, timeUnit);
    }

    public void putBlock(Location location, Block block) {
        putBlock(location, block, 20, TimeUnit.SECONDS);
    }

    public void removeBlock(Location location) {
        if (placedBlock.containsKey(location)) {
            var world = location.getWorld();

            placedBlock.remove(location);

            if (world == null) return;

            var block = world.getBlockAt(location);

            BukkitThreading.runTask(() -> block.setType(Material.AIR));
        }
    }

    public boolean contains(Location location) {
        return placedBlock.containsKey(location);
    }

    public void cleanUp() {
        getPlacedBlock().keySet().forEach(this::removeBlock);
    }

}