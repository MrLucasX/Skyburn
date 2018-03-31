package de.skyburn.data;

public enum Text {

	PREFIX("�b�l>> �7"),
	NOPERMISSION("Daf�r hast du keine Rechte."),
	NOPLAYER("Dieser Befehl ist nur f�r Spieler."),
	
	PERFORMSPELL("Zauber wird ausgef�hrt."),
	
	ERROR("Es ist ein Fehler aufgetretten.");
	
	private String de;
	
	Text(String de){
		
		this.de = de;
		
	}
	
	public String getDE(){
		return de;
	}
	
}
