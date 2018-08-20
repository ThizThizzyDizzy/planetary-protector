package planetaryprotector;
import planetaryprotector.menu.options.MenuOptions1;
import planetaryprotector.menu.options.MenuOptions2;
import planetaryprotector.menu.options.MenuOptions3;
import planetaryprotector.menu.options.MenuOptions4;
import planetaryprotector.menu.options.MenuOptions5;
import planetaryprotector.menu.options.MenuOptions6;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.Util;
import simplelibrary.Sys;
import simplelibrary.error.ErrorCategory;
import simplelibrary.error.ErrorLevel;
import simplelibrary.openal.SoundStash;
public class Sounds{
    public static boolean autoplay = true;
    private static boolean paused = false;
    private static void addMusic(){
        addSong("Music1", "Killers", "https://www.dropbox.com/s/e2p5uwdyzznked0/Killers.mp3?dl=1");
        addSong("Music2", "Clenched Teeth", "https://www.dropbox.com/s/jzstxq3404lai4q/Clenched%20Teeth.mp3?dl=1");
        addSong("Music3", "Achilles", "https://www.dropbox.com/s/agtrfb81kmix3f2/Achilles.mp3?dl=1");
        addSong("Music4", "Noble Race", "https://www.dropbox.com/s/jmqmcttfx9in4a5/Noble%20Race.mp3?dl=1");
        addSong("Music5", "Rynos Theme", "https://www.dropbox.com/s/uodl6uu825hik38/Rynos%20Theme.mp3?dl=1");
        addSong("Boss1Music1", "Eternal Terminal", "https://www.dropbox.com/s/rd2yfsma9994rvn/Eternal%20Terminal.mp3?dl=1");
        addSong("Boss1Music2", "Five Armies", "https://www.dropbox.com/s/n2nnl7o3qp6pfmu/Five%20Armies.mp3?dl=1");
        addSong("Boss2Music1", "Corruption", "https://www.dropbox.com/s/0wjbtmx2ya5t7f6/Corruption.mp3?dl=1");
        addSong("Boss2Music2", "Clash Defiant", "https://www.dropbox.com/s/6p6ulhfp58jty24/Clash%20Defiant.mp3?dl=1");
        addSong("Boss3Music1", "Metalmania", "https://www.dropbox.com/s/p6e99sa8z9kdckp/Metalmania.mp3?dl=1");
        addSong("Boss3Music2", "Obliteration", "https://www.dropbox.com/s/2685hexub0a1tm0/Obliteration.mp3?dl=1");
        addSong("Boss4Music1", "Death and Axes", "https://www.dropbox.com/s/uk9m4xnlcm5mqao/Death%20and%20Axes.mp3?dl=1");
        addSong("Boss4Music2", "Grim Idol", "https://www.dropbox.com/s/606kbzltk24v307/Grim%20Idol.mp3?dl=1");
        addSong("EndMusic1", "Killers", "https://www.dropbox.com/s/e2p5uwdyzznked0/Killers.mp3?dl=1");
        addSong("SadMusic1", "A Turn for the Worse", "https://www.dropbox.com/s/7xllni5jw99d7j1/A%20Turn%20for%20the%20Worse.mp3?dl=1");
        addSong("SadMusic2", "A Little Faith", "https://www.dropbox.com/s/h2ygg4q05boayfk/A%20Little%20Faith.mp3?dl=1");
        addSong("SadMusic3", "At Rest", "https://www.dropbox.com/s/xoh1zc2nztttuhw/At%20Rest.mp3?dl=1");
        addSong("SadMusic4", "Awaiting Return", "https://www.dropbox.com/s/xcq6wpxqseoxy0b/Awaiting%20Return.mp3?dl=1");
        addSong("SadMusic5", "Bittersweet", "https://www.dropbox.com/s/nsu2dzc5ua73wb4/Bittersweet.mp3?dl=1");
        addSong("SadMusic6", "Colorless Aura", "https://www.dropbox.com/s/i9w7nmaicjkitse/Colorless%20Aura.mp3?dl=1");
        addSong("SadMusic7", "Cryptic Sorrow", "https://www.dropbox.com/s/mbpq6q7x8qdmmed/Cryptic%20Sorrow.mp3?dl=1");
        addSong("SadMusic8", "Dark Times", "https://www.dropbox.com/s/1kyom747avrm8le/Dark%20Times.mp3?dl=1");
        addSong("SadMusic9", "Dark Walk", "https://www.dropbox.com/s/vthmioptxl13khf/Dark%20Walk.mp3?dl=1");
        addSong("SadMusic10", "Despair and Triumph", "https://www.dropbox.com/s/alsswa1pi426qo7/Despair%20and%20Triumph.mp3?dl=1");
        addSong("SadMusic11", "Disquiet", "https://www.dropbox.com/s/7t7s0rnwumi07fx/Disquiet.mp3?dl=1");
        addSong("SadMusic12", "End of the Era", "https://www.dropbox.com/s/tr5e2jk74fk1fbd/End%20of%20the%20Era.mp3?dl=1");
        addSong("SadMusic13", "Heartbreaking", "https://www.dropbox.com/s/vaejmyxhsq9ryok/Heartbreaking.mp3?dl=1");
        addSong("SadMusic14", "Heavy Heart", "https://www.dropbox.com/s/l4q6j6rir5hkws8/Heavy%20Heart.mp3?dl=1");
        addSong("SadMusic15", "Immersed", "https://www.dropbox.com/s/puoderxzd0odn9x/Immersed.mp3?dl=1");
        addSong("SadMusic16", "Lasting Hope", "https://www.dropbox.com/s/fwkeuf273bh43sf/Lasting%20Hope.mp3?dl=1");
        addSong("SadMusic17", "Lone Harvest", "https://www.dropbox.com/s/b54lp4mnls8yzj2/Lone%20Harvest.mp3?dl=1");
        addSong("SadMusic18", "Lost Frontier", "https://www.dropbox.com/s/5jbvcsicfd4nxh8/Lost%20Frontier.mp3?dl=1");
        addSong("SadMusic19", "Memory Lane", "https://www.dropbox.com/s/grtc76gfuh0n75s/Memory%20Lane.mp3?dl=1");
        addSong("SadMusic20", "On the Passing of Time", "https://www.dropbox.com/s/8zmq188r0flij3v/On%20the%20Passing%20of%20Time.mp3?dl=1");
        addSong("SadMusic21", "Rains Will Fall", "https://www.dropbox.com/s/ox2prgtrwh1xz2p/Rains%20Will%20Fall.mp3?dl=1");
        addSong("SadMusic22", "Reaching Out", "https://www.dropbox.com/s/qtyrk790g3xd7kq/Reaching%20Out.mp3?dl=1");
        addSong("SadMusic23", "Sad Trio", "https://www.dropbox.com/s/bsp72cdunr4ktep/Sad%20Trio.mp3?dl=1");
        addSong("SadMusic24", "Stages of Grief", "https://www.dropbox.com/s/fmwiflng3dk4bz1/Stages%20of%20Grief.mp3?dl=1");
        addSong("SadMusic25", "The Parting", "https://www.dropbox.com/s/vt6d11fdp4dh0gq/The%20Parting.mp3?dl=1");
        addSong("SadMusic26", "Time Passes", "https://www.dropbox.com/s/3az3otjjqh8u0ij/Time%20Passes.mp3?dl=1");
        addSong("SadMusic27", "When the Wind Blows", "https://www.dropbox.com/s/okofwwbz9ks5xlp/When%20The%20Wind%20Blows.mp3?dl=1");
        addSong("SadMusic28", "Wounded", "https://www.dropbox.com/s/dmn6nfoetoiyko6/Wounded.mp3?dl=1");
        addSong("WinMusic", "Americana", "https://www.dropbox.com/s/v2q3gcp17uync61/Americana.mp3?dl=1");
        addSong("SuspenseMusic1", "Final Battle of the Dark Wizards", "https://www.dropbox.com/s/0yccf5vyouhiubn/Final%20Battle%20of%20the%20Dark%20Wizards.mp3?dl=1");
        addSong("VictoryMusic1", "Jet Fueled Vixen", "https://www.dropbox.com/s/8604fjnzfggdd80/Jet%20Fueled%20Vixen.mp3?dl=1");
    }
    /**
     * Gets a list of all currently playable music.
     * @return a list of sound names that can be played as music at the current time (One will be randomly chosen)
     */
    private static void getPlayableMusic(ArrayList<String> playableMusic){
        if(Core.game!=null&&Core.game.isDestroyed()){
            for(int i = 1; i<=28; i++){
                playableMusic.add("SadMusic"+i);
            }
        }else if(Core.game!=null&&Core.game.won){
            for(int i = 1; i<=1; i++){
                playableMusic.add("VictoryMusic"+i);
            }
        }else{
            if(Core.game!=null&&Core.game.mothership!=null){
                switch(Core.game.mothership.phase){
                    case 1:
                        playableMusic.add("Boss1Music1");
                        playableMusic.add("Boss1Music2");
                        break;
                    case 2:
                        playableMusic.add("Boss2Music1");
                        playableMusic.add("Boss2Music2");
                        break;
                    case 3:
                        playableMusic.add("Boss3Music1");
                        playableMusic.add("Boss3Music2");
                        break;
                    case 4:
                        playableMusic.add("Boss4Music1");
                        playableMusic.add("Boss4Music2");
                        break;
                }
            }else{
                playableMusic.add("Music2");
                playableMusic.add("Music3");
                playableMusic.add("Music4");
                playableMusic.add("Music5");
            }
        }
    }
    private static boolean running = false;
    public static HashMap<String, String> soundNames = new HashMap<>();
    public static HashMap<String, String> songURLs = new HashMap<>();
    public static boolean mute;
    public static String nowPlaying;
    public static float vol = 2.5f;
    private static float volume = 1f;
    private static String fading = null;
    private static String fadingSource = null;
    private static long songTimer;
    private static int whichSound = 0;
    /**
     * Disables the sound system.
     * Equivalent to AL.destroy();
     */
    public static void destroy(){
        AL.destroy();
        running = false;
    }
    /**
     * Starts the sound system, music thread, and music downloading thread.
     * Equivalent to AL.destroy();
     */
    public static void create() throws LWJGLException{
        soundNames.clear();
        songURLs.clear();
        addMusic();
        running = true;
        AL.create();
        new Thread(() -> {
                while(running){
                    if(!isPlaying("music")){
                        nowPlaying = null;
                    }
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException ex){
                        Sys.error(ErrorLevel.severe, "Failed to wait: Too impatient!", ex, ErrorCategory.threading);
                    }
                    if(mute){
                        if(isPlaying("music")){
                            stopSound("music");
                        }
                        continue;
                    }
                    if(autoplay&&!isPlaying("music")){
                        ArrayList<String> strs = new ArrayList<>();
                        getPlayableMusic(strs);
                        if(!strs.isEmpty()){
                            playSoundOneChannel("music", strs.get(new Random().nextInt(strs.size())));
                        }
                    }
                }
        }).start();
        new Thread(() -> { //Music Downloader
                System.out.println("Starting Music Download...");
                for(String key : songURLs.keySet()){
                    if(!running){
                        return;
                    }
                    String filepath = soundNames.get(key);
                    String url = songURLs.get(key);
                    File from = new File(filepath.replace(".wav", ".mp3"));
                    File to = new File(filepath.replace(".mp3", ".wav"));
                    if(!from.exists()){
                        System.out.println("Downloading Song "+key+"...");
                        downloadFile(url, from);
                        System.out.println("Song Downloaded: "+key+"...");
                    }
                    if(to.exists()) continue;
                    Converter c = new Converter();
                    try{
                        System.out.println("Converting Song "+from.getName()+"...");
                        c.convert(from.getAbsolutePath(), to.getAbsolutePath());
                    }catch(JavaLayerException ex){
                        Sys.error(ErrorLevel.severe, "Failed to convert file: "+from.getName()+". Deleting...", ex, ErrorCategory.audio);
                        if(from.exists()) from.delete();
                        if(to.exists()) to.delete();
                    }
                        System.out.println("Song Converted: "+from.getName()+"...");
                }
                System.out.println("All songs are up to date.");
        }).start();
    }
    static void tick(boolean lastTick){
        if(lastTick){
            destroy();
            return;
        }
        vol = Math.max(0,Math.min(5,vol));
        if(nowPlaying!=null&&!canPlayMusic(nowPlaying)){
            fadeSound("music");
        }
        AL10.alSourcef(SoundStash.getSource("music"), AL10.AL_GAIN, volume*vol);
        if(fadingSource!=null){
            AL10.alSourcef(SoundStash.getSource(fadingSource), AL10.AL_GAIN, volume*vol);
            if(volume>0){
                volume-=0.025;
            }else{
                stopSound(fadingSource);
                volume = 1f;
                if(fading!=null){
                    playSoundOneChannel(fadingSource, fading);
                    fading = null;
                }
                AL10.alSourcef(SoundStash.getSource(fadingSource), AL10.AL_GAIN, vol);
                fadingSource = null;
            }
        }
    }
    public static int songTimer(){
        return (int) ((System.currentTimeMillis()-songTimer)/50);
    }
    public static synchronized void playSound(String source, String sound){
        whichSound++;
        if(whichSound>20){
            whichSound = 1;
        }
        if(isPlaying(source, whichSound)){
            stopSound(source, whichSound);
        }
        AL10.alSourcef(SoundStash.getSource(source), AL10.AL_GAIN, vol);
        try{
            AL10.alSourceUnqueueBuffers(SoundStash.getSource(source+whichSound));
            Util.checkALError();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        AL10.alSourceQueueBuffers(SoundStash.getSource(source+whichSound), SoundStash.getBuffer(soundNames.get(sound)));
        AL10.alSourcePlay(SoundStash.getSource(source+whichSound));
    }
    public static synchronized void playSoundOneChannel(String source, String sound){
        if(!new File(soundNames.get(sound)).exists())return;
        if(source.equals("music")){
            if(!canPlayMusic(sound)) return;
        }
        if(isPlaying(source)){
            fadeSound(source, sound);
            return;
        }
        AL10.alSourcef(SoundStash.getSource(source), AL10.AL_GAIN, vol);
        try{
            AL10.alSourceUnqueueBuffers(SoundStash.getSource(source));
            Util.checkALError();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        AL10.alSourceQueueBuffers(SoundStash.getSource(source), SoundStash.getBuffer(soundNames.get(sound)));
        AL10.alSourcePlay(SoundStash.getSource(source));
        if(source.equals("music")){
            nowPlaying = sound;
            songTimer = System.currentTimeMillis();
        }
    }
    public static boolean canPlayMusic(String music){
//        ArrayList<String> playable = new ArrayList<>();
//        getPlayableMusic(playable);
//        if(!playable.contains(music)){
//            return false;
//        }
        switch(music){
            case "Music1":
                return MenuOptions1.song1;
            case "Music2":
                return MenuOptions1.song2;
            case "Music3":
                return MenuOptions1.song3;
            case "Music4":
                return MenuOptions1.song4;
            case "Music5":
                return MenuOptions1.song5;
            case "Boss1Music1":
                return MenuOptions2.boss11;
            case "Boss1Music2":
                return MenuOptions2.boss12;
            case "Boss2Music1":
                return MenuOptions2.boss21;
            case "Boss2Music2":
                return MenuOptions2.boss22;
            case "Boss3Music1":
                return MenuOptions2.boss31;
            case "Boss3Music2":
                return MenuOptions2.boss32;
            case "Boss4Music1":
                return MenuOptions2.boss41;
            case "Boss4Music2":
                return MenuOptions2.boss42;
            case "EndMusic1":
                return MenuOptions1.song1;
            case "SadMusic1":
                return MenuOptions3.music1;
            case "SadMusic2":
                return MenuOptions3.music2;
            case "SadMusic3":
                return MenuOptions3.music3;
            case "SadMusic4":
                return MenuOptions3.music4;
            case "SadMusic5":
                return MenuOptions3.music5;
            case "SadMusic6":
                return MenuOptions3.music6;
            case "SadMusic7":
                return MenuOptions3.music7;
            case "SadMusic8":
                return MenuOptions3.music8;
            case "SadMusic9":
                return MenuOptions3.music9;
            case "SadMusic10":
                return MenuOptions3.music10;
            case "SadMusic11":
                return MenuOptions4.music1;
            case "SadMusic12":
                return MenuOptions4.music2;
            case "SadMusic13":
                return MenuOptions4.music3;
            case "SadMusic14":
                return MenuOptions4.music4;
            case "SadMusic15":
                return MenuOptions4.music5;
            case "SadMusic16":
                return MenuOptions4.music6;
            case "SadMusic17":
                return MenuOptions4.music7;
            case "SadMusic18":
                return MenuOptions4.music8;
            case "SadMusic19":
                return MenuOptions4.music9;
            case "SadMusic20":
                return MenuOptions4.music10;
            case "SadMusic21":
                return MenuOptions5.music1;
            case "SadMusic22":
                return MenuOptions5.music2;
            case "SadMusic23":
                return MenuOptions5.music3;
            case "SadMusic24":
                return MenuOptions5.music4;
            case "SadMusic25":
                return MenuOptions5.music5;
            case "SadMusic26":
                return MenuOptions5.music6;
            case "SadMusic27":
                return MenuOptions5.music7;
            case "SadMusic28":
                return MenuOptions5.music8;
            case "WinMusic":
                return MenuOptions6.music1;
            case "SuspenseMusic1":
                return MenuOptions6.music2;
            case "VictoryMusic1":
                return MenuOptions6.music3;
            default:
                throw new IllegalArgumentException("Unknown music: "+music);
        }
    }
    public static synchronized void stopSounds(String source){
        for(int i = 1; i<=20; i++){
            if(isPlaying(source, i)){
                AL10.alSourceStop(SoundStash.getSource(source+i));
            }
        }
    }
    public static synchronized void stopSound(String source, int channel){
        if(isPlaying(source, channel)){
            AL10.alSourceStop(SoundStash.getSource(source+channel));
        }
    }
    public static synchronized void fadeSound(String source, String sound){
        if(source.equals("music")&&sound.equals(nowPlaying)) return;
        if(!isPlaying(source)){
            playSoundOneChannel(source, sound);
            return;
        }
        fadingSource = source;
        fading = sound;
    }
    public static synchronized void fadeSound(String source){
        if(!isPlaying(source)){
            return;
        }
        fadingSource = source;
    }
    public static synchronized void stopSound(String source){
        if(source.equals("music")){
            nowPlaying = null;
        }
        if(isPlaying(source)){
            AL10.alSourceStop(SoundStash.getSource(source));
        }
    }
    public static synchronized boolean isPlaying(String source){
        if(!running)return false;
        return AL10.alGetSourcei(SoundStash.getSource(source), AL10.AL_SOURCE_STATE)==AL10.AL_PLAYING;
    }
    public static synchronized boolean isPlaying(String source, int channel){
        if(!running)return false;
        return AL10.alGetSourcei(SoundStash.getSource(source+channel), AL10.AL_SOURCE_STATE)==AL10.AL_PLAYING;
    }
    private static File downloadFile(String link, File destinationFile){
        if(destinationFile.exists()||link==null){
            return destinationFile;
        }
        destinationFile.getParentFile().mkdirs();
        try {
            URL url = new URL(link);
            int fileSize;
            URLConnection connection = url.openConnection();
            connection.setDefaultUseCaches(false);
            if ((connection instanceof HttpURLConnection)) {
                ((HttpURLConnection)connection).setRequestMethod("HEAD");
                int code = ((HttpURLConnection)connection).getResponseCode();
                if (code / 100 == 3) {
                    return null;
                }
            }
            fileSize = connection.getContentLength();
            byte[] buffer = new byte[65535];
            int unsuccessfulAttempts = 0;
            int maxUnsuccessfulAttempts = 3;
            boolean downloadFile = true;
            while (downloadFile) {
                downloadFile = false;
                URLConnection urlconnection = url.openConnection();
                if ((urlconnection instanceof HttpURLConnection)) {
                    urlconnection.setRequestProperty("Cache-Control", "no-cache");
                    urlconnection.connect();
                }
                String targetFile = destinationFile.getName();
                FileOutputStream fos;
                int downloadedFileSize;
                try (InputStream inputstream=Main.getRemoteInputStream(targetFile, urlconnection)) {
                    fos=new FileOutputStream(destinationFile);
                    downloadedFileSize=0;
                    int read;
                    while ((read = inputstream.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                        downloadedFileSize += read;
                    }
                }
                fos.close();
                if (((urlconnection instanceof HttpURLConnection)) && 
                    ((downloadedFileSize != fileSize) && (fileSize > 0))){
                    unsuccessfulAttempts++;
                    if (unsuccessfulAttempts < maxUnsuccessfulAttempts){
                        downloadFile = true;
                    }else{
                        throw new Exception("failed to download "+targetFile);
                    }
                }
            }
            return destinationFile;
        }catch (Exception ex){
            return null;
        }
    }
    /**
     * Adds a song so it can be played. Songs are downloaded on sound system startup as MP3s and decompressed into .wav files.
     * @param name the name of the song, used when playing it.
     * @param songName The file name, without the extention
     * @param url the URL the song can be downloaded at, as an MP3
     */
    private static void addSong(String name, String songName, String url){
        soundNames.put(name, Main.getAppdataRoot()+"\\Music\\"+songName+".wav");
        songURLs.put(name, url);
    }
    /**
     * Adds a sound effect so it can be played. Sound effects are not downloaded. They are found in the jarfile under /sounds
     * All sounds should be .wav files
     * @param name the name of the song, used when playing it.
     * @param songName The file name, without the extention.
     */
    private static void addSound(String name, String songName){
        soundNames.put(name, "/sounds/"+songName+".wav");
    }
    public static void pauseMusic(){
        if(!paused)
            AL10.alSourcePause(SoundStash.getSource("music"));
        paused = true;
    }
    public static void unpauseMusic(){
        if(paused)
            AL10.alSourcePlay(SoundStash.getSource("music"));
        paused = false;
    }
    static boolean isPlayingMusic(){
        return isPlaying("music")||paused;
    }
    static void toggleMusic(){
        if(paused){
            unpauseMusic();
        }else{
            pauseMusic();
        }
    }
}