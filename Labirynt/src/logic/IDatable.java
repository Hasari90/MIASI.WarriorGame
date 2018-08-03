package logic;

import java.awt.Point;

public interface IDatable {

	public int GetX();
	
	public int GetY();
	
	public void SetX(int x);
	
	public void SetY(int y);
		
	public Point GetPoint();
	
	public void setLocation(int x, int y);
}
