
import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.*;

public class LPExercise3c {

	private IMIP linearProgram;

	public LPExercise3c() {
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

		Constraint c1 = new Constraint(CompareType.LEQ, 1200);
		c1.addTerm(12, x);
		c1.addTerm(8, y);
		linearProgram.add(c1);

		Constraint c2 = new Constraint(CompareType.GEQ, 0);
		c2.addTerm(1, x);
		linearProgram.add(c2);

		Constraint c3 = new Constraint(CompareType.GEQ, 0);
		c3.addTerm(1, y);
		linearProgram.add(c3);
	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		IMIPResult result = solverClient.solve(linearProgram);
		return result;
	}

	public static void main(String[] argv) {
		LPExercise3c exercise = new LPExercise3c();
		System.out.println(exercise.getLP());
		System.out.println(exercise.solve());
	}
}
