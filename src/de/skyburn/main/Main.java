package de.skyburn.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.skyburn.cmd.Command_Spell;
import de.skyburn.spells.SpellManager;

public class Main extends JavaPlugin{

	private static Main instance;
	
	public static Main getInstance(){
		return instance;
	}
	
	private SpellManager sm = new SpellManager();
	
	public SpellManager getSpellManager(){
		return sm;
	}
	
	
	@Override
	public void onEnable(){
		
		instance = this;
		
		getCommand("spell").setExecutor(new Command_Spell());
		
		sm.load();
		
	}
	
	@Override
	public void onDisable(){
		
		sm.unload();
		
	}
	
}
