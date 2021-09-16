package sim;

import java.util.ArrayList;

/**
 * Implement your team competition agent here
 * Rename the class by substituting "ABC" by your team's initials,
 * e.g. BudgetMRTK
 *
 */

public class BudgetPIBO extends Agent{

	public BudgetPIBO(int id, int value, int budget) {
		super(id, value, budget);
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

		/* qi and qi-1 */
		/* BEGIN */

		ArrayList<Integer> iterations = history.getSlotClicks(t-1);;
		ArrayList<Integer> numclicks = new ArrayList<>();
		int topslot = (int) (Math.round(35*Math.cos(Math.PI*t/24) + 40));
		for (int j=0; j<iterations.size();++j) {
			int element;
			if (targetSlot == 0) {
				element = topslot;
				numclicks.add(element);}
			else {
				int previous=j-1;
				element = (int) (Math.round(Math.pow(0.7,previous) * topslot));
				numclicks.add(element);}
		}

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

		ArrayList<Integer> iterations = history.getSlotClicks(t-1);;
		ArrayList<Integer> numclicks = new ArrayList<>();
		int topslot = (int) (Math.round(35*Math.cos(Math.PI*t/24) + 40));
		for (int j=0; j<iterations.size();++j) {
			int element;
			if (j == 0) {
				element = topslot;
				numclicks.add(element);}
			else {
				int previous=j-1;
				element = (int) (Math.round(Math.pow(0.7,previous) * topslot));
				numclicks.add(element);}}

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
