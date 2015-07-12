package projet;

import java.util.TreeSet;
import java.util.Vector;
import java.util.HashSet;

class Arete implements Comparable<Arete> {
	int sommetDepart;
	int sommetArrive;
	int cout;

	public Arete(int sommetDepart, int sommetArrive, int cout) {
		this.sommetDepart = sommetDepart;
		this.sommetArrive = sommetArrive;
		this.cout = cout;
	}

	public int SommetDepart() {
		return this.sommetDepart;
	}

	public int SommetArrive() {
		return this.sommetArrive;
	}

	public int getCout() {
		return this.cout;
	}

	public String toString() {
		return this.sommetDepart + " " + this.sommetArrive + " " + this.cout;
	}

	public int compareTo(Arete arete) {
		return (this.cout < arete.cout) ? -1 : 1;
	}
}

class KruskalAretes {
	Vector<HashSet<Integer>> sommetGroupes = new Vector<HashSet<Integer>>();
	// TreeSet permet d'avoir des aretes constamment triées selon le cout par
	// ordre croissant
	TreeSet<Arete> kruskalAretes = new TreeSet<Arete>();

	public TreeSet<Arete> getAretes() {
		return kruskalAretes;
	}

	// Pour savoir si un sommet est contenu dans un groupe
	HashSet<Integer> getSommetGroupe(int sommetDepart) {
		for (HashSet<Integer> sommetGroupe : sommetGroupes) {
			if (sommetGroupe.contains(sommetDepart)) {
				return sommetGroupe;
			}
		}
		return null;
	}

	/*
	 * On regarde d'abord si un sommet a l'extremite de l'arete est contenu dans
	 * une liste 
	 * 1 - Si aucun des deux ne sont présents dans un groupe alors on
	 * crée un groupe contenant les deux sommets 
	 * 2 - Si l'un des sommets à
	 * l'extremite de l'arete existe dans un groupe et pas l'autre alors on
	 * ajoute le sommmet qui n'existe pas dans le groupe qui contient l'autre
	 * sommet 
	 * 3 - Si les deux sommets existent dans des différents groupes alors
	 * on fusionne les deux groupes en un
	 */
	public void rajouterArete(Arete arete) {
		int sommetDepart = arete.SommetDepart();
		int sommetArrive = arete.SommetArrive();
		HashSet<Integer> sommetGroupeDepart = getSommetGroupe(sommetDepart);
		HashSet<Integer> sommetGroupeArrive = getSommetGroupe(sommetArrive);

		if (sommetGroupeDepart == null) {
			if (sommetGroupeArrive == null) {
				// 1
				// On fusionne les deux sommets dans un groupe si aucun des
				// sommets ne sont presents dans les groupe
				HashSet<Integer> NouveauGroupeSommet = new HashSet<Integer>();
				NouveauGroupeSommet.add(sommetDepart);
				NouveauGroupeSommet.add(sommetArrive);
				sommetGroupes.add(NouveauGroupeSommet);
				kruskalAretes.add(arete);
			} else {
				// 2
				// On ajoute le sommet de depart qui n'existe pas dans le groupe
				// des sommets d'arrive
				sommetGroupeArrive.add(sommetDepart);
				kruskalAretes.add(arete);
			}
		} else {
			if (sommetGroupeArrive == null) {
				// 2
				// On ajoute le sommet d'arrive qui n'existe pas dans le groupe
				// des sommets dde depart
				sommetGroupeDepart.add(sommetArrive);
				kruskalAretes.add(arete);
			} else if (sommetGroupeDepart != sommetGroupeArrive) {
				// on fusionne les deux groupes
				sommetGroupeDepart.addAll(sommetGroupeArrive);
				sommetGroupes.remove(sommetGroupeArrive);
				kruskalAretes.add(arete);
			}
		}
	}
}