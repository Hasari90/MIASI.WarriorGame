import jade.core.Agent;

import java.awt.Color;
import java.awt.Point;
import java.util.*;



public class Warrior extends Agent {
	
	private static final long serialVersionUID = 1L;
	private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	public float Speed;
	public int Health,  Damage, Courage, Strength;
	public int type; //looking object/character on map
	public List<String> backpack; //for the treasures
	public Point start, end; // position in map
	public Player player; // info about position in map visualized
	public List<Tile>  actualPath;
	private boolean[][] visited;
	
	public Warrior() {
		// TODO Auto-generated constructor stub
	}
	
	Warrior(int health, int damage, float speed, int courage, int stength ){
		this.Health =  health;
		this.Damage = damage;
		this.Speed  = speed;
		this.Courage = courage;
		this.Strength =  stength;
	}
	
	Warrior(Point position) {
		this.Health = 0;
		this.Damage = 0;
		this.Speed  = 0.0f;
		this.Courage = 0;
		this.Strength = 0;
		player  =  new Player(Color.RED);
		player.setLocation(position.x,position.y);
		player.x = position.x;
		player.y = position.y;		
	}
	
	//Attack opponent
	public int Attack(Warrior opponent) {
		return opponent.Health -= this.Strength;	
	}
	
	//find next object to go
	public Point FindNext(List<Point> elements) {
		Point point = new Point();
		double dist = 0.0f;
		double mindist = 0.0f;
		for(Point p:elements) {
			dist = CalculateDistance(start,p);			
			if(dist < mindist) {
				mindist = dist;
				point =  p;
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
	

	public Tile MoveByOne(Game game) {
		Tile moveByOne= new Tile(0,0);
		//find end point 
		if(end == null || end==start) {
	//		end = FindNext(game.elements);
		}
		//find path for the character
		if(actualPath == null || actualPath.isEmpty() ) {
			visited = null;
			actualPath = Move(Game.map);
		}
		//move by one on path
		moveByOne = actualPath.get(0);
		actualPath.remove(0);
		return moveByOne;
	}
	
    public List<Tile> Move(int[][] maze) {
        LinkedList<Tile> nextToVisit = new LinkedList<>();
        Tile started = new Tile(start.x,start.y);
        nextToVisit.add(started);

        while (!nextToVisit.isEmpty()) {
        	Tile cur = nextToVisit.remove();

            if (!isValidLocation(cur.getX(), cur.getY()) || isExplored(cur.getX(), cur.getY())) {
                continue;
            }

            if (isWall(maze, cur.getX(), cur.getY())) {
                setVisited(cur.getX(), cur.getY(), true);
                continue;
            }

            if (isEndPoint(cur.getX(), cur.getY(), end)) {
                return backtrackPath(cur);
            }

            for (int[] direction : DIRECTIONS) {
                Tile coordinate = new Tile(cur.getX() + direction[0], cur.getY() + direction[1]);
                nextToVisit.add(coordinate);
                setVisited(cur.getX(), cur.getY(), true);
            }
        }
        return Collections.emptyList();
    }

    private List<Tile> backtrackPath(Tile cur) {
        List<Tile> path = new ArrayList<>();
        Tile iter = cur;

        while (iter != null) {
            path.add(iter);
            //iter = iter.parent;
        }

        return path;
    }
    
    public boolean isEndPoint(int x, int y, Point end) {
        return x == end.getX() && y == end.getY();
    }

    public boolean isStart(int x, int y, Point start) {
        return x == start.getX() && y == start.getY();
    }

    public boolean isExplored(int row, int col) {
        return visited[row][col];
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
