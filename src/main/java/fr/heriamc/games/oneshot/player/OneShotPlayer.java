package fr.heriamc.games.oneshot.player;

import fr.heriamc.api.user.unlock.HeriaUnlockable;
import fr.heriamc.games.engine.ffa.player.FFAGamePlayer;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.player.kit.OneShotGameKit;
import fr.heriamc.games.oneshot.player.wallet.OneShotCoins;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class OneShotPlayer extends FFAGamePlayer {

    private final OneShotCoins points;
    private final OneShotGameKit kit;

    private final HeriaUnlockable unlockedCosmetics;
    private final Map<CosmeticType, Cosmetic> selectedCosmetics;

    private OneShotPlayerTime time;
    private OneShotPlayer lastAttacker;

    public OneShotPlayer(UUID uuid,
                         int kills,
                         int deaths,
                         int killStreak,
                         int bestKillStreak,
                         double wallet,
                         HeriaUnlockable unlockedCosmetics,
                         Map<CosmeticType, Cosmetic> selectedCosmetics,
                         OneShotPlayerTime time,
                         boolean spectator)
    {
        super(uuid, kills, deaths, killStreak, bestKillStreak, spectator);
        this.points = new OneShotCoins(wallet);
        this.unlockedCosmetics = unlockedCosmetics;
        this.selectedCosmetics = selectedCosmetics;
        this.kit = new OneShotGameKit(this);
        this.time = time;
        this.lastAttacker = null;
    }

    public <C extends Cosmetic> C getSelectedCosmetic(CosmeticType type, Class<C> clazz) {
        return clazz.cast(selectedCosmetics.get(type));
    }

    public <C extends Cosmetic> boolean hasSelected(CosmeticType type, C cosmetic) {
        return selectedCosmetics.get(type) == cosmetic;
    }

    public void onKill() {
        player.setHealth(20);
        kit.addArrow();

        addKill();
        addKillStreak();
        points.add(2.0);
    }

    public void onDeath() {
        this.lastAttacker = null;
        player.setHealth(20);

        addDeath();
        resetKillStreak();
    }

    public void onDisconnect() {
        this.lastAttacker = null;

        addDeath();
        resetKillStreak();
    }

    public void reset() {
        this.kills = 0;
        this.deaths = 0;
        this.killStreak = 0;
        this.bestKillStreak = 0;
    }

    public void cleanUp() {
        var inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);

        player.setHealth(20);
        player.getActivePotionEffects().clear();
    }

    public void setTime(OneShotPlayerTime time) {
        this.time = time;
        player.setPlayerTime(time.getTicks(), false);
    }

}