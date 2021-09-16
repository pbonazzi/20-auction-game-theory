package sim;

import java.util.ArrayList;

/**
 * This class implements the balanced bidding agent
 * Rename the class by substituting "TeamABC" by your team's initials,
 * e.g. BBAgentMRTK
 */
public class BBAgentPIBO extends Agent{

	public BBAgentPIBO(int id, int value, int budget){
		super(id,value,budget);
	}
	
	@Override
	public int initialBid(int reserve) {
		return getValue()/2;
	}

	@Override
	public int bid(int t, History history, int reserve) {
		ArrayList<Integer> targetInfo = targetSlot(t, history, reserve);
		
		int targetSlot = targetInfo.get(0);
		int bid = 0;
		
		/*TODO compute the bid according to the balanced bidding strategy:
		 * a) find the clickrates for the target and the next higher slot and
		 * b) solve the balanced bidding equation q_i* (w_i-p_i*) = q_(i*-1)(w_i-b_i)
		 * 
		 * hint 1: use history.getSlotClicks(t-1)
		 * hint 2: as described in the assignment, clicks_(targetSlot-1) = 2*clicks_(target_slot) if targetSlot == 0
		 */

		/* qi and qi-1 */
		/* BEGIN */
		ArrayList<Integer> numclicks = history.getSlotClicks(t-1);
		int qi__star;
		int qi_1__star;

		if(targetSlot==0){
			qi__star = numclicks.get(targetSlot);
			qi_1__star = qi__star * 2;}
		else {
			qi__star = numclicks.get(targetSlot);
			qi_1__star = numclicks.get(targetSlot-1);}
		/* END*/

		/* wi */
		/* BEGIN */
		int wi = getValue();
		/* END*/

		/* pi__star */
		/* BEGIN */
		ArrayList<Integer> perClickPaymentsHist = history.getPerClickPayments(t-1);
		int pi__star;
		if (perClickPaymentsHist.size()==0) {pi__star = reserve;}
		else {pi__star = perClickPaymentsHist.get(targetSlot);}
		/* END*/

		/* final optimisation equation*/
		/* BEGIN */
		int testbid = wi - ((qi__star * (wi - pi__star)) / qi_1__star);
		if (testbid>wi) { bid = wi;}
		else {bid = testbid;}
		/* END*/


		return bid;
	}
	
	/**
	 * This function returns a list of Pairs (minBid,maxBid) for each slot (i.e. slot1,slot2, ...).
	 * (minBid,maxBid)_i defines the miminum and maximum bid to win slot i, given that all other
	 * agents bid the same amount as in the last round
	 * @param t, the time step
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return list of Pairs (minBid,maxBid)_i for all slots i
	 */
	public ArrayList<Pair> slotInfo(int t, History history, int reserve){
		ArrayList<Pair> info = new ArrayList<Pair>();
		
		//get bids from last round
		ArrayList<Integer> lastBids = (ArrayList<Integer>)history.getBids(t-1).clone();
		lastBids.remove(getId());


		//get clicks from last rounds
		int numSlots = history.getSlotClicks(t-1).size();
		for (int slot = 0; slot<numSlots; slot++){
			info.add(GSP.bidRangeForSlot(slot, reserve, lastBids));
		}
		return info;
	}
	
	/**
	 * The function computes the expected utilities for all slots, assuming that all other
	 * agents bid the same amount as in the last round
	 * @param t, the time step (of the current round, not last round)
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return a list of integers (eu1, eu2, ...), in which eu_i is the agent's expected utility of winning slot i 
	 */
	public ArrayList<Integer> expectedUtilities(int t, History history, int reserve){
		ArrayList<Integer> expectedUtils = new ArrayList<>();
		
		/* TODO compute the expected utilities for each available slot, assuming that the other bidders
		 * keep their bids constants from the previous round
		 * 
		 * hint: use history.getSlotClicks(t-1), and slotInfo(t, history, reserve)
		 */

		ArrayList<Integer> numclicks = history.getSlotClicks(t-1);

		ArrayList<Integer> bids = new ArrayList<>();
		ArrayList<Pair> bidtowin = slotInfo(t, history, reserve);

		for(int i=0; i < numclicks.size(); ++i){
			int bet = (bidtowin.get(i).getFirst()+bidtowin.get(i).getSecond())/2;
			bids.add(i,bet);}

		int value = getValue();

		for(int i=0; i < numclicks.size(); ++i){
			int utility = numclicks.get(i) *(value - bids.get(i));
			expectedUtils.add(i,utility);}

		return expectedUtils;
	}

	
	/**
	 * This function computes the slot with the highest expected utility and returns
	 * this slotId together with (minBid,maxBid)_i
	 * @param t, the time step
	 * @param history, the object in which the information of all previous rounds is stored
	 * @param reserve, the reserve price
	 * @return a triple (slot,minBid,maxBid), where slot is the target slot, minBid and maxBid
	 * are the minimum and maximum bid to win this slot assuming all other bid the same amount as in the last round
	 */

	public ArrayList<Integer> targetSlot(int t, History history, int reserve){
		ArrayList<Integer> targetSlot = new ArrayList<Integer>();
		ArrayList<Integer> expectedUtils = expectedUtilities(t, history, reserve);
		int target = 0;
		for (int i = 1; i<expectedUtils.size(); i++){
			if(expectedUtils.get(target) < expectedUtils.get(i)){
				target = i;
			}
		}
		targetSlot.add(target);
		
		ArrayList<Pair> info = slotInfo(t, history, reserve);
		targetSlot.add(info.get(target).getFirst());
		targetSlot.add(info.get(target).getSecond());
		return targetSlot;
	}

}
