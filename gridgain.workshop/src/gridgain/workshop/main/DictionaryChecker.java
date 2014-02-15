package gridgain.workshop.main;

import java.io.BufferedReader;
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
	@Nullable
	public static String checkPrime(String password, String fileName, int id) {
		// Loop through all divisors in the range and check if the value passed
		// in is divisible by any of these divisors.
		// Note that we also check for thread interruption which may happen
		// if the job was cancelled from the grid task.
		String rightStatement = "Everything is Ok";
		String wrongStatement = "Wrong Password";
		boolean success = false;
		String command = "call C:\\7-Zip\\7z.exe x C:\\Temp\\" + fileName
				+ " -oC:\\Temp\\output" + id + " -p" + password + " -y";
		try {
			Runtime.getRuntime().exec("cmd");

			System.out.println("Windows command: " + command);
			String line;
			Process process = Runtime.getRuntime().exec("cmd /c " + command);
			Reader r = new InputStreamReader(process.getInputStream());
			BufferedReader in = new BufferedReader(r);
			while ((line = in.readLine()) != null) {
				if (line.contains(wrongStatement) || line.contains("null")) {

					success = false;
					return null;
					// break;
				} else if (line.contains(rightStatement)) {
					return password;
					// success = true;
					// result[0] = ""+success;
					// result[2] = ""+i;
					// break;
				}
				// System.out.println(line);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Nullable
	public static String checkPrime(String[] password, String fileName,
			int min, int max) {
		// Loop through all divisors in the range and check if the value passed
		// in is divisible by any of these divisors.
		// Note that we also check for thread interruption which may happen
		// if the job was cancelled from the grid task.
		String rightStatement = "Everything is Ok";
		String wrongStatement = "Wrong Password";
		boolean success = false;
		for (int i = min; i <= max && !Thread.currentThread().isInterrupted(); i++) {
			String command = "call C:\\7-Zip\\7z.exe x C:\\Temp\\" + fileName
					+ " -oC:\\Temp\\output" + min + " -p" + password[i] + " -y";
			try {
				Runtime.getRuntime().exec("cmd");

				System.out.println("Windows command: " + command);
				String line;
				Process process = Runtime.getRuntime()
						.exec("cmd /c " + command);
				Reader r = new InputStreamReader(process.getInputStream());
				BufferedReader in = new BufferedReader(r);
				while ((line = in.readLine()) != null) {
					if (line.contains(wrongStatement)) {

						success = false;
//						return null;
						// break;
					} else if (line.contains(rightStatement)) {
						return password[i];
						// success = true;
						// result[0] = ""+success;
						// result[2] = ""+i;
						// break;
					}
					// System.out.println(line);
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

