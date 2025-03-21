package fr.heriamc.games.oneshot.player.data;

import com.google.gson.annotations.SerializedName;
import fr.heriamc.api.data.SerializableData;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.player.OneShotPlayerTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class OneShotPlayerData implements SerializableData<UUID> {

    @SerializedName("id")
    private UUID identifier;

    private int kills, deaths, killStreak, bestKillStreak;
    private double points;
    private Map<CosmeticType, String> selectedCosmetics;
    private OneShotPlayerTime time;

    public void updateStats(OneShotPlayer gamePlayer) {
        this.kills = gamePlayer.getKills();
        this.deaths = gamePlayer.getDeaths();
        this.killStreak = gamePlayer.getKillStreak();
        this.bestKillStreak = gamePlayer.getBestKillStreak();
        this.points = gamePlayer.getPoints().getWallet();
        this.selectedCosmetics = gamePlayer.getSelectedCosmetics().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getId()));
        this.time = gamePlayer.getTime();
    }

    public void updateStatsOnDisconnect() {
        this.kills += 1;
        this.killStreak += 1;
        this.bestKillStreak = Math.max(killStreak, bestKillStreak);
        this.points += 2;
    }

}