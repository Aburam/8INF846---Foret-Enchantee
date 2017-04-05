package model.environnement;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import model.agent.Agent;
import model.capteurs.Capteur;

public class Carte {

	private int m_longueur;
	private Case[][] m_cases;
	private Agent m_agent;
	private Prolog m_engine;

	
	public Carte(int longueur, Agent agent, Prolog engine){
		m_longueur = longueur;
		m_agent = agent;
		m_cases = new Case[m_longueur][m_longueur];
		m_engine = engine;
		init();
		m_cases[0][0].setAgent(m_agent);
		m_cases[0][0].setType(Type.NEUTRE);
		addCapteurs();
		
		
	}
	
	public void init(){
		int positionLumiereX = 0;
		int positionLumiereY = 0;

		for(int positionX = 0; positionX<m_longueur ; ++positionX){
			for(int positionY = 0; positionY<m_longueur; ++positionY){
				m_cases[positionY][positionX] = new Case(positionX, positionY);
				try {
					m_engine.solve("setWalkable("+positionX+","+positionY+").");
				} catch (MalformedGoalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				if(calculProbabiliteCrevasse() ){
					m_cases[positionY][positionX].setType(Type.CREVASSE);
					
				}
				else if(calculProbabiliteMonstre()){
					m_cases[positionY][positionX].setType(Type.MONSTRE);

				}
			}
			
		}
		while(positionLumiereX == 0 && positionLumiereY ==0)
		{
			positionLumiereX = (int) (Math.random() * m_longueur);
			positionLumiereY = (int) (Math.random() * m_longueur);
		}
		m_cases[positionLumiereY][positionLumiereX] = new Case(positionLumiereX, positionLumiereY);
		m_cases[positionLumiereY][positionLumiereX].addCapteur(Capteur.LUMIERE);
		try {
			m_engine.solve("setLight("+positionLumiereX+","+positionLumiereY+").");
		} catch (MalformedGoalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	
	public void addCapteurs(){
		for(int x = 0; x < m_longueur ; x++){
			for(int y = 0 ; y < m_longueur; y++){
				if(m_cases[y][x].getCapteur()!=Capteur.LUMIERE)
				{
					if(y<m_longueur-1){
						switch(m_cases[y+1][x].getType()){
							case CREVASSE:
								m_cases[y][x].addCapteur(Capteur.VENT);
								try {
									m_engine.solve("setWind("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case MONSTRE:
								m_cases[y][x].addCapteur(Capteur.ODEUR);
								try {
									m_engine.solve("setPoop("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
					if(y>0){
						switch(m_cases[y-1][x].getType()){
							case CREVASSE:
								m_cases[y][x].addCapteur(Capteur.VENT);
								try {
									m_engine.solve("setWind("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case MONSTRE:
								m_cases[y][x].addCapteur(Capteur.ODEUR);
								try {
									m_engine.solve("setPoop("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
					if(x<m_longueur-1){
						switch(m_cases[y][x+1].getType()){
							case CREVASSE:
								m_cases[y][x].addCapteur(Capteur.VENT);
								try {
									m_engine.solve("setWind("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case MONSTRE:
								m_cases[y][x].addCapteur(Capteur.ODEUR);
								try {
									m_engine.solve("setPoop("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
					if(x>0){
						switch(m_cases[y][x-1].getType()){
							case CREVASSE:
								m_cases[y][x].addCapteur(Capteur.VENT);
								break;
							case MONSTRE:
								m_cases[y][x].addCapteur(Capteur.ODEUR);
								try {
									m_engine.solve("setPoop("+x+","+y+").");
								} catch (MalformedGoalException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
				}
			}
		}
	}
	
	public Case[][] getCases(){
		return m_cases;
	}
	
	public Case getCase(int positionX, int positionY) throws Exception{
		if(positionX <m_longueur && positionY <m_longueur)
		{
			return m_cases[positionX][positionY];
		}
		else
		{
			throw new Exception("Coordonnées invalides : la case demandée n'existe pas");
		}
	}
	
	public boolean calculProbabiliteCrevasse(){
		return (Math.random()<0.1) ? true : false;
	}
	
	public boolean calculProbabiliteMonstre(){
		return (Math.random()<0.1) ? true : false;
	}
	
	public String toString(){
		String ret ="CARTE : \n";
		for(int cpt=0; cpt< m_cases.length; cpt++)
		{
			ret += "|";
			for(Case currentCase : m_cases[cpt] )
			{
				ret += currentCase + "|";
			}
			ret += "\n";
		}
		return ret;
	}
	
	
}
