import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.Variable;

public class IPExercise3 {

	private static final String PATH_TO_DISTANCES_TXT_FILE = "src/distances.txt";

	private static final int NUMBER_OF_CITIES = 17;

	private String[] cities;
	private double[][] distances;
	private int numberOfCities;
	private IMIPResult result = null;

	private IMIP mip;

	public IPExercise3(String[] cities, double[][] distances) {
		this.cities = cities;
		this.numberOfCities = cities.length;
		if (distances.length != numberOfCities || distances[0].length != numberOfCities) {
			throw new IllegalArgumentException(
					String.format("Matrix dimension should be %s x %s", numberOfCities, numberOfCities));
		}

		this.distances = distances;
		this.createMIP();
	}

	private void createMIP() {

		this.numberOfCities = cities.length;
		ArrayList<ArrayList<Variable> > PATH = new ArrayList<>();

		for (int i=0; i<numberOfCities; i++) {
			PATH.add(new ArrayList<>());
			for (int j=0; j<numberOfCities; j++) {
				if (i!=j) {PATH.get(i).add(new Variable("x"+i+"_"+j, VarType.INT, 0, 1));}
				else {PATH.get(i).add(new Variable("0", VarType.INT, 0, 0));}
				mip.add(PATH.get(i).get(j));
			}
		}

		//objective function
		mip.setObjectiveMax(false);
		for (int i=0; i<numberOfCities; i++) {
			for (int j=0; j<numberOfCities; j++) {mip.addObjectiveTerm(distances[i][j], PATH.get(i).get(j));}}

		//first constraint
		for (int i=0; i<numberOfCities; i++) {
			Constraint c1 = new Constraint(CompareType.EQ, 1);
			for (int j=0; j<numberOfCities; j++) {c1.addTerm(1, PATH.get(i).get(j));}
			mip.add(c1);}

		//second constraint
		for (int i=0; i<numberOfCities; i++) {
			Constraint c2 = new Constraint(CompareType.EQ, 1);
			for (int j=0; j<numberOfCities; j++) {c2.addTerm(1, PATH.get(j).get(i));}
			mip.add(c2);}

		//third constraint
		for (int i=0; i<numberOfCities; i++) {
			for (int j=0; j<numberOfCities; j++) {
				Constraint c3 = new Constraint(CompareType.LEQ, numberOfCities-1);
				c3.addTerm(numberOfCities, PATH.get(j).get(i));
				mip.add(c3); } }


	}


	private void solve() {
		SolverClient solverClient = new SolverClient();
		result = solverClient.solve(mip);
	}

	public String toString() {
		if (result == null) {
			return "Unsolved TSP for cities: " + cities;
		}

		else {
			String returnString = "TSP solved in " + result.getSolveTime() / 1000 + " seconds:\n";

			for (int i = 0; i < numberOfCities; i++) {
				for (int j = 0; j < numberOfCities; j++) {
					returnString += "From City "  + "to City \n";}
			}


			returnString += "Total distance: " + result.getObjectiveValue() + "\n";
			return returnString;
		}
	}

	/**
	 * Method to run your code and store solution in text file. Do not modify.
	 */
	public static void main(String args[]) throws IOException {
		String[] cities = createCities();
		double[][] distances = loadDistancesFromFile(PATH_TO_DISTANCES_TXT_FILE);

		IPExercise3 exercise = new IPExercise3(cities, distances);
		exercise.solve();

		File file = new File("output.txt");

		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(exercise.toString());
		bw.close();
	}

	/**
	 * Method to read distances from text file. Do not modify.
	 */
	private static double[][] loadDistancesFromFile(String fileName) {
		double[][] distances = new double[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String sCurrentLine = null;
			int currentRow = 0;
			while ((sCurrentLine = reader.readLine()) != null) {
				String[] numbersInLine = sCurrentLine.split(" +");
				int currentColumn = 0;
				for (String number : numbersInLine) {
					if (number.matches("-?\\d+(\\.\\d+)?")) {
						distances[currentRow][currentColumn] = Double.valueOf(number);
						currentColumn++;
					}
				}
				currentRow++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distances;
	}

	/**
	 * Method to create cities of the instance. Do not modify.
	 */
	private static String[] createCities() {
		String[] cities = new String[NUMBER_OF_CITIES];
		for (int i = 1; i <= NUMBER_OF_CITIES; i++) {
			cities[i - 1] = "City " + String.valueOf(i);
		}
		return cities;
	}

	public void setMip(IMIP mip) {
		this.mip = mip;
	}
}