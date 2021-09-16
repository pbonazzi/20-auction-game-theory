

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

public class LPExercise2b {

	private IMIP linearProgram;

	public LPExercise2b() {
		this.buildLinearProgram();
	}

	public IMIP getLP() {
		return linearProgram;
	}

	private void buildLinearProgram() {
		linearProgram = new MIP();

		/**
		 * Simple JOpt usage example:
		 *
		 * Max 4x1 + 2x2
		 * subject to 2x1 + x2 ≤ 3
		 * x1 + 2x2 ≤ 3
		 * x1 ≥ 0
		 * x2 ≥ 0
		 **/

		Variable x = new Variable("x", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);
		Variable y = new Variable("y", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);

		linearProgram.add(x);
		linearProgram.add(y);

		linearProgram.setObjectiveMax(true);
		linearProgram.addObjectiveTerm(4, x);
		linearProgram.addObjectiveTerm(2, y);

		Constraint c1 = new Constraint(CompareType.LEQ, 3);
		c1.addTerm(2, x);
		c1.addTerm(1, y);
		linearProgram.add(c1);

		Constraint c2 = new Constraint(CompareType.LEQ, 3);
		c2.addTerm(1, x);
		c2.addTerm(2, y);
		linearProgram.add(c2);

		Constraint c3 = new Constraint(CompareType.GEQ, 0);
		c3.addTerm(1, x);
		linearProgram.add(c3);

		Constraint c4 = new Constraint(CompareType.GEQ, 0);
		c4.addTerm(1, y);
		linearProgram.add(c4);
	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		IMIPResult result = solverClient.solve(linearProgram);
		return result;
	}

	public static void main(String[] argv) {
		LPExercise2b exercise = new LPExercise2b();
		System.out.println(exercise.getLP());
		System.out.println(exercise.solve());
	}
}
