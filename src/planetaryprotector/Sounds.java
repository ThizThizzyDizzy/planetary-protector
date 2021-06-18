package planetaryprotector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuLost;
import simplelibrary.openal.Autoplayer;
import simplelibrary.openal.SoundChannel;
import simplelibrary.openal.SoundSystem;
public class Sounds{
    private static SoundSystem soundSystem;
    private static SoundChannel musicChannel;
    private static Autoplayer autoplayer = new Autoplayer() {
            @Override
            public Object next(){
                ArrayList<String> strs = new ArrayList<>();
                getPlayableMusic(strs);
                if(!strs.isEmpty()){
                    String sound = strs.get(new Random().nextInt(strs.size()));
                    return soundSystem.getSong(soundNames.get(sound));
                }
                return null;
            }
            @Override
            public float getVolume(){
                return vol;
            }
        };
    public static boolean autoplay = true;
    private static void addMusic(){
        addSong("EndMusic1", "Killers", "https://www.dropbox.com/s/e2p5uwdyzznked0/Killers.mp3?dl=1", 9541);
        addSong("Music1", "Killers", "https://www.dropbox.com/s/e2p5uwdyzznked0/Killers.mp3?dl=1", 9541);
//        addSong("Music2", "Clenched Teeth", "https://www.dropbox.com/s/jzstxq3404lai4q/Clenched%20Teeth.mp3?dl=1", 3563);
        addSong("Music3", "Achilles", "https://www.dropbox.com/s/agtrfb81kmix3f2/Achilles.mp3?dl=1", 2482);
        addSong("Music4", "Noble Race", "https://www.dropbox.com/s/jmqmcttfx9in4a5/Noble%20Race.mp3?dl=1", 10399);
        addSong("Music5", "Rynos Theme", "https://www.dropbox.com/s/uodl6uu825hik38/Rynos%20Theme.mp3?dl=1", 5805);
        addSong("Boss1Music1", "Eternal Terminal", "https://www.dropbox.com/s/rd2yfsma9994rvn/Eternal%20Terminal.mp3?dl=1", 6175);
        addSong("Boss1Music2", "Five Armies", "https://www.dropbox.com/s/n2nnl7o3qp6pfmu/Five%20Armies.mp3?dl=1", 6090);
        addSong("Boss2Music1", "Corruption", "https://www.dropbox.com/s/0wjbtmx2ya5t7f6/Corruption.mp3?dl=1", 13223);
        addSong("Boss2Music2", "Clash Defiant", "https://www.dropbox.com/s/6p6ulhfp58jty24/Clash%20Defiant.mp3?dl=1", 11740);
//        addSong("Boss3Music1", "Metalmania", "https://www.dropbox.com/s/p6e99sa8z9kdckp/Metalmania.mp3?dl=1", 5955);
        addSong("Boss3Music2", "Obliteration", "https://www.dropbox.com/s/2685hexub0a1tm0/Obliteration.mp3?dl=1", 4672);
        addSong("Boss4Music1", "Death and Axes", "https://www.dropbox.com/s/uk9m4xnlcm5mqao/Death%20and%20Axes.mp3?dl=1", 5255);
        addSong("Boss4Music2", "Grim Idol", "https://www.dropbox.com/s/606kbzltk24v307/Grim%20Idol.mp3?dl=1", 7030);
        addSong("SadMusic1", "A Turn for the Worse", "https://www.dropbox.com/s/7xllni5jw99d7j1/A%20Turn%20for%20the%20Worse.mp3?dl=1", 2658);
        addSong("SadMusic2", "A Little Faith", "https://www.dropbox.com/s/h2ygg4q05boayfk/A%20Little%20Faith.mp3?dl=1", 3539);
        addSong("SadMusic3", "At Rest");
        addSong("SadMusic4", "Awaiting Return", "https://www.dropbox.com/s/xcq6wpxqseoxy0b/Awaiting%20Return.mp3?dl=1", 3460);
        addSong("SadMusic5", "Bittersweet", "https://www.dropbox.com/s/nsu2dzc5ua73wb4/Bittersweet.mp3?dl=1", 6317);
        addSong("SadMusic6", "Colorless Aura", "https://www.dropbox.com/s/i9w7nmaicjkitse/Colorless%20Aura.mp3?dl=1", 6177);
        addSong("SadMusic7", "Cryptic Sorrow", "https://www.dropbox.com/s/mbpq6q7x8qdmmed/Cryptic%20Sorrow.mp3?dl=1", 18907);
        addSong("SadMusic8", "Dark Times", "https://www.dropbox.com/s/1kyom747avrm8le/Dark%20Times.mp3?dl=1", 7178);
        addSong("SadMusic9", "Dark Walk", "https://www.dropbox.com/s/vthmioptxl13khf/Dark%20Walk.mp3?dl=1", 3456);
        addSong("SadMusic10", "Despair and Triumph", "https://www.dropbox.com/s/alsswa1pi426qo7/Despair%20and%20Triumph.mp3?dl=1", 11164);
        addSong("SadMusic11", "Disquiet", "https://www.dropbox.com/s/7t7s0rnwumi07fx/Disquiet.mp3?dl=1", 5786);
        addSong("SadMusic12", "End of the Era", "https://www.dropbox.com/s/tr5e2jk74fk1fbd/End%20of%20the%20Era.mp3?dl=1", 8352);
        addSong("SadMusic13", "Heartbreaking", "https://www.dropbox.com/s/vaejmyxhsq9ryok/Heartbreaking.mp3?dl=1", 3855);
        addSong("SadMusic14", "Heavy Heart", "https://www.dropbox.com/s/l4q6j6rir5hkws8/Heavy%20Heart.mp3?dl=1", 9275);
        addSong("SadMusic15", "Immersed", "https://www.dropbox.com/s/puoderxzd0odn9x/Immersed.mp3?dl=1", 7783);
        addSong("SadMusic16", "Lasting Hope", "https://www.dropbox.com/s/fwkeuf273bh43sf/Lasting%20Hope.mp3?dl=1", 5739);
        addSong("SadMusic17", "Lone Harvest", "https://www.dropbox.com/s/b54lp4mnls8yzj2/Lone%20Harvest.mp3?dl=1", 2444);
        addSong("SadMusic18", "Lost Frontier", "https://www.dropbox.com/s/5jbvcsicfd4nxh8/Lost%20Frontier.mp3?dl=1", 10352);
        addSong("SadMusic19", "Memory Lane", "https://www.dropbox.com/s/grtc76gfuh0n75s/Memory%20Lane.mp3?dl=1", 4447);
        addSong("SadMusic20", "On the Passing of Time", "https://www.dropbox.com/s/8zmq188r0flij3v/On%20the%20Passing%20of%20Time.mp3?dl=1", 8919);
        addSong("SadMusic21", "Rains Will Fall", "https://www.dropbox.com/s/ox2prgtrwh1xz2p/Rains%20Will%20Fall.mp3?dl=1", 8690);
        addSong("SadMusic22", "Reaching Out", "https://www.dropbox.com/s/qtyrk790g3xd7kq/Reaching%20Out.mp3?dl=1", 2486);
        addSong("SadMusic23", "Sad Trio", "https://www.dropbox.com/s/bsp72cdunr4ktep/Sad%20Trio.mp3?dl=1", 9960);
        addSong("SadMusic24", "Stages of Grief", "https://www.dropbox.com/s/fmwiflng3dk4bz1/Stages%20of%20Grief.mp3?dl=1", 4323);
        addSong("SadMusic25", "The Parting", "https://www.dropbox.com/s/vt6d11fdp4dh0gq/The%20Parting.mp3?dl=1", 7187);
        addSong("SadMusic26", "Time Passes", "https://www.dropbox.com/s/3az3otjjqh8u0ij/Time%20Passes.mp3?dl=1", 2867);
        addSong("SadMusic27", "When the Wind Blows", "https://www.dropbox.com/s/okofwwbz9ks5xlp/When%20The%20Wind%20Blows.mp3?dl=1", 10396);
        addSong("SadMusic28", "Wounded", "https://www.dropbox.com/s/dmn6nfoetoiyko6/Wounded.mp3?dl=1", 7945);
        addSong("WinMusic", "Americana", "https://www.dropbox.com/s/v2q3gcp17uync61/Americana.mp3?dl=1", 7902);
        addSong("SuspenseMusic1", "Final Battle of the Dark Wizards", "https://www.dropbox.com/s/0yccf5vyouhiubn/Final%20Battle%20of%20the%20Dark%20Wizards.mp3?dl=1", 8491);
        addSong("VictoryMusic1", "Jet Fueled Vixen");
        addSong("MysteryMusic1", "Almost New", "https://www.dropbox.com/s/8395bxrhjy27024/Almost%20New.mp3?dl=1", 6332);
        addSong("MysteryMusic2", "Comfortable Mystery", "https://www.dropbox.com/s/zxen3zdteuho6i5/Comfortable%20Mystery.mp3?dl=1", 7626);//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic3", "Comfortable Mystery 2", "https://www.dropbox.com/s/i72a2bevg5ea5eo/Comfortable%20Mystery%202.mp3?dl=1", 2195);//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic4", "Comfortable Mystery 3", "https://www.dropbox.com/s/q3c1j4c9a6tvrnf/Comfortable%20Mystery%203.mp3?dl=1", 2690);//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic5", "Comfortable Mystery 4", "https://www.dropbox.com/s/qt8quuz4bcvdt4j/Comfortable%20Mystery%204.mp3?dl=1", 3052);//shooting star hits the ground, and fades after the song is over. if you collect it, you unlock the Observatory, which lets you go stargazing, and some other stuff too
        addSong("MysteryMusic6", "Constancy Part Three", "https://www.dropbox.com/s/3184fyu6skqcisg/Constancy%20Part%20Three.mp3?dl=1", 2493);
        addSong("MysteryMusic7", "Earth Prelude", "https://www.dropbox.com/s/cbu9ahxwhj8kfc9/Earth%20Prelude.mp3?dl=1", 4573);
        addSong("MysteryMusic8", "Floating Cities", "https://www.dropbox.com/s/r4j7sjwz7p8tf2l/Floating%20Cities.mp3?dl=1", 5755);
        addSong("MysteryMusic9", "Frost Waltz (Alternate", "https://www.dropbox.com/s/0ocb8t5gz9xkc67/Frost%20Waltz%20%28Alternate%29.mp3?dl=1", 5421);
        addSong("MysteryMusic10", "Frost Waltz", "https://www.dropbox.com/s/rzpmbn5j231zmi0/Frost%20Waltz.mp3?dl=1", 5398);
        addSong("MysteryMusic11", "The Chamber", "https://www.dropbox.com/s/ede0uy4mgcxwhbi/The%20Chamber.mp3?dl=1", 4551);
        addSong("MysteryMusic12", "The Other Side of the Door", "https://www.dropbox.com/s/u96wzzbwa59k5ap/The%20Other%20Side%20of%20the%20Door.mp3?dl=1", 6558);
        addSong("MysteryMusic13", "The Snow Queen", "https://www.dropbox.com/s/vnku5ywj9x512ru/The%20Snow%20Queen.mp3?dl=1", 8600);
    }
    /**
     * Gets a list of all currently playable music.
     * @return a list of sound names that can be played as music at the current time (One will be randomly chosen)
     */
    private static void getPlayableMusic(ArrayList<String> playableMusic){
        Game game = Core.getGame();
        if(Core.gui.menu instanceof MenuLost){
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
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership) e).phase);
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
    private static boolean running = false;
    public static HashMap<String, String> soundNames = new HashMap<>();
    public static HashMap<String, Integer> songSizes = new HashMap<>();
    public static HashMap<String, String> songURLs = new HashMap<>();
    public static ArrayList<String> downloadOrder = new ArrayList<>();
    private static float vol = 1f;
    /**
     * Disables the sound system.
     * Equivalent to AL.destroy();
     */
    public static void destroy(){
        soundSystem.destroy();
        running = false;
    }
    /**
     * Starts the sound system, music thread, and music downloading thread.
     */
    public static void create(){
        soundNames.clear();
        songURLs.clear();
        downloadOrder.clear();
        addMusic();
        soundSystem = new SoundSystem(10, "/sounds/", ".wav", "music");
        musicChannel = soundSystem.getChannel("music");
        running = true;
        Thread musicDownloader = new Thread(() -> {
            int downloadSize = 0;
            for(String key : downloadOrder){
                File file = new File(soundNames.get(key).replace(".wav", ".mp3"));
                if(!file.exists()){
                    downloadSize+=songSizes.get(key);
                }
            }
            if(downloadSize<=0){
                return;
            }
            System.out.println("Starting Music Download...");
            for(String key : downloadOrder){
                if(!running){
                    return;
                }
                String filepath = soundNames.get(key);
                String url = songURLs.get(key);
                File from = new File(filepath);
                if(!from.exists()){
                    System.out.println("Downloading Song "+key+"...");
                    downloadFile(url, from);
                    System.out.println("Song Downloaded: "+key+"...");
                }
            }
            System.out.println("All songs are up to date.");
        });
        musicDownloader.setName("Music Downloader");
        musicDownloader.start();
    }
    static void tick(boolean lastTick){
        if(!running)return;
        if(lastTick){
            destroy();
            return;
        }
        if(musicChannel.isStopped()){
            enableAutoplay();
        }
    }
    public static void setVolume(float volume){
        vol = Math.max(0,Math.min(1,volume));
        soundSystem.setMasterVolume(Math.max(0.0001f,vol));
    }
    /**
     * @return song time in TICKS
     */
    public static int songTimer(){
        return musicChannel.getPlayheadPosition()/50;
    }
    public static synchronized void playSound(String source, String sound){
        if(source.equals("music")){
            soundSystem.getChannel(source).fadeTo(120, soundSystem.getSong(soundNames.get(sound)));
            enableAutoplay();
        }
        soundSystem.getChannel(source).play(soundNames.get(sound));
    }
    @Deprecated
    public static synchronized void playSoundOneChannel(String source, String sound){
        playSound(source, sound);
    }
    @Deprecated
    public static synchronized void stopSounds(String source){
        stopSound(source);
    }
    @Deprecated
    public static synchronized void stopSound(String source, int channel){
        stopSound(source);
    }
    public static synchronized void fadeSound(String source, String sound){
        soundSystem.getChannel(source).fadeTo(60, soundSystem.getSong(soundNames.get(sound)));
    }
    public static synchronized void fadeSound(String source){
        soundSystem.getChannel(source).fadeSkip(60);
    }
    public static synchronized void stopSound(String source){
        soundSystem.getChannel(source).stop();
    }
    public static synchronized boolean isPlaying(String source){
        return soundSystem.getChannel(source).isPlaying();
    }
    @Deprecated
    public static synchronized boolean isPlaying(String source, int channel){
        return isPlaying(source);
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
    private static void addSong(String name, String songName, String url, int kb){
        soundNames.put(name, Main.getAppdataRoot()+"\\Music\\"+songName+".mp3");
        songURLs.put(name, url);
        downloadOrder.add(name);
        songSizes.put(name, kb);
    }
    private static void addSong(String name, String songName){
        soundNames.put(name, "/sounds/music/"+songName+".mp3");
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
    static boolean isPlayingMusic(){
        return musicChannel.isPlaying();
    }
    static void toggleMusic(){
        if(musicChannel.isPaused()){
            musicChannel.play();
        }else{
            musicChannel.pause();
        }
    }
    public static void enableAutoplay(){
        musicChannel.autoplay(autoplayer);
}
    public static void disableAutoplay(){
        musicChannel.stop();
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
}