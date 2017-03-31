package model.capteurs;

public enum Capteur {
	VENT("V"),
	LUMIERE("L"),
	ODEUR("O");
	
	private String m_print = "";
	   
	//Constructeur
	Capteur(String print){
		this.m_print = print;
	}
	
	public String toString(){
		return m_print;
	}
}

