package gridgain.workshop.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.jetbrains.annotations.Nullable;

/**
 * @author Matthias Riedel and Philipp Trumpp
 * 
 * This class isolates methods implementing the password cracking logic.
 * Methods will be executed on every node of the grid.
 * Implements more utility methods for cleaning up the temporary files on each node.
 *
 */
public class DictionaryChecker {
	
	private static final String successfulExtract = "Everything is Ok";
	private static final String failedExtract = "Wrong Password";
	private static boolean execute7zipCommand = true;
	
	/**
	 * Method crackArchive
	 * Method checks the nodes operating system and uses the compatible method
	 * @param passwords
	 * @param fileName
	 * @param minRange
	 * @param maxRange
	 * @param deleteInputFile
	 * @return resulting password
	 */
	@Nullable
	public static String crackArchive(String[] passwords, String fileName,
			int minRange, int maxRange, boolean deleteInputFile) {

		if (System.getProperty("os.name").startsWith("Windows")) {
			// includes: Windows 2000, Windows 95, Windows 98, Windows NT,
			// Windows Vista, Windows XP, Windows 7, Windows 8...
			return checkPasswordWindows(passwords, fileName, minRange, maxRange,
					deleteInputFile);
		} else { // UNIX and Mac
			System.out.println("Your Operating System is not yet supported. Please remove this Node!\n Passwords in range " + minRange + " - " + maxRange + "will not be tested!");
		}
		return null;
	}

	/**
	 * Method checkPasswordWindows
	 * Method executes the 7-Zip "command line"-call for each specific password in list passwords
	 * @param passwords
	 * @param fileName
	 * @param minRange
	 * @param maxRange
	 * @param deleteInputFile
	 * @return resulting password
	 */
	private static String checkPasswordWindows(String[] passwords,
			String fileName, int minRange, int maxRange, boolean deleteInputFile) {
		File fileCheck = new File(fileName);

		if (!fileCheck.exists()) {
			System.out
					.println("The 7zip file: "
							+ fileName
							+ " is not availible on this node, passwords in the range: "
							+ minRange + "-" + maxRange + " are not tested");
			return null;
		}

		String outputFoldername = new File("").getAbsolutePath() + "\\output"
				+ minRange;

		for (int i = minRange; i < maxRange && !Thread.currentThread().isInterrupted(); i++) {
			String command = "call 7z x " + fileName + " -o" + outputFoldername
					+ " -p\"" + passwords[i] + "\" -y";
			if(execute7zipCommand) {
			try {
				System.out.println("Windows command: " + command);
				String line;
				Process process = Runtime.getRuntime()
						.exec("cmd /c " + command);	// Execution of command line call
				// bash -c //for future unix functionality in seperated method
				Reader r = new InputStreamReader(process.getInputStream());
				BufferedReader in = new BufferedReader(r);
				while ((line = in.readLine()) != null) {	//examination of command line output
					if (line.contains(failedExtract) || line.contains("null")) {
					} else if (line.contains(successfulExtract)) {
						in.close();
						deleteFile(fileName, minRange, outputFoldername,
								deleteInputFile);
						return passwords[i];
					}
				}
				in.close();
			} catch (IOException e) {
				System.out.println("Exception: Execute 7zip command Statement failed");
			}
		}}
		deleteFile(fileName, minRange, outputFoldername, deleteInputFile);
		return null;
	}

	/**
	 * Method deleteFile
	 * Method deletes temporary extracted files on every node
	 * @param fileName
	 * @param folderIndex
	 * @param foldername
	 * @param deleteInputFile
	 */
	private static void deleteFile(String fileName, int folderIndex, String foldername,
			boolean deleteInputFile) {

		if (deleteInputFile) {
			deleteElement(fileName);
			System.out.println("Delete file: " + fileName);
		}

		File folder = new File(foldername);
		String[] children = folder.list();
		for (int i = 0; children != null && i < children.length; i++) {
			deleteElement(folder.getAbsoluteFile() + "\\" + children[i]);
		}
		folder.delete();
		System.out.println("Deleted folder with its elements: " + foldername);
	}

	/**
	 * Method deleteElement
	 * Method deletes the specific file object
	 * @param name
	 */
	private static void deleteElement(String name) {
		File file = new File(name);
		file.delete();
	}
}
