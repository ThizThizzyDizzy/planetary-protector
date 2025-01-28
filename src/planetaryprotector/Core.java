package planetaryprotector;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoadTextures;
//import simplelibrary.Sys;
//import simplelibrary.error.ErrorAdapter;
//import simplelibrary.error.ErrorCategory;
//import simplelibrary.font.FontManager;
//import simplelibrary.game.GameHelper;
//import simplelibrary.image.Color;
//import simplelibrary.opengl.Renderer2D;
//import simplelibrary.opengl.gui.GUI;
//import simplelibrary.texture.TexturePack;
//import simplelibrary.texture.TexturePackManager;
public class Core{// extends Renderer2D{
//    public static GUI gui;
//    public static GameHelper helper;
    public static ArrayList<Long> FPStracker = new ArrayList<>();
    public static final boolean is3D = false;
    public static final boolean enableCullFace = true;
    public static final int LEVELS = 2;
    @Deprecated
    public static int latestLevel = 1;//Options.options.level
    private static DiscordRichPresence discord;
    public static String discordState;
    public static String discordDetails;
    public static String discordLargeImageKey;
    public static String discordSmallImageKey;
    public static String discordLargeImageText;
    public static String discordSmallImageText;
    public static long discordEndTimestamp;
    private static int fpsLimit = 60;
    public static boolean updateAvailable;
//    private static Updater updater;
//    public static void main(String[] args) throws NoSuchMethodException{
//        helper = new GameHelper();
//        helper.setBackground(Color.GRAY);
//        helper.setDisplaySize(800, 600);
//        helper.setRenderInitMethod(Core.class.getDeclaredMethod("renderInit", new Class<?>[0]));
//        helper.setTickInitMethod(Core.class.getDeclaredMethod("tickInit", new Class<?>[0]));
//        helper.setFinalInitMethod(Core.class.getDeclaredMethod("finalInit", new Class<?>[0]));
//        helper.setRenderMethod(Core.class.getDeclaredMethod("render", int.class));
//        helper.setTickMethod(Core.class.getDeclaredMethod("tick", boolean.class));
//        helper.setWindowTitle(Main.applicationName+" "+VersionManager.currentVersion);
//        helper.setMode(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D);
//        helper.setAntiAliasing(4);
//        helper.setFrameOfView(90);
//        initDiscord();
//        Sys.initLWJGLGame(new File(Main.getAppdataRoot()+"/errors/"), new ErrorAdapter(){
//            private final Logger logger = Logger.getLogger(Core.class.getName());
//            @Override
//            public void log(String message, Throwable error, ErrorCategory category){
//                System.err.println(Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Log");
//                logger.log(Level.INFO, message, error);
//            }
//            @Override
//            public void warningError(String message, Throwable error, ErrorCategory category){
//                System.err.println(Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Warning");
//                logger.log(Level.WARNING, message, error);
//            }
//            @Override
//            public void minorError(String message, Throwable error, ErrorCategory category){
//                System.err.println("Minor "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
//                logger.log(Level.SEVERE, message, error);
//            }
//            @Override
//            public void moderateError(String message, Throwable error, ErrorCategory category){
//                System.err.println("Moderate "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
//                logger.log(Level.SEVERE, message, error);
//            }
//            @Override
//            public void severeError(String message, Throwable error, ErrorCategory category){
//                System.err.println("Severe "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
//                logger.log(Level.SEVERE, message, error);
//            }
//            @Override
//            public void criticalError(String message, Throwable error, ErrorCategory category){
//                System.err.println("Critical "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
//                logger.log(Level.SEVERE, message, error);
//                System.exit(1);
//            }
//        }, null, helper);
//    }
//    public static void renderInit(){
//        System.out.println("Loading fonts");
//        FontManager.addFont("/planetaryprotector/high resolution");
//        FontManager.addFont("/planetaryprotector/slim");
//        FontManager.setFont("high resolution");
//        System.out.println("Initializing render engine");
//        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
//        GL11.glEnable(GL13.GL_MULTISAMPLE);
//        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glEnable(GL11.GL_BLEND);
//        if(is3D){
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            if(enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
//        }
//        System.out.println("Creating texture pack manager");
//        new TexturePackManager(null, new TexturePack(){
//            @Override
//            public InputStream getResourceAsStream(String name){
//                if(name.startsWith("/")){
//                    return super.getResourceAsStream(name);
//                }
//                try{
//                    return new FileInputStream(new File(name));
//                }catch(FileNotFoundException ex){}
//                return super.getResourceAsStream(name);
//            }
//        });
//        System.out.println("Initializing GUI");
//        gui = new GUI(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D, helper);
//        gui.open(new MenuLoadTextures(gui));
//        System.out.println("Render initialization complete!");
//    }
//    public static void finalInit(){
//        System.out.println("Activating GUI");
//        helper.assignGUI(gui);
//        System.out.println("Initializing Sound System");
//        Sounds.init();
//        System.out.println("Startup complete!");
//        System.out.println("Checking for updates...");
//        updater = Updater.read("https://raw.githubusercontent.com/ThizThizzyDizzy/planetary-protector/master/versions.txt", VersionManager.currentVersion, Main.applicationName);
//        if(updater!=null&&updater.getVersionsBehindLatestDownloadable()>0){
//            updateAvailable = true;
//        }
//        System.out.println("Update Check Complete.");
//        Sounds.enableAutoplay();
//        Game.refreshTheme();
//        for(ParticleEffectType p : ParticleEffectType.values()){
//            for(int i = 0; i<p.frames; i++){
//                p.images[i] = "/textures/particles/"+p.texture+"/"+(i+1)+".png";
//            }
//        }
//    }
//    public static void tick(boolean isLastTick){
//        Sounds.tick(isLastTick);
//        if(!isLastTick){
//            for(int i = 0; i<speedMult; i++){
//                gui.tick();
//            }
//            if(discord!=null){
//                discord.state = discordState;
//                discord.details = discordDetails;
//                discord.largeImageKey = discordLargeImageKey;
//                discord.smallImageKey = discordSmallImageKey;
//                discord.largeImageText = discordLargeImageText;
//                discord.smallImageText = discordSmallImageText;
//                discord.endTimestamp = discordEndTimestamp;
//                DiscordRPC.INSTANCE.Discord_UpdatePresence(discord);
//            }
//        }else{
//            saveOptions();
//        }
//    }
//    public static void render(int millisSinceLastTick){
//        if(is3D&&enableCullFace) GL11.glDisable(GL11.GL_CULL_FACE);
//        if(gui.menu instanceof MenuGame){
//            ((MenuGame)gui.menu).renderWorld(millisSinceLastTick);
//        }
//        gui.render(millisSinceLastTick);
//        if(is3D&&enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
//        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_EQUAL)){
//            Sounds.setVolume(Sounds.getVolume()+.01f);
//        }
//        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_MINUS)){
//            Sounds.setVolume(Sounds.getVolume()-.01f);
//        }
//        FPStracker.add(System.currentTimeMillis());
//        while(FPStracker.get(0)<System.currentTimeMillis()-5_000){
//            FPStracker.remove(0);
//        }
//    }
//    public static long getFPS(){
//        return FPStracker.size()/5;
//    }
//    public static void update() throws URISyntaxException, IOException{
//        System.out.println("Updating...");
//        Main.startJava(new String[0], new String[]{"justUpdated"}, updater.update(updater.getLatestDownloadableVersion()));
//        System.exit(0);
//    }
//    public static boolean canPlayLevel(int i){
//        return debugMode||LEVELS>=i;
//    }
//    public static void drawRegularPolygon(double x, double y, double radius, int quality, int texture){
//        if(quality<3){
//            throw new IllegalArgumentException("A polygon must have at least 3 sides!");
//        }
//        ImageStash.instance.bindTexture(texture);
//        GL11.glBegin(GL11.GL_TRIANGLES);
//        double angle = 0;
//        for(int i = 0; i<quality; i++){
//            GL11.glVertex2d(x, y);
//            double X = x+Math.cos(Math.toRadians(angle-90))*radius;
//            double Y = y+Math.sin(Math.toRadians(angle-90))*radius;
//            GL11.glVertex2d(X, Y);
//            angle+=(360D/quality);
//            X = x+Math.cos(Math.toRadians(angle-90))*radius;
//            Y = y+Math.sin(Math.toRadians(angle-90))*radius;
//            GL11.glVertex2d(X, Y);
//        }
//        GL11.glEnd();
//    }
//    public static void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture){
//        drawOval(x, y, xRadius, yRadius, xThickness, yThickness, quality, texture, 0, quality-1);
//    }
//    public static void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture){
//        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, 0, quality-1);
//    }
//    public static void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture, int left, int right){
//        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, left, right);
//    }
//    public static void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture, int left, int right){
//        if(quality<3){
//            throw new IllegalArgumentException("Quality must be >=3!");
//        }
//        while(left<0)left+=quality;
//        while(right<0)right+=quality;
//        while(left>quality)left-=quality;
//        while(right>quality)right-=quality;
//        ImageStash.instance.bindTexture(texture);
//        GL11.glBegin(GL11.GL_QUADS);
//        double angle = 0;
//        for(int i = 0; i<quality; i++){
//            boolean inRange = false;
//            if(left>right)inRange = i>=left||i<=right;
//            else inRange = i>=left&&i<=right;
//            if(inRange){
//                double X = x+Math.cos(Math.toRadians(angle-90))*xRadius;
//                double Y = y+Math.sin(Math.toRadians(angle-90))*yRadius;
//                GL11.glVertex2d(X, Y);
//                X = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
//                Y = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
//                GL11.glVertex2d(X, Y);
//            }
//            angle+=(360D/quality);
//            if(inRange){
//                double X = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
//                double Y = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
//                GL11.glVertex2d(X, Y);
//                X = x+Math.cos(Math.toRadians(angle-90))*xRadius;
//                Y = y+Math.sin(Math.toRadians(angle-90))*yRadius;
//                GL11.glVertex2d(X, Y);
//            }
//        }
//        GL11.glEnd();
//    }
//    public static void winLevel(int i){
//        latestLevel = Math.min(LEVELS,Math.max(latestLevel, i+1));
//    }
//    /**
//     * @return the currently loaded game
//     * @deprecated ONLY USED FOR MUSIC
//     */
//    @Deprecated
//    public static Game getGame(){
//        if(gui.menu instanceof MenuGame)return ((MenuGame) gui.menu).game;
//        return null;
//    }
//    public static String drawCenteredTextWithWordWrap(double leftPossibleEdge, double topEdge, double rightPossibleEdge, double bottomEdge, String text){
//        String[] words = text.split(" ");
//        String str = words[0];
//        double height = bottomEdge-topEdge;
//        double length = rightPossibleEdge-leftPossibleEdge;
//        for(int i = 1; i<words.length; i++){
//            String string = str+" "+words[i];
//            if(Renderer.getStringWidth(string.trim(), height)>=length){
//                drawCenteredTextWithWrap(leftPossibleEdge, topEdge, rightPossibleEdge, bottomEdge, str.trim());
//                return text.replaceFirst("\\Q"+str, "").trim();
//            }else{
//                str = string;
//            }
//        }
//        return drawCenteredTextWithWrap(leftPossibleEdge, topEdge, rightPossibleEdge, bottomEdge, text);
//    }
//    public static String drawTextWithWordWrap(double leftEdge, double topEdge, double rightPossibleEdge, double bottomEdge, String text){
//        String[] words = text.split(" ");
//        String str = words[0];
//        double height = bottomEdge-topEdge;
//        double length = rightPossibleEdge-leftEdge;
//        for(int i = 1; i<words.length; i++){
//            String string = str+" "+words[i];
//            if(Renderer.getStringWidth(string.trim(), height)>=length){
//                drawTextWithWrap(leftEdge, topEdge, rightPossibleEdge, bottomEdge, str.trim());
//                return text.replaceFirst("\\Q"+str, "").trim();
//            }else{
//                str = string;
//            }
//        }
//        return drawTextWithWrap(leftEdge, topEdge, rightPossibleEdge, bottomEdge, text);
//    }
    public static boolean debugMode;
}