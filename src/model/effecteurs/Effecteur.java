package model.effecteurs;

import model.action.Action;
import model.agent.Agent;
import model.environnement.Carte;
import model.environnement.Case;

import static model.action.Action.BougerH;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;

public class Effecteur {
    private Agent agent;
    private Carte carte;

	private Prolog m_engine;

    public Effecteur(Agent agent, Carte carte, Prolog engine) {
        this.agent = agent;
        this.carte = carte;
        m_engine=engine;
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
    
    private void flee(){
		SolveInfo info;
		boolean north=false,south=false,east=false,west=false;
		try {
			info = m_engine.solve("fleeNorth("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				north=true;
			}
			info = m_engine.solve("fleeSouth("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				south=true;
			}
			info = m_engine.solve("fleeEast("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				west=true;
			}
			info = m_engine.solve("fleeWest("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				east=true;
			}
		} catch (MalformedGoalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(north){
			try {
				performAction(Action.BougerH);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(south){
			try {
				performAction(Action.BougerB);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (east){
			try {
				performAction(Action.BougerD);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (west){
			try {
				performAction(Action.BougerG);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			
		}
	}

	public void agir(){
		SolveInfo info;
		try {
			info = m_engine.solve("poop("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				info = m_engine.solve("shootHim("+agent.getX()+","+agent.getY()+").");
				if(info.isSuccess()){
					Case caseCible = agent.getMeilleurCaseVoisine(carte);
				}
			}
		} catch (MalformedGoalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
