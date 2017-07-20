package application.model;

import java.util.HashMap;

/**
 * Encoder et décoder des chaines de caractères grâce au chiffre de Vigenere
 * @author clement
 */
public class ChiffrementVigenere {

	/** Indice de coincidence de la langue francaise */
	public static final double INDICE_COINCIDENCE_FR = 0.0778;

	/** Taux d'erreur accepté pour l'indice de coincidence */
	public static final double ERREUR_ACCEPTEE_FR = 0.008;

	/** Chaine originale */
	public String original;

	/** Chaine encodée */
	public String encode;

	/** Clé utilisée */
	public String cle;

	/**
	 * Déterminer la clé de vigenere en conaissant la longueur de la clé
	 * 
	 * @param chaine
	 * @param longueurCle
	 * @return
	 */
	public static String chercherCleVigenere(String chaine, int longueurCle) {
		// Création de sous chaines
		String[] sousChaines = new String[longueurCle];
		StringBuilder iChaine;

		// récréer une chaine propre
		chaine = OutilsChaines.nettoyerChaine(chaine);

		// formation des sous chaines a analyser séparemment
		for (int i = 0; i < longueurCle; i++) {
			// formation de chaque chaine
			iChaine = new StringBuilder();
			for (int j = 0; i + j < chaine.length(); j += longueurCle) {
				iChaine.append(chaine.charAt(i + j));
			}
			sousChaines[i] = iChaine.toString();
		}

		// Utilisation du code de cesar sur chaque sous chaine pour déterminer
		// les caractères de la clé;
		String cle = "";
		for (int i = 0; i < sousChaines.length; i++) {
			cle += "" + (char) ('A' + CodeCesar.rechercheCle(sousChaines[i]));
			// System.out.println("Cle pour " + sousChaines[i] + " = > " +
			// CodeCesar.rechercheCle(sousChaines[i]));
		}

		return cle;
	}

	public static String chercherCleVigenere(String chaine) {
		chaine = OutilsChaines.nettoyerChaine(chaine);

		int longueurCle = determinerLongueurCle(chaine);
		return chercherCleVigenere(chaine, longueurCle);

	}

	/**
	 * Retourne une HashMap contenant pour chaque caractères le nombre
	 * d'occurences de celui-ci dans la chaine passée en parametre
	 * 
	 * @param chaine chaine à analyser
	 * @return une hashmap précisant le nombre d'occurences de chaque caractères
	 *         de la chaine
	 */
	public static HashMap<Character, Integer> apparitions(String chaine) {
		HashMap<Character, Integer> resultat = new HashMap<Character, Integer>();

		for (Character c : chaine.toCharArray()) {
			if (Character.isLetter(c)) {
				c = Character.toUpperCase(c);
				if (resultat.containsKey(c)) {
					resultat.put(c, resultat.get(c) + 1);
				} else {
					resultat.put(c, 1);
				}
			}
		}
		return resultat;

	}

	/**
	 * Calcul de l'indice de coïncidence
	 * @param chaine la chaine à analyser
	 * @return l'indice de coïncidence
	 */
	public static double indiceCoincidence(String chaine) {
		HashMap<Character, Integer> apparitions = apparitions(chaine);

		double s = 0.0;
		double sommeTotale = 0.0;
		for (Character c : apparitions.keySet()) {
			s += (apparitions.get(c) * (apparitions.get(c) - 1));
			sommeTotale += apparitions.get(c);
		}

		return s / (sommeTotale * (sommeTotale - 1));
	}

	/**
	 * Déterminer la longueur de la clé utilisée pour chiffrer une chaine
	 * Attention => ne fonctionne que si la clé est inférieure à 50
	 * @param chaine
	 * @return la longueur de la clé
	 */
	public static int determinerLongueurCle(String chaine) {
		int longueurCle = -1;
		double indiceCoincidenceCle = Double.MAX_VALUE;

		int longueurTemp;
		double indiceCoincidenceTemp;

		// Analyse de chaine en les decomposant de 1 à 12 textes
		for (int essai = 1; essai <= 20; essai++) {

			// Création de sous chaines
			String[] sousChaines = new String[essai];
			StringBuilder iChaine;

			// formation des sous chaines a analyser séparemment
			for (int i = 0; i < essai; i++) {
				// formation de chaque chaine
				iChaine = new StringBuilder();
				for (int j = 0; i + j < chaine.length(); j += essai) {
					iChaine.append(chaine.charAt(i + j));
				}
				sousChaines[i] = iChaine.toString();
			}

			// calcul des indices de coincidences
			double sommeIndice = 0.0;
			for (int i = 0; i < sousChaines.length; i++) {
				sommeIndice += ChiffrementVigenere.indiceCoincidence(sousChaines[i]);
			}

			longueurTemp = sousChaines.length;
			indiceCoincidenceTemp = sommeIndice / sousChaines.length;
			if (Math.abs(indiceCoincidenceTemp - INDICE_COINCIDENCE_FR) < Math
					.abs(indiceCoincidenceCle - INDICE_COINCIDENCE_FR)) {
				indiceCoincidenceCle = indiceCoincidenceTemp;
				longueurCle = longueurTemp;

				if (Math.abs(indiceCoincidenceTemp - INDICE_COINCIDENCE_FR) < ERREUR_ACCEPTEE_FR) {
					break;
				}

			}
		}

		return longueurCle;

	}

	/**
	 * 
	 * @param cle la clé utilisée pour encoder/décoder la chaine mot
	 * @param mot la chaine à encoder / décoder
	 * @param dejaChiffre boolean indiquant si mot est chiffré ou non
	 */
	public ChiffrementVigenere(String cle, String mot, boolean dejaChiffre) {
		String cleTemp = cle.toUpperCase();
		char[] charCle = cleTemp.toCharArray();
		// eliminer les caractères indésirables
		StringBuilder vraieCle = new StringBuilder();
		for (char c : charCle) {
			if ((c + "").matches("\\p{Alpha}")) {
				vraieCle.append(Character.toUpperCase(c));
			}
		}

		this.cle = vraieCle.toString();

		mot = OutilsChaines.enMaj(mot);

		if (dejaChiffre) {
			encode = mot;
			original = dechiffrer();
		} else {
			original = mot;
			encode = chiffrer();
		}
	}

	public String chiffrer() {
		char[] tabCle = cle.toCharArray();
		char[] aChiffrer = original.toCharArray();
		StringBuilder chiffre = new StringBuilder();

		for (int i = 0, j = 0; i < aChiffrer.length; i++) {
			if ((aChiffrer[i] + "").matches("\\p{Alpha}")) {
				chiffre.append((char) (65 + ((Character.toUpperCase((aChiffrer[i])) + tabCle[j % cle.length()]) % 26)));
				j++;
			} else {
				chiffre.append(Character.toUpperCase(aChiffrer[i]));
			}
		}
		return chiffre.toString();
	}

	public String dechiffrer() {
		char[] tabCle = cle.toCharArray();
		char[] aDechiffrer = encode.toCharArray();
		int difference;
		StringBuilder dechiffre = new StringBuilder();

		for (int i = 0, j = 0; i < aDechiffrer.length; i++) {
			if ((aDechiffrer[i] + "").matches("\\p{Alpha}")) {
				difference = (aDechiffrer[i] - tabCle[j % cle.length()]) % 26;
				if (difference >= 0) {
					dechiffre.append((char) (65 + difference));
				} else {
					dechiffre.append((char) (65 + 26 + difference));
				}
				j++;
			} else {
				dechiffre.append(aDechiffrer[i]);
			}
		}
		return dechiffre.toString();
	}

	/**
	 * Déterminer les fréquences d'apparitions de chaque lettres sur le mot
	 * encodé
	 * 
	 * @return une HashMap Character > Double
	 */
	public HashMap<Character, Double> determinerFrequences() {

		HashMap<Character, Double> frequences = new HashMap<>();
		for (Character c : encode.toCharArray()) {
			if (Character.isLetter(c)) {
				c = Character.toUpperCase(c);
				if (frequences.containsKey(c)) {
					frequences.put(c, frequences.get(c) + 1.0);
				} else {
					frequences.put(c, 1.0);
				}
			}
		}

		for (Character cle : frequences.keySet()) {
			frequences.put(cle, frequences.get(cle) / original.length() * 1000);
		}

		return frequences;
	}

	public String getCle() {
		return cle;
	}

	public String getOriginal() {
		return original;
	}

	public String getEncode() {
		return encode;
	}
}
