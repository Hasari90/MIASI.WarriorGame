package agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import logic.Player;
import logic.Explorer;
import logic.Warrior;

public class PlayerAgent extends MyAgent {
	
	private static final long serialVersionUID = 1L;
	private Player playerInfo;
	private Boolean gameOver = false;

	private class PlayerBehaviour extends MyBehaviour {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void action(){
			ACLMessage msg = blockingReceive();
			String[] parts = msg.getContent().split("-", 2);
			switch(parts[0]){
			case "STARTE": 
				sendReply(msg, "GAME", ACLMessage.ACCEPT_PROPOSAL);
				playerInfo = new Explorer(100,10,1,1,1);
				break;
			case "STARTW": 
				sendReply(msg, "GAME", ACLMessage.ACCEPT_PROPOSAL);
				playerInfo = new Warrior(100,10,1,1,1);
				break;
			case "FIGHT": 
				//int maxBid = Integer.parseInt(parts[1]);
				//lasthit = playerInfo.hit(Damage);
				//sendReply(msg, "F" + lasthit, ACLMessage.PROPOSE);
				break;
			case "KILL": 
				//playerInfo.kill(lasthit);
				//sendReply(msg, "K" + lasthit, ACLMessage.INFORM);
				break;
			case "CHOOSEW": 
				break;
			case "CHOOSE":
				break;

			default:
				break;
			}
		}
		
		@Override
		public boolean done() {
			return gameOver;
		}
	};

	protected void setup() {
		
		registryDF("Player", getAID().getLocalName());
		
		System.out.println(""+ getAID().getLocalName() +" jest gotowy do gry.");
				
		addBehaviour(new PlayerBehaviour());
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
