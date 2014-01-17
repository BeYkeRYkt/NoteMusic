package ykt.BeYkeRYkt.NoteMusic.NBS;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import ykt.BeYkeRYkt.NoteMusic.NoteMusic;

import com.xxmicloxx.NoteBlockAPI.Instrument;
import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.Note;
import com.xxmicloxx.NoteBlockAPI.NotePitch;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

public class SignSongPlayer extends SongPlayer{


	private Sign block;
	
	
   public SignSongPlayer(Song song) {
      super(song);
   }
   
   public void setSingPlayer(Sign block){
	   this.block = block;
   }
   
   public Sign getSingPlayer(){
	   return block;
   }

   @Override
   public void playTick(Player player, int tick) {
      if(this.block.getType() == Material.WALL_SIGN) {
    	  if(player.getWorld().getName().equals(this.block.getWorld().getName())) {

    		byte playerVolume = 100;

            for (Layer l : this.song.getLayerHashMap().values()) {
               Note note = l.getNote(tick);
               if(note != null) {
                 player.getWorld().playSound(this.block.getLocation(), Instrument.getInstrument(note.getInstrument()), (float)(l.getVolume() * this.getVolume() * playerVolume) / 1000000.0F, NotePitch.getPitch(note.getKey() - 33));
               }
            }
    	  }
         
      }else if(this.block.getType() != Material.WALL_SIGN){
    	  SongPlayer song = NoteMusic.getAPI().getSingPlayers().get(this.block.getLocation());
    	  song.setPlaying(false);
    	  song.destroy();
    	  NoteMusic.getAPI().getSingPlayers().remove(block.getLocation());
      }
   }
}
