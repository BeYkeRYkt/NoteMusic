package ykt.BeYkeRYkt.NoteMusic;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class MusicListener implements Listener{
	
	private NoteMusic plugin;
	
	
	public MusicListener(NoteMusic plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteractSign(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        World world = player.getWorld();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if(block.getType() == Material.WALL_SIGN){
            	if(block.getRelative(1,0,0).getType() == Material.JUKEBOX || block.getRelative(-1,0,0).getType() == Material.JUKEBOX || block.getRelative(0,0,1).getType() == Material.JUKEBOX || block.getRelative(0,0,-1).getType() == Material.JUKEBOX){
                    BlockState st = block.getState();
                    Sign s = (Sign) st;
                    
                    if (s.getLine(0).equals(ChatColor.DARK_RED + "[NoteMusic]") && !s.getLine(1).isEmpty()) {
                                        	    
                    			plugin.getAPI().playSong(player, s.getLine(1), s);
                    			
                		s.setLine(0, ChatColor.DARK_GREEN + "[NoteMusic]");
                		s.update(true);
                    	
                    }else if (s.getLine(0).equals(ChatColor.DARK_GREEN + "[NoteMusic]") && !s.getLine(1).isEmpty()) {

                    	        player.sendMessage(ChatColor.DARK_GREEN + "[NoteMusic]" + ChatColor.GREEN + "Music stopped.");
                               	plugin.getAPI().stopSong(player, block.getLocation());

                		s.setLine(0, ChatColor.DARK_RED + "[NoteMusic]");
                		s.update(true);
                    }
            	}
            }
        }
	}
	
    @EventHandler
    public void onPlayerBuild(SignChangeEvent event){
    	Player p = event.getPlayer();
    	Block block = event.getBlock();
    	if(event.getBlock().getType() == Material.WALL_SIGN){
                
    		
                if(event.getLine(0).equals("[NoteMusic]") && !event.getLine(1).isEmpty()) {
                	if(block.getRelative(1,0,0).getType() == Material.JUKEBOX || block.getRelative(-1,0,0).getType() == Material.JUKEBOX || block.getRelative(0,0,1).getType() == Material.JUKEBOX || block.getRelative(0,0,-1).getType() == Material.JUKEBOX){
                	

            		 if (!new File(plugin.getDataFolder(), event.getLine(1) + ".nbs").exists()) {
            		 System.out.println("[NoteMusic] .nbs not found");
            		 p.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "File not found");
             		event.setCancelled(true);
            		block.breakNaturally();
            		 }else{	
                    event.setLine(0, ChatColor.DARK_RED + "[NoteMusic]");
    		        p.sendMessage(ChatColor.DARK_GREEN + "[NoteMusic]" + ChatColor.GREEN + "Jukebox has been created!");
                	}
        		    }
        		    
        		    //Other
                	}else if(event.getLine(0).equals("[NoteMusic]") && event.getLine(1).isEmpty()) {
                		event.setCancelled(true);
                		block.breakNaturally();
        		        p.sendMessage(ChatColor.DARK_RED + "[NoteMusic]" + ChatColor.RED + "Enter the file name");
                	}
                }
    }
	
    
    @EventHandler
    public void onDestroySign(BlockBreakEvent event){
       	Player p = event.getPlayer();
    	Block block = event.getBlock();
    	if(event.getBlock().getType() == Material.WALL_SIGN){
            BlockState st = block.getState();
            Sign s = (Sign) st;
            if (s.getLine(0).equals(ChatColor.DARK_RED + "[NoteMusic]") && !s.getLine(1).isEmpty()|| s.getLine(0).equals(ChatColor.DARK_GREEN + "[NoteMusic]") && !s.getLine(1).isEmpty()){

                       	plugin.getAPI().stopSong(p, block.getLocation());
            	
            }
    	}
    }
}