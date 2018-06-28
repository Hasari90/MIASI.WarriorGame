import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.proto.ContractNetInitiator;

import java.util.*;

public class Monster extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] targetBookTitles;
	// The list of known seller agents
	private AID[] sellerAgents;
	
	public Monster()
	{
		
	}
	
	protected void setup() {
		// Printout a welcome message
		System.out.println("Hallo! Buyer-agent "+getAID().getName()+" is ready.");

		// Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			String targetBookTitle = (String) args[0];
			targetBookTitles = targetBookTitle.split(";");
			System.out.println("Target book is "+targetBookTitle);

			// Add a TickerBehaviour that schedules a request to seller agents every minute
			addBehaviour(new TickerBehaviour(this, 60000) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				protected void onTick() {
					for(String targetBookTitle : targetBookTitles) {
						System.out.println("Trying to buy " + targetBookTitle);
						// Update the list of seller agents
						DFAgentDescription template = new DFAgentDescription();
						ServiceDescription sd = new ServiceDescription();
						sd.setType("book-selling");
						template.addServices(sd);
						try {
							DFAgentDescription[] result = DFService.search(myAgent, template);
							System.out.println("Found the following seller agents:");
							sellerAgents = new AID[result.length];
							for (int i = 0; i < result.length; ++i) {
								sellerAgents[i] = result[i].getName();
								System.out.println(sellerAgents[i].getName());
							}
						} catch (FIPAException fe) {
							fe.printStackTrace();
						}

						// Perform the request
						myAgent.addBehaviour(new RequestPerformer(targetBookTitle));
					}
				}
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("No target book title specified");
			doDelete();
		}
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
	}

	/**
	   Inner class RequestPerformer.
	   This is the behaviour used by Book-buyer agents to request seller 
	   agents the target book.
	 */
	private class RequestPerformer extends Behaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private AID bestSeller; // The agent who provides the best offer 
		private int bestPrice;  // The best offered price
		private int repliesCnt = 0; // The counter of replies from seller agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private String targetBookTitle;

		RequestPerformer(String book)
		{
			targetBookTitle = book;
		}

		public void action() {
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				} 
				cfp.setContent(targetBookTitle);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from seller agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						int price = Integer.parseInt(reply.getContent());
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present
							bestPrice = price;
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2; 
					}
				}
				else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(targetBookTitle);
				order.setConversationId("book-trade");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
			case 3:      
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println(targetBookTitle+" successfully purchased from agent "+reply.getSender().getName());
						System.out.println("Price = "+bestPrice);

						final List<String> list = new ArrayList<String>();
						Collections.addAll(list, targetBookTitles);
						list.remove(targetBookTitle);
						targetBookTitles = list.toArray(new String[list.size()]);
						if(list.size() == 0)
							myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: requested book already sold.");
					}

					step = 4;
				}
				else {
					block();
				}
				break;
			}        
		}

		public boolean done() {
			if (step == 2 && bestSeller == null) {
				System.out.println("Attempt failed: "+targetBookTitle+" not available for sale");
			}
			return ((step == 2 && bestSeller == null) || step == 4);
		}
	}  // End of inner class RequestPerformer

	private class BuyBookBehaviour extends ContractNetInitiator{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BuyBookBehaviour(Agent a, ACLMessage cfp)
		{
			super(a, cfp);
		}

		protected void handleAllResponses(Vector responses, Vector acceptances){
			int bestPrice = -1;
			AID bestSeller = null;
			ACLMessage bestReply = null;

			for(Object r : responses)
			{
				ACLMessage response = (ACLMessage) r;
				if(response.getPerformative() == ACLMessage.PROPOSE){
					ACLMessage reply = response.createReply();
					reply.getPerformative(ACLMessage.REJECT_PROPOSAL);
					acceptances.add(reply);
					int price = Integer.parseInt(response.getContent());
					if(bestReply == null || price < bestPrice)
					{
						bestPrice = price;
						bestReply = reply;
						bestSeller = response.getSender();
					}
 				}
			}
			if(bestReply != null){
				bestReply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				//System.out.printf("Sprzedaj¹cy %s oferuje najlepsz¹ cenê za [%d] za ksi¹¿kê '%s' \n",bestSeller.getLocalName(),bestPrice,_requestedBookTitle);
			}
		}
	}
}
