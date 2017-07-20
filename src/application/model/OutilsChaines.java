package application.model;

public class OutilsChaines {

	/**
	 * Supprimer les caracteres non alphabetiques
	 * 
	 * @param pChaine
	 * @return
	 */
	public static String nettoyerChaine(String pChaine) {
		StringBuilder chainePropre = new StringBuilder();

		for (char c : pChaine.toCharArray()) {
			if (Character.isAlphabetic(c)) {
				chainePropre.append(c);
			}
		}

		return chainePropre.toString();
	}

	public static String enMaj(String pChaine) {
		StringBuilder chainePropre = new StringBuilder();

		for (char c : pChaine.toCharArray()) {
			chainePropre.append(Character.toUpperCase(c));
		}

		return chainePropre.toString();
	}
}
