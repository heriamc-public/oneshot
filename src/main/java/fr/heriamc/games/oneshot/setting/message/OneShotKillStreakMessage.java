package fr.heriamc.games.oneshot.setting.message;

import fr.heriamc.games.engine.utils.CollectionUtils;
import fr.heriamc.games.oneshot.OneShotGame;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OneShotKillStreakMessage {

    DOUBLE_KILL (2,
            "§7❱❱ §c%s §7explose tout avec un §c§ldouble kill §7!",
            "§7❱❱ §c%s §7laisse un sillage de destruction, §c§ldouble kill §7!"),

    TRIPLE_KILL (3,
            "§7❱❱ §c%s §7est en feu avec un §c§ltriplé §7!",
            "§7❱❱ §c%s §7enchaîne avec un §c§ltriplé §7!",
            "§7❱❱ §c%s §7fait trembler l’arène, §c§ltriplé §7!",
            "§7❱❱ §c%s §7déclenche une tempête de kills, §c§ltriplé §7!"),

    QUADRA_KILL (4, "§7❱❱ §c%s §7fait un massacre, §c§lquadruplé §7!",
            "§7❱❱ §c%s §7règne sur le champ de bataille, §c§lquadruplé §7!"),

    PENTA_KILL (5,
            "§7❱❱ §c%s §7a réussi un §c§lquintuplé §7!",
            "§7❱❱ §c%s §7fait un carnage ! §c§l5 ÉLIMINATIONS",
            "§7❱❱ §c%s §7est imparable, §c§lquintuple élimination §7!",
            "§7❱❱ §c%s §7est une machine à tuer, §c§lquintuplé §7!",
            "§7❱❱ §c%s §7est une §6légende§7, §c§lquintuple élimination §7!",
            "§7❱❱ §c%s §7ne laisse personne debout, §c§lquintuplé §7!");

    private final int kills;
    private final String[] messages;

    private static final OneShotKillStreakMessage[] killStreakMessages = values();

    OneShotKillStreakMessage(int kills, String... messages) {
        this.kills = kills;
        this.messages = Arrays.copyOf(messages, messages.length);
    }

    public static void sendMessage(OneShotGame game, int kills, Object... objects) {
        for (OneShotKillStreakMessage message : killStreakMessages) {
            if (message.kills == kills)
                game.broadcast(message.getMessage(objects));
        }
    }

    public String getMessage(Object... objects) {
        return CollectionUtils.oldRandom(messages).orElseThrow().formatted(objects);
    }

}