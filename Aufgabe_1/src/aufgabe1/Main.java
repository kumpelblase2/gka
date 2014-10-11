package aufgabe1;

public class Main {

	public static void main(String[] args) {
	GroovyParser gP = new GroovyParser();
		
		try{
			gP.setPath(""); //Add the path of the gka-file here
			gP.parse();		
		}
		catch(Exception ex){
			System.out.println("FileNotFound, please add or check path");
		}

	}

}
