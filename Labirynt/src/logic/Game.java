package logic;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Game extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int rows = 30;
    public static int columns = 30;
    public static int panelSize = 25;
    public static int map[][] = new int[columns][rows];
    public static int endLevelLoc;
    public static Tile tile;
    public List<IDatable> listCharacters;
    public List<IDatable> elements;
    public List<Explorer> explorers;
    Warrior w;
    Monster m;
    
	private BufferedReader br;
    
    public Game(){
    	this.explorers = new ArrayList<Explorer>(); 
    }
    
    public void addPlayer(Explorer explorer)
    {
    	//Add new player
    	explorers.add(explorer);
    	this.add(explorer.move);
    }
    
    public void loadMap(String str){
        try{
            br = new BufferedReader(new FileReader(str));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String mapStr = sb.toString();
            
            int counter = 0;
            for(int y = 0; y < columns; y++){
                for(int x = 0; x < rows; x++){
                    String mapChar = mapStr.substring(counter, counter+1);
                    if(!mapChar.equals("\r\n") && !mapChar.equals("\n")&& !mapChar.equals("\r")){
                        map[x][y] = Integer.parseInt(mapChar);
                    }else{
                        x--;
                        System.out.print(mapChar);
                    }
                    counter++;
                }
            }
        }catch(Exception e){
            System.out.println("Problem z za³adowaniem mapy.");
        }
    }
    
    public void removeElement(Point element, List<IDatable> table) {
    	if(table.size()!=0) {
    		for (int i= 0;i <=table.size()-1;i++) {      			
        		if((table.get(i).GetX() == element.getX()) &&(table.get(i).GetY() == element.getY())) {
        			table.remove(i);
        			return;
        		}
    		}
    	}
    }
    
    public void startGame(String str)
    {
    	loadMap(str);
        this.setResizable(false);
        this.setSize((columns*panelSize)+50, (rows*panelSize)+70);
        this.setTitle("WarriorGame");
        this.setLayout(null);        
        this.listCharacters =  new ArrayList<IDatable>();
        this.elements = new ArrayList<IDatable>();
         
        
        this.setLocationRelativeTo(null);
        
        //Create player
    	w = new Warrior(100, 10, 0, 0, 0);
    	this.add(w.move);
    	this.listCharacters.add(w);
   	
    	m = new Monster(100, 10, 0, 0, 0);
    	this.add(m.move);
    	this.listCharacters.add(m);
    	
        //Color map
        for(int y = 0; y < columns; y++){
            for(int x = 0; x < rows; x++){
                tile = new Tile(x, y);
                tile.setSize(panelSize, panelSize);
                tile.setLocation((x*panelSize)+23, (y*panelSize)+25);
                if(map[x][y] == 0){
                    tile.setBackground(Color.GRAY);
                }else if(map[x][y] == 2)
                {
                	tile.setBackground(Color.YELLOW);
                	elements.add(tile);
                }
                else{
                    tile.setBackground(Color.WHITE);
                    tile.setWall(false);
                    if(x == 0){                  	
                    }
                    if(x == columns-1){
                    	endLevelLoc = y;
                    }
                }
                
                tile.setVisible(true);
                this.add(tile);
            }
        }
        this.setVisible(true);
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tick();
                    }
                });
                timer.setRepeats(true);
                timer.setCoalesce(true);
                timer.start();
            }
        });
    }
    
    protected void tick() {
    	
		// if you want move to the point of other character, just set end parameter int the warrior class, it will find path to this position and automatically go there
		if(!elements.isEmpty()) {
			w.Move(this);
		}
		if(!listCharacters.isEmpty()) {
			m.Move(this);
		}
		if(!explorers.isEmpty())
		{
			explorers.get(0).MovePlayer();
		}
		
    	this.revalidate();
		this.repaint();
    }
}
