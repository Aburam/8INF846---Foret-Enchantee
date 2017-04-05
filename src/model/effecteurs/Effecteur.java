package model.effecteurs;

import model.action.Action;
import model.agent.Agent;
import model.environnement.Carte;

import static model.action.Action.BougerH;

public class Effecteur {
    private Agent agent;
    private Carte carte;

    public Effecteur(Agent agent, Carte carte) {
        this.agent = agent;
        this.carte = carte;
    }

    public void performAction(Action action) throws Exception {
        switch (action) {
            case BougerH:
                this.bouger(-1,0);
                System.out.println("HAUT");
                break;
            case BougerB:
                this.bouger(1,0);
                System.out.println("BAS");
                break;
            case BougerG:
                this.bouger(0,-1);
                System.out.println("GAUCHE");
                break;
            case BougerD:
                this.bouger(0,1);
                System.out.println("DROITE");
                break;
        }

    }

    public void bouger(int dx, int dy) throws Exception {
        carte.getCase(agent.getX()+dx,agent.getY()+dy).setAgent(agent);
        carte.getCase(agent.getX(),agent.getY()).setAgent(null);
        agent.bouger(dx,dy);
		agent.ajouterCaseVisitee(carte, carte.getCase(dx, dy));
    }
}
