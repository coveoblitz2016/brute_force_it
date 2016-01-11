/**
 * Copyright (c) 2013 - 2016, Coveo Solutions Inc.
 */
package com.coveo.blitz.client.util;

import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Hero;
import com.coveo.blitz.client.dto.GameState.Position;

public class SpawnUtils
{
    public static boolean isSpawnOfOtherUser(GameState gameState,
                                             Position position)
    {
        boolean isSpawnOfOtherUser = false;

        Hero currentHero = gameState.getHero();
        for (Hero hero : gameState.getGame().getHeroes()) {
            if (hero.getId() != currentHero.getId()) {
                if (position.equals(hero.getSpawnPos())) {
                    isSpawnOfOtherUser = true;
                    break;
                }
            }
        }

        return isSpawnOfOtherUser;
    }
}
