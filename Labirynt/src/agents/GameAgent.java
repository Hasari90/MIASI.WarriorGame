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
import jade.core.Agent;
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
						e.agent = player.getLocalName();
						gameInfo.addPlayer(e);
						sendMessage(player.getLocalName(), "READY", ACLMessage.PROPOSE);
						break;
					case "WARRIOR":
						Warrior w = new Warrior(250,20,1,5,5);
						w.agent = player.getLocalName();
						gameInfo.addPlayer(w);
						sendMessage(player.getLocalName(), "READY", ACLMessage.PROPOSE);
						break;
					case "MONSTER":
						Monster m = new Monster(100,15,2,2,2);
						m.agent = player.getLocalName();
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

		}

		@Override
		public boolean done() {
			return gameOver;
		}
		
		
		
	};
	
	private class TickBehaviour extends TickerBehaviour{

		public TickBehaviour(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			AID[] matchingAgents = null;
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			sd.setType("Player");

			template.addServices(sd);
			try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			matchingAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
					matchingAgents[i] = result[i].getName();}
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			for(int i = 0; i < matchingAgents.length; i++){
				String playerName = matchingAgents[i].getLocalName();
				String playerClass = matchingAgents[i].getName();
				System.out.println("Ruch " + playerName);
				sendMessage(playerName, "MOVE", ACLMessage.REQUEST);
				System.out.println("Wys³ano: MOVE do: " + playerName);
			}
			
			
			ACLMessage msg = blockingReceive();
			AID player = msg.getSender();
			String playerName = player.getLocalName().toString();
			String content = msg.getContent();
			int performative = msg.getPerformative();
			
			if(performative == ACLMessage.INFORM){
				switch(content){
				case "MOVE":
					for (Player gamer : gameInfo.players) {
				        if (gamer.agent.equals(playerName)) {
				            gamer.Move(gameInfo);
				        }
				    }
					gameInfo.revalidate();
					gameInfo.repaint();
					break;
				case "STAY":
					gameInfo.revalidate();
					gameInfo.repaint();
					break;
				case "RUN":
					for (Player gamer : gameInfo.players) {
				        if (gamer.agent.equals(playerName)) {
				            gamer.Move(gameInfo);
				            gameInfo.revalidate();
							gameInfo.repaint();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				            gamer.Move(gameInfo);
				        }
				    }
					gameInfo.revalidate();
					gameInfo.repaint();
					break;
				}	
			}
		}
	}
	
	protected void setup() {
		Object[] args = getArguments();
		N_PLAYERS = Integer.parseInt(args[0].toString());
		gameInfo = new Game();
		gameOver = false;
		
		registryDF("Administrator", getAID().getLocalName());
		
		System.out.println("Administrator jest gotowy");

		addBehaviour(new StartingBehaviour());
		addBehaviour(new GameBehaviour());
		addBehaviour(new TickBehaviour(this, 1000));
	}
	
	protected void takeDown() {
		 
		try {
			 DFService.deregister(this);
		 }
		 catch (FIPAException fe) {
			 fe.printStackTrace();
		 }		 
	}

}
