package processbuilder001;

public class Principal {

	public static void main(String[] args) {

		try {
			Process proceso = Runtime.getRuntime().exec("notepad");

			proceso.waitFor();

		}

		catch (Exception e) {
			e.printStackTrace();

		}
	}

}
