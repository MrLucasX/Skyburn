package de.skyburn.data;

public enum Text {

	PREFIX("§b§l>> §7"),
	NOPERMISSION("Dafür hast du keine Rechte."),
	NOPLAYER("Dieser Befehl ist nur für Spieler."),
	
	PERFORMSPELL("Zauber wird ausgeführt."),
	
	ERROR("Es ist ein Fehler aufgetretten.");
	
	private String de;
	
	Text(String de){
		
		this.de = de;
		
	}
	
	public String getDE(){
		return de;
	}
	
}
