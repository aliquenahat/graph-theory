package projet;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;
import java.util.LinkedList;

public class Graphes {
	private int[][] graphe;
	private int nbSommets, nbAretes;
	private int coutTotal;
	private boolean oriente;
	private int seconds;
	private boolean connexe;
	private int[] visited;
	private AreteElim[] tas;
	private int nbNoeuds;
	private int[][] resultat;
	private int[][] tableau;
	private boolean[] visiteCycle;
	private AreteElim arrete;
	private Stack<AreteElim> pile;
	private boolean cycleTrouve;
	private String cheminFichier;

	public Graphes(String cheminFichier) throws FileNotFoundException {
		this.cheminFichier = cheminFichier;
		this.graphe = getGraphe(cheminFichier);
	}

	public int getCoutTotal() {
		return this.coutTotal;
	}

	public float getSeconds() {
		return this.seconds;
	}

	private int[][] getGraphe(String cheminFichier)
			throws FileNotFoundException {
		this.connexe = true;
		Scanner scanner = new Scanner(new File(cheminFichier));
		String tabContent[] = new String[10000000];
		int u = 0;
		System.out.println("Lecture du fichier...");
		// On scanne chaque ligne que l'on stock dans un tableau temporaire.
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			tabContent[u] = line;
			u += 1;
		}
		scanner.close();
		this.oriente = true;
		if (tabContent[0].contains("U")) {
			this.oriente = false;
			// On prend les nombres se situant sur les lignes 1 et 2, que l'on
			// associe respectivement au nombre de sommet et d'arete.
			this.nbSommets = Integer.parseInt(tabContent[1].substring(9));
			System.out.println("Nombre de sommets : " + this.nbSommets);
			this.nbAretes = Integer.parseInt(tabContent[2].substring(9));
			System.out.println("Nombre d'aretes : " + this.nbAretes);

			// On crée deux pointeurs pour pouvoir isoler les nombres du graphe
			String blanc = new String("	");
			int pointeurBlanc;
			int pointeur;
			int graphe[][] = new int[nbAretes][3];

			// Copie du contenu du fichier texte (graphe temporaire) dans un
			// tableau pour pouvoir ensuite les traiter //
			for (int i = 4; i < nbAretes + 4; i++) {
				pointeur = 0;
				pointeurBlanc = 0;
				for (int k = 0; k < 2; k++) {
					pointeurBlanc = tabContent[i].indexOf(blanc, pointeur);
					graphe[i - 4][k] = Integer.parseInt(tabContent[i]
							.substring(pointeur, pointeurBlanc));
					pointeur = pointeurBlanc + 1;
				}
				graphe[i - 4][2] = Integer.parseInt(tabContent[i]
						.substring(pointeur));
			}

			// Fin de la lecture du fichier
			int[] related = new int[nbSommets];
			for (int i = 0; i < nbSommets; i++) {
				related[i] = 0;
			}
			related[0] = 1;
			for (int i = 0; i < nbSommets; i++) {
				for (int j = 0; j < nbAretes; j++) {
					if (related[graphe[j][0]] == 1) {
						related[graphe[j][1]] = 1;
					}
					if (related[graphe[j][1]] == 1) {
						related[graphe[j][0]] = 1;
					}
				}
			}
			for (int i = 0; i < nbSommets; i++) {
				if (related[i] == 0) {
					connexe = false;
				}
			}
			return graphe;
		} else {
			graphe = null;
			return graphe;
		}
	}

	public boolean getOriente() {
		return this.oriente;
	}

	public int getNbSommets() {
		return this.nbSommets;
	}

	public int getNbAretes() {
		return this.nbAretes;
	}

	public boolean getConnexe() {
		return this.connexe;
	}

	/*
	 * 
	 * TRAITEMENT DE KRUSKAL
	 */
	public void kruskal() {
		System.out.println("\nKruskal : ");
		// Lancement du chronomètre
		long tempsDebut = System.currentTimeMillis();

		// TreeSet permet d'avoir des aretes constamment triées selon le cout
		// par ordre croissant
		TreeSet<Arete> aretes = new TreeSet<Arete>();

		// On rajoute toutes les aretes dans la liste et ils sont triés
		// automatiquement
		for (int i = 0; i < nbAretes; i++) {
			aretes.add(new Arete(graphe[i][0], graphe[i][1], graphe[i][2]));
		}
		System.out.println("   Tri par ordre croissant des couts...");
		KruskalAretes arbreFinal = new KruskalAretes();
		// On rajoute les aretes puis la classe Aretes va vérifier si il
		// n'y a pas de cycle pour l'ajouter définitivement dans arbreFinal
		for (Arete arete : aretes) {
			// System.out.println(arete);
			arbreFinal.rajouterArete(arete);
		}

		/* Affichage de l'abre couvrant obtenu */
		System.out
				.println("Voici l'arbre couvrant minimal obtenu avec l'algorithme de Kruskal :\n");
		this.coutTotal = 0;
		for (Arete arete : arbreFinal.getAretes()) {
			System.out.println(arete);
			this.coutTotal += arete.getCout();
		}

		System.out.println("   Cout total minimal : " + this.coutTotal);
		long tempsFin = System.currentTimeMillis();
		seconds = (int)Math.ceil(tempsFin - tempsDebut);
		System.out.println("   Temps de calcul : " + Integer.toString(seconds)
				+ "ms.");
	}

	/*
	 * 
	 * TRAITEMENT DE PRIM
	 */

	public void prim() {
		System.out.println("\nPrim : ");
		long tempsDebut = System.currentTimeMillis();
		this.coutTotal = 0;
		int arbreFinal[][] = new int[this.nbAretes - 1][3];
		int plusCourtChemin[] = new int[3];
		int sommetRetire = 1;
		int[] visited = new int[this.nbSommets];
		for (int i = 0; i < this.nbSommets; i++) {
			visited[i] = 0;
		}

		// On selectionne un premier sommet au hasard, nous prendrons le premier
		// sommet
		visited[0] = 1;
		// On initialise le cout de l'ajout d'une arËte ‡ l'infinie.
		plusCourtChemin[2] = (int) Float.POSITIVE_INFINITY;
		// On boucle n-1 fois pour obtenir l'arbre couvrant minimal avec n-1
		// arrËtes
		for (int t = 0; t < this.nbSommets - 1; t++) {
			// On boucle pour trouver une arrËte que l'on rajoute ‡ notre arbre
			// couvrant minimal
			for (int i = 0; i < this.nbAretes; i++) {
				if (visited[this.graphe[i][0]] == 1) {
					if (visited[this.graphe[i][1]] == 0) {
						if (this.graphe[i][2] < plusCourtChemin[2]) {
							plusCourtChemin[0] = this.graphe[i][0];
							plusCourtChemin[1] = this.graphe[i][1];
							plusCourtChemin[2] = this.graphe[i][2];
						}
					}
				}
			}
			// Avant de relancer une nouvelle recherche on vÈrifie qu'il
			// n'existe pas de coup plus faible, on consiËdre donc la deuxiËme
			// colonne par rapport ‡ la premiËre
			for (int i = 0; i < this.nbAretes; i++) {
				if (visited[this.graphe[i][0]] == 0) {
					if (visited[this.graphe[i][1]] == 1) {
						if (this.graphe[i][2] < plusCourtChemin[2]) {
							plusCourtChemin[0] = this.graphe[i][1];
							plusCourtChemin[1] = this.graphe[i][0];
							plusCourtChemin[2] = this.graphe[i][2];
						}
					}
				}
			}
			// On passe le sommet selectionnÈ comme visitÈ
			visited[plusCourtChemin[1]] = 1;
			this.coutTotal = this.coutTotal + plusCourtChemin[2];
			arbreFinal[sommetRetire - 1][0] = plusCourtChemin[0];
			arbreFinal[sommetRetire - 1][1] = plusCourtChemin[1];
			arbreFinal[sommetRetire - 1][2] = plusCourtChemin[2];
			sommetRetire = sommetRetire + 1;
			plusCourtChemin[2] = (int) Float.POSITIVE_INFINITY;

		}
		/* Affichage de l'abre couvrant obtenu */
		System.out
				.println("Voici l'arbre couvrant minimal obtenu avec l'algorithme de Prim :\n");
		for (int i = 0; i < this.nbSommets - 1; i++) {
			System.out.println(arbreFinal[i][0] + "	" + arbreFinal[i][1] + " "
					+ arbreFinal[i][2]);
		}
		System.out.println("   Cout total minimal : " + this.coutTotal);
		long tempsFin = System.currentTimeMillis();
		seconds = (int)Math.ceil(tempsFin - tempsDebut);
		System.out.println("   Temps de calcul : " + Integer.toString(seconds)
				+ "ms.");
	}

	/*
	 * 
	 * TRAITEMENT AVEC ELIMINATION DE CYCLES
	 */
	public void elimination() {
		LectureFichier fichier = new LectureFichier(this.cheminFichier);
		String buffer = fichier.read();
		String[] buffers = buffer.split("\n");
		String[] tmp;

		System.out.println(buffer);
		nbNoeuds = 0;
		LinkedList<LinkedList<AreteElim>> graphe2 = new LinkedList<LinkedList<AreteElim>>();
		resultat = new int[nbSommets][nbSommets];
		tableau = new int[nbSommets][nbSommets];
		tas = new AreteElim[nbSommets - 1];
		for (int i = 0; i < tas.length; i++) {
			tas[i] = null;
		}
		visited = new int[nbSommets];
		visiteCycle = new boolean[visited.length];
		for (int i = 0; i < nbSommets; i++) {
			visited[i] = 0;
			visiteCycle[i] = false;
		}

		for (int i = 0; i < nbSommets; i++) {
			for (int j = 0; j < nbSommets; j++) {
				graphe2.add(new LinkedList<AreteElim>());
				resultat[i][j] = Integer.MAX_VALUE;
				tableau[i][j] = Integer.MAX_VALUE;
			}

		}

		for (int i = 4; i < nbAretes + 4; i++) {
			tmp = buffers[i].split("\t");
			graphe2.get(Integer.parseInt(tmp[0])).add(
					new AreteElim(new Point(Integer.parseInt(tmp[0]), Integer
							.parseInt(tmp[1])), Integer.parseInt(tmp[2])));
			tableau[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])] = Integer
					.parseInt(tmp[2]);
			tableau[Integer.parseInt(tmp[1])][Integer.parseInt(tmp[0])] = Integer
					.parseInt(tmp[2]);
		}
		System.out.println("\nElimination de cycles : ");
		long tempsDebut = System.currentTimeMillis();
		this.coutTotal = 0;
		elimCycles(nbSommets - 1, nbSommets - 1);
		/* Affichage de l'abre couvrant obtenu */
		System.out
				.println("Voici l'arbre couvrant minimal obtenu avec l'algorithme de l'elimination de cycles :\n");
		for (int i = 0; i < nbSommets; i++) {
			for (int j = 0; j < i; j++) {
				if (resultat[i][j] < Integer.MAX_VALUE) {
					System.out.println(i + " " + j + " " + resultat[i][j]);
					this.coutTotal += resultat[i][j];
				}
			}
		}
		System.out.println("   Cout total minimal : " + this.coutTotal);
		long tempsFin = System.currentTimeMillis();
		this.seconds = (int)Math.ceil(tempsFin - tempsDebut);
		System.out.println("   Temps de calcul : " + Integer.toString(seconds)
				+ "ms.");
	}

	public void elimCycles(int actuel, int precedent) {
		if (visited[actuel] == 0) {

			for (int i = 0; i < actuel; i++) {
				if (tableau[i][actuel] < Integer.MAX_VALUE) {

					// System.out.println("Ajout de " + i + " à " + actuel
					// + " :" + tableau[i][actuel]);

					insert(new AreteElim(new Point(i, actuel),
							tableau[i][actuel]));

					elimCycles(i, actuel);
				}
			}
			visited[actuel] = 1;
		}
	}

	public void getMax() {
		// TODO Auto-generated method stub
		for (int i = 0; i < visiteCycle.length; i++) {
			visiteCycle[i] = false;
		}
		pile = new Stack<AreteElim>();
		// System.out.println("\n\n\n"+arrete.x);
		cycleTrouve = false;
		parcourirCycle(arrete.x, arrete.x);
	}

	public void parcourirCycle(int actuel, int precedent) {
		if (!visiteCycle[actuel]) {
			if (actuel != precedent) {
				pile.push(new AreteElim(new Point(precedent, actuel),
						resultat[actuel][precedent]));
			}
			visiteCycle[actuel] = true;
			if (!cycleTrouve) {
				for (int i = 0; i < nbSommets; i++) {
					if (resultat[actuel][i] < Integer.MAX_VALUE) {

						if (i != precedent && !cycleTrouve) {
							// System.out.println(i+ " "
							// +actuel+" "+resultat[i][actuel]);

							parcourirCycle(i, actuel);
							if (!pile.empty()) {
								pile.pop();
							}
						}
					}
				}
			}
		} else if (actuel != precedent && !cycleTrouve) {
			cycleTrouve = true;
			// System.out.println("actuel: "+actuel+" precedent : "+precedent);
			AreteElim max = new AreteElim(new Point(precedent, actuel),
					resultat[precedent][actuel]);
			AreteElim depart = max;
			AreteElim tmp = pile.pop();
			// System.out.println("MAX : "+max+ "  ");
			if (max.value < tmp.value) {
				max = tmp;
			}
			// TODO:
			while (tmp.x != depart.y) {
				// System.out.println("TMP : "+tmp+ "  ");

				tmp = pile.pop();
				if (tmp.value > max.value) {
					max = tmp;
				}
			}
			if (tmp.y == depart.x) {
				// System.out.println("CYCLE : "+tmp+ "  "+ depart);
			}

			if (tmp.value > max.value) {
				max = tmp;
			}

			// System.out.println("Suppression de "+max.x+" à "+max.y);
			resultat[max.x][max.y] = Integer.MAX_VALUE;
			resultat[max.y][max.x] = Integer.MAX_VALUE;
		}

	}

	public void insertAncien(AreteElim arrete) {
		nbNoeuds++;
		tas[nbNoeuds - 1] = arrete;
		int i = nbNoeuds - 1;
		while (i > 0 && tas[i].value > tas[i / 2].value) {
			swap(i, i / 2);
			i /= 2;
		}
	}

	public boolean Contains(AreteElim p) {
		for (int i = 1; i < nbNoeuds; i++) {
			if (p.equals(tas[i])) {
				return true;
			}
		}
		return false;
	}

	public AreteElim getMaxAncien() {
		AreteElim retour = tas[0];
		deleteAncien();
		System.out.println(retour.value + " a été supprimé s1:" + retour.x
				+ " s2:" + retour.y);
		return retour;
	}

	public void deplaceHaut(int i) {

		AreteElim tmp = tas[i];
		while (i / 2 > 0 && tmp.value >= tas[i / 2].value) {
			tas[i] = tas[i / 2];
			i /= 2;
		}
		tas[i] = tmp;

	}

	public void deleteAncien() {
		tas[0] = tas[nbNoeuds - 1];
		nbNoeuds--;
		AreteElim tmp = tas[0];
		int i = 0, j = 0;
		while ((2 * i) + 1 < nbNoeuds) {
			j = (2 * i) + 1;
			if (j + 1 < nbNoeuds)
				if (tas[j + 1].value > tas[j].value)
					j++;
			if (tmp.value >= tas[j].value)
				break;
			swap(i, j);
			i = j;
		}
		tas[i] = tmp;
	}

	public void swap(int i, int j) {
		AreteElim tmp = tas[i];
		tas[i] = tas[j];
		tas[j] = tmp;

	}

	public void insert(AreteElim arrete) {
		// TODO:
		resultat[arrete.x][arrete.y] = arrete.value;
		resultat[arrete.y][arrete.x] = arrete.value;
		this.arrete = arrete;
		getMax();
	}

}
