package ykt.BeYkeRYkt.NoteMusic.NBS;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import ykt.BeYkeRYkt.NoteMusic.NoteMusic;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

public class NoteMusicAPI{
	  private HashMap<Location, SongPlayer> jukebox = new HashMap();
	  private NoteMusic plugin;
	  
	  
	  public NoteMusicAPI(NoteMusic plugin){
		  this.plugin = plugin;
	  }

	  public HashMap<Location, SongPlayer> getSingPlayers(){
		  return  jukebox;
	  }
	  
	  public void playSong(Player player, String file, Sign block){
		  
		   File f = new File(plugin.getDataFolder() + "/Tracks", file + ".nbs");
		  		  
			  Song nbs = NBSDecoder.parse(f);
			  
				  SongPlayer song = new SignSongPlayer(nbs);

				  ((SignSongPlayer) song).setSingPlayer(block);
				  			  
				  
				  song.addPlayer(player);
				  song.setPlaying(true);
				  song.setAutoDestroy(true);
				  jukebox.put(block.getLocation(), song);
	         	  player.sendMessage(ChatColor.DARK_GREEN + "[NoteMusic]" + ChatColor.GREEN + "Playing music : " + ChatColor.YELLOW + song.getSong().getTitle());
	  }
	  
	  public void playSongForPlayer(Player player, String file, Player by){
		  
		  File f = new File(plugin.getDataFolder() + "/Tracks", file + ".nbs");
		  
		  NoteBlockPlayerMain.stopPlaying(player);
		  		  
			  Song nbs = NBSDecoder.parse(f);
			  
				  SongPlayer song = new RadioSongPlayer(nbs);

				  			  
				  song.addPlayer(player);
				  song.setPlaying(true);
				  song.setAutoDestroy(true);
				  
	         	  player.sendMessage(ChatColor.DARK_GREEN + "[NoteMusic]" + ChatColor.GREEN + "Playing music : " + ChatColor.YELLOW + song.getSong().getTitle() + ChatColor.GREEN + " by " + ChatColor.RED + by.getName());
	  }

	  public void playSongForMe(Player player, String file){
		  
		  File f = new File(plugin.getDataFolder() + "/Tracks", file + ".nbs");
		  		  
		  NoteBlockPlayerMain.stopPlaying(player);
		  
			  Song nbs = NBSDecoder.parse(f);
			  
				  SongPlayer song = new RadioSongPlayer(nbs);

				  			  
				  song.addPlayer(player);
				  song.setPlaying(true);
				  song.setAutoDestroy(true);
				  
	         	  player.sendMessage(ChatColor.DARK_GREEN + "[NoteMusic]" + ChatColor.GREEN + "Playing music : " + ChatColor.YELLOW + song.getSong().getTitle());
	  }
	  
	  public void stopSong(Player player, Location loc){
            if(jukebox.containsKey(loc)){
            	SongPlayer song = jukebox.get(loc);
            	song.removePlayer(player);
            	song.destroy();
            	jukebox.remove(loc);
            }
	  }
	  
	  public void stopSongForPlayer(Player player){
		  NoteBlockPlayerMain.stopPlaying(player);
	  }
	  
}