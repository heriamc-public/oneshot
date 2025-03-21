package fr.heriamc.games.oneshot.setting.message;

import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum OneShotMessages {

    PREFIX ("§6§lONESHOT §8┃ "),

    WELCOME_TITLE ("§e» §6§lOneShot §e«", "§fBon jeu !"),
    WELCOME_MESSAGE (
            "WELCOME MESSAGE",
            "NEED TO BE SET"),

    MAX_BUILD_HEIGHT_REACHED ("§cLa hauteur maximale a été dépassée."),

    SETTINGS_SUCCESSFUL_RESET_STATS ("§aVous avez réinitialiser vos statistiques avec succès ! %s"),

    SHOP_SELECT_COSMETIC ("§aVous avez équiper %s §a!"),
    SHOP_SUCCESSFUL_PURCHASE ("§aVous avez acheter %s §a!"),
    SHOP_FAIL_PURCHASE ("§c"),

    KILL_KILLER_MESSAGE ("§aVous avez tué §c%s §a! %s"),
    KILL_VICTIM_MESSAGE ("§aVous avez été tué par §c%s §a! %s"),
    KILL_REWARD_MESSAGE ("§7§l➼ §eVous avez reçu §6%d ⛃"),

    UNAVAILABLE_FUNCTIONALITY ("§cCette fonctionnalité n'est pas encore disponible.");

    private String message;
    private String[] messages;

    OneShotMessages(String message) {
        this.message = message;
    }

    OneShotMessages(String... messages) {
        this.messages = messages;
    }

    public String getMessage() {
        return PREFIX.message + message;
    }

    public String getMessage(Object... objects) {
        return PREFIX.message + message.formatted(objects);
    }

    public String[] getMessages(Object... objects) {
        List<String> formattedMessages = new ArrayList<>(messages.length);
        var index = 0;

        for (String message : messages) {
            if (message.contains("%")) {
                formattedMessages.add(message.formatted(objects[index]));
                index++;
            } else
                formattedMessages.add(message);
        }

        return formattedMessages.toArray(String[]::new);
    }

    public void sendAsTitle(OneShotPlayer gamePlayer, int fadeIn, int stay, int fadeOut) {
        var title = messages[0];
        var subTitle = messages[1];

        gamePlayer.sendTitle(fadeIn, stay, fadeOut,
                title == null ? "" : title,
                subTitle == null ? "" : subTitle);
    }

    public void sendAsActionBar(OneShotPlayer gamePlayer, Object... objects) {
        gamePlayer.sendActionBar(message.formatted(objects));
    }

    public String getMessageWithoutPrefix() {
        return message;
    }

}