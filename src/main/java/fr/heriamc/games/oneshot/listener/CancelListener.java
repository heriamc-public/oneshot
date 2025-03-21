package fr.heriamc.games.oneshot.listener;

import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.engine.utils.MaterialUtils;
import fr.heriamc.games.oneshot.OneShotAddon;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.lobby.OneShotLobbyItems;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public record CancelListener(OneShotAddon addon, GameManager<OneShotGame> gameManager) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var action = event.getAction();
        var game = gameManager.getNullableGame(player);

        if (game == null || !game.containsPlayer(player)) return;

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            var block = event.getClickedBlock();

            if (block != null && MaterialUtils.isInteractable(block.getType())) {
                event.setCancelled(true);
                return;
            }
        }

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null) return;

        switch (gamePlayer.getState()) {
            case IN_LOBBY -> {
                var itemStack = event.getItem();

                if (itemStack != null && itemStack.hasItemMeta())
                    OneShotLobbyItems.getLobbyItem(itemStack)
                            .ifPresent(item -> item.getConsumer().accept(addon, game, gamePlayer));

                event.setCancelled(true);
            }
            case IN_GAME -> {
                var itemStack  = event.getItem();

                event.setCancelled(false);

                // REWORK THIS
                if (itemStack != null
                        && itemStack.getType() == Material.BOW
                        && itemStack.hasItemMeta()) {

                    if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                        var vector = player.getLocation().getDirection();

                        vector.multiply(-1);
                        vector.setY(0);
                        vector = vector.normalize().multiply(1.5);

                        player.setVelocity(vector);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        var player = (Player) event.getWhoClicked();
        var game = gameManager.getNullableGame(player);

        if (game == null || !game.containsPlayer(player)) return;

        var gamePlayer = game.getNullablePlayer(player);

        if (gamePlayer == null) return;

        switch (gamePlayer.getState()) {
            case IN_LOBBY -> event.setCancelled(true);
            case IN_GAME -> event.setCancelled(false);
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow)
            arrow.remove();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}