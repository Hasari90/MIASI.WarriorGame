package logic;

import java.awt.Color;
import java.awt.Point;
import java.util.*;



public class Warrior extends Player{
	
	private static final long serialVersionUID = 1L;
	private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	public Point start, end; // position in map
	public Move move; // info about position in map visualized
	public List<Tile>  actualPath;
	private boolean[][] visited =  new boolean[Game.columns][Game.rows];
	
	public Warrior() {
		// TODO Auto-generated constructor stub
	}
	
	public Warrior(int health, int damage, float speed, int courage, int stength){
		this.Health =  health;
		this.Damage = damage;
		this.Speed  = speed;
		this.Courage = courage;
		this.Strength =  stength;
		this.move = new Move(Color.RED);
		this.move.setVisible(true);
		this.start = new Point(0,0);
	}
	
	public void SetStartPoint(Tile start) {
		this.start =  new Point(start.x, start.y);
	}
	
	//Attack opponent
	public int Attack(Warrior opponent) {
		return opponent.Health -= this.Strength;	
	}
	
	//find next object to go
	public Point FindNext(List<Tile> elements) {
		Point point = new Point(elements.get(0).x,elements.get(0).y);
		double dist = 0.0f;
		double mindist = CalculateDistance(start, point);
		for(Tile p:elements) {
			dist = CalculateDistance(start,new Point(p.x,p.y));			
			if(dist < mindist) {
				mindist = dist;
				point =  new Point(p.x,p.y);
			}
		}
		return point;		
	}
	
	//calculate distance
	private double CalculateDistance(Point a, Point b) {
		double distance = 0.0f;
		double x1,y1 = 0.0f;
		x1 = b.x - a.x;
		y1 = b.y - a.y;
		distance =  Math.sqrt(x1*x1 + y1*y1);
		return distance;
	}
	
	public void Move(Game game) {
		Tile tile = MoveByOne(game);		
		move.moveByPoint(new Point(tile.x,tile.y));
	}

	public Tile MoveByOne(Game game) {
		Tile moveByOne = new Tile(start.x, start.y);
		//find end point 
		if(end == null || end.equals(start)) {
			end = FindNext(game.elements);
		}
		//find path for the character
		if(actualPath == null || actualPath.isEmpty() ) {
			visited =  new boolean[Game.columns][Game.rows];
			actualPath = MoveToNext(Game.map);
		}
		//move by one on path
		if(actualPath.size() != 0 || !(actualPath.isEmpty())) {
			moveByOne = actualPath.get(actualPath.size()-1) ;
			actualPath.remove(actualPath.size()-1 );
		}
		return moveByOne;
	}
	
    public List<Tile> MoveToNext(int[][] maze) {
        LinkedList<Tile> nextToVisit = new LinkedList<>();
        Tile started = new Tile(start.x,start.y);
        nextToVisit.add(started);

        while (!nextToVisit.isEmpty()) {
        	Tile cur = nextToVisit.remove();

            if (!isValidLocation(cur.x, cur.y) || isExplored(cur.x, cur.y)) {
                continue;
            }

            if (isWall(maze, cur.x, cur.y)) {
                setVisited(cur.x, cur.y, true);
                continue;
            }

            if (isEndPoint(cur.x, cur.y, end)) {
                return backtrackPath(cur);
            }

            for (int[] direction : DIRECTIONS) {
                Tile coordinate = new Tile(cur.x + direction[0], cur.y + direction[1],cur);
                nextToVisit.add(coordinate);
                setVisited(cur.x, cur.y, true);
            }
        }
        return Collections.emptyList();
    }

    private List<Tile> backtrackPath(Tile cur) {
        List<Tile> path = new ArrayList<>();
        Tile iter = cur; 
        while (iter != null) {
            path.add(iter);
            iter = iter.parent;
        }
        return path;
    }
    
    public boolean isEndPoint(int x, int y, Point end) {
        boolean state =  x == end.getX() && y == end.getY();
    	return state;
    }

    public boolean isStart(int x, int y, Point start) {
        boolean state =  x == start.getX() && y == start.getY();
    	return state;
    }

    public boolean isExplored(int row, int col) {
    	 boolean state= visited[row][col];
    	 return state;
    }

    public boolean isWall(int[][] map,int row, int col) {
        return map[row][col] == 0;
    }

    public void setVisited(int row, int col, boolean value) {
        visited[row][col] = value;
    }

    public boolean isValidLocation(int row, int col) {
        if (row < 0 || row >= Game.rows || col < 0 || col >= Game.columns) {
            return false;
        }
        return true;
    }
	
	
}
