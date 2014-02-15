package gridgain.workshop.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.GridSecuritySubjectType;
import org.gridgain.grid.typedef.*;

import static org.gridgain.grid.GridClosureCallMode.*;

public final class ZipDecryption {
	private static final int N = 1000;

	private static ArrayList<ArrayList<String>> array;
	
	private static double calcPi(int start) {
		double acc = 0.0;
		for (int i = start; i < start + N; i++)
			acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
		return acc;
	}

	private static void copyFile(File source, int i) throws IOException{
		 InputStream is = null;
	        OutputStream os = null;
	        File file = new File(".");  
	        System.out.println("Current dir : " + file.getCanonicalPath());
	        String destlink = file.getCanonicalPath();
	        destlink = destlink.replace("\\", "/");
	        File dest = new File(destlink+"/installlog"+i+".txt");
	        if (dest!=null)
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
	}
	
	private static void readPWFile(int size) throws IOException{
		File file = new File("C:\\Temp\\PW.txt");
		BufferedReader in = new BufferedReader(new FileReader (file));
		String inputLine = null;
		int counter = 0;
		array = new ArrayList<ArrayList<String>>(size);
		for (int i = 0; i < array.size(); i++){
			
		}
//		while((inputLine = in.readLine()) != null){
//			array.get()
//		}

	}
	
	public static void main(String[] args) throws GridException, FileNotFoundException {
		G.start();
		Grid g = G.grid();
//		g.configuration().getSecureSessionSpi().
		final File file = new File("C:\\Temp\\tools.jar");
//		final File file = new File("C:\\Temp\\installlog.txt");
		
		readPWFile(g.size());
		try {
			System.out.println("Pi estimate: "
					+ g.reduce(SPREAD, F.yield(F.range(0, g.size()),
							new C1<Integer, Double>() {
								@Override
								public Double apply(Integer i) {
									try {
										copyFile(file, i);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return calcPi(i * N);
								}
							}), F.sumDoubleReducer()));
		} finally {
			G.stop(true);
		}
	}
}
