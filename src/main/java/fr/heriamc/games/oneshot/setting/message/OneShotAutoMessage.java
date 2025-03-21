package fr.heriamc.games.oneshot.setting.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OneShotAutoMessage {

    NO_ALLIANCE ("§c§lRappel: Les alliances sont strictements interdites !"),
    NO_TOWER ("§c§lRappel: Les towers sont strictements interdites !"),
    CPS ("§c§lRappel: Vous n'avez pas le droit de dépassez les 20 CPS.");

    private final String message;

    private static final OneShotAutoMessage[] messages = values();

    public OneShotAutoMessage nextMessage() {
        return messages[(ordinal() + 1) % messages.length];
    }

    public String getMessage() {
        return "§6§lONESHOT §8┃ " + message;
    }

}