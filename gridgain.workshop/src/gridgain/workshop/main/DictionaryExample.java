package gridgain.workshop.main;

import gridgain.workshop.gui.GuiController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridClosureCallMode;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridNode;
import org.gridgain.grid.lang.GridOutClosure;
import org.gridgain.grid.typedef.CO;
import org.gridgain.grid.typedef.G;
import org.gridgain.grid.typedef.R1;
import org.gridgain.grid.typedef.X;
import org.jetbrains.annotations.*;

/**
 * Simple prime checkers. The implementation of this class iterates through the
 * passed in list of divisors and checks if the value is divisible by any of
 * these divisors.
 * 
 * @author 2013 Copyright (C) GridGain Systems
 * @version In-Memory HPC 5.3.1
 */
public final class DictionaryExample {

	static private GuiController guiController = null;

	private static String copyFile(File source, int i) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		File file = new File("");
		System.out.println("Current dir : " + file.getCanonicalPath());
		String destlink = file.getAbsolutePath();
		destlink = destlink.replace("\\", "/");
		String FileName = source.getName();
		if (FileName.lastIndexOf(".") != -1) {
			String type = FileName.substring(FileName.lastIndexOf("."));
			System.out.println("Type: " + type);
			FileName = FileName.substring(0, FileName.lastIndexOf("."));
			System.out.println("SubFileName: " + FileName);
			FileName = FileName + i + type;
		}
		File dest = new File(destlink + "/" + FileName);
		System.out.println("Panik: " + new File(destlink).getAbsolutePath());
		if (dest != null)
			dest.delete();
		dest.createNewFile();
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
		System.out.println("Destpath: " + dest.getAbsolutePath());
		System.out.println("Filename: " + FileName);
		return dest.getAbsolutePath();
	}

	private static String[] readPWFile() throws IOException {
		File file = new File(new File("").getAbsolutePath() + "\\PW.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));
		String inputLine = null;
		ArrayList<String> array = new ArrayList<String>();

		while ((inputLine = in.readLine()) != null) {
			array.add(inputLine);
		}
		String[] pwArray = new String[array.size()];

		for (int i = 0; i < pwArray.length; i++) {
			pwArray[i] = array.get(i);
		}
		return pwArray;
	}

	private static String[] readPWFile(String filename) throws IOException {
		
		File file = new File(filename);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String inputLine = null;
		ArrayList<String> array = new ArrayList<String>();

		while ((inputLine = in.readLine()) != null) {
			array.add(ecsapePassword(inputLine));
		}
		String[] pwArray = new String[array.size()];

		for (int i = 0; i < pwArray.length; i++) {
			pwArray[i] = array.get(i);
		}
		return pwArray;
	}
	
	public static String ecsapePassword(String input) {
		input = input.replace("\"", "");
		return input;
	}
	
	public static void printIPAdress(Collection<String> collection){
		for(String string : collection){
			System.out.println(string);
		}
	}
	
	public static void executeDictionary(String filename, String dictionaryname, String args[],
			GuiController guiController, boolean deleteInput, boolean remoteUse) throws IOException, GridException {
		if (DictionaryExample.guiController == null) {
			DictionaryExample.guiController = guiController;
		}
		String[] posPW = readPWFile(dictionaryname);

		X.println(">>>");
		X.println(">>> Starting to check with the following dictionary: "
				+ Arrays.toString(posPW));

		try (Grid g = args.length == 0 ? G.start("config/default-config.xml")
				: G.start(args[0])) {
			long start = System.currentTimeMillis();

			final File file = new File(filename);

			Collection<String> a = g.localNode().addresses();
			printIPAdress(a);
			for(GridNode sg : G.grid().nodes()){
				Collection<String> i = sg.externalAddresses();
				printIPAdress(i);
				
			}
			
			String divisor = g.reduce(GridClosureCallMode.SPREAD,
					closures(g.size(), file, deleteInput, remoteUse, dictionaryname), new R1<String, String>() {

						private String divisor;

						/** {@inheritDoc} */
						@Override
						public boolean collect(String e) {
							// If divisor is found then stop collecting.
							return (divisor = e) == null;
						}

						/** {@inheritDoc} */
						@Override
						public String apply() {
							return divisor;
						}
					});

			if (divisor == null) {
				X.println(">>> There is no Password");
				DictionaryExample.guiController.showPassword("");
			} else{
				X.println(">>> Value '" + divisor + "' is Password");
				DictionaryExample.guiController.showPassword(divisor);
			}

			long totalTime = System.currentTimeMillis() - start;

			X.println(">>> Total time to calculate all pws (milliseconds): "
					+ totalTime);
			X.println(">>>");
		}
	}


	private static Collection<GridOutClosure<String>> closures(int gridSize,
			final File file, final boolean deleteInput, final boolean remoteUsed, String dictionaryname) throws IOException {
		final String[] posPW = readPWFile(dictionaryname);
		Collection<GridOutClosure<String>> cls = new ArrayList<>(gridSize);
		long val = posPW.length;

		long taskMinRange = 2;

		long numbersPerTask = val / gridSize < 10 ? 10 : val / gridSize;

		long jobMinRange;
		long jobMaxRange = 0;

		// In this loop we create as many grid jobs as
		// there are nodes in the grid.
		for (int i = 0; jobMaxRange < val; i++) {
			jobMinRange = i * numbersPerTask;// + taskMinRange;
			jobMaxRange = (i + 1) * numbersPerTask + taskMinRange - 1;

			if (jobMaxRange > val)
				jobMaxRange = val;

			final long min = jobMinRange;

			final long max = jobMaxRange;

			cls.add(new CO<String>() {
				/**
				 * Check if the value passed in is divisible by any of the
				 * divisors in the range. If so, return the first divisor found,
				 * otherwise return {@code null}.
				 * 
				 * @return First divisor found or {@code null} if no divisor was
				 *         found.
				 */
				@Nullable
				@Override
				public String apply() {
					// Return first divisor found or null if no
					// divisor was found.
					// return GridPrimeChecker.checkPrime(val, min, max);

				
					boolean distributedNodes = remoteUsed;

					String filename = new File("").getAbsolutePath() + "\\"
							+ file.getName();
					System.out.println("Relativer Filepfad: " + filename);
					try {
//						distributedNodes = true;
						if (!distributedNodes) {
							filename = copyFile(file,
									Integer.parseInt(String.valueOf(min)));
						}
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Closures: " + filename);
					boolean deleteCopyInputFile = deleteInput;
					return DictionaryChecker.checkPrime(posPW, filename,
							Integer.parseInt(String.valueOf(min)),
							Integer.parseInt(String.valueOf(max)),
							deleteCopyInputFile);
				}
			});
		}

		return cls;

	}
}