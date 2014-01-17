package ykt.BeYkeRYkt.NoteMusic;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ykt.BeYkeRYkt.NoteMusic.NBS.NoteMusicAPI;

import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;

public class NoteMusic extends JavaPlugin{
	
	private static NoteMusicAPI api;
	private static NoteMusic plugin;
	private ArrayList<String> list = new ArrayList<String>();
	public File dir;
	
	//Custom Permissions
	public String playPermissions;
	public String playMePermissions;
	public String stopPermissions;
	public String stopMePermissions;
	public String playblockPermissions;
	public String stopblockPermissions;
	
	@Override
	public void onEnable(){
				
		if(Bukkit.getPluginManager().getPlugin("NoteBlockAPI") == null){
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.RED + " To work needed NoteBlockAPI.");
			this.setEnabled(false);
		}else if(Bukkit.getPluginManager().getPlugin("NoteBlockAPI") != null){
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.GREEN + " Found NoteBlockAPI!");

			PluginDescriptionFile pdFile = getDescription();
			try {
				FileConfiguration fc = getConfig();
				if (!new File(getDataFolder(), "config.yml").exists()) {
					fc.options().header("NoteMusic v" + pdFile.getVersion() + " Configuration" + 
						"\nTo work requires NoteBlockAPI"+
						"\nAuthor: BeYkeRYkt");
					fc.addDefault("Permissions.play", "nm.play");
					fc.addDefault("Permissions.playMe", "nm.playMe");
					fc.addDefault("Permissions.stop", "nm.stop");
					fc.addDefault("Permissions.stopMe", "nm.stopMe");
					fc.addDefault("Permissions.play-block", "nm.block.play");
					fc.addDefault("Permissions.stop-block", "nm.block.stop");

					fc.options().copyDefaults(true);
					saveConfig();  
					fc.options().copyDefaults(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			registerCustomStrings();
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.WHITE + " Registered Strings!");
			
			registerCustomPermissions();
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.WHITE + " Registered Permissions!");
			
	    	api = new NoteMusicAPI(this);
			plugin = this;
			getDataFolder().mkdir();
			
			File test = new File(plugin.getDataFolder(),  "/Tracks");
			
			if(!test.exists()){
			test.mkdir();
			}
			
			loadList();
						
		Bukkit.getPluginManager().registerEvents(new MusicListener(this), this);

		}
	}
	
	
	public void registerCustomStrings(){
		playPermissions = this.getConfig().getString("Permissions.play");
		playMePermissions= this.getConfig().getString("Permissions.playMe");
		stopPermissions= this.getConfig().getString("Permissions.stop");
		stopMePermissions= this.getConfig().getString("Permissions.stopMe");
		playblockPermissions= this.getConfig().getString("Permissions.play-block");
		stopblockPermissions= this.getConfig().getString("Permissions.stop-block");
	}
	
	public void registerCustomPermissions(){
		Bukkit.getPluginManager().addPermission(new Permission(playPermissions, PermissionDefault.FALSE));
		Bukkit.getPluginManager().addPermission(new Permission(playMePermissions, PermissionDefault.TRUE));
		Bukkit.getPluginManager().addPermission(new Permission(stopPermissions, PermissionDefault.FALSE));
		Bukkit.getPluginManager().addPermission(new Permission(stopMePermissions, PermissionDefault.TRUE));
		Bukkit.getPluginManager().addPermission(new Permission(playblockPermissions, PermissionDefault.TRUE));
		Bukkit.getPluginManager().addPermission(new Permission(stopblockPermissions, PermissionDefault.TRUE));
	}
	
	  public void reloadPlugin(){
       this.onDisable();
       
		playPermissions = this.getConfig().getString("Permissions.play");
		playMePermissions= this.getConfig().getString("Permissions.playMe");
		stopPermissions= this.getConfig().getString("Permissions.stop");
		stopMePermissions= this.getConfig().getString("Permissions.stopMe");
		playblockPermissions= this.getConfig().getString("Permissions.play-block");
		stopblockPermissions= this.getConfig().getString("Permissions.stop-block");
       
		Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.WHITE + " Registered Strings!");
		
		registerCustomPermissions();
		
		Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.WHITE + " Registered Permissions!");
		
		File test = new File(plugin.getDataFolder(),  "/Tracks");
		
		if(!test.exists()){
		test.mkdir();
		}
		
		loadList();
		
		Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.GREEN + " Plugin reloaded!");
	  }
	
	public void loadList(){
		dir =  new File(plugin.getDataFolder(), "Tracks");
		String[] dirlist = dir.list();
		int amount = dirlist.length;
		dir.mkdir();
		
		for(int i = 0; i < amount; ++i) {
			String File = dirlist[i];
			if(File.contains(".nbs")) {
				File clan = new File(dir, File);
				String name = File.replace(".nbs", "");
				plugin.list.add(name);
				plugin.getLogger().info("Added new .nbs! " + name);
			}
		}
	}
	
	@Override
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
		list.clear();
		
		Bukkit.getPluginManager().removePermission(new Permission(playPermissions));
		Bukkit.getPluginManager().removePermission(new Permission(playMePermissions));
		Bukkit.getPluginManager().removePermission(new Permission(stopPermissions));
		Bukkit.getPluginManager().removePermission(new Permission(stopMePermissions));
		Bukkit.getPluginManager().removePermission(new Permission(playblockPermissions));
		Bukkit.getPluginManager().removePermission(new Permission(stopblockPermissions));
	}
	
	
	public static NoteMusicAPI getAPI(){
		return api;
	}
	
	public static NoteMusic getPlugin(){
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("nm")){
			  if(args.length == 0){
				  player.sendMessage(ChatColor.GOLD + "#" + ChatColor.GREEN + "====== " + this.getDescription().getFullName() + ChatColor.GREEN +  " ======" + ChatColor.GOLD + "#");
				  player.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.YELLOW + this.getDescription().getVersion());
				  player.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.YELLOW + this.getDescription().getAuthors());

					String list = "";
					
					for(String string : this.list){
						list+=string +", ";
						}
				  
					  
					  player.sendMessage(ChatColor.GREEN + "Tracks: " + ChatColor.YELLOW + list);
					
				  player.sendMessage("");
				  //Other Commands
				  player.sendMessage(ChatColor.GREEN + "/nm play [Player] [track]" + ChatColor.YELLOW + " - play a song for a certain player");
				  player.sendMessage(ChatColor.GREEN + "/nm playMe [track]" + ChatColor.YELLOW + " - play the song for you");
				  player.sendMessage(ChatColor.GREEN + "/nm stop [Player]" + ChatColor.YELLOW + " - stop all songs for a certain player");
				  player.sendMessage(ChatColor.GREEN + "/nm stopMe" + ChatColor.YELLOW + " - stop all songs for you");
				  player.sendMessage(ChatColor.DARK_RED + "/nm reload" + ChatColor.RED + " - reload plugin (Admin)");
				  return true;
			  }else if(args.length ==1){
				  if(args[0].equalsIgnoreCase("play")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm play [Player] [File name]");
						return true;
				  }else if(args[0].equalsIgnoreCase("playMe")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm playMe [File name]");
						return true;
				  }else if(args[0].equalsIgnoreCase("stop")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm stop [Player]");
						return true;
				  }else if(args[0].equalsIgnoreCase("stopMe")){
						NoteBlockPlayerMain.stopPlaying(player);
						return true;
				  }else if(args[0].equalsIgnoreCase("reload")){
					  if(player.isOp()){
						  this.reloadPlugin();
						  player.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
						  return true;
					  }
				  }else{
					  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "Unknown command. Type '/nm' for help.");
					  return true; 
				  }
			  }else if(args.length == 2){
				  if(args[0].equalsIgnoreCase("play")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm play [Player] [File name]");
						return true;
				  }else if(args[0].equalsIgnoreCase("playMe")){
					  if(player.hasPermission(playMePermissions)){
					  getAPI().playSongForMe(player, args[1]);
						return true;
					  }else{
                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
                  		return true;
                  	}
				  }else if(args[0].equalsIgnoreCase("stop")){
					  if(player.hasPermission(stopPermissions)){
						  
						  
					  OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					  if(target.isOnline()){
						getAPI().stopSongForPlayer((Player) target);
						return true;
					  }else if(!target.isOnline()){
						  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.YELLOW + target.getName() + ChatColor.RED + " is offline!");
						  return true;
					  }
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  	}
					  
				  }else if(args[0].equalsIgnoreCase("stopMe")){
					  if(player.hasPermission(stopMePermissions)){
					  getAPI().stopSongForPlayer(player);
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  }
				  }else if(args[0].equalsIgnoreCase("reload")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm reload");
						return true;
				  }else{
					  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "Unknown command. Type '/nm' for help.");
					  return true; 
				  }
			  }else if(args.length == 3){
				  if(args[0].equalsIgnoreCase("play")){
					  
					  if(player.hasPermission(playPermissions)){
					  
					  OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
					  if(target.isOnline()){
					  getAPI().playSongForPlayer((Player) target, args[2], player);
						return true;
					  }else if(!target.isOnline()){
						  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.YELLOW + target.getName() + ChatColor.RED + " is offline!");
						  return true;
					  }
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  }
				  }else if(args[0].equalsIgnoreCase("playMe")){
					  if(player.hasPermission(playMePermissions)){
					  
							sender.sendMessage(ChatColor.RED + "Usage: /nm playMe [track]");
							return true;
  
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  	}
				  }else if(args[0].equalsIgnoreCase("stop")){
					  if(player.hasPermission(stopPermissions)){	  
							sender.sendMessage(ChatColor.RED + "Usage: /nm stop [Player]");
							return true;
						  
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  	}
				  }else if(args[0].equalsIgnoreCase("stopMe")){
					  if(player.hasPermission(stopMePermissions)){
							sender.sendMessage(ChatColor.RED + "Usage: /nm stopMe");
							return true;
					  }else{
	                  		player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "You don't have permission.");
	                  		return true;
	                  	}
				  }else if(args[0].equalsIgnoreCase("reload")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm reload");
						return true;
				  }else{
					  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "Unknown command. Type '/nm' for help.");
					  return true; 
				  }
			  }else if(args.length > 3){
				  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "Unknown command. Type '/nm' for help.");
				  return true;
			  }
			}
		}
		//Nit >:D
		return false;
	}
}