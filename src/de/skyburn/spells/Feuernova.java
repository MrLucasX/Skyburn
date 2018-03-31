package de.skyburn.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public class Feuernova implements Spell{

	private Player player;
	private int time_ms;
	private double radius;
	private double damage;
	private boolean flammable;
	private boolean push;
	
	public Feuernova(Player player, int zeit_ms, double radius, double damage, boolean flammable, boolean push){
		setPlayer(player);
		setTimeMS(zeit_ms);
		setRadius(radius);
		setDamage(damage);
		setFlammable(flammable);
		setPush(push);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getTimeMS() {
		return time_ms;
	}

	public void setTimeMS(int time_ms) {
		this.time_ms = time_ms;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}
	
	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}
	
	private long startTime = 0;
	private boolean finished = false;
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		
		if(System.currentTimeMillis() > startTime + getTimeMS()){
			finished = true;
			return;
		}
		
		double faktor = ( (double) System.currentTimeMillis() - (double) startTime ) / (double) getTimeMS();
		double radius = getRadius() * faktor;
		double anzahl = 1 / (radius * 3); // 3 Partikel pro Blockeinheit
		
		System.out.println("Anzahl: " + anzahl);
		
		if(anzahl < 0.024){
			anzahl = 0.024;
		}
		
		for(double c = 0; c < Math.PI * 2; c += anzahl){
			double x = Math.cos(c);
			double z = Math.sin(c);
			Location l = player.getLocation().clone();		
			l.add(x * radius, 0.2, z * radius);
			sendParticle(l);
				
			
			
			for(Entity en : l.getWorld().getNearbyEntities(l, 0.1, 0.1, 0.1)){
					
				if(en instanceof LivingEntity && en != getExecuter()){
						
					LivingEntity d = (LivingEntity) en;
					d.damage(getDamage(), getExecuter());
						
					if(isFlammable()){
						d.setFireTicks(20);	
					}
						
					if(isPush()){
						d.setVelocity( d.getLocation().toVector().subtract(getExecuter().getLocation().toVector()).multiply(0.1) );	
					}
						
				}
					
			}
			
			
			
		}
		
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public Player getExecuter() {
		return player;
	}
	
	private void sendParticle(Location loc){
		
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.01F, 0.01F, 0.01F, 1, 0, 0);	
		Bukkit.getOnlinePlayers().stream().filter(e -> e.getLocation().getWorld() == loc.getWorld()).filter(e -> e.getLocation().distance(loc) < 64).forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet) );
		
	}


}
