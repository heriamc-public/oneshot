package fr.heriamc.games.oneshot.cosmetic;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.Material;

public interface Cosmetic {

    String getId();
    String getName();

    Material getIcon();
    int getPrice();
    HeriaRank getRequiredRank();

    CosmeticType getType();

    boolean isPurchasable();

    void select(OneShotPlayer gamePlayer);
    void deselect(OneShotPlayer gamePlayer);

    void setPrice(int price);
    void setRequiredRank(HeriaRank requiredRank);
    void setPurchasable(boolean purchasable);

    void buy(OneShotPlayer gamePlayer);

    boolean has(OneShotPlayer gamePlayer);
    boolean hasRequiredRank(OneShotPlayer gamePlayer);

    boolean isSelected(OneShotPlayer gamePlayer);

    boolean canSelect(OneShotPlayer gamePlayer);
    boolean canBuy(OneShotPlayer gamePlayer);

}