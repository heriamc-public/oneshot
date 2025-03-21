package fr.heriamc.games.oneshot.player.wallet;

import fr.heriamc.games.engine.economy.currency.DoubleGameCurrency;

public class OneShotCoins extends DoubleGameCurrency {

    public OneShotCoins(double wallet) {
        super("coins", "⛃", wallet);
    }

    @Override
    public String getWalletFormated() {
        return formatWallet().apply(wallet) + " " + symbol;
    }

}