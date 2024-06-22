package planetaryprotector;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.sound.Sound;
import com.thizthizzydizzy.dizzyengine.sound.SoundSource;
import com.thizthizzydizzy.dizzyengine.sound.SoundSystem;
import com.thizthizzydizzy.dizzyengine.ui.FlatUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.openal.AL10;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuLost;
public class Sounds{
    private static SoundSource musicSource;
    private static int musicFading;
    private static Sound fadeInto;
    private static void addMusic(){
        addSong("EndMusic1", "Killers");
        addSong("Music1", "Killers");
//        addSong("Music2", "Clenched Teeth");
        addSong("Music3", "Achilles");
        addSong("Music4", "Noble Race");
        addSong("Music5", "Rynos Theme");
        addSong("Boss1Music1", "Eternal Terminal");
        addSong("Boss1Music2", "Five Armies");
        addSong("Boss2Music1", "Corruption");
        addSong("Boss2Music2", "Clash Defiant");
//        addSong("Boss3Music1", "Metalmania");
        addSong("Boss3Music2", "Obliteration");
        addSong("Boss4Music1", "Death and Axes");
        addSong("Boss4Music2", "Grim Idol");
        addSong("SadMusic1", "A Turn for the Worse");
        addSong("SadMusic2", "A Little Faith");
        addSong("SadMusic3", "At Rest");
        addSong("SadMusic4", "Awaiting Return");
        addSong("SadMusic5", "Bittersweet");
        addSong("SadMusic6", "Colorless Aura");
        addSong("SadMusic7", "Cryptic Sorrow");
        addSong("SadMusic8", "Dark Times");
        addSong("SadMusic9", "Dark Walk");
        addSong("SadMusic10", "Despair and Triumph");
        addSong("SadMusic11", "Disquiet");
        addSong("SadMusic12", "End of the Era");
        addSong("SadMusic13", "Heartbreaking");
        addSong("SadMusic14", "Heavy Heart");
        addSong("SadMusic15", "Immersed");
        addSong("SadMusic16", "Lasting Hope");
        addSong("SadMusic17", "Lone Harvest");
        addSong("SadMusic18", "Lost Frontier");
        addSong("SadMusic19", "Memory Lane");
        addSong("SadMusic20", "On the Passing of Time");
        addSong("SadMusic21", "Rains Will Fall");
        addSong("SadMusic22", "Reaching Out");
        addSong("SadMusic23", "Sad Trio");
        addSong("SadMusic24", "Stages of Grief");
        addSong("SadMusic25", "The Parting");
        addSong("SadMusic26", "Time Passes");
        addSong("SadMusic27", "When the Wind Blows");
        addSong("SadMusic28", "Wounded");
        addSong("WinMusic", "Americana");
        addSong("SuspenseMusic1", "Final Battle of the Dark Wizards");
        addSong("VictoryMusic1", "Jet Fueled Vixen");
        addSong("MysteryMusic1", "Almost New");
        addSong("MysteryMusic2", "Comfortable Mystery");//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic3", "Comfortable Mystery 2");//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic4", "Comfortable Mystery 3");//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic5", "Comfortable Mystery 4");//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic6", "Constancy Part Three");
        addSong("MysteryMusic7", "Earth Prelude");
        addSong("MysteryMusic8", "Floating Cities");
        addSong("MysteryMusic9", "Frost Waltz (Alternate)");
        addSong("MysteryMusic10", "Frost Waltz");
        addSong("MysteryMusic11", "The Chamber");
        addSong("MysteryMusic12", "The Other Side of the Door");
        addSong("MysteryMusic13", "The Snow Queen");
    }
    /**
     * Gets a list of all currently playable music.
     *
     * @return a list of sound names that can be played as music at the current
     * time (One will be randomly chosen)
     */
    private static void getPlayableMusic(ArrayList<String> playableMusic){
        Game game = Core.getGame();
        if(DizzyEngine.getLayer(FlatUI.class).menu instanceof MenuLost){
            playableMusic.add("SadMusic3");
            return;
        }
        if(game!=null){
            if(game.isDestroyed()){
                if(game.phase>3){
                    playableMusic.add("SadMusic7");
                }else{
                    for(int i = 1; i<=28; i++){
                        if(i==3)continue;
                        playableMusic.add("SadMusic"+i);
                    }
                }
            }else if(game.won){
                for(int i = 1; i<=1; i++){
                    playableMusic.add("VictoryMusic"+i);
                }
            }else if(game.secretWaiting>-1){
                game.playSecret(playableMusic);
            }else{
                int mothershipPhase = 0;
                for(Enemy e : game.enemies){
                    if(e instanceof EnemyMothership){
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership)e).phase);
                    }
                }
                if(mothershipPhase>0){
                    switch(mothershipPhase){
                        case 1:
                            playableMusic.add("Boss1Music1");
                            playableMusic.add("Boss1Music2");
                            break;
                        case 2:
                            playableMusic.add("Boss2Music1");
                            playableMusic.add("Boss2Music2");
                            break;
                        case 3:
//                            playableMusic.add("Boss3Music1");
                            playableMusic.add("Boss3Music2");
                            break;
                        case 4:
                            playableMusic.add("Boss4Music1");
                            playableMusic.add("Boss4Music2");
                            break;
                    }
                }else{
//                    playableMusic.add("Music2");
                    playableMusic.add("Music3");
                    playableMusic.add("Music4");
                    playableMusic.add("Music5");
                }
            }
        }else{
//            playableMusic.add("Music2");
            playableMusic.add("Music3");
            playableMusic.add("Music4");
            playableMusic.add("Music5");
        }
    }
    public static HashMap<String, String> soundNames = new HashMap<>();
    private static float vol = 1f;
    public static void init(){
        SoundSystem.init();
        soundNames.clear();
        addMusic();
        musicSource = new SoundSource();
    }
    static void tick(long tickCounter){
        if(musicFading>0){//TODO get redo music fading
            musicFading--;
            if(musicFading==0){
                musicSource.playSound(fadeInto);
                musicSource.setGain(vol);
                fadeInto = null;
            }else
                musicSource.setGain((float)(musicFading/40));//TODO don't hardcode to 2 seconds!
        }
        if(musicSource.getState()==AL10.AL_STOPPED){
            ArrayList<String> strs = new ArrayList<>();
            getPlayableMusic(strs);
            if(!strs.isEmpty()){
                String sound = strs.get(new Random().nextInt(strs.size()));
                musicSource.playSound(getSong(soundNames.get(sound)));
            }
        }
    }
    public static void setVolume(float volume){
        vol = Math.max(0, Math.min(1, volume));
        musicSource.setGain(Math.max(0.0001f, vol));
    }
    /**
     * @return song time in TICKS
     */
    public static int songTimer(){
        return musicChannel.getPlayheadPosition()/50;
    }
    private static void addSong(String name, String songName){
        soundNames.put(name, "/assets/sounds/music/"+songName+".mp3");
    }
    @Deprecated
    static boolean isPlayingMusic(){
        return musicSource.getState()==AL10.AL_PLAYING;
    }
    static void toggleMusic(){
        if(musicSource.getState()==AL10.AL_PLAYING){
            musicSource.play();
        }else{
            musicSource.pause();
        }
    }
    public static String nowPlaying(){
        for(String sound : soundNames.keySet()){
            String path = soundNames.get(sound);
            if(path.equals(musicChannel.getCurrentSound())){
                return sound;
            }
        }
        return null;
    }
    public static float getVolume(){
        return vol;
    }
    private static Sound getSong(String path){
        return new Sound(path);
    }
    public static void fadeMusic(String music1){
        if(musicFading<=0)musicFading = 40;
        fadeInto = getSong(soundNames.get(music1));
    }
}
