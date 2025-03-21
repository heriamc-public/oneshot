package fr.heriamc.games.oneshot.board;

import fr.heriamc.games.engine.board.GameBoard;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.player.OneShotPlayer;

public class OneShotGameBoard extends GameBoard<OneShotGame, OneShotPlayer> {

    private int kills, deaths, killStreak, bestKillStreak, connected;
    private String wallet, ratio;

    public OneShotGameBoard(OneShotGame game, OneShotPlayer gamePlayer) {
        super(game, gamePlayer);
        this.wallet = "0 ⛃";
        this.ratio = "0.0";
    }

    @Override
    public void reloadData() {
        this.wallet = gamePlayer.getPoints().getWalletFormated();

        this.kills = gamePlayer.getKills();
        this.deaths = gamePlayer.getDeaths();

        this.ratio = gamePlayer.getRatio();
        this.killStreak = gamePlayer.getKillStreak();
        this.bestKillStreak = gamePlayer.getBestKillStreak();

        this.connected = game.getSize();
    }

    @Override
    public void setLines(String ip) {
        objectiveSign.clearScores();
        objectiveSign.setDisplayName("§e§l» §6§lOneShot §e§l«");

        objectiveSign.setLine(0, "§1");
        objectiveSign.setLine(2, "§8■ §7Points : §6" + wallet);
        objectiveSign.setLine(3, "§2");
        objectiveSign.setLine(4, "§8■ §7Tué(s) : §c" + kills);
        objectiveSign.setLine(5, "§8■ §7Mort(s) : §c" + deaths);
        objectiveSign.setLine(6, "§3");
        objectiveSign.setLine(7, "§8■ §7Ratio : §c" + ratio);
        objectiveSign.setLine(8, "§8■ §7KillStreak : §c" + killStreak);
        objectiveSign.setLine(9, "§8■ §7RecordKS : §c" + bestKillStreak);
        objectiveSign.setLine(11, "§4");
        objectiveSign.setLine(12, "§8■ §7Connectés : §b" + connected);
        objectiveSign.setLine(13, "§5");
        objectiveSign.setLine(14, ip);

        objectiveSign.updateLines();
    }

}