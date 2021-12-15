import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

public class Host {
	
	public static void writingToFileByHost(File file, String str) {
		try (FileWriter fw = new FileWriter(file, true)) {
			fw.append(str).append("\n");
			System.out.println("File write action executed on file:"+file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void waitingTimeInSec(long time) {
		try{
			TimeUnit.SECONDS.sleep(time);
		}
		catch(Exception e){
			System.out.println("Wait time entered is incorrect,failure during conversion");
			System.exit(1);
		}
	}

	public static void ReceiverTypeFunc(int host, int lan){

		File hinFile = new File("hin" + host);
		if (!hinFile.exists()) {
			try {
				hinFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		File houtfile = new File("hout" + host);
		if (!houtfile.exists()) {
			try {
				houtfile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		File lanFile = new File("lan" + lan);
		if (!lanFile.exists()) {
			try {
				lanFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(lanFile));
		} catch (FileNotFoundException e) {
			System.out.println("Error during writing to lan file\n");
			e.printStackTrace();
			System.exit(1);
		}
		
		writingToFileByHost(houtfile, "receiver " + lan);
		for (int i = 1; i <= 120; i++) {
			if (i % 10 == 0) {
				writingToFileByHost(houtfile, "receiver " + lan);
			}
			String line;
			try {
				while((line = br.readLine()) != null) {
					if (line.matches("^data " + lan + " " + "[0-9]+")){
						writingToFileByHost(hinFile, line);
					}
				}
			} catch (Exception e) {
				System.out.println("Error during writing to hin host file\n");
				e.printStackTrace();
				System.exit(1);
			}
			waitingTimeInSec(1);
		}
		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void SenderTypeFunc(int host, int lan, int ttos, int period) {
		File houtfile = new File("hout" + host);
		if (!houtfile.exists()) {
			try {
				houtfile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		waitingTimeInSec(ttos);
		for (int i = 0; i < 120 - ttos; i++) {
			if (i % period == 0) {
				writingToFileByHost(houtfile, "data"+ " " + lan + " " + lan);
			}
			waitingTimeInSec(1);
		}
	}
	
	public static void PrintingErrorInfoFunc() {
		System.out.println("ErrorInfoLine(Arguments don't match for the type arguments): check host line input");
		System.exit(1);
	}

	public static void main(String[] arglist) {
		String type = " ";
		//int host = 1 , lan = 1;
		//int host = 10,lan = 2 , tts = 30 , period = 10;
		//String type = "sender";
		int host = -1, lan = -1 , ttos = -1, period = -1; 
		if (arglist.length == 5) {
			type = arglist[2];
			//checking for type argument here
			/*if(type.equals("sender")){
				System.out.println("arguments correct");
			}
			else{
				System.out.println("arguments incorrect");
			}
			*/
			if (!type.equals("sender")){
				PrintingErrorInfoFunc();
			}
			else{
				host = Integer.parseInt(arglist[0]);
				lan = Integer.parseInt(arglist[1]);
				ttos = Integer.parseInt(arglist[3]);
				period = Integer.parseInt(arglist[4]);
				if ((host < 0) || (host > 9)) {
					System.out.println("Error on input line ,check the host number value");
					System.exit(1);
				}

				if((lan < 0) || (lan > 9)){
					System.out.println("Error on input line ,check the lan number value");
					System.exit(1);
				}
				
				if((ttos < 0) || (ttos >= 120 )){
					System.out.println("Error on input line ,check the time to start value");
					System.exit(1);
				}
			}
		}
		else if (arglist.length == 3) {
			type = arglist[2];
			//checking for type argument here
			/*if(type.equals("receiver")){
				System.out.println("arguments correct");
			}
			else{
				System.out.println("arguments incorrect");
			}
			*/
			if (!type.equals("receiver")){
				PrintingErrorInfoFunc();
			}
			else{
				host = Integer.parseInt(arglist[0]);
				lan = Integer.parseInt(arglist[1]);
				
				if ((host < 0) || (host > 9 )) {
					System.err.println("Error on input line ,check the host number value");
					System.exit(1);
				}

				if((lan < 0 ) || (lan > 9)){
					System.err.println("Error on input line ,check the lan number value");
					System.exit(1);
				}
			}
		} 
	     else {
			PrintingErrorInfoFunc();
		}
		
		if (type.equals("receiver")) {
			ReceiverTypeFunc(host, lan);
		} else {
			SenderTypeFunc(host, lan, ttos, period);
		}
		
	}
}
