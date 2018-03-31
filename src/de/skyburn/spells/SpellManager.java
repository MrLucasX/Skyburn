package de.skyburn.spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import de.skyburn.main.Main;

public class SpellManager {

	private List<Spell> spells = new ArrayList<Spell>();
	private BukkitTask thread;
	private boolean run;
	
	public void addSpell(Spell spell){
		
		spells.add(spell);
		spell.start();
		
	}
	
	public void load() {
		
		run = true;
		
		thread = Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				while(run){
					List<Spell> remove = new ArrayList<Spell>();
					
					for(Spell spell : spells){
						
						spell.tick();
						
						if(spell.isFinished()){
							spell.stop();
							remove.add(spell);
						}
						
					}
					
					for(Spell spell : remove){
						spells.remove(spell);
					}
					
					try {
						Thread.sleep(25); // 40 Ticks pro Sekunde
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
	}

	public void unload() {
		
		run = false;
		thread.cancel();
		
	}

}
