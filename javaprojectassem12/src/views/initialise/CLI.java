package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CLI {

	private BufferedReader reader;
	private BufferedWriter writer;
	public CLI(){
		this.reader= new BufferedReader(new InputStreamReader(System.in));
		this.writer= new BufferedWriter(new OutputStreamWriter(System.out));
		this.run();
	}
	private void run() {


		try {
			String derp =reader.readLine();
			writer.write(derp+"succes");
			writer.flush();
		} catch (IOException e) {
			System.out.println("u failed xd");
		}

	}

	class Communication{
		public void write(String input) throws IOException{
			writer.write(input);
			writer.flush();
		}
		public String readInput() throws IOException{
			return reader.readLine();
		}
	}

}
