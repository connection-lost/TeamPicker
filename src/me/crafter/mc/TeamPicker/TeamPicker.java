package me.crafter.mc.TeamPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPicker extends JavaPlugin {
	
	public final Logger logger = Logger.getLogger("Mincraft");
	public final Picker pk = new Picker();
	
	@Override
    public void onEnable(){
    	PluginDescriptionFile pdfFile = getDescription();
        this.logger.info("TeamPicker " + pdfFile.getVersion() + " has been ENABLED!");
        this.logger.info("TeamPicker is a Rev-Craft custom plugin.");
        }
 
	@Override
    public void onDisable() {
    	PluginDescriptionFile pdfFile = getDescription();
        this.logger.info("TeamPicker " + pdfFile.getVersion() + " has been DISABLED!");
    	}
    
	private int currentpick = 999;
	private List<Player> allplayers = new ArrayList<Player>();
	private List<Player> redplayers = new ArrayList<Player>();
	private List<Player> blueplayers = new ArrayList<Player>();
	private String playerlist;
	private Player redcaptain;
	private Player bluecaptain;
	private List<Integer> picklist = new ArrayList<Integer>();
	private List<Integer> returnlist = new ArrayList<Integer>();
	
	public void throwusage(Player p){
		p.sendMessage(ChatColor.RED+"Usage: /teampickercreate (redteam captain) (blueteam captain) (including you or not)");
		p.sendMessage(ChatColor.RED+"Then /teampickerstart");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

		
		if (cmd.getName().equals("pick") && currentpick == 999 && args.length == 1){
			sender.sendMessage("Team Picking is not running!");
			return false;
		}
		
		if (cmd.getName().equals("teampickercreate")){ /*CREATE TEAM PICKING*/
			playerlist = "";
			while (allplayers != null && !allplayers.isEmpty()){
				allplayers.remove(0);
			}
			while (redplayers != null && !redplayers.isEmpty()){
				allplayers.remove(0);
			}
			while (blueplayers !=null && !blueplayers.isEmpty()){
				allplayers.remove(0);
			}
			while (picklist !=null && !blueplayers.isEmpty()){
				allplayers.remove(0);
			}
			while (returnlist !=null && !blueplayers.isEmpty()){
				allplayers.remove(0);
			}
			allplayers.addAll(Arrays.asList(Bukkit.getServer().getOnlinePlayers()));
			if (Bukkit.getServer().getPlayer(args[0])==null || Bukkit.getServer().getPlayer(args[1])==null){
				sender.sendMessage(ChatColor.RED+"Invalid players.");
				return true;
			}
			allplayers.remove(Bukkit.getServer().getPlayer(args[0]));/*CHECKED THEY ARE ONLINE AND REMOVE THEM*/
			allplayers.remove(Bukkit.getServer().getPlayer(args[1]));
			redcaptain = Bukkit.getServer().getPlayer(args[0]);
			bluecaptain = Bukkit.getServer().getPlayer(args[1]);
			int length = allplayers.size();
			if (length<1){
				sender.sendMessage(ChatColor.RED+"Need at least 4 players.");
				return true;
			}
			picklist = generateorder(length);
			Bukkit.broadcastMessage(ChatColor.GREEN+"Team Picking started!");
			Bukkit.broadcastMessage(ChatColor.RED+"Red Team Leader: "+args[0]);
			Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team Leader: "+args[1]);
			for (Player x:allplayers){
				playerlist += x.getName()+" ";
			}
			Bukkit.broadcastMessage(ChatColor.GRAY+"Player Available: "+playerlist);
			currentpick = 1;
			Bukkit.broadcastMessage(ChatColor.RED+"It is now Red Team to pick!");
		}
		
		if (cmd.getName().equals("pick") && (Player)sender == redcaptain && picklist.get(currentpick-1) == 1){
			if (Bukkit.getServer().getPlayer(args[0])==null || !isinlist(Bukkit.getServer().getPlayer(args[0]))){
				sender.sendMessage("Not a valid player! Please retry!");
				return true;
			}
			allplayers.remove(Bukkit.getServer().getPlayer(args[0]));
			Bukkit.broadcastMessage(ChatColor.RED+"Red Team picks "+args[0]);
			redplayers.add(Bukkit.getServer().getPlayer(args[0]));
			currentpick++;
			if (allplayers.size()==0){
				finishup();
			}
			else if (picklist.get(currentpick-1) == 3){
				randomlastplayer();
			}
			else {
				announcewhopick();
			}
			}
		
		if (cmd.getName().equals("pick") && (Player)sender == bluecaptain && picklist.get(currentpick-1) == 2){
			if (Bukkit.getServer().getPlayer(args[0])==null || !isinlist(Bukkit.getServer().getPlayer(args[0]))){
				sender.sendMessage("Not a valid player! Please retry!");
				return true;
			}
			allplayers.remove(Bukkit.getServer().getPlayer(args[0]));
			Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team picks "+args[0]);
			blueplayers.add(Bukkit.getServer().getPlayer(args[0]));
			currentpick++;
			if (allplayers.size()==0){
				finishup();
			}
			else if (picklist.get(currentpick-1) == 3){
				randomlastplayer();
			}
			else {
				announcewhopick();
			}
			}
		return true;
	}
	
	public boolean isinlist(Player p){
		for (Player x:allplayers){
			if (x==p) return true;
		}
		return false;
	}
	
	
	public void randomlastplayer(){
		if (redplayers.hashCode() > blueplayers.hashCode()){
			redplayers.add(allplayers.get(0));
			allplayers.remove(0);
		}
		else{
			blueplayers.add(allplayers.get(0));
			allplayers.remove(0);
		}
		finishup();
	}
	
	private void finishup() {
		currentpick = 999;
		Bukkit.broadcastMessage(ChatColor.GREEN+"Team Picking has finished!");
		String temp1 = redcaptain.getName()+" ";
		for (Player x:redplayers){
			temp1 += x.getName()+" ";
		}
		String temp2 = bluecaptain.getName()+" ";
		for (Player x:blueplayers){
			temp2 += x.getName()+" ";
		}
		Bukkit.broadcastMessage(ChatColor.RED+"Red Team: "+temp1);
		Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Team: "+temp2);
	}

	private void announcewhopick() {
		if (picklist.get(currentpick-1)==1){
			Bukkit.broadcastMessage(ChatColor.RED+"It is now Red Team to pick!");
			playerlist = "";
			for (Player x:allplayers){
				playerlist += x.getName()+" ";
			}
			Bukkit.broadcastMessage(ChatColor.GRAY+"Player Available: "+playerlist);
			redcaptain.sendMessage("Pick your player:");
		}
		else{
			Bukkit.broadcastMessage(ChatColor.BLUE+"It is now Blue Team to pick!");
			playerlist = "";
			for (Player x:allplayers){
				playerlist += x.getName()+" ";
			}
			Bukkit.broadcastMessage(ChatColor.GRAY+"Player Available: "+playerlist);
			bluecaptain.sendMessage("Pick your player:");
		}
		
	}
	

	public List<Integer> generateorder(int numplayers){
		for (int x=0 ; x<=numplayers; x++){
			returnlist.add(ptp(x,numplayers));
		}
		if (numplayers % 2 == 1){
			returnlist.add(3);
		}
		return returnlist;		
	}

	public Player getcaptain(int currentpicker){
		if (currentpicker == 1){
			return redcaptain;
		}
		else{
			return bluecaptain;
		}
	}
	
    public int ptp(int num, int numPlayers)
    {
            boolean even = (((numPlayers - 2) / 2) % 2 == 0);

            if (num == 0) return 1;
            else if (num == numPlayers - 1) return (even) ? 2 : 1;
            else return ((num - 1) % 4 < 2) ? 2 : 1;
    }
}
