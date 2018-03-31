package de.skyburn.spells;

import org.bukkit.entity.Player;

public interface Spell {
	
	public abstract void start();
	public abstract void stop();
	public abstract void tick();
	public abstract boolean isFinished();
	public abstract Player getExecuter();
	
}
