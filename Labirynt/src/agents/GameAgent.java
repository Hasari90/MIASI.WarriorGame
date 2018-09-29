package agents;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import logic.Explorer;
import logic.Game;
import logic.IDatable;
import logic.Monster;
import logic.Player;
import logic.Tile;
import logic.Warrior;

public class GameAgent extends MyAgent {

	private static final long serialVersionUID = 1L;
	public static Game gameInfo;
	private int N_PLAYERS = 2;
	private Boolean gameOver;
	
	private class StartingBehaviour extends MyBehaviour{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void action(){
				
			AID[] result;
			while ((result = searchPlayers()).length != N_PLAYERS)
				block();

			for(int i = 0; i < result.length; i++){
				String playerName = result[i].getLocalName();
				String playerClass = result[i].getName();
				System.out.println("Start " + playerName);
				//Scanner reader = new Scanner(System.in);
				//String c = reader.toString();
				//char d = c.charAt(c.length()-2);
//				char d = result[i].getLocalName().charAt(result[i].getLocalName().length()-1);
//				switch(d){
//				case 'E':
//					Explorer e = new Explorer(100,10,1,1,1);
//					e.agent = result[i];
//					gameInfo.addPlayer(e);
//					break;
//				case 'W':
//					Warrior w = new Warrior(100,10,1,1,1);
//					w.agent = result[i];
//					gameInfo.addPlayer(w);
//					break;
//				case 'M':
//					Monster m = new Monster(100,10,1,1,1);
//					m.agent = result[i];
//					gameInfo.addPlayer(m);
//					break;
//				}
				sendMessage(playerName, "START", ACLMessage.PROPOSE);
				System.out.println("Wys³ano: START do: " + playerName);
				//reader.close();
			}
			
			//Wait acknowledgment of players to start the game
			ACLMessage msg; 
			int counter = 0;
			
			while(counter != N_PLAYERS){
				msg = blockingReceive();
				AID player = msg.getSender();
				String content = msg.getContent();
				int performative = msg.getPerformative();
				
				if(performative == ACLMessage.ACCEPT_PROPOSAL){
					switch(content){
					case "EXPLORER":
						Explorer e = new Explorer(150,10,2,1,1);
						e.agent = player;
						gameInfo.addPlayer(e);
						sendMessage(player.getLocalName(), "READY", ACLMessage.PROPOSE);
						break;
					case "WARRIOR":
						Warrior w = new Warrior(250,20,1,5,5);
						w.agent = player;
						gameInfo.addPlayer(w);
						sendMessage(player.getLocalName(), "READY", ACLMessage.PROPOSE);
						break;
					case "MONSTER":
						Monster m = new Monster(100,15,2,2,2);
						m.agent = player;
						gameInfo.addPlayer(m);
						sendMessage(player.getLocalName(), "READY", ACLMessage.PROPOSE);
						break;
					}	
				}
				
				if(content.equals("GAME") && performative == ACLMessage.ACCEPT_PROPOSAL){
					counter++;
				}
				
				if(counter == N_PLAYERS){
					gameInfo.startGame("Level 0.map");
				}
			}
		}

		@Override
		public boolean done() {
			return true;
		}
	};
	
	private class GameBehaviour extends MyBehaviour{

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			
			AID[] result;
			while ((result = searchPlayers()).length != N_PLAYERS)
				block();

			for(int i = 0; i < result.length; i++){
				String playerName = result[i].getLocalName();
				String playerClass = result[i].getName();
				System.out.println("Ruch " + playerName);
				sendMessage(playerName, "MOVE", ACLMessage.REQUEST);
				System.out.println("Wys³ano: MOVE do: " + playerName);
			}
			
			
			ACLMessage msg = blockingReceive();
			AID player = msg.getSender();
			String content = msg.getContent();
			int performative = msg.getPerformative();
			
			if(performative == ACLMessage.INFORM){
				switch(content){
				case "MOVE":
					for (Player gamer : gameInfo.players) {
				        //if (gamer.agent.getLocalName() == player.getLocalName()) {
				            gamer.Move(gameInfo);
				        //}
				    }
					gameInfo.revalidate();
					gameInfo.repaint();
					break;
				}	
			}
		}

		@Override
		public boolean done() {
			return gameOver;
		}
		
		
		
	};
	
	
	
	protected void setup() {
		Object[] args = getArguments();
		N_PLAYERS = Integer.parseInt(args[0].toString());
		gameInfo = new Game();
		gameOver = false;
		
		registryDF("Administrator", getAID().getLocalName());
		
		System.out.println("Administrator jest gotowy");

		addBehaviour(new StartingBehaviour());
		addBehaviour(new GameBehaviour());
//		addBehaviour(new TickerBehaviour(this, 1000){
//			protected void onTick() {
//				AID[] result;
//				result = searchPlayers();
//
//				for(int i = 0; i < result.length; i++){
//					String playerName = result[i].getLocalName();
//					String playerClass = result[i].getName();
//					sendMessage(playerName, "START", ACLMessage.PROPOSE);
//					System.out.println("Wys³ano: START do: " + playerName);
//				}
//			}
//		});
	}
	
	protected void takeDown() {
		 
		try {
			 DFService.deregister(this);
		 }
		 catch (FIPAException fe) {
			 fe.printStackTrace();
		 }		 
	}

//	public class Game extends JFrame{

//		private static final long serialVersionUID = 1L;
//		public int rows = 30;
//	    public int columns = 30;
//	    public int panelSize = 25;
//	    public int map[][] = new int[columns][rows];
//	    public int endLevelLoc;
//	    public Tile tile;
//	    public List<IDatable> listCharacters;
//	    public List<IDatable> elements;
//	    public List<Player> players;
//	    
//		private BufferedReader br;
//	    
//	    public Game(){
//	    	this.players = new ArrayList<Player>(); 
//	    }
//	    
//	    public void addPlayer(Player player)
//	    {
//	    	//Add new player
//	    	players.add(player);
//	    	this.add(player.move);
//	    }
//	    
//	    public void loadMap(String str){
//	        try{
//	            br = new BufferedReader(new FileReader(str));
//	            StringBuilder sb = new StringBuilder();
//	            String line = br.readLine();
//
//	            while (line != null) {
//	                sb.append(line);
//	                sb.append(System.lineSeparator());
//	                line = br.readLine();
//	            }
//	            String mapStr = sb.toString();
//	            
//	            int counter = 0;
//	            for(int y = 0; y < columns; y++){
//	                for(int x = 0; x < rows; x++){
//	                    String mapChar = mapStr.substring(counter, counter+1);
//	                    if(!mapChar.equals("\r\n") && !mapChar.equals("\n")&& !mapChar.equals("\r")){
//	                        map[x][y] = Integer.parseInt(mapChar);
//	                    }else{
//	                        x--;
//	                        System.out.print(mapChar);
//	                    }
//	                    counter++;
//	                }
//	            }
//	        }catch(Exception e){
//	            System.out.println("Problem z za³adowaniem mapy.");
//	        }
//	    }
//	    
//	    public void removeElement(Point element, List<IDatable> table) {
//	    	if(table.size()!=0) {
//	    		for (int i= 0;i <=table.size()-1;i++) {      			
//	        		if((table.get(i).GetX() == element.getX()) &&(table.get(i).GetY() == element.getY())) {
//	        			table.remove(i);
//	        			return;
//	        		}
//	    		}
//	    	}
//	    }
//	    
//	    public void startGame(String str)
//	    {
//	    	loadMap(str);
//	        this.setResizable(false);
//	        this.setSize((columns*panelSize)+50, (rows*panelSize)+70);
//	        this.setTitle("WarriorGame");
//	        this.setLayout(null);        
//	        this.listCharacters =  new ArrayList<IDatable>();
//	        this.elements = new ArrayList<IDatable>();
//	         
//	        
//	        this.setLocationRelativeTo(null);
//	    	
//	        //Color map
//	        for(int y = 0; y < columns; y++){
//	            for(int x = 0; x < rows; x++){
//	                tile = new Tile(x, y);
//	                tile.setSize(panelSize, panelSize);
//	                tile.setLocation((x*panelSize)+23, (y*panelSize)+25);
//	                if(map[x][y] == 0){
//	                    tile.setBackground(Color.GRAY);
//	                }else if(map[x][y] == 2)
//	                {
//	                	tile.setBackground(Color.YELLOW);
//	                	elements.add(tile);
//	                }
//	                else{
//	                    tile.setBackground(Color.WHITE);
//	                    tile.setWall(false);
//	                    if(x == 0){                  	
//	                    }
//	                    if(x == columns-1){
//	                    	endLevelLoc = y;
//	                    }
//	                }
//	                
//	                tile.setVisible(true);
//	                this.add(tile);
//	            }
//	        }
//	        this.setVisible(true);
//	        EventQueue.invokeLater(new Runnable() {
//	            @Override
//	            public void run() {
//	                Timer timer = new Timer(100, new ActionListener() {
//	                    @Override
//	                    public void actionPerformed(ActionEvent e) {
//	                        tick();
//	                    }
//	                });
//	                timer.setRepeats(true);
//	                timer.setCoalesce(true);
//	                timer.start();
//	            }
//	        });
//	    }
//	    
//	    protected void tick() {
//	    	
//			if(!players.isEmpty())
//			{
//				for(int i = 0; i < players.size(); i++){
//				players.get(i).Move(this);
//				}
//			}
//			
//	    	this.revalidate();
//			this.repaint();
//	    }
//	}
}
