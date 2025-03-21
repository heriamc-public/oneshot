package fr.heriamc.games.oneshot.cosmetic.kill;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.cosmetic.kill.effect.BloodEffect;
import fr.heriamc.games.oneshot.cosmetic.kill.effect.FireWorkEffect;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum KillCosmetics implements KillCosmetic {

    NONE ("oneshot.effects.none", "Aucun", null, Material.BARRIER, 0, HeriaRank.PLAYER, false),
    FIRE_WORK ("oneshot.effects.firework", "§6Feu d'artifice", new FireWorkEffect(), Material.FIREWORK, 1000, HeriaRank.PLAYER, true),
    BLOOD ("oneshot.effects.blood", "§cSanglant", new BloodEffect(), Material.REDSTONE, 1000, HeriaRank.PLAYER, true);

    private final String id, name;
    private final KillEffectTask task;

    private final Material icon;
    private int price;
    private HeriaRank requiredRank;

    private boolean purchasable;

    public static final List<KillCosmetics> effects = Arrays.asList(values());

    public static KillCosmetic getFromId(String id) {
        return effects.stream().filter(effect -> effect.getId().equals(id)).findFirst().orElse(NONE);
    }

    public static KillCosmetic getFromIdNullable(String id) {
        return effects.stream().filter(sound -> sound.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void play(OneShotPlayer attacker, Location location) {
        if (task == null) return;

        VirtualThreading.runAsync(() -> task.run(attacker, location));
    }

    @Override
    public CosmeticType getType() {
        return CosmeticType.KILL_EFFECT;
    }

    @Override
    public void select(OneShotPlayer gamePlayer) {
        gamePlayer.getSelectedCosmetics().put(CosmeticType.KILL_EFFECT, this);
    }

    @Override
    public void deselect(OneShotPlayer gamePlayer) {
        gamePlayer.getSelectedCosmetics().put(CosmeticType.KILL_EFFECT, NONE);
    }

    @Override
    public void buy(OneShotPlayer gamePlayer) {
        gamePlayer.getUnlockedCosmetics().unlock(id);
        gamePlayer.getPoints().remove((double) price);
    }

    @Override
    public boolean has(OneShotPlayer gamePlayer) {
        return gamePlayer.getUnlockedCosmetics().isUnlocked(id);
    }

    @Override
    public boolean hasRequiredRank(OneShotPlayer gamePlayer) {
        return gamePlayer.getHeriaPlayer().getRank().getPower() >= requiredRank.getPower();
    }

    @Override
    public boolean isSelected(OneShotPlayer gamePlayer) {
        return gamePlayer.hasSelected(CosmeticType.KILL_EFFECT, this);
    }

    @Override
    public boolean canSelect(OneShotPlayer gamePlayer) {
        return has(gamePlayer) && !isSelected(gamePlayer);
    }

    @Override
    public boolean canBuy(OneShotPlayer gamePlayer) {
        return purchasable && gamePlayer.getPoints().getWallet() >= price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setRequiredRank(HeriaRank requiredRank) {
        this.requiredRank = requiredRank;
    }

    @Override
    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

}