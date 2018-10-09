package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Path {
	
	private Point start;
	private Point end;
	public List<Tile>  actualPath;
	private boolean[][] visited =  new boolean[Game.columns][Game.rows];
	private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	
	public Path() {		
	}		
	
	//find next object to go
	public  void FindNextEndPoint(List<IDatable> elements) {
		Point point = new Point(elements.get(0).GetX() ,elements.get(0).GetY());
		double dist = 0.0f;
		double mindist = CalculateDistance(getStart(), point);
		
		java.util.Random random = new java.util.Random();
		int random_element = random.nextInt(elements.size());
		
		IDatable p = elements.get(random_element);
		point =  new Point(p.GetX(),p.GetY());
		
//		for(IDatable p: elements) {
//			dist = CalculateDistance(getStart(),new Point(p.GetX(),p.GetY()));			
//			if(dist < mindist & dist != 0.0) {
//				mindist = dist;
//				point =  new Point(p.GetX(),p.GetY());
//			}
//		}
		end = point;		
	}	
	
	//calculate distance
	protected double CalculateDistance(Point a, Point b) {
		double distance = 0.0f;
		double x1,y1 = 0.0f;
		x1 = b.x - a.x;
		y1 = b.y - a.y;
		distance =  Math.sqrt(x1*x1 + y1*y1);
		return distance;
	}
	
	public void FindFullPath() {
        LinkedList<Tile> nextToVisit = new LinkedList<>();
        Tile started = new Tile(start.x,start.y);
        nextToVisit.add(started);

        while (!nextToVisit.isEmpty()) {
        	Tile cur = nextToVisit.remove();

            if (!isValidLocation(cur.x, cur.y) || isExplored(cur.x, cur.y)) {
                continue;
            }

            if (isWall(Game.map, cur.x, cur.y)) {
                setVisited(cur.x, cur.y, true);
                continue;
            }

            if (isEndPoint(cur.x, cur.y)) {
            	backtrackPath(cur);
                return ;           
            }

            for (int[] direction : DIRECTIONS) {
                Tile coordinate = new Tile(cur.x + direction[0], cur.y + direction[1],cur);
                nextToVisit.add(coordinate);
                setVisited(cur.x, cur.y, true);
            }
        }
        this.actualPath= Collections.emptyList();
    }	
	
	private void  backtrackPath(Tile cur) {
        List<Tile> path = new ArrayList<>();
        Tile iter = cur; 
        while (iter != null) {
            path.add(iter);
            iter = iter.parent;
        }
        this.actualPath = path;
    }
	
	private boolean isEndPoint(int x, int y) {
        boolean state =  x == this.end.x && y == this.end.y;
    	return state;
    }

	private boolean isExplored(int row, int col) {
    	 boolean state= this.visited[row][col];
    	 return state;
    }

	private boolean isWall(int[][] map,int row, int col) {
        return map[row][col] == 0;
    }

	private void setVisited(int row, int col, boolean value) {
        this.visited[row][col] = value;
    }

	private boolean isValidLocation(int row, int col) {
        if (row < 0 || row >= Game.rows || col < 0 || col >= Game.columns) {
            return false;
        }
        return true;
    }
    
    public Tile GetOneMoveFromPath() {
    	Tile moveByOne = new Tile(this.start.x,this.start.y);
    	if(this.actualPath.size() != 0 || !(this.actualPath.isEmpty())) {
			moveByOne = this.actualPath.get(this.actualPath.size()-1) ;
			this.actualPath.remove(this.actualPath.size()-1 );
			return moveByOne;
		}
    	return moveByOne;
    }	
    
    public void MovetoNextPoinInPath() {
		if(this.actualPath == null || this.actualPath.isEmpty() ) {
			this.visited =  new boolean[Game.columns][Game.rows];
			FindFullPath();
		}
    }
    
	public boolean CheckIfEndOfPath() {
		if(getStart().equals(getEnd())) {	
			return true;
		}
		return false;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

}
