/**
 * Copyright (c) 2013 - 2016, Coveo Solutions Inc.
 */
package com.coveo.blitz.client.bruteforceit;

import java.util.List;

import com.coveo.blitz.client.bot.BotMove;
import com.coveo.blitz.client.bot.Point;
import com.coveo.blitz.client.dto.GameState;

public interface Pathfinder
{
    public Point get(GameState.Position position);
}
