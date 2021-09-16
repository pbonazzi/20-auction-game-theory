
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

import java.util.Arrays;

public class IPExercise2b {

	int[] ITEM_WEIGHTS = new int[] { 5, 3, 8, 3, 2, 4, 5, 5, 1, 5, 6 };

	int CAPACITY = 10;

	private IMIP mip;

	public IPExercise2b() {
		this.buildMIP();
	}

	private void buildMIP() {
		mip = new MIP();

		Variable numSuitcase = new Variable("x", VarType.INT, -MIP.MAX_VALUE, MIP.MAX_VALUE);
		mip.add(numSuitcase);

		mip.setObjectiveMax(false);
		mip.addObjectiveTerm(1, numSuitcase);

		int coeff = (Arrays.stream(ITEM_WEIGHTS).sum()+CAPACITY-1)/CAPACITY;
		Constraint c1 = new Constraint(CompareType.GEQ, coeff);
		c1.addTerm(1, numSuitcase);
		mip.add(c1);

	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		return solverClient.solve(mip);
	}

	public static void main(String[] argv) {
		IPExercise2b exercise = new IPExercise2b();
		IMIPResult result = exercise.solve();

		if (result != null && result.getObjectiveValue() == 5.0) {
			System.out.println("Congratulations. You obtained the correct objective value.");
		} else {
			System.out.println("The obtained solution is not correct. ");
		}
	}
}
