package fr.heriamc.games.oneshot.listener;

import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.engine.ffa.player.FFAGamePlayerState;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetic;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetics;
import fr.heriamc.games.oneshot.setting.PlacedBlockCache;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class PlayerBlockListener implements Listener {

    private final GameManager<OneShotGame> gameManager;
    private final PlacedBlockCache placedBlockCache;

    private final Set<Material> allowedBlock;

    public PlayerBlockListener(GameManager<OneShotGame> gameManager) {
        this.gameManager = gameManager;
        this.placedBlockCache = new PlacedBlockCache();
        this.allowedBlock = BlockCosmetics.blocks.stream().map(BlockCosmetic::getMaterial).collect(Collectors.toSet());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var block = event.getBlockPlaced();
        var location = block.getLocation();
        var game = gameManager.getNullableGame(player);

        if (game == null) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null) return;

        if (gamePlayer.getState() == FFAGamePlayerState.IN_LOBBY) {
            event.setCancelled(true);
            return;
        }

        if (allowedBlock.contains(player.getInventory().getItemInHand().getType())
                && allowedBlock.contains(block.getType())) {

            // USELESS ?
            event.setCancelled(false);

            if (location.getY() >= game.getSettings().getBuildMaxY()) {
                player.sendMessage(OneShotMessages.MAX_BUILD_HEIGHT_REACHED.getMessage());
                event.setCancelled(true);
                return;
            }

            var stack = player.getItemInHand().clone();
            stack.setAmount(64);

            player.getInventory().setItemInHand(stack);
            player.updateInventory();
            placedBlockCache.putBlock(location, block);
        } else
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();
        var location = block.getLocation();
        var game = gameManager.getNullableGame(player);

        block.getDrops().clear();

        if (game == null) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null) return;

        if (gamePlayer.getState() == FFAGamePlayerState.IN_LOBBY) {
            event.setCancelled(true);
            return;
        }

        if (placedBlockCache.getPlacedBlock().remove(location, block))
            block.setType(Material.AIR);

        event.setCancelled(true);
    }

}