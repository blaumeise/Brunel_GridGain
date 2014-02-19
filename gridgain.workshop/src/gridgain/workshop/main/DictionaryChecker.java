package gridgain.workshop.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.jetbrains.annotations.Nullable;

public class DictionaryChecker {
	/**
	 * Checks if given value is a prime number.
	 * 
	 * @param val
	 *            Value to check for prime.
	 * @param minRage
	 *            Lower boundary of divisors range.
	 * @param maxRange
	 *            Upper boundary of divisors range.
	 * @return First divisor found or {@code null} if no divisor was found.
	 */

	private static final String rightStatement = "Everything is Ok";
	private static final String wrongStatement = "Wrong Password";

	@Nullable
	public static String checkPrime(String[] password, String fileName,
			int min, int max, boolean deleteInputFile) {

		if (System.getProperty("os.name").startsWith("Windows")) {
			// includes: Windows 2000, Windows 95, Windows 98, Windows NT,
			// Windows Vista, Windows XP, Windows 7, Windows 8...
			return checkPasswordWindows(password, fileName, min, max,
					deleteInputFile);
		} else { // UNIX and Mac
			// everything else
		}
		System.out.println("Operation not yet supported");
		return null;
	}

	private static String checkPasswordWindows(String[] password,
			String fileName, int min, int max, boolean deleteInputFile) {
		File fileCheck = new File(fileName);

		if (!fileCheck.exists()) {
			System.out
					.println("The 7zip file: "
							+ fileName
							+ " is not availible on this node, passwords in the range: "
							+ min + "-" + max + " are not tested");
			return null;
		}

		String outputFoldername = new File("").getAbsolutePath() + "\\output"
				+ min;

		for (int i = min; i < max && !Thread.currentThread().isInterrupted(); i++) {
			// String command = "call C:\\7-Zip\\7z.exe x C:\\Temp\\" + fileName
			// + " -oC:\\Temp\\output" + min + " -p" + password[i] + " -y";
			String command = "call 7z x " + fileName + " -o" + outputFoldername
					+ " -p" + password[i] + " -y";
			try {
				System.out.println("Windows command: " + command);
				String line;
				Process process = Runtime.getRuntime()
						.exec("cmd /c " + command);
				// bash -c
				Reader r = new InputStreamReader(process.getInputStream());
				BufferedReader in = new BufferedReader(r);
				while ((line = in.readLine()) != null) {
					if (line.contains(wrongStatement) || line.contains("null")) {
					} else if (line.contains(rightStatement)) {
						in.close();
						deleteFile(fileName, min, outputFoldername,
								deleteInputFile);
						return password[i];
					}
				}
				in.close();
			} catch (IOException e) {
				System.out.println("Exception: Execute 7zip command Statement failed");
			}
		}
		deleteFile(fileName, min, outputFoldername, deleteInputFile);
		return null;
	}

	private static void deleteFile(String fileName, int min, String foldername,
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

	private static void deleteElement(String name) {
		File file = new File(name);
		file.delete();
	}
}
