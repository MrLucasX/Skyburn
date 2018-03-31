package de.skyburn.spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import de.skyburn.main.Main;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public class Meteorregen implements Spell, Listener{

	private Player player;
	private int time_ms;
	private double radius;
	private double damage;
	private double hitspersecond;
	private boolean flammable;
	
	public Meteorregen(Player player, int time_ms, double radius, double damage, double hitspersecond, boolean flammable){
		setPlayer(player);
		setTime_ms(time_ms);
		setRadius(radius);
		setDamage(damage);
		setHitspersecond(hitspersecond);
		setFlammable(flammable);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getTime_ms() {
		return time_ms;
	}

	public void setTime_ms(int time_ms) {
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

	public double getHitspersecond() {
		return hitspersecond;
	}

	public void setHitspersecond(double hitspersecond) {
		this.hitspersecond = hitspersecond;
	}

	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}
	
	
	private boolean finished = false;
	private long startTime = 0;
	private long lastHit = 0;
	private Listener listener;
	private List<Fireball> fireballs = new ArrayList<Fireball>();
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
		
		this.listener = new Listener(){
			
            @EventHandler
            public void onHit(EntityDamageByEntityEvent e){
            	
            	if(fireballs.contains(e.getDamager())){
            		
            		if(e.getEntity() != null){
            			
            			e.setDamage(getDamage());
            			
            			if(isFlammable()){
            				e.getEntity().setFireTicks(20);
            			}
            			
            		}
            		
            	}
            	
            }
			
		};
		
		Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
		
	}

	@Override
	public void stop() {
		
		HandlerList.unregisterAll(listener);
		
	}

	@Override
	public void tick() {
		
		if(System.currentTimeMillis() > startTime + getTime_ms()){
			finished = true;
			return;
		}
		
		List<Player> spieler = new ArrayList<>();
		Bukkit.getOnlinePlayers().stream().filter(e -> e.getLocation().getWorld() == getExecuter().getWorld()).filter(e -> e.getLocation().distance(getExecuter().getLocation()) < getRadius()).forEach(e -> spieler.add(e));
		
		if(lastHit + 1000 / getHitspersecond() < System.currentTimeMillis()){
			
			lastHit = System.currentTimeMillis();
			
			for(Player pe : spieler){
				
				if(pe == getExecuter())continue;
				
				Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						Location loc = pe.getLocation().clone().add(3 * Math.sin(System.currentTimeMillis()), 8, 3 * Math.cos(System.currentTimeMillis()));
						
						Fireball f = (Fireball) pe.getWorld().spawnEntity(loc, EntityType.FIREBALL);
						
						f.setShooter(getExecuter());
						f.setIsIncendiary(false);
						f.setDirection( pe.getLocation().toVector().subtract(loc.toVector()) );
						
						fireballs.add(f);
						
					}
				});
				
			}
			
		}
		
		for(Fireball f : fireballs){
			sendParticle(f.getLocation());
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
		
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 2.01F, 2.01F, 2.01F, 0, 10, 0);	
		Bukkit.getOnlinePlayers().stream().filter(e -> e.getLocation().getWorld() == loc.getWorld()).filter(e -> e.getLocation().distance(loc) < 64).forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet) );
		
	}

}
