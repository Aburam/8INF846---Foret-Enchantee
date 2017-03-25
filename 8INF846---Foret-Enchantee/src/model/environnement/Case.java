package model.environnement;

public class Case {

	private int m_x;
	private int m_y;
	private boolean m_display;

	
	public Case(int x, int y){
		m_x = x;
		m_y = y;
		m_display = false;
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
}
