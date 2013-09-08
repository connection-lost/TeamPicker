package me.crafter.mc.TeamPicker;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Picker implements Listener {
	
	private int picked = 999;
	private List<Player> allplayers;
	private List<Player> redplayers;
	private List<Player> blueplayers;
	private String playerlist;
	private Player redcaptain;
	private Player bluecaptain;
	
	public void throwusage(Player p){
		p.sendMessage(ChatColor.RED+"Usage: /teampickercreate (redteam captain) (blueteam captain) (including you or not)");
		p.sendMessage(ChatColor.RED+"Then /teampickerstart");
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

		
		if (cmd.getName().equals("pick") && picked == 999 && args.length == 1){
			sender.sendMessage("Team Picking is not running!");
			return false;
		}
		
		if (cmd.getName().equals("teampickercreate")){ /*CREATE TEAM PICKING*/
			allplayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
			if (Bukkit.getServer().getPlayer(args[0])==null || Bukkit.getServer().getPlayer(args[1])==null) return false;
			allplayers.remove(args[0]);/*CHECKED THEY ARE ONLINE AND REMOVE THEM*/
			allplayers.remove(args[1]);
			redcaptain = Bukkit.getServer().getPlayer(args[0]);
			bluecaptain = Bukkit.getServer().getPlayer(args[1]);
			int length = allplayers.size()-2;
			if (length<1){
				sender.sendMessage(ChatColor.RED+"Need at least 4 players.");
			}
			Bukkit.broadcastMessage(ChatColor.GREEN+"Team Picking started!");
			Bukkit.broadcastMessage(ChatColor.RED+"Red Team Leader: "+args[0]);
			Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team Leader: "+args[1]);
			for (Player x:allplayers){
				playerlist += x.getName()+" ";
			}
			Bukkit.broadcastMessage(ChatColor.GRAY+"Player Available: "+playerlist);
			picked = 0;
			Bukkit.broadcastMessage(ChatColor.RED+"It is now Red Team to pick!");
		}
		
		if (cmd.getName().equals("pick") && ptp(picked) == redcaptain){
			if (Bukkit.getServer().getPlayer(args[0])==null) return false;
			allplayers.remove(Bukkit.getServer().getPlayer(args[0]));
			Bukkit.broadcastMessage(ChatColor.RED+"Red Team picks "+args[0]);
			redplayers.add(Bukkit.getServer().getPlayer(args[0]));
			picked++;
			if (allplayers.size()==0){
				finishup();
			}
			else {
				announcewhopick();
			}
			}
		
		if (cmd.getName().equals("pick") && ptp(picked) == bluecaptain){
			if (Bukkit.getServer().getPlayer(args[0])==null) return false;
			allplayers.remove(Bukkit.getServer().getPlayer(args[0]));
			Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team picks "+args[0]);
			blueplayers.add(Bukkit.getServer().getPlayer(args[0]));
			picked++;
			if (allplayers.size()==0){
				finishup();
			}
			else {
				announcewhopick();
			}
			}
		return true;
	}
	
	
	private void finishup() {
		picked = 999;
		Bukkit.broadcastMessage(ChatColor.GREEN+"Team Picking has finished!");
		String temp1 = "";
		for (Player x:redplayers){
			temp1 += x.getName()+" ";
		}
		String temp2 = "";
		for (Player x:blueplayers){
			temp2 += x.getName()+" ";
		}
		Bukkit.broadcastMessage(ChatColor.RED+"Red Team: "+temp1);
		Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team: "+temp2);
	}

	private void announcewhopick() {
		if (ptp(picked)==redcaptain){
			Bukkit.broadcastMessage(ChatColor.RED+"It is now Red Team to pick!");
		}
		else{
			Bukkit.broadcastMessage(ChatColor.BLUE+"It is now Blue Team to pick!");
		}
		
	}

	public Player ptp (int num){
		int dis = (num+1)/2+1;
		if (dis==1){ 
			return redcaptain;
		}
		else{
			return bluecaptain;
		}
	}
	
	
	
	
	

}
