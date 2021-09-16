package ch.uzh.ifi.ce.ca.winnerdetermination;

import java.util.*;

import ch.uzh.ifi.ce.ca.domain.*;
import ch.uzh.ifi.ce.ca.domain.mechanisms.Allocator;
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.mip.MIP;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.Variable;

/**
 * Wraps an OR or OR* winner determination
 * 
 * @author Benedikt Buenz
 * 
 */
public class XORWinnerDetermination extends WinnerDetermination implements Allocator {
    private Map<BundleBid, Variable> bidVariables = new HashMap<>();
    private IMIP winnerDeterminationProgram;

    public XORWinnerDetermination(Auction auction) {
        super(auction);
        winnerDeterminationProgram = createWinnerDeterminationMIP(auction);
    }

    private IMIP createWinnerDeterminationMIP(Auction auction) {
        MIP winnerDeterminationProgram = new MIP();

    	//TODO: Implement XOR WinnerDetermination Be sure to fill up the bidVariables Map

        Auction ThisAuction = getAuction();
        Map<Bidder, Bid> ListBid = ThisAuction.getBids().getBidMap();

        for (Bidder key: ListBid.keySet()) {
            Bid AllBids = ListBid.get(key);
            Set<BundleBid> ThisBundleBids = AllBids.getBundleBids();
            Iterator <BundleBid> NextBB = ThisBundleBids.iterator();
            while (NextBB.hasNext()) {
                BundleBid AgentBundleBid = NextBB.next();
                Variable Z = new Variable("x" + key, VarType.INT, 0, 1);
                bidVariables.put(AgentBundleBid, Z);
                winnerDeterminationProgram.add(Z);
            }
        }

        //objective function
        winnerDeterminationProgram.setObjectiveMax(true);
        for (BundleBid key: bidVariables.keySet()) {
            winnerDeterminationProgram.addObjectiveTerm(key.getAmount(), bidVariables.get(key));
        }


        return winnerDeterminationProgram;
    }

    protected IMIP getMIP() {
        return winnerDeterminationProgram;
    }

    @Override
    protected Variable getBidVariable(BundleBid bundleBid) {
        return bidVariables.get(bundleBid);
    }
}
