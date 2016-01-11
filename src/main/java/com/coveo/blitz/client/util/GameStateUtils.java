/**
 * Copyright (c) 2013 - 2016, Coveo Solutions Inc.
 */
package com.coveo.blitz.client.util;

import com.coveo.blitz.client.bot.Tile;
import com.coveo.blitz.client.dto.GameState;

public class GameStateUtils
{
    public static GameState nextStateFromTarget(GameState state,
                                                Tile[][] table,
                                                GameState.Position nextPosition)
    {

        GameState.Hero hero = state.getHero();
        GameState.Game game = state.getGame();
        GameState.Position pos = hero.getPos();

        int gold = hero.getGold();
        int mineCount = hero.getMineCount();
        int life = hero.getLife();

        life = life - 1;

        Tile nextTile = table[nextPosition.getX()][nextPosition.getY()];

        state.getGame();
        state.getToken();
        state.getViewUrl();
        state.getPlayUrl();

        return state;
    }
}
