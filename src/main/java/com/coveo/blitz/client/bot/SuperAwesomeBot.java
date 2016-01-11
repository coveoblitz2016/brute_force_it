package com.coveo.blitz.client.bot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coveo.blitz.client.bruteforceit.GameAstar;
import com.coveo.blitz.client.bruteforceit.WaveTargetStrategy;
import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Board;
import com.coveo.blitz.client.dto.GameState.Position;

public class SuperAwesomeBot implements SimpleBot
{
    private static final Logger logger = LogManager.getLogger(RandomBot.class);

    private static final int GOTTA_GET_A_BEER_BRO_BUFFER = 10;
    private static final Integer MINE_HEALTH = 26;
    private static final Integer MINE_HEALTH_BUFFER = 10;
    public static final Integer MAX_PLAYER_HEALTH = 85;

    private BoardParser parser = new BoardParser();

    @Override public BotMove move(GameState gameState)
    {
        logger.info(gameState.getHero().getPos().toString());

        Board board = gameState.getGame().getBoard();
        List<Tile> tiles = parser.parse(gameState.getGame().getBoard().getTiles());

        Tile[][] table = create2dtable(board, tiles);

        boolean canGetMine = (gameState.getHero().getLife() - getMineHealthPlusBuffer()) > 0;

        List<Position> targets;
        GameAstar astar = new GameAstar(gameState, table);

        targets = new ArrayList<>(new WaveTargetStrategy().getTarget(gameState, table, TargetEnum.BEER));
        Point resolvedMoves = getBotMove(gameState, targets, astar, false);
        if (resolvedMoves != null && resolvedMoves.cost > gameState.getHero().getLife() - GOTTA_GET_A_BEER_BRO_BUFFER) {
            return beSafeWithResults(gameState, resolvedMoves);
        }

        if (!canGetMine) {
            if (gameState.getHero().getLife() >= MAX_PLAYER_HEALTH) {
                return BotMove.STAY;
            }
            logger.info("Going for a beer");
            targets = new ArrayList<>(new WaveTargetStrategy().getTarget(gameState, table, TargetEnum.BEER));
        } else {
            logger.info("Going for a mine");
            targets = new ArrayList<>(new WaveTargetStrategy().getTarget(gameState, table, TargetEnum.MINE));
        }

        resolvedMoves = getBotMove(gameState, targets, astar, canGetMine);

        if (resolvedMoves == null) {
            if (gameState.getHero().getLife() >= MAX_PLAYER_HEALTH) {
                return BotMove.STAY;
            }
            logger.info("Going for a beer backup");
            resolvedMoves = getBotMove(gameState,
                                       new ArrayList<>(new WaveTargetStrategy().getTarget(gameState,
                                                                                          table,
                                                                                          TargetEnum.BEER)),
                                       astar,
                                       false);
        }

        return beSafeWithResults(gameState, resolvedMoves);
    }

    private BotMove beSafeWithResults(GameState gameState,
                                      Point resolvedMoves)
    {
        BotMove result;
        if (resolvedMoves == null) {
            result = new RandomBot().move(gameState);
        } else if (resolvedMoves.getPath().isEmpty()) {
            result = BotMove.STAY;
        } else {
            result = resolvedMoves.getPath().get(0);
        }
        return result;
    }

    @Override public void setup()
    {
    }

    @Override public void shutdown()
    {
    }

    private Point getBotMove(
            GameState gameState, List<Position> targets, GameAstar astar, boolean isMine)
    {
        Point resultTotal = null;
        Position finalTarget = null;
        try {
            Point best = null;
            for (Position target : targets) {
                Point result = astar.get(target);
                if (best == null || result.cost < best.cost) {
                    best = result;
                    finalTarget = target;

                    if ((isMine && best.cost > gameState.getHero().getLife())) {
                        finalTarget = null;
                    }
                }
            }

            if (finalTarget != null && best != null) {
                resultTotal = best;

                logger.info("Current target is (" + finalTarget.getX() + ", " + finalTarget.getY() + ")");
            }
        } catch (Exception e) {
            logger.info("OMG ALEX PLEASE STOP THROWING SHIT");
        }

        return resultTotal;
    }

    public static Tile[][] create2dtable(
            Board board, List<Tile> tiles)
    {
        Tile[][] table = new Tile[board.getSize()][board.getSize()];
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                table[i][j] = tiles.get(i * board.getSize() + j);
            }
        }

        return table;
    }

    public static int getMineHealthPlusBuffer()
    {
        return MINE_HEALTH + MINE_HEALTH_BUFFER;
    }
}