
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

public class LPExercise3b {

	private IMIP linearProgram;

	public LPExercise3b() {
		this.buildLinearProgram();
	}

	public IMIP getLP() {
		return linearProgram;
	}

	private void buildLinearProgram() {
		linearProgram = new MIP();

		Variable x = new Variable("x", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);
		Variable y = new Variable("y", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);

		linearProgram.add(x);
		linearProgram.add(y);

		linearProgram.setObjectiveMax(true);
		linearProgram.addObjectiveTerm(10, x);
		linearProgram.addObjectiveTerm(6, y);

		Constraint c1 = new Constraint(CompareType.LEQ, 2800);
		c1.addTerm(6, x);
		c1.addTerm(6, y);
		linearProgram.add(c1);

		Constraint c2 = new Constraint(CompareType.LEQ, 3000);
		c2.addTerm(6, x);
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
		LPExercise3b exercise = new LPExercise3b();
		System.out.println(exercise.getLP());
		System.out.println(exercise.solve());
	}
}
