/**
 * Copyright (c) 2013 - 2016, Coveo Solutions Inc.
 */
package com.coveo.blitz.client.util;

import com.coveo.blitz.client.bot.Tile;
import com.coveo.blitz.client.dto.GameState;

public class MineUtils
{
    public static boolean ourMine(GameState state,
                                  Tile tile)
    {
        return tile.isMine() && tile.getSymbol().endsWith(Integer.toString(state.getHero().getId()));
    }
}