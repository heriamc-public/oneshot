package fr.heriamc.games.oneshot.setting.message;

import fr.heriamc.games.engine.utils.CollectionUtils;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class OneShotMessage {

    private final OneShotGame game;

    private final Set<String> killDistanceMessages;

    private OneShotAutoMessage currentAutoMessage;

    /*
        Remake this
     */
    public OneShotMessage(OneShotGame game) {
        this.game = game;
        this.killDistanceMessages = new HashSet<>();
        this.currentAutoMessage = OneShotAutoMessage.NO_ALLIANCE;
    }

    public void setup() {
        addMessages(killDistanceMessages,
                "§7❱❱ §3%player_name% §7est en pleine forme ! Un tir magnifique à §b§l%distance% mètres §7vient d'être réalisé !",
                "§7❱❱ §3%player_name% §7est imparable ! Il vient d'envoyer un tir puissant à §b§l%distance% mètres §7!",
                "§7❱❱ §3%player_name% §7est impressionnant ! Il vient de tirer à §b§l%distance% mètres §7avec une précision incroyable !",
                "§7❱❱ §3%player_name% §7fait sensation ! Il a atteint les §b§l%distance% mètres §7avec une maîtrise exceptionnelle !",
                "§7❱❱ §3%player_name% §7est survolté ! Il a réussi un tir remarquable à §b§l%distance% mètres §7!");

        MultiThreading.schedule(() -> {
            this.currentAutoMessage = currentAutoMessage.nextMessage();
            game.broadcast(getCurrentAutoMessage().getMessage());
        }, 1, 5, TimeUnit.MINUTES);
    }

    public void sendDistanceMessage(int distance, OneShotPlayer gamePlayer) {
        if (distance >= 20 && distance <= 70)
            CollectionUtils.random(killDistanceMessages)
                    .map(message -> message
                            .replace("%distance%", String.valueOf(distance))
                            .replace("%player_name%", gamePlayer.getName()))
                    .ifPresent(game::broadcast);
    }

    private void addMessages(Collection<String> collection, String... messages) {
        collection.addAll(Arrays.asList(messages));
    }

}