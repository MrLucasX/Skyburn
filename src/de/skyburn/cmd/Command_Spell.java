package de.skyburn.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.skyburn.data.Text;
import de.skyburn.main.Main;
import de.skyburn.spells.Feuernova;
import de.skyburn.spells.Heiltotem;
import de.skyburn.spells.Meteorregen;

public class Command_Spell implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			
			sender.sendMessage(Text.NOPLAYER.getDE());
			return true;
			
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("skyburn.cmd.spell")){
			
			p.sendMessage(Text.PREFIX.getDE() + Text.NOPERMISSION.getDE());
			return true;
			
		}
		
		if(args.length <= 0){
			
			p.sendMessage("§7/spell 1 §b (Meteorregen)");
			p.sendMessage("§7/spell 2 §b (Feuernova)");
			p.sendMessage("§7/spell 3 §b (Heilen)");
			return true;
			
		}
		
		if(args[0].equalsIgnoreCase("1")){
			
			// Meteorregen (Radius = 5B, Alle Spieler, Zeit = 5S, Entity = Fireball)
			p.sendMessage(Text.PREFIX.getDE() + Text.PERFORMSPELL.getDE());
			
			Main.getInstance().getSpellManager().addSpell(new Meteorregen(p, 5000, 5, 1, 1, true));
			
			return true;
			
		}
		
		if(args[0].equalsIgnoreCase("2")){
					
			// Feuernova (Radius = 10 Blöcke, Zeit variable, Ausbreitender Feuerkreis)	
			p.sendMessage(Text.PREFIX.getDE() + Text.PERFORMSPELL.getDE());
					
			Main.getInstance().getSpellManager().addSpell(new Feuernova(p, 1000, 10, 0.5, true, true));
					
			return true;
					
		}
		
		if(args[0].equalsIgnoreCase("3")){
			
			// Heilen (Totem spawnen, Zeit = 30S, Radius = 20B, 1/2Herzen / sec, Grüne Partikel)
			p.sendMessage(Text.PREFIX.getDE() + Text.PERFORMSPELL.getDE());
			
			Main.getInstance().getSpellManager().addSpell(new Heiltotem(p, 1000 * 30, 20, 1));
			
			return true;
			
		}
		
		
		p.sendMessage("§7/spell 1 §b (Meteorregen)");
		p.sendMessage("§7/spell 2 §b (Feuernova)");
		p.sendMessage("§7/spell 3 §b (Heilen)");
		return true;
		
	}

}
