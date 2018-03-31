package de.skyburn.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.skyburn.main.Main;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.WorldServer;

public class Heiltotem implements Spell{

	private Player player;
	private int time_ms;
	private double radius;
	private double healpersecond;
	
	public Heiltotem(Player player, int time_ms, double radius, double healpersecond){
		setPlayer(player);
		setTime_ms(time_ms);
		setRadius(radius);
		setHealpersecond(healpersecond);
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

	public double getHealpersecond() {
		return healpersecond;
	}

	public void setHealpersecond(double healpersecond) {
		this.healpersecond = healpersecond;
	}
	
	private long startTime = 0;
	private boolean finished = false;
	private long lastHeal = 0;
	private Location loc;
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		lastHeal = System.currentTimeMillis();
		loc = getExecuter().getLocation().clone();
		
		spawnTotem();
		
	}

	@Override
	public void stop() {
		despawnTotem();
	}

	@Override
	public void tick() {
		
		if(System.currentTimeMillis() > startTime + getTime_ms()){
			finished = true;
			return;
		}
		
		if(lastHeal + 1000 < System.currentTimeMillis()){
		
			lastHeal = System.currentTimeMillis();
			
			Bukkit.getOnlinePlayers().stream().
			filter(e -> e.getLocation().getWorld() == loc.getWorld()).
			filter(e -> e.getLocation().distance(loc) < getRadius()).
			forEach(e -> heal(e));
			
			sendParticle(loc);
			
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
	
	private EntityArmorStand as;
	
	private void spawnTotem(){
		
		WorldServer s =  ( (CraftWorld) getExecuter().getWorld() ).getHandle();
		as = new EntityArmorStand(s);
		
		as.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
		as.setCustomName( "§a§lHeil Totem" );
		as.setCustomNameVisible(true);
		
		ItemStack is = new ItemStack(Material.SKULL, 1, (short) 1);
		as.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(is));
		as.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(is));
		
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving( as );
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			public void run() {
				Bukkit.getOnlinePlayers().stream().
				filter(e -> e.getLocation().getWorld() == loc.getWorld()).
				filter(e -> e.getLocation().distance(loc) < 64).
				forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet) );
			}
		});
		
	}
	
	private void despawnTotem(){
		
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(  as.getId() );
		
		Bukkit.getOnlinePlayers().stream().forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet) );
		
	}
	
	@SuppressWarnings("deprecation")
	private void heal(Player p){
		if(p.getHealth() + getHealpersecond() > p.getMaxHealth()){
			p.setHealth(p.getMaxHealth());
		}else{
			p.setHealth(p.getHealth() + getHealpersecond());
			sendParticle(p.getLocation());
		}
	}
	
	private void sendParticle(Location loc){
		
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 1.01F, 2.01F, 1.01F, 1, 10, 0);	
		Bukkit.getOnlinePlayers().stream().filter(e -> e.getLocation().getWorld() == loc.getWorld()).filter(e -> e.getLocation().distance(loc) < 64).forEach(e -> ((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet) );
		
	}

}
