package model.environnement;

import model.agent.Agent;
import model.capteurs.Capteur;

public class Case {

	private int m_x;
	private int m_y;
	private boolean m_display;
	private Capteur m_capteur;
	private Type m_type;
	private Agent m_agent;

	
	public Case(int x, int y){
		m_x = x;
		m_y = y;
		m_display = false;
		m_type = Type.NEUTRE;
	}
	
	public int getPositionX(){
		return m_x;
	}
	
	public int getPositionY(){
		return m_y;
	}
	
	public boolean isDisplayed(){
		return m_display;
	}
	
	public void display(){
		m_display = true;
	}
	
	public void hide(){
		m_display = false;
	}
	
	public void addCapteur(Capteur capteur){
		m_capteur = capteur;
	}

	public void setType(Type type){
		m_type = type;
	}
	
	public Type getType(){
		return m_type;
	}
	
	public Capteur getCapteur(){
		return m_capteur;
	}
	
	public void setAgent(Agent agent){
		m_agent = agent;
	}
	public Agent getAgent(){
		return m_agent;
	}
	
	public String toString(){
		String ret ="";
		switch(m_type){
		case NEUTRE:
			ret = (m_capteur != null)? m_capteur.toString() : (m_agent!=null)? m_agent.toString() : " ";
			break;
		case MONSTRE:
			ret = "M";
			break;
		case CREVASSE:
			ret = "C";
		}
		return ret;
	}
}
