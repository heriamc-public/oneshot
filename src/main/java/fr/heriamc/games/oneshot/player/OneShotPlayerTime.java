package fr.heriamc.games.oneshot.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OneShotPlayerTime {

    SUNRISE ("Lever du soleil", 23000),
    MORNING ("Matin", 0),
    DAY ("Jour",1000),
    NOON ("Midi", 6000),
    SUNSET ("Coucher de soleil", 12000),
    NIGHT ("Nuit", 13000),
    MIDNIGHT ("Minuit", 18000);

    private final String displayName;
    private final long ticks;

    public static final OneShotPlayerTime[] times = values();

    public OneShotPlayerTime getPrevious() {
        return times[(ordinal() - 1  + times.length) % times.length];
    }

    public OneShotPlayerTime getNext() {
        return times[(ordinal() + 1) % times.length];
    }

}