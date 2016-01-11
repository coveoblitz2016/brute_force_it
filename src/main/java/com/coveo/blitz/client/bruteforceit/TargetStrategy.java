package com.coveo.blitz.client.bruteforceit;

import java.util.HashSet;

import com.coveo.blitz.client.bot.TargetEnum;
import com.coveo.blitz.client.bot.Tile;
import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Position;

public interface TargetStrategy
{
    HashSet<Position> getTarget(GameState gameState,
                                Tile[][] table,
                                TargetEnum target);
}
