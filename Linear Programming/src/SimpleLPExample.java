import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.MIP;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.Variable;

/**
 * Simple JOpt usage example:
 * 
 * Maximize 3x + 2y 
 * subject to 
 * x - 2x <= 7 
 * y = 3
 **/

public class SimpleLPExample {

	private IMIP linearProgram;

	public SimpleLPExample() {
		this.buildLinearProgram();
	}

	public IMIP getMIP() {
		return linearProgram;
	}

	private void buildLinearProgram() {
		linearProgram = new MIP();

		Variable x = new Variable("x", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);
		Variable y = new Variable("y", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);

		linearProgram.add(x);
		linearProgram.add(y);

		linearProgram.setObjectiveMax(true);
		linearProgram.addObjectiveTerm(3, x);
		linearProgram.addObjectiveTerm(2, y);

		Constraint c1 = new Constraint(CompareType.LEQ, 7);
		c1.addTerm(1, x);
		c1.addTerm(-2, y);
		linearProgram.add(c1);

		Constraint c2 = new Constraint(CompareType.EQ, 3);
		c2.addTerm(1, y);
		linearProgram.add(c2);
	}

	public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
		return solverClient.solve(linearProgram);
	}

	public static void main(String[] argv) {
		SimpleLPExample exercise = new SimpleLPExample();
		System.out.println(exercise.getMIP());
		System.out.println(exercise.solve());
	}
}
