package com.coveo.blitz.client.bruteforceit;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coveo.blitz.client.Main;
import com.coveo.blitz.client.bot.SuperAwesomeBot;
import com.coveo.blitz.client.bot.TargetEnum;
import com.coveo.blitz.client.bot.Tile;
import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Position;
import com.coveo.blitz.client.util.MineUtils;
import com.coveo.blitz.client.util.SpawnUtils;

public class WaveTargetStrategy implements TargetStrategy
{
    private static final int LIFE_IS_CRITICAL = 25;
    private static final Integer NUMBER_OF_EXPECTED_TARGETS = 10;
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public HashSet<Position> getTarget(GameState gameState,
                                       Tile[][] table,
                                       TargetEnum targetEnum)
    {
        Set<Position> tilesAlreadyDetermined = new HashSet<>();
        Position heroPosition = gameState.getHero().getPos();

        double shortestDistance = Double.MAX_VALUE;
        HashSet<Position> targets = new HashSet<>();
        int boardSizeMaxCoordinates = gameState.getGame().getBoard().getSize() - 1;

        for (int wave = 1; wave <= 16; wave++) {
            //            logger.info("Wave : {}", wave);
            for (int x = heroPosition.getX() - wave; x <= heroPosition.getX() + wave; x++) {
                if (x < 0 || x > boardSizeMaxCoordinates) {
                    //                    logger.info("Skipping x = {}", x);
                    continue;
                }
                for (int y = heroPosition.getY() - wave; y <= heroPosition.getY() + wave; y++) {
                    Position currentPosition = new Position(x, y);
                    if (y < 0 || y > boardSizeMaxCoordinates || heroPosition.equals(currentPosition)
                            || tilesAlreadyDetermined.contains(currentPosition)) {
                        //                        logger.info("Skipping x = {}, y = {}", x, y);
                        continue;
                    }
                    //                    logger.info("x = {}, y = {}", x, y);
                    tilesAlreadyDetermined.add(new Position(x, y));

                    if (waveOneExceptionTile(wave, x, y)) {
                        if (table[x][y].isBeer() && gameState.getHero().getLife() < SuperAwesomeBot.MAX_PLAYER_HEALTH) {
                            targets.add(new Position(x, y));
                            return targets;
                        }
                    }
                    if (getLength(heroPosition, x, y) <= 2) {
                        if (table[x][y].isHero() && !SpawnUtils.isSpawnOfOtherUser(gameState, new Position(x, y))
                                && killThatMotherfucker(gameState, table, x, y)) {
                            targets.add(new Position(x, y));
                            return targets;
                        }
                    }
                    if (!MineUtils.ourMine(gameState, table[x][y])) {
                        boolean result = table[x][y].isMine();

                        if (targetEnum.equals(TargetEnum.BEER)) {
                            result = table[x][y].isBeer();
                        }

                        if (result) {
                            double distance = Math.sqrt((currentPosition.getX() - x) * (currentPosition.getX() - x)
                                    + (currentPosition.getY() - y) * (currentPosition.getY() - y));

                            if (distance < shortestDistance) {
                                shortestDistance = distance;
                                targets.add(new Position(x, y));
                                if (targets.size() == NUMBER_OF_EXPECTED_TARGETS) {
                                    break;
                                }
                            }
                        }
                    }
                }
                if (targets.size() == NUMBER_OF_EXPECTED_TARGETS) {
                    break;
                }
            }
            if (targets.size() == NUMBER_OF_EXPECTED_TARGETS) {
                break;
            }
        }
        return targets;
    }

    private boolean waveOneExceptionTile(int wave,
                                         int x,
                                         int y)
    {
        return wave == 1 && !cornerOfWave(x, y);
    }

    private boolean cornerOfWave(int x,
                                 int y)
    {
        return Math.abs(x) == Math.abs(y);
    }

    private boolean killThatMotherfucker(GameState gameState,
                                         Tile[][] table,
                                         int x,
                                         int y)
    {
        return gameState.getGame().getHeroes().get(getHeroIndex(table, x, y)).getLife() <= LIFE_IS_CRITICAL
                && !gameState.getGame().getHeroes().get(getHeroIndex(table, x, y)).isCrashed()
                && gameState.getGame().getHeroes().get(getHeroIndex(table, x, y)).getMineCount() > 0;
    }

    private int getHeroIndex(Tile[][] table,
                             int x,
                             int y)
    {
        return Character.getNumericValue(table[x][y].getSymbol().charAt(1)) - 1;
    }

    private int getLength(Position position,
                          int x,
                          int y)
    {
        return Math.abs(position.getX() - x) + Math.abs(position.getY() - y);
    }
}