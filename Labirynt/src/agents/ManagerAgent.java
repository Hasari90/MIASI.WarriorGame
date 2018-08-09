package agents;

import java.util.Scanner;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import logic.Explorer;
import logic.Game;
import logic.Monster;
import logic.Warrior;

public class ManagerAgent extends MyAgent {

	private static final long serialVersionUID = 1L;
	private static Game gameInfo;
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
				System.out.println("Start " + playerName);
				//Scanner reader = new Scanner(System.in);
				//String c = reader.toString();
				//char d = c.charAt(c.length()-2);
				char d = result[i].getLocalName().charAt(result[i].getLocalName().length()-1);
				switch(d){
				case 'E':
					Explorer e = new Explorer(100,10,1,1,1);
					e.agent = result[i];
					gameInfo.addPlayer(e);
					break;
				case 'W':
					Warrior w = new Warrior(100,10,1,1,1);
					w.agent = result[i];
					gameInfo.addPlayer(w);
					break;
				case 'M':
					Monster m = new Monster(100,10,1,1,1);
					m.agent = result[i];
					gameInfo.addPlayer(m);
					break;
				}
				sendMessage(playerName, "START" + d + "-", ACLMessage.PROPOSE);
				//reader.close();
			}
			
			//Wait acknowledgment of players to start the game
			ACLMessage msg; 
			int counter = 0;
			
			while(counter != N_PLAYERS){
				msg = blockingReceive();
				String content = msg.getContent();
				int performative = msg.getPerformative();
				
				if(content.equals("GAME") && performative == ACLMessage.ACCEPT_PROPOSAL){
					counter++;
				}
			}
			
			gameInfo.startGame("Level 0.map");
		}

		@Override
		public boolean done() {
			return true;
		}
	};
	
	private class ManagerBehaviour extends MyBehaviour{

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			
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
		addBehaviour(new ManagerBehaviour());
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
