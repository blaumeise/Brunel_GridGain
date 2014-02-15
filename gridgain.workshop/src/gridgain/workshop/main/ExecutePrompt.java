package gridgain.workshop.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ExecutePrompt {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String rightStatement = "Everything is Ok";
		String wrongStatement = "Wrong Password";
		boolean success = false;
		String pass = "1234";
		String command = "call C:\\Programme\\7-Zip\\7z.exe x C:\\Temp\\PW_old.rar -oC:\\Temp\\output -p" + pass + " -y";
		try {
			Runtime.getRuntime().exec("cmd");
			System.out.println("Windows command: " + command);
			String line;
			Process process = Runtime.getRuntime().exec("cmd /c " + command);
			Reader r = new InputStreamReader(process.getInputStream());
			BufferedReader in = new BufferedReader(r);
			while((line = in.readLine()) != null){
				if(line.contains(wrongStatement) || line.contains("null")) {
					success = false;
					System.out.println("falsches pw");
					break;
				}
				else if(line.contains(rightStatement)) {
					success = true;
					System.out.println("richtiges pw");
					break;
				}
					
			} System.out.println(line);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
