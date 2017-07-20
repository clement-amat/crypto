package application.model;

import java.util.HashMap;

public class CodeCesar {
	
	/** Fréquences moyennes d'utilisation des lettres françaises (wikipedia) */
	public static HashMap<Character, Double> francais;
	
	/** 
	 * cl� de chiffrement
	 * d�calage en nombre de caract�res � utiliser 
	 */
	private int cle;
	
	/** mot orignal d�chiffr� */
	private String original;
	
	/** mot chiffr� */
	private String encode;
	

	/**
	 * Initialisation de la HashMap représentant les moyennes d'utilisation des lettres francaises
	 */
	static {
		francais = new HashMap<>();
		final int[] FREQ = {942,102,264,339,1587,95,104,77,841,89,0,534,324,715,514,286,106,646,790,726,624,215,0,30,24,32};
		
		int i = 0;
		for (char c = 'A' ; c <= 'Z' ; c++) {
			francais.put(new Character(c), FREQ[i] + 0.0);
			i++;
		}
	}

	private static String dechiffrer(int c, String aDechiffrer) {
		return chiffrer(26 - c, aDechiffrer);
	}

	private static String chiffrer(int c, String mot) {
		StringBuilder motChiffre = new StringBuilder();
		char[] lettres = mot.toCharArray();

		for (int i = 0 ; i < lettres.length ; i++) {
			lettres[i] = Character.toUpperCase(lettres[i]);
			if (Character.isLetter(lettres[i])) {
				lettres[i] = (char) ('A' + ((lettres[i] + c)-'A') % 26);
			}
			motChiffre.append(lettres[i]);
		}

		return motChiffre.toString();
	}
	
	/**
	 * @return un tableau de String contenant les 26 possibilit�s de 
	 *		   d�chiffrement d'un mot donn�
	 */
	public static String[] bruteForce(String mot) {
		String[] possibilites = new String[26];
		
		for (int cle = 0 ; cle < possibilites.length ; cle ++) {
			possibilites[cle] = dechiffrer(cle, mot);
		}
		return possibilites;
	}
	
	/**
	 * Déterminer les fréquences d'apparitions de chaque lettres sur le mot encodé
	 * @return une HashMap Character > Double
	 */
	public static HashMap<Character, Double> determinerFrequences(String chaine) {
		
		HashMap<Character, Double> frequences = new HashMap<>();
		for (Character c : chaine.toCharArray()) {
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
			frequences.put(cle, frequences.get(cle) / chaine.length() * 10000);
		}
	
		return frequences;
	}

	public static int rechercheCle(String aDecoder) {
		/* 
		 * Pour chaque indice correspond la corrélation entre les fréquences des lettres du texte chiffrée
		 * et les fréquence du francais normal
		 */
		double[] correspondances = new double[26];
		
		// On essaye de déchiffrer le texte avec les 26 clés possibles
		for (int i = 0 ; i < 26 ; i++) {
		
			// fréquences calculées pour le décalage en cours d'analyse (varie fonction de i)
			HashMap<Character, Double> iFrequences = determinerFrequences(dechiffrer(i, aDecoder));
			
			// calcul de la somme des produits entre chaque lettre entre le francais moyen et le texte chiffré
			double somme = 0.0;
			for (Character cle : iFrequences.keySet()) {
				somme += iFrequences.get(cle) * francais.get(cle);
			}
			correspondances[i] = somme;
					
		}
		
		// calcul de l'indice pour lequel la corrélation est maximale
		int indiceMax = 0;
		for (int i = 0 ; i < 26 ; i++) {
			if (correspondances[i] > correspondances[indiceMax]) {
				indiceMax = i;
			}
		}
		
		return indiceMax;
	}
	
	/**
	 * 
	 * @param cle le d�calage
	 * @param mot le mot a chiffre ou a dechiffrer
	 * @param chiffre true si le mot en parametre est chiffre, false sinon
	 */
	public CodeCesar(int cle, String mot, boolean chiffre) {
		this.cle = cle % 26;
		if (!chiffre) {
			original = mot;
			encode = chiffrer(cle, original);
		} else {
			encode = mot;
			original = dechiffrer(cle, encode);
		}
	}
	
	public int getCle() {
		return cle;
	}

	public String getOriginal() {
		return original;
	}

	public String getEncode() {
		return encode;
	}

}
