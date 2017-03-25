package model.environnement;

public class Carte {

	private int m_longueur;
	private Case[][] m_cases;

	
	public Carte(int longueur){
		m_longueur = longueur;
		for(int positionX = 0; positionX<longueur ; positionX++){
			for(int positionY = 0; positionY<longueur; positionY++){
				m_cases[positionY][positionX] = new Case(positionX, positionY);
			}
		}
	}
	
	public Case[][] getCases(){
		return m_cases;
	}
	
	public Case getCase(int positionX, int positionY) throws Exception{
		if(positionX <m_longueur && positionY <m_longueur)
		{
			return m_cases[positionY][positionX];
		}
		else
		{
			throw new Exception("Coordonnées invalides : la case demandée n'existe pas");
		}
	}
}
