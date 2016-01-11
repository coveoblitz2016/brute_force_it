package com.coveo.blitz.client.bot;

public enum Tile
{
    Air("  "),
    Wall("##"),
    Spikes("^^"),
    Tavern("[]"),
    Hero1("@1"),
    Hero2("@2"),
    Hero3("@3"),
    Hero4("@4"),
    MineNeutral("$-"),
    MinePlayer1("$1"),
    MinePlayer2("$2"),
    MinePlayer3("$3"),
    MinePlayer4("$4"),
    UNKNOWN("??");

    private final String symbol;

    Tile(String symbol)
    {
        this.symbol = symbol;
    }

    @Override
    public String toString()
    {
        return symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public boolean isMine()
    {
        return getSymbol().charAt(0) == '$';
    }

    public boolean isBeer()
    {

        return getSymbol().equals(Tile.Tavern.getSymbol());
    }

    public boolean isHero()
    {
        return getSymbol().charAt(0) == '@';
    }

    public boolean isId(int id)
    {
        return getSymbol().charAt(1) == Integer.toString(id + 1).charAt(0);
    }
}
