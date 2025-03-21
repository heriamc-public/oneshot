package fr.heriamc.games.oneshot.setting;

import fr.heriamc.games.engine.point.MultiplePoint;
import fr.heriamc.games.engine.point.SinglePoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OneShotGameConfiguration {

    private String templateName;

    private int buildYMax;

    private SinglePoint spawn;
    private MultiplePoint random;

}