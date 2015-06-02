package jchess;
import java.util.UUID;


public class Biblio {

	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		if(uuid.length() > 5) {
			uuid = uuid.substring(0, 5);
		}
		System.out.println(uuid);
		return uuid;
	}
	
	/*
	public static void main(String[] args) {
		Biblio.generateUUID();
	}*/
}
