
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

import java.util.ArrayList;

public class IPExercise2a {

	int[] ITEM_VALUES = new int[] { 2, 4, 6, 3, 5, 7, 5, 8, 5, 6, 3 };
	int[] ITEM_WEIGHTS = new int[] { 5, 3, 8, 3, 2, 4, 5, 5, 1, 5, 6 };

	int CAPACITY_A = 12;
	int CAPACITY_B = 10;

	private IMIP mip;

	public IPExercise2a() {
		this.buildMIP();
	}

	private void buildMIP() {
		mip = new MIP();

		ArrayList<Variable> ITEM_ALICE_CHOICE = new ArrayList<>();
		ArrayList<Variable> ITEM_BOB_CHOICE = new ArrayList<>();

		for (int i=0; i<ITEM_VALUES.length; i++) {
			ITEM_ALICE_CHOICE.add(new Variable("x"+i, VarType.INT, 0, 1));
			ITEM_BOB_CHOICE.add(new Variable("y"+i, VarType.INT, 0, 1));
			 mip.add(ITEM_ALICE_CHOICE.get(i));
			 mip.add(ITEM_BOB_CHOICE.get(i));
		}

		mip.setObjectiveMax(true);
			for (int i=0; i<ITEM_VALUES.length; i++) {
				mip.addObjectiveTerm(ITEM_VALUES[i], ITEM_ALICE_CHOICE.get(i));
				mip.addObjectiveTerm(ITEM_VALUES[i], ITEM_BOB_CHOICE.get(i));
			}

		Constraint c1 = new Constraint(CompareType.LEQ, CAPACITY_A);
		for (int i=0; i<ITEM_VALUES.length; i++) {
			c1.addTerm(ITEM_WEIGHTS[i], ITEM_ALICE_CHOICE.get(i));
		}
		mip.add(c1);

		Constraint c2 = new Constraint(CompareType.LEQ, CAPACITY_B);
		for (int i=0; i<ITEM_VALUES.length; i++) {
			c2.addTerm(ITEM_WEIGHTS[i], ITEM_BOB_CHOICE.get(i));
		}
		mip.add(c2);

		for (int i=0; i<ITEM_VALUES.length; i++) {
			Constraint c3 = new Constraint(CompareType.LEQ, 1);
			c3.addTerm(1, ITEM_ALICE_CHOICE.get(i));
			c3.addTerm(1, ITEM_BOB_CHOICE.get(i));
			mip.add(c3);
		}

	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		return solverClient.solve(mip);
	}

	public static void main(String[] argv) {
		IPExercise2a exercise = new IPExercise2a();
		IMIPResult result = exercise.solve();

		if (result != null && result.getObjectiveValue() == 36.0) {
			System.out.println("Congratulations. You obtained the correct objective value.");
		} else {
			System.out.println("The obtained solution is not correct.");
		}
	}
}
