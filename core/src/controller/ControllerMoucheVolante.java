package controller;

import model.MoucheVolante;

public class ControllerMoucheVolante {
	MoucheVolante mouche;
	
	public ControllerMoucheVolante(MoucheVolante mouche) {
		this.mouche = mouche;
	}
	
	public void majMouche(float delta) {
		mouche.deplacer(delta);
	}

	public MoucheVolante getMouche() {
		return mouche;
	}

	public void setMouche(MoucheVolante mouche) {
		this.mouche = mouche;
	}
	
}
