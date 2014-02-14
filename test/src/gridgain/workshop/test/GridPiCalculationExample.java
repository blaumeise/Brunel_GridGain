package gridgain.workshop.test;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.*;
import static org.gridgain.grid.GridClosureCallMode.*;

public final class GridPiCalculationExample {
	private static final int N = 1000;

	private static double calcPi(int start) {
		double acc = 0.0;
		for (int i = start; i < start + N; i++)
			acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
		return acc;
	}

	public static void main(String[] args) throws GridException {
		G.start();
		Grid g = G.grid();
		try {
			System.out.println("Pi estimate: "
					+ g.reduce(SPREAD, F.yield(F.range(0, g.size()),
							new C1<Integer, Double>() {
								@Override
								public Double apply(Integer i) {
									return calcPi(i * N);
								}
							}), F.sumDoubleReducer()));
		} finally {
			G.stop(true);
		}
	}
}