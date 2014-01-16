package ykt.BeYkeRYkt.NoteMusic;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;

import ykt.BeYkeRYkt.NoteMusic.NBS.NoteMusicAPI;

public class NoteMusic extends JavaPlugin{
	
	private static NoteMusicAPI api;
	private static NoteMusic plugin;
	private ArrayList<String> list = new ArrayList<String>();
	public File dir;
	
	@Override
	public void onEnable(){
		
	    	  api = new NoteMusicAPI(this);
		
		if(Bukkit.getPluginManager().getPlugin("NoteBlockAPI") == null){
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.RED + " To work needed NoteBlockAPI.");
			this.setEnabled(false);
		}else if(Bukkit.getPluginManager().getPlugin("NoteBlockAPI") != null){
			Bukkit.getConsoleSender().sendMessage("[NoteMusic]" + ChatColor.GREEN + " Found NoteBlockAPI!");
	
			plugin = this;
			getDataFolder().mkdir();

		Bukkit.getPluginManager().registerEvents(new MusicListener(this), this);

		
		loadList();
		}
	}
	
	public void loadList(){
		dir =  new File(plugin.getDataFolder(), "");
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
	}
	
	
	public static NoteMusicAPI getAPI(){
		return api;
	}
	
	public static NoteMusic getPlugin(){
		return plugin;
	}
	
	//getFiles
	private File[] getFiles() {
		File dir = new File(this.getDataFolder() + File.separator + "nbs");
		File[] list = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".nbs");
			}
		});
		Arrays.sort(list);
		return list;
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
				  player.sendMessage(ChatColor.GREEN + "/nm play [Player] [file name]" + ChatColor.YELLOW + " - play a song for a certain player");
				  player.sendMessage(ChatColor.GREEN + "/nm playMe  [file name]" + ChatColor.YELLOW + " - play the song for you");
				  player.sendMessage(ChatColor.GREEN + "/nm stop [Player]" + ChatColor.YELLOW + " - stop all songs for a certain player");
				  player.sendMessage(ChatColor.GREEN + "/nm stopMe" + ChatColor.YELLOW + " - stop all songs for you");
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
				  }
			  }else if(args.length == 2){
				  if(args[0].equalsIgnoreCase("play")){
						sender.sendMessage(ChatColor.RED + "Usage: /nm play [Player] [File name]");
						return true;
				  }else if(args[0].equalsIgnoreCase("playMe")){
					  getAPI().playSongForMe(player, args[1]);
						return true;
				  }else if(args[0].equalsIgnoreCase("stop")){
					  
					  Player target = Bukkit.getPlayer(args[1]);
					  if(target.isOnline()){
						getAPI().stopSongForPlayer(target);
						return true;
					  }else if(!target.isOnline()){
						  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.WHITE + target.getName() + ChatColor.RED + " is offline!");
						  return true;
					  }
						
				  }else if(args[0].equalsIgnoreCase("stopMe")){
					  getAPI().stopSongForPlayer(player);
				  }
			  }else if(args.length == 3){
				  if(args[0].equalsIgnoreCase("play")){
					  Player target = Bukkit.getPlayer(args[1]);
					  if(target.isOnline()){
					  getAPI().playSongForPlayer(target, args[2], player);
						return true;
					  }else if(!target.isOnline()){
						  player.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.WHITE + target.getName() + ChatColor.RED + " is offline!");
						  return true;
					  }
				  }
			  }
			}
		}
		//Nit >:D
		return false;
	}
}