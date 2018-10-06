package agents;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import logic.Player;

public class ExplorerAgent extends MyAgent {
	
	private static final long serialVersionUID = 1L;
	private Player playerInfo;
	private Boolean gameOver = false;

	private class ExplorerBehaviour extends MyBehaviour {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void action(){
			ACLMessage msg = blockingReceive();
			String[] parts = msg.getContent().split("-", 2);
			switch(parts[0]){
			case "START": 
				sendReply(msg, "EXPLORER", ACLMessage.ACCEPT_PROPOSAL);
				break;
			case "READY": 
				sendReply(msg, "GAME", ACLMessage.ACCEPT_PROPOSAL);
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
			case "RUN": 
				sendReply(msg, "RUN", ACLMessage.INFORM);
				break;
			case "CHOOSE":
				sendReply(msg, "CHOOSE", ACLMessage.INFORM);
				break;
			case "MOVE":
				Random r = new Random();
				int result = r.nextInt(4);
				switch(result){
				case 0: 
					sendReply(msg, "MOVE", ACLMessage.INFORM);
					System.out.println(""+ getAID().getLocalName() +" wysy³a MOVE");
					break;
				case 1: 
					sendReply(msg, "STAY", ACLMessage.INFORM);
					System.out.println(""+ getAID().getLocalName() +" wysy³a STAY");
					break;
				case 2: 
					sendReply(msg, "RUN", ACLMessage.INFORM);
					System.out.println(""+ getAID().getLocalName() +" wysy³a RUN");
					break;
				case 3: 
					//playerInfo.kill(lasthit);
					//sendReply(msg, "K" + lasthit, ACLMessage.INFORM);
					break;
				}
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

	private class TickBehaviour extends TickerBehaviour{

		public TickBehaviour(Agent a, long period) {
			super(a, period);
		}

		public void sendReply(ACLMessage msg, String content, int performative){
			ACLMessage reply = msg.createReply();
			reply.setPerformative(performative);
			reply.setContent(content);
			myAgent.send(reply);
		}
		
		@Override
		protected void onTick() {
			ACLMessage msg = blockingReceive();
			Random r = new Random();
			int result = r.nextInt(4);
			switch(result){
			case 0: 
				sendReply(msg, "MOVE", ACLMessage.INFORM);
				System.out.println(""+ getAID().getLocalName() +" wysy³a MOVE");
				break;
			case 1: 
				sendReply(msg, "STAY", ACLMessage.INFORM);
				System.out.println(""+ getAID().getLocalName() +" wysy³a STAY");
				break;
			case 2: 
				sendReply(msg, "RUN", ACLMessage.INFORM);
				System.out.println(""+ getAID().getLocalName() +" wysy³a RUN");
				break;
			case 3: 
				//playerInfo.kill(lasthit);
				//sendReply(msg, "K" + lasthit, ACLMessage.INFORM);
				break;
			}
		}
	}
	
	protected void setup() {
		
		registryDF("Player", getAID().getLocalName());
		
		System.out.println(""+ getAID().getLocalName() +" jest gotowy do gry.");
				
		addBehaviour(new ExplorerBehaviour());
		addBehaviour(new TickBehaviour(this, 500));
	}
	
	protected void takeDown() {
		 
		try {
			 DFService.deregister(this);
		 }
		 catch (FIPAException fe) {
			 fe.printStackTrace();
		 }
		
		System.out.println(""+ getAID().getLocalName() +" umiera.");
	}
}
