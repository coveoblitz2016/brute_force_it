package com.coveo.blitz.client.bruteforceit;

import java.util.HashMap;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coveo.blitz.client.bot.Point;
import com.coveo.blitz.client.bot.SuperAwesomeBot;
import com.coveo.blitz.client.bot.Tile;
import com.coveo.blitz.client.dto.GameState;

public class GameAstar implements Pathfinder
{

    private static final Logger logger = LogManager.getLogger(GameAstar.class);

    private GameState gameState;
    private HashMap<GameState.Position, Point> points;
    private Tile[][] tiles;

    public GameAstar(
            GameState gameState, Tile[][] tiles)
    {
        this.gameState = gameState;
        points = new HashMap<>();
        GameState.Position position = gameState.getHero().getPos();
        points.put(position, new Point(position, null, 0, false));
        this.tiles = tiles;
    }

    @Override public Point get(GameState.Position position)
    {
        calc(position);
        return points.get(position);
    }

    private void calc(GameState.Position position)
    {
        if (this.points.containsKey(position)) {
            return; // Already solve;
        }
        boolean resolve = false;
        Stack<Point> points = new Stack<>();
        points.push(this.points.get(gameState.getHero().getPos()));
        while (!resolve && points.size() > 0) {
            Point point = points.pop();
            if (!point.blocking && this.points.get(point.position) == point) {
                // UP
                addPointToStack(point, 0, -1, points);
                addPointToStack(point, 0, 1, points);
                addPointToStack(point, -1, 0, points);
                addPointToStack(point, 1, 0, points);
            }
            if (point.equals(position)) {
                resolve = true;
            }
        }
    }

    private void addPointToStack(
            Point point, int x, int y, Stack<Point> stack)
    {
        GameState.Position position = new GameState.Position(point.position.getX() + x, point.position.getY() + y);
        int newX = point.position.getX() + x;
        int newY = point.position.getY() + y;
        int max = gameState.getGame().getBoard().getSize();
        if (newX >= max || newX < 0 || newY >= max || newY < 0) {
            return;
        }

        Tile tile = this.tiles[newX][newY];
        boolean blocking = blocking(tile);

        int cost = this.cost(tile);

        if (point.from == null && gameState.getHero().getLife() < SuperAwesomeBot.getMineHealthPlusBuffer()
                && willBeInDanger(position)) {
            cost = SuperAwesomeBot.getMineHealthPlusBuffer();
        }

        if (gameState.getHero().getMineCount() > 0 && tile == Tile.Spikes && cost + point.cost >= gameState.getHero()
                                                                                                           .getLife()) {
            blocking = true;
        }

        Point newPoint = new Point(position, point, cost + point.cost, blocking);

        if (this.points.containsKey(newPoint.position)) {
            Point oldPoint = this.points.get(newPoint.position);
            if (newPoint.cost < oldPoint.cost) {
                this.points.put(newPoint.position, newPoint);
                stack.push(newPoint);
            }
        } else {
            this.points.put(newPoint.position, newPoint);
            stack.push(newPoint);
        }
    }

    private boolean willBeInDanger(GameState.Position position)
    {
        int x = position.getX();
        int y = position.getY();
        Tile tile = getTile(x - 1, y);
        if (tile.isHero() && !tile.isId(gameState.getHero().getId())) {
            return true;
        }
        tile = getTile(x + 1, y);
        if (tile.isHero() && !tile.isId(gameState.getHero().getId())) {
            return true;
        }
        tile = getTile(x, y - 1);
        if (tile.isHero() && !tile.isId(gameState.getHero().getId())) {
            return true;
        }
        tile = getTile(x, y + 1);
        if (tile.isHero() && !tile.isId(gameState.getHero().getId())) {
            return true;
        }
        return false;
    }

    public Tile getTile(
            int x, int y)
    {
        int max = gameState.getGame().getBoard().getSize();
        if (x >= max || x < 0 || y >= max || y < 0) {
            return Tile.Wall;
        }

        return this.tiles[x][y];
    }

    private boolean blocking(Tile tile)
    {
        /*
        Walk in people
        if (tile.isHero() && !tile.isId(gameState.getHero().getId()) && gameState.getHero().getLife() > 30) {
            return false;
        }
        */
        return tile != Tile.Air && tile != Tile.Spikes;
    }

    private int cost(Tile tile)
    {
        switch (tile) {
            case Air:
                return 1;
            case Spikes:
                return 11;
            default:
                return 25;
        }
    }
}