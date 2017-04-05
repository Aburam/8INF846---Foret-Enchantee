package model.effecteurs;

import model.action.Action;
import model.agent.Agent;
import model.capteurs.Capteur;
import model.environnement.Carte;
import model.environnement.Case;
import model.environnement.Type;
import model.capteurs.Capteur;

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
            default:
            	shoot(action);
            	break;
        }

    }
    
    private void shoot(Action ac){
    	try {
			Case posAgent = carte.getCase(agent.getX(), agent.getY());
			Case posMonstre;
	    	switch (ac){
	    	case TirerH:
	    		posMonstre=carte.getCase(agent.getX(), agent.getY()-1);
	    		if(posMonstre.getType().equals(Type.MONSTRE)){
	    			posMonstre.setType(Type.NEUTRE);
	    		}
	        	break;
	        case TirerB:
	        	posMonstre=carte.getCase(agent.getX(), agent.getY()+1);
	    		if(posMonstre.getType().equals(Type.MONSTRE)){
	    			posMonstre.setType(Type.NEUTRE);
	    		}
	        	break;
	        case TirerG:
	        	posMonstre=carte.getCase(agent.getX()-1, agent.getY());
	    		if(posMonstre.getType().equals(Type.MONSTRE)){
	    			posMonstre.setType(Type.NEUTRE);
	    		}
	        	break;
	        case TirerD:
	        	posMonstre=carte.getCase(agent.getX()+1, agent.getY()-1);
	    		if(posMonstre.getType().equals(Type.MONSTRE)){
	    			posMonstre.setType(Type.NEUTRE);
	    		}
	        	break;
	    	}
	    	
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void bouger(int dx, int dy) throws Exception {
        carte.getCase(agent.getX()+dx,agent.getY()+dy).setAgent(agent);
        carte.getCase(agent.getX(),agent.getY()).setAgent(null);
        agent.bouger(dx,dy);
    }
    
    private void move(){
    	SolveInfo info;
		boolean north=false,south=false,east=false,west=false;
		try {
			info = m_engine.solve("goNorth("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				north=true;
			}
			info = m_engine.solve("goSouth("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				south=true;
			}
			info = m_engine.solve("goEast("+agent.getX()+","+agent.getY()+").");
			if(info.isSuccess()){
				west=true;
			}
			info = m_engine.solve("goWest("+agent.getX()+","+agent.getY()+").");
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
			Case currentPosition;
			Case bestNext;
			try {
				currentPosition = carte.getCase(agent.getX(), agent.getY());
				bestNext=agent.getMeilleurCaseVoisine(carte);
				int distanceX = calculateDistance(currentPosition.getPositionX(), bestNext.getPositionX());
				int distanceY = calculateDistance(currentPosition.getPositionY(), bestNext.getPositionY());
				if(currentPosition.getCapteur().equals(Capteur.ODEUR)){
					if(distanceX != 0 )
					{

						switch(distanceX){
							case 1:
								shoot(Action.TirerD);
								break;
							case -1:
								shoot(Action.TirerG);
								break;
						}
					}
					else if(distanceY !=0)
					{
						switch(distanceY){
							case 1:
								shoot(Action.TirerB);
								break;
							case -1:
								shoot(Action.TirerH);
								break;
						}
					}
				}
				else if(currentPosition.getCapteur().equals(Capteur.VENT)){
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void agir(){
		SolveInfo info,infoWind,infoPoop;
		try {
			infoPoop = m_engine.solve("poop("+agent.getX()+","+agent.getY()+").");
			infoWind = m_engine.solve("wind("+agent.getX()+","+agent.getY()+").");
			if(infoPoop.isSuccess()){
				info = m_engine.solve("shootHim("+agent.getX()+","+agent.getY()+").");
				if(info.isSuccess()){
					Case caseCible = agent.getMeilleurCaseVoisine(carte);
					
				}
				else {
					flee();
				}
			}
			else if (infoWind.isSuccess()){
				info = m_engine.solve("cold("+agent.getX()+","+agent.getY()+").");
				if(info.isSuccess()){
					Case caseCible = agent.getMeilleurCaseVoisine(carte);
					
				}
				else {
					flee();
				}
			}
			else {
				move();
			}
		} catch (MalformedGoalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int calculateDistance(int currentPosition, int nextPosition){
		return nextPosition-currentPosition;
	}
}
