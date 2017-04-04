package model.agent;

import model.capteurs.Capteur;
import model.environnement.Carte;
import model.environnement.Case;

import java.util.*;

public class Agent{

	private int x;
	private int y;

	private List<Case> casesVisitee;
	private List<Case> casesFrontiere;

	public Agent(int x, int y) {
		this.x = x;
		this.y = y;
		casesVisitee = new ArrayList<>();
		casesFrontiere = new ArrayList<>();
	}

	public String toString()
	{
		return "A";
	}

	public int getX(){return this.x;};
	public int getY(){return this.y;};

	public void bouger(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	//Ajout d'une nouvelle case visitee et update des cases voisines
	public void ajouterCaseVisitee(Carte carte, Case caseVisitee) {
		if(!casesVisitee.contains(caseVisitee)) {
			casesVisitee.add(caseVisitee);

			if(casesFrontiere.contains(caseVisitee))
				casesFrontiere.remove(caseVisitee);

			int [] dx = {1,0,0,-1};
			int [] dy = {0,-1,1,0};

			// Ajout des nouvelles cases frontieres
			for(int i=0; i<4; i++) {
				try {
					Case caseVoisine = carte.getCase(this.x+dx[i], this.y+dy[i]);
					if(!casesFrontiere.contains(caseVoisine) && !casesVisitee.contains(caseVoisine)) {
						casesFrontiere.add(caseVoisine);
					}
				} catch (Exception e) {

				}
			}
		}
	}

	public void calculerProbaFrontiere() {

		Capteur[] capteurs = {Capteur.VENT, Capteur.ODEUR};
		//FIXME: faire le calcul des probas sur toute la frontiere (pas seulement pour la premiere case)
		int index = 0;

		List<Case> frontiereDanger = new ArrayList<>();
		List<Case> sousFrontiereDanger = new ArrayList<>();
		//List<Integer> solution = new ArrayList<>();
		//List<Integer> realSolution = new ArrayList<>(); // realSolution est comme solution sauf qu'on a toujours une valeur "1" pour la case qu'on évalue


		//TODO calculer aussi la proba pour les odeurs

		HashMap<Case, Double> probas = new HashMap<>();

		for(int k=0; k<2; k++) {
			int[] solution = calculerFrontiereDanger(frontiereDanger, sousFrontiereDanger, capteurs[k]);

			for(int i=0; i<frontiereDanger.size(); i++) {

				initializeSolution(solution);

				Case currentCheckingCase = null;
				//On enleve la case sur laquelle on calcule la probabilité (elle sera automatiquement considérée comme une crevasse ou un monstre)
				if(solution != null) {
					currentCheckingCase = frontiereDanger.get(index);
					frontiereDanger.remove(index);
				}



				double proba = 0;

				while(solution != null && solution[0]!=-1) {

					if(checkIsSolutionIsFeasible(solution, frontiereDanger, sousFrontiereDanger, currentCheckingCase)) {
						proba += evaluerSolution(solution);
					}
					nextSolution(solution);
				}
				//On multiplie par la probabilité qu'il y ait un monstre ou crevasse sur la case courante
				proba*=0.1;

				if(!probas.containsKey(currentCheckingCase)) {
					probas.put(currentCheckingCase, proba);
				} else {
					probas.put(currentCheckingCase, probas.get(currentCheckingCase) + proba);
				}

/*				if(frontiereDanger.size() > 0) {
					System.out.println("Proba de la case (" + frontiereDanger.get(index).getPositionX() +", "  +  frontiereDanger.get(index).getPositionY() + ") = " + proba);
				}*/

				frontiereDanger.add(currentCheckingCase);
			}
		}

		System.out.println("Probas");
		printMap(probas);


	}


	public static void printMap(Map<Case, Double> mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Case, Double> pair = (Map.Entry)it.next();
			System.out.println("Proba ["+ pair.getKey()+"] (" + pair.getKey().getPositionX() +", " +  pair.getKey().getPositionY() + ") = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public void initializeSolution(int[] soluion) {
		for(int i=0; i<soluion.length; i++) {
			soluion[i] = 1;
		}
	}

	// Aller à la solution suivante (la premiere solution est "111...111" et la derniere apres toutes les itérations est "000...0000"
	public void nextSolution(int[] solution) {
		int total = 0;
		for (Integer i : solution) { // assuming list is of type List<Integer>
			total = 10*total + i;
		}

		if(total == 0) {
			solution[0] = -1;
		} else {
			int base10 = Integer.parseInt(Integer.toString(total), 2);
			base10-=1;

			String newSolution = Integer.toString(base10, 2);

			int diff = solution.length - newSolution.length();
			for(int i=0; i<diff; i++) {
				solution[i] = 0;
			}

			for(int i=0; i < newSolution.length(); i++) {
				if(newSolution.charAt(i) == '0') {
					solution[i+diff] = 0;
				} else {
					solution[i+diff] = 1;
				}
			}
		}
	}

	//Cette fonction calcul la frontiere ou il y a potentiellement un danger "Monstre" ou "Crevasse" ainsi que la "sous-frontiere" associée (les cases touchant la frontière mais déja visistées)
	//Elle initialise aussi le vecteur "solution"
	public int[] calculerFrontiereDanger(List<Case> frontiereDanger, List<Case> sousFrontiereDanger, Capteur typeDeDanger) {

		for(Case caseFrontiere : this.casesFrontiere) {
			boolean toAdd = false;
			for(Case caseVisitee : this.casesVisitee) {
				Capteur capteur = caseVisitee.getCapteur();
				if(capteur == typeDeDanger && caseVisitee.isVoisine(caseFrontiere)) {
					toAdd = true;
					if(!sousFrontiereDanger.contains(caseVisitee)) {
						sousFrontiereDanger.add(caseVisitee);
					}
				}
			}
			if(toAdd) {
				frontiereDanger.add(caseFrontiere);
			}
		}

		if(frontiereDanger.size() == 0) {
			return null;
		} else {
			int[] solution = new int[frontiereDanger.size()-1];
			for(int i=0; i<solution.length; i++) {
				solution[i] = 1;
			}
			return solution;
		}

	}

	//currentCheckingCase est la case où l'on test la probabilité qu'il y ait une crevasse ou un monstre
	public boolean checkIsSolutionIsFeasible(int[] solution, List<Case> frontiereDanger, List<Case> sousFrontiereDanger, Case currentCheckingCase) {

		boolean isSolutionFeasible = true;

		// Checker si toutes les cases de la sous frontière sont à coté d'au moins un "monstre" ou "crevasse"
		for(Case caseSousFrontiere : sousFrontiereDanger) {

			boolean isFeasible = false;

			for(int i=0; i<solution.length; i++) {

				if(solution[i] == 1) {
					Case caseFrontiere = frontiereDanger.get(i);
					if(caseSousFrontiere.isVoisine(caseFrontiere)) {
						isFeasible = true;
						break;
					}
				}
			}

			if(!isFeasible) {
				if(!caseSousFrontiere.isVoisine(currentCheckingCase)){
					isSolutionFeasible = false;
					break;
				}
			}
		}
		return isSolutionFeasible;
	}

	// 0.9 si il n'y a rien et 0.1 si il y a un monstre ou crevasse
	public double evaluerSolution(int[] solution) {
		double proba = 1;
		for(int i = 0; i<solution.length; i++) {
			if(solution[i] == 1) {
				proba*=0.9;
			} else {
				proba*=0.1;
			}
		}
		return proba;
	}

}
