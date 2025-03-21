package fr.heriamc.games.oneshot.listener;

import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.engine.ffa.player.FFAGamePlayerState;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetic;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetic;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetic;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetics;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotKillStreakMessage;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerDamageListener implements Listener {

    private final GameManager<OneShotGame> gameManager;

    private final Set<Material> swords;
    private final EnumSet<EntityDamageEvent.DamageCause> damageCauses;

    public PlayerDamageListener(GameManager<OneShotGame> gameManager) {
        this.gameManager = gameManager;
        this.swords = Arrays.stream(SwordCosmetics.values()).map(SwordCosmetic::getSword).collect(Collectors.toSet());
        this.damageCauses = EnumSet.of(
                EntityDamageEvent.DamageCause.VOID,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.SUFFOCATION,
                EntityDamageEvent.DamageCause.FALLING_BLOCK,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.DROWNING,
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.LIGHTNING,
                EntityDamageEvent.DamageCause.MAGIC,
                EntityDamageEvent.DamageCause.POISON,
                EntityDamageEvent.DamageCause.WITHER
        );
    }

    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player victim) {
            var game = gameManager.getNullableGame(victim);

            if (game == null) return;

            var gamePlayer = game.getNullablePlayer(victim);

            if (gamePlayer == null) return;

            if (event.getDamager() instanceof Player a) {
                var weapon = a.getItemInHand();
                var attacker = game.getNullablePlayer(a);

                if (weapon == null || attacker == null) return;

                event.setDamage(1.0);
                gamePlayer.setLastAttacker(attacker);

                if (swords.contains(weapon.getType()))
                    event.setDamage(EntityDamageEvent.DamageModifier.BASE, (victim.getMaxHealth() / 3.0) + 0.1);

                if (victim.getHealth() - event.getDamage() <= 0) {
                    handleDeath(game, gamePlayer.getLastAttacker(), gamePlayer);
                    event.setCancelled(true);
                }

                return;
            }

            if (event.getDamager() instanceof Projectile
                    && event.getDamager() instanceof Arrow arrow
                    && arrow.getShooter() instanceof Player shooterPlayer) {

                event.setCancelled(true);

                var shooter = game.getNullablePlayer(shooterPlayer);

                if (shooter == null
                        || shooter.equals(gamePlayer)
                        || gamePlayer.getState() == FFAGamePlayerState.IN_LOBBY) return;

                gamePlayer.setLastAttacker(shooter);

                event.setDamage(victim.getMaxHealth());
                shooter.getInventory().remove(Material.ARROW);

                handleDeathByArrow(game, shooter, gamePlayer);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            var game = gameManager.getNullableGame(player);

            if (game == null) return;

            var gamePlayer = game.getNullablePlayer(player);

            if (gamePlayer == null) return;

            // NEED TO BE RETURN ?
            if (gamePlayer.getState() == FFAGamePlayerState.IN_LOBBY)
                event.setCancelled(true);

            if (damageCauses.contains(event.getCause())) {
                event.setCancelled(true);
                return;
            }

            if (event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
                onEntityDamage(entityDamageByEntityEvent);
                return;
            }

            if (!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)
                    && player.getHealth() - event.getDamage() <= 0) {

                event.setCancelled(true);
                handleDeath(game, gamePlayer.getLastAttacker(), gamePlayer);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    private void handleDeathByArrow(OneShotGame game, OneShotPlayer shooter, OneShotPlayer victim) {
        var distance = shooter.getLocation().distance(victim.getLocation());

        sendKillMessage(shooter, victim, "§7(Distance: §b" + String.format("%.2f", distance) + "§7)");

        playSoundCosmetic(shooter, victim);
        playEffectCosmetic(shooter, victim);

        shooter.onKill();
        victim.onDeath();

        OneShotKillStreakMessage.sendMessage(game, shooter.getKillStreak(), shooter.getName());
        game.getMessage().sendDistanceMessage((int) distance, shooter);

        victim.getCraftPlayer().getHandle().getDataWatcher().watch(9, (byte) 0);
        game.getLobby().onSetup(game, victim);
    }

    private void handleDeath(OneShotGame game, OneShotPlayer attacker, OneShotPlayer victim) {
        if (attacker != null) {
            sendKillMessage(attacker, victim, "");

            playSoundCosmetic(attacker, victim);
            playEffectCosmetic(attacker, victim);

            attacker.onKill();
            OneShotKillStreakMessage.sendMessage(game, attacker.getKillStreak(), attacker.getName());
        }

        victim.onDeath();
        game.getLobby().onSetup(game, victim);
    }

    private void playEffectCosmetic(OneShotPlayer attacker, OneShotPlayer victim) {
        var effect = attacker.getSelectedCosmetic(CosmeticType.KILL_EFFECT, KillCosmetic.class);

        if (effect == null || effect == KillCosmetics.NONE) return;

        effect.play(attacker, victim.getLocation());
    }

    private void playSoundCosmetic(OneShotPlayer attacker, OneShotPlayer victim) {
        var sound = attacker.getSelectedCosmetic(CosmeticType.SOUND_EFFECT, SoundCosmetic.class);

        if (sound == null || sound == SoundCosmetics.NONE) return;

        sound.play(attacker, victim.getLocation());
    }

    private void sendKillMessage(OneShotPlayer attacker, OneShotPlayer victim, String distance) {
        OneShotMessages.KILL_REWARD_MESSAGE.sendAsActionBar(attacker, 2);

        victim.sendMessage(OneShotMessages.KILL_VICTIM_MESSAGE.getMessage(attacker.getName(), distance));
        attacker.sendMessage(OneShotMessages.KILL_KILLER_MESSAGE.getMessage(victim.getName(), distance));
    }

}