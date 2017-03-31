package main;

import model.agent.Agent;
import model.environnement.Carte;

public class Main {

	public static void main(String[] args) {
		Agent agent = new Agent();
		Carte carte = new Carte(3, agent);
		System.out.println(carte);
	}

}
