package model.agent;

import model.environnement.Carte;
import model.environnement.Case;

import java.util.ArrayList;
import java.util.List;

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
					if(!casesVisitee.contains(caseVoisine)) {
						casesFrontiere.add(caseVoisine);
					}
				} catch (Exception e) {

				}
			}

		}

	}

}
