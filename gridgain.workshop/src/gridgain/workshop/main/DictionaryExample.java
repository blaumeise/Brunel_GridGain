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
import java.util.UUID;

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
 * 
 * @author Matthias Riedel and Philipp Trumpp
 * 
 * This class is called by the GuiController to get access on the dictionary-attack logic.
 * It prepares and distributes the tasks on the available nodes and starts the procedure.
 * The deprecated methods were used for debugging purpose when we tried to disable selected nodes.
 *
 */
public final class DictionaryExample {

	static private GuiController guiController = null;

	/**
	 * Method copyFile
	 * Method distributes zip-archive on nodes.
	 * Works only on local nodes because of what seems like missing permissions.
	 * @param source
	 * @param i
	 * @return absolute path of file
	 * @throws IOException
	 */
	private static String copyFile(File source, int i) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		File file = new File("");
		String destlink = file.getAbsolutePath();
		destlink = destlink.replace("\\", "/");
		String FileName = source.getName();
		if (FileName.lastIndexOf(".") != -1) {
			String type = FileName.substring(FileName.lastIndexOf("."));
			FileName = FileName.substring(0, FileName.lastIndexOf("."));
			FileName = FileName + i + type;
		}
		File dest = new File(destlink + "/" + FileName);
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
		return dest.getAbsolutePath();
	}

	/**
	 * Method readPasswordFile
	 * Method reads file of passwords at static path
	 * @return array of passwords
	 * @throws IOException
	 */
	private static String[] readPasswordFile() throws IOException {
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

	/**
	 * Method readPasswordFile
	 * Method reads file of passwords at specific path from parameter filename
	 * @param filename
	 * @return array of passwords
	 * @throws IOException
	 */
	private static String[] readPasswordFile(String filename) throws IOException {
		
		File file = new File(filename);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String inputLine = null;
		ArrayList<String> array = new ArrayList<String>();

		while ((inputLine = in.readLine()) != null) {
			array.add(escapePassword(inputLine));
		}
		String[] pwArray = new String[array.size()];

		for (int i = 0; i < pwArray.length; i++) {
			pwArray[i] = array.get(i);
		}
		return pwArray;
	}
	
	/**
	 * Method escapePassword
	 * @param input
	 * @return escaped password
	 */
	public static String escapePassword(String input) {
		input = input.replace("\"", "");
		return input;
	}
	
	@Deprecated
	public static void printIPAdress(Collection<String> collection){
		for(String string : collection){
			System.out.println(string);
		}
	}
	
	@Deprecated
	public static String detectMasterIP(Collection<String>collection){
		int counter = 0;
		for(String value : collection){
			if(counter ==3 ){ //TODO Muss 2 sein, fuer test die drei
				return value;
			}
			counter++;
		}
		return "";
	}

	@Deprecated
	public static boolean compareIPs(Collection<String>collection, String masterIP){
		for (String value : collection){
			if(value.equals(masterIP)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method executeDictionary
	 * Method routes gui information to dictionary logic.
	 * Implementation of time measurement.
	 * Disabled code within method tried to remove specific nodes from grid.
	 * @param filename
	 * @param dictionaryname
	 * @param args
	 * @param guiController
	 * @param deleteInput
	 * @param remoteUse
	 * @throws IOException
	 * @throws GridException
	 */
	public static void executeDictionary(String filename, String dictionaryname, String args[],
			GuiController guiController, boolean deleteInput, boolean remoteUse) throws IOException, GridException {
		if (DictionaryExample.guiController == null) {
			DictionaryExample.guiController = guiController;
		}
		String[] posPW = readPasswordFile(dictionaryname);

		X.println(">>>");
//		X.println(">>> Starting to check with the following dictionary: "
//				+ Arrays.toString(posPW));

		try (Grid g = args.length == 0 ? G.start("config/default-config.xml")
				: G.start(args[0])) {
			long start = System.currentTimeMillis();

			final File file = new File(filename);

//			Collection<String> a = g.localNode().addresses();
//			System.out.println("Master");
//			printIPAdress(a);
//			String masterIP = detectMasterIP(a);
//			Collection<UUID> nodesToDisable = null;
//			
//			for(GridNode sg : G.grid().nodes()){
//				Collection<String> i = sg.externalAddresses();
//				Collection<String> j = sg.internalAddresses();
//				Collection<String> k = sg.addresses();
//				System.out.println("Schleifendurchlauf");
//				printIPAdress(i);
//				printIPAdress(j);
//				printIPAdress(k);
//				System.out.println("Nodeid: "+ sg.id());
//				
//				if(
//						//compareIPs(i, masterIP) || 
////						compareIPs(j, masterIP) || 
//						compareIPs(k, masterIP)){
//					System.out.println("compare IPs");
//					UUID nodeID = sg.id();
////					nodesToDisable.add(nodeID);
//					System.out.println("groesse before stop: " + g.size());
//					int se = g.size();
////					g.stopNodes(nodeID);
//					
//					
//					System.out.println("groesse after stop: " + g.size());
//				}
//			}
//			System.out.println("-----Node Disable Start-----");
//			g.stopNodes(nodesToDisable);
//			System.out.println("Grid Size: " + g.size());
//			System.out.println("-----Node Disable End-----");
//			
//			System.out.println("-----Node Enable Start-----");
//			g.restartNodes(nodesToDisable);
//			System.out.println("Grid Size: " + g.size());
//			System.out.println("-----Node Enable End-----");
//			
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

/**
 * Method closures
 * Method distributes specific range of passwords on nodes
 * @param gridSize
 * @param file
 * @param deleteInput
 * @param remoteUsed
 * @param dictionaryname
 * @return job results as collection
 * @throws IOException
 */
	private static Collection<GridOutClosure<String>> closures(int gridSize,
			final File file, final boolean deleteInput, final boolean remoteUsed, String dictionaryname) throws IOException {
		final String[] posPW = readPasswordFile(dictionaryname);
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
					
					boolean distributedNodes = remoteUsed;

					String filename = new File("").getAbsolutePath() + "\\"
							+ file.getName();
					try {
						if (!distributedNodes) {
							filename = copyFile(file,
									Integer.parseInt(String.valueOf(min)));
						}
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Relativer Filepfad: " + filename);
					boolean deleteCopyInputFile = deleteInput;
					return DictionaryChecker.crackArchive(posPW, filename,
							Integer.parseInt(String.valueOf(min)),
							Integer.parseInt(String.valueOf(max)),
							deleteCopyInputFile);
				}
			});
		}

		return cls;

	}
}