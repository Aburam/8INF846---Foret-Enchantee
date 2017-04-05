package main;

import model.action.Action;
import model.agent.Agent;
import model.capteurs.Capteur;
import model.effecteurs.Effecteur;
import model.environnement.*;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

	public static void main (String[] args) throws FileNotFoundException, IOException {
		Prolog engine = new Prolog();
		try {
			Theory theory = new Theory(new FileInputStream("src/model/Prolog/db.pl"));
			engine.setTheory(theory);
			
		} catch (InvalidTheoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Sorry not here "+e1.line);
			System.exit(0);
		} finally {
			Agent agent = new Agent(0,0);
			Carte carte = new Carte(6, agent,engine);
			Effecteur effecteur = new Effecteur(agent, carte);
			
			Random generator = new Random();

			try {
				Case currentCase = carte.getCase(agent.getX(), agent.getY());
				// Sauvegarde de la case visitee
				agent.ajouterCaseVisitee(carte, currentCase);
			} catch (Exception e) {
				e.printStackTrace();
			}


			boolean isFinish = false;

				while(!isFinish){

					try {

						//agent.calculerProbaFrontiere();
						Case meilleurCaseVoisine = agent.getMeilleurCaseVoisine(carte);


						// Trouver le prochain mouvement
						//FIXME: Ici le mouvement est aléatoire
						int rand = generator.nextInt(4);
						switch (rand) {
							case 0:
								effecteur.performAction(Action.BougerB);
								break;
							case 1:
								effecteur.performAction(Action.BougerD);
								break;
							case 2:
								effecteur.performAction(Action.BougerG);
								break;
							case 3:
								effecteur.performAction(Action.BougerH);
								break;
						}



						Case currentCase = carte.getCase(agent.getX(), agent.getY());

						// Sauvegarde de la case visitee
						agent.ajouterCaseVisitee(carte, currentCase);

						Capteur currentCapteur = currentCase.getCapteur();
						Type currentType = currentCase.getType();
						if(currentType == Type.CREVASSE || currentType == Type.MONSTRE || currentCapteur == Capteur.LUMIERE) {
							isFinish = true;
						}
						System.out.println(currentCase);
						System.out.println(carte);
					} catch (Exception e) {
					}
				}




			try {
				Case currentCase = carte.getCase(agent.getX(), agent.getY());
				Type currentType = currentCase.getType();
				if(currentType == Type.CREVASSE || currentType == Type.MONSTRE) {
					System.out.println("YOU LOSE");
				} else {
					System.out.println("YOU WIN");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		}




}
