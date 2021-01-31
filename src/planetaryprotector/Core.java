package planetaryprotector;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import planetaryprotector.game.Game;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.particle.ParticleEffectType;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.GLFW;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.WorldGenerator;
import planetaryprotector.game.Story;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.MenuLoadTextures;
import planetaryprotector.menu.options.MenuOptions;
import planetaryprotector.menu.options.MenuOptionsDiscord;
import planetaryprotector.structure.Structure.Upgrade;
import planetaryprotector.structure.StructureType;
import simplelibrary.Sys;
import simplelibrary.config2.Config;
import simplelibrary.error.ErrorAdapter;
import simplelibrary.error.ErrorCategory;
import simplelibrary.font.FontManager;
import simplelibrary.game.GameHelper;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.texture.TexturePack;
import simplelibrary.texture.TexturePackManager;
public class Core extends Renderer2D{
    public static GUI gui;
    public static GameHelper helper;
    public static ArrayList<Long> FPStracker = new ArrayList<>();
    public static boolean debugMode = false;
    public static final boolean is3D = false;
    public static boolean enableCullFace = true;
    public static final int LEVELS = 2;
    public static int latestLevel = 1;
    public static int speedMult = 1;
    private static DiscordRichPresence discord;
    public static String discordState;
    public static String discordDetails;
    public static String discordLargeImageKey;
    public static String discordSmallImageKey;
    public static String discordLargeImageText;
    public static String discordSmallImageText;
    public static long discordEndTimestamp;
    private static int fpsLimit = 60;
    public static void main(String[] args) throws NoSuchMethodException{
        helper = new GameHelper();
        helper.setBackground(Color.gray);
        helper.setDisplaySize(800, 600);
        helper.setRenderInitMethod(Core.class.getDeclaredMethod("renderInit", new Class<?>[0]));
        helper.setTickInitMethod(Core.class.getDeclaredMethod("tickInit", new Class<?>[0]));
        helper.setFinalInitMethod(Core.class.getDeclaredMethod("finalInit", new Class<?>[0]));
        helper.setRenderMethod(Core.class.getDeclaredMethod("render", int.class));
        helper.setTickMethod(Core.class.getDeclaredMethod("tick", boolean.class));
        helper.setWindowTitle(Main.applicationName+" "+VersionManager.currentVersion);
        helper.setMode(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D);
        helper.setFrameOfView(90);
        initDiscord();
        Sys.initLWJGLGame(new File(Main.getAppdataRoot()+"/errors/"), new ErrorAdapter(){
            private final Logger logger = Logger.getLogger(Core.class.getName());
            @Override
            public void log(String message, Throwable error, ErrorCategory category){
                System.err.println(Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Log");
                logger.log(Level.INFO, message, error);
            }
            @Override
            public void warningError(String message, Throwable error, ErrorCategory category){
                System.err.println(Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Warning");
                logger.log(Level.WARNING, message, error);
            }
            @Override
            public void minorError(String message, Throwable error, ErrorCategory category){
                System.err.println("Minor "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
                logger.log(Level.SEVERE, message, error);
                if(Main.hasAWT){
                    String details = "";
                    Throwable t = error;
                    while(t!=null){
                        details+=t.getClass().getName()+" "+t.getMessage();
                        StackTraceElement[] stackTrace = t.getStackTrace();
                        for(StackTraceElement e : stackTrace){
                            if(e.getClassName().startsWith("net."))continue;
                            if(e.getClassName().startsWith("com."))continue;
                            String[] splitClassName = e.getClassName().split("\\Q.");
                            String filename = splitClassName[splitClassName.length-1]+".java";
                            String nextLine = "\nat "+e.getClassName()+"."+e.getMethodName()+"("+filename+":"+e.getLineNumber()+")";
                            if((details+nextLine).length()+4>1024){
                                details+="\n...";
                                break;
                            }else details+=nextLine;
                        }
                        t = t.getCause();
                        if(t!=null)details+="\nCaused by ";
                    }
                    String[] options = new String[]{"Ignore", "Exit"};
                    switch(javax.swing.JOptionPane.showOptionDialog(null, details, "Minor "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error: "+message, javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
                        case 1:
                            helper.running = false;
                            break;
                        case 0:
                        default:
                            break;
                    }
                }
            }
            @Override
            public void moderateError(String message, Throwable error, ErrorCategory category){
                System.err.println("Moderate "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
                logger.log(Level.SEVERE, message, error);
                if(Main.hasAWT){
                    String details = "";
                    Throwable t = error;
                    while(t!=null){
                        details+=t.getClass().getName()+" "+t.getMessage();
                        StackTraceElement[] stackTrace = t.getStackTrace();
                        for(StackTraceElement e : stackTrace){
                            if(e.getClassName().startsWith("net."))continue;
                            if(e.getClassName().startsWith("com."))continue;
                            String[] splitClassName = e.getClassName().split("\\Q.");
                            String filename = splitClassName[splitClassName.length-1]+".java";
                            String nextLine = "\nat "+e.getClassName()+"."+e.getMethodName()+"("+filename+":"+e.getLineNumber()+")";
                            if((details+nextLine).length()+4>1024){
                                details+="\n...";
                                break;
                            }else details+=nextLine;
                        }
                        t = t.getCause();
                        if(t!=null)details+="\nCaused by ";
                    }
                    String[] options = new String[]{"Ignore", "Exit"};
                    switch(javax.swing.JOptionPane.showOptionDialog(null, details, "Moderate "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error: "+message, javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
                        case 1:
                            helper.running = false;
                            break;
                        case 0:
                        default:
                            break;
                    }
                }
            }
            @Override
            public void severeError(String message, Throwable error, ErrorCategory category){
                System.err.println("Severe "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
                logger.log(Level.SEVERE, message, error);
                if(Main.hasAWT){
                    String details = "";
                    Throwable t = error;
                    while(t!=null){
                        details+=t.getClass().getName()+" "+t.getMessage();
                        StackTraceElement[] stackTrace = t.getStackTrace();
                        for(StackTraceElement e : stackTrace){
                            if(e.getClassName().startsWith("net."))continue;
                            if(e.getClassName().startsWith("com."))continue;
                            String[] splitClassName = e.getClassName().split("\\Q.");
                            String filename = splitClassName[splitClassName.length-1]+".java";
                            String nextLine = "\nat "+e.getClassName()+"."+e.getMethodName()+"("+filename+":"+e.getLineNumber()+")";
                            if((details+nextLine).length()+4>1024){
                                details+="\n...";
                                break;
                            }else details+=nextLine;
                        }
                        t = t.getCause();
                        if(t!=null)details+="\nCaused by ";
                    }
                    String[] options = new String[]{"Ignore", "Exit"};
                    switch(javax.swing.JOptionPane.showOptionDialog(null, details, "Severe "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error: "+message, javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
                        case 1:
                            helper.running = false;
                            break;
                        case 0:
                        default:
                            break;
                    }
                }
            }
            @Override
            public void criticalError(String message, Throwable error, ErrorCategory category){
                System.err.println("Critical "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error");
                logger.log(Level.SEVERE, message, error);
                if(Main.hasAWT){
                    String details = "";
                    Throwable t = error;
                    while(t!=null){
                        details+=t.getClass().getName()+" "+t.getMessage();
                        StackTraceElement[] stackTrace = t.getStackTrace();
                        for(StackTraceElement e : stackTrace){
                            if(e.getClassName().startsWith("net."))continue;
                            if(e.getClassName().startsWith("com."))continue;
                            String[] splitClassName = e.getClassName().split("\\Q.");
                            String filename = splitClassName[splitClassName.length-1]+".java";
                            String nextLine = "\nat "+e.getClassName()+"."+e.getMethodName()+"("+filename+":"+e.getLineNumber()+")";
                            if((details+nextLine).length()+4>1024){
                                details+="\n...";
                                break;
                            }else details+=nextLine;
                        }
                        t = t.getCause();
                        if(t!=null)details+="\nCaused by ";
                    }
                    String[] options = new String[]{"Exit"};
                    switch(javax.swing.JOptionPane.showOptionDialog(null, details, "Critical "+Character.toUpperCase(category.toString().charAt(0))+category.toString().substring(1)+" Error: "+message, javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
                        case 0:
                        default:
                            helper.running = false;
                            break;
                    }
                }
            }
        }, null, helper);
    }
    public static void renderInit(){
        System.out.println("Loading fonts");
        FontManager.addFont("/planetaryprotector/high resolution");
        FontManager.addFont("/planetaryprotector/slim");
        FontManager.setFont("high resolution");
        System.out.println("Initializing render engine");
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        if(is3D){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if(enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        }
        System.out.println("Creating texture pack manager");
        new TexturePackManager(null, new TexturePack(){
            @Override
            public InputStream getResourceAsStream(String name){
                if(name.startsWith("/")){
                    return super.getResourceAsStream(name);
                }
                try{
                    return new FileInputStream(new File(name));
                }catch(FileNotFoundException ex){}
                return super.getResourceAsStream(name);
            }
        });
        System.out.println("Initializing GUI");
        gui = new GUI(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D, helper);
        gui.open(new MenuLoadTextures(gui));
        System.out.println("Render initialization complete!");
    }
    public static void tickInit(){
        loadOptions();
        for(Upgrade upgrade : Upgrade.values());//initialize upgrades to add them to building upgrade lists
        for(StructureType type : StructureType.structureTypes){
            System.out.println("Loaded structure "+type.name+". Max level: "+type.getMaxLevel());
        }
    }
    public static void finalInit(){
        System.out.println("Activating GUI");
        helper.assignGUI(gui);
        System.out.println("Initializing Sound System");
        Sounds.create();
        Sounds.enableAutoplay();
        Game.refreshTheme();
        for(AsteroidMaterial m : AsteroidMaterial.values()){
            for(int i = 0; i<10; i++){
                m.images[i] = "/textures/asteroids/"+m.texture+"/"+(i+1)+".png";
                if(i<9) {
                    m.images[i+10] = "/textures/asteroids/crash/"+(i+1)+".png";
                }
            }
        }
        for(ParticleEffectType p : ParticleEffectType.values()){
            for(int i = 0; i<p.frames; i++){
                p.images[i] = "/textures/particles/"+p.texture+"/"+(i+1)+".png";
            }
        }
    }
    public static void tick(boolean isLastTick){
        Sounds.tick(isLastTick);
        if(!isLastTick){
            for(int i = 0; i<speedMult; i++){
                gui.tick();
            }
            if(discord!=null){
                discord.state = discordState;
                discord.details = discordDetails;
                discord.largeImageKey = discordLargeImageKey;
                discord.smallImageKey = discordSmallImageKey;
                discord.largeImageText = discordLargeImageText;
                discord.smallImageText = discordSmallImageText;
                discord.endTimestamp = discordEndTimestamp;
                DiscordRPC.INSTANCE.Discord_UpdatePresence(discord);
            }
        }else{
            saveOptions();
        }
    }
    public static void render(int millisSinceLastTick){
        if(is3D&&enableCullFace) GL11.glDisable(GL11.GL_CULL_FACE);
        if(gui.menu instanceof MenuGame){
            ((MenuGame)gui.menu).renderWorld(millisSinceLastTick);
        }
        gui.render(millisSinceLastTick);
        if(is3D&&enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_EQUAL)){
            Sounds.setVolume(Sounds.getVolume()+.01f);
        }
        if(gui.keyboardWereDown.contains(GLFW.GLFW_KEY_MINUS)){
            Sounds.setVolume(Sounds.getVolume()-.01f);
        }
        FPStracker.add(System.currentTimeMillis());
        while(FPStracker.get(0)<System.currentTimeMillis()-5_000){
            FPStracker.remove(0);
        }
    }
    public static void saveOptions(){
        Config config = Config.newConfig(Main.getAppdataRoot()+"\\options.cfg");
        config.set("level", latestLevel);
        config.set("fog", MenuOptionsGraphics.fog);
        config.set("clouds", MenuOptionsGraphics.clouds);
        config.set("particle meteors", MenuOptionsGraphics.particulateMeteors);
        config.set("particles", MenuOptionsGraphics.particles);
        config.set("cloudI", MenuOptionsGraphics.cloudIntensity);
        config.set("fogI", MenuOptionsGraphics.fogIntensity);
        config.set("theme", MenuOptionsGraphics.theme);
        config.set("autosave", MenuOptions.autosave);
        config.set("health", MenuOptionsGraphics.health);
        config.save();
    }
    public static void loadOptions(){
        Config config = Config.newConfig(Main.getAppdataRoot()+"\\options.cfg");
        config.load();
        latestLevel = config.get("level", 1);
        if(latestLevel==0)latestLevel = 1;
        MenuOptionsGraphics.fog = config.get("fog", true);
        MenuOptionsGraphics.clouds = config.get("clouds", true);
        MenuOptionsGraphics.particulateMeteors = config.get("particle meteors", false);
        MenuOptionsGraphics.particles = config.get("particles", 1);
        MenuOptionsGraphics.cloudIntensity = config.get("cloudI", MenuOptionsGraphics.cloudIntensity);
        MenuOptionsGraphics.fogIntensity = config.get("fogI", MenuOptionsGraphics.fogIntensity);
        MenuOptionsGraphics.theme = config.get("theme", MenuOptionsGraphics.theme);
        MenuOptions.autosave = config.get("autosave", MenuOptions.autosave);
        MenuOptionsGraphics.health = config.get("health", MenuOptionsGraphics.health);
    }
    public static long getFPS(){
        return FPStracker.size()/5;
    }
    public static double distance(GameObject o1, GameObject o2){
        return Math.sqrt(Math.pow((o1.x+o1.width/2)-(o2.x+o2.width/2), 2)+Math.pow((o1.y+o1.height/2)-(o2.y+o2.height/2), 2));
    }
    public static double distance(GameObject object, double x, double y) {
        return Math.sqrt(Math.pow((object.x+object.width/2)-x, 2)+Math.pow((object.y+object.height/2)-y, 2));
    }
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
    }
    public static boolean isPointWithinComponent(double x, double y, MenuComponent component){
        return isClickWithinBounds(x, y, component.x, component.y, component.x+component.width, component.y+component.height);
    }
    public static double getValueBetweenTwoValues(double pos1, double val1, double pos2, double val2, double pos){
        if(pos1>pos2){
            return getValueBetweenTwoValues(pos2, val2, pos1, val1, pos);
        }
        double posDiff = pos2-pos1;
        double percent = pos/posDiff;
        double valDiff = val2-val1;
        return percent*valDiff+val1;
    }
    public static boolean canPlayLevel(int i){
        return debugMode||LEVELS>=i;
    }
    private static boolean rpc = false;
    public static void initDiscord() {
        if(Main.os!=Main.OS_WINDOWS)return;
        if(MenuOptionsDiscord.rpc){
            rpc = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
    //        handlers.ready = new DiscordEventHandlers.OnReady() {
    //            @Override
    //            public void accept(DiscordUser user) {
    //                System.out.println("Discord RPC Ready!");
    //            }
    //        };
            DiscordRPC.INSTANCE.Discord_Initialize(Main.discordAppID, handlers, true, null);
            discord = new DiscordRichPresence();
            discord.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            discord.largeImageKey = "city";
            discord.largeImageText = "Starting up...";
            discord.details = "Starting up...";
            DiscordRPC.INSTANCE.Discord_UpdatePresence(discord);
            // in a worker thread
            new Thread(() -> {
                while (rpc&&helper.running&&!Thread.currentThread().isInterrupted()) {
                    DiscordRPC.INSTANCE.Discord_RunCallbacks();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }, "RPC-Callback-Handler").start();
        }else{
            if(rpc){
                rpc = false;
                DiscordRPC.INSTANCE.Discord_Shutdown();
            }
        }
    }
    /*
        extra draw methods Borrowed from simplelibrary extended
    */
    public static void drawRegularPolygon(double x, double y, double radius, int quality, int texture){
        if(quality<3){
            throw new IllegalArgumentException("A polygon must have at least 3 sides!");
        }
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_TRIANGLES);
        double angle = 0;
        for(int i = 0; i<quality; i++){
            GL11.glVertex2d(x, y);
            double X = x+Math.cos(Math.toRadians(angle-90))*radius;
            double Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
            angle+=(360D/quality);
            X = x+Math.cos(Math.toRadians(angle-90))*radius;
            Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
    }
    public static void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture){
        drawOval(x, y, xRadius, yRadius, xThickness, yThickness, quality, texture, 0, quality-1);
    }
    public static void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture){
        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, 0, quality-1);
    }
    public static void drawOval(double x, double y, double xRadius, double yRadius, double thickness, int quality, int texture, int left, int right){
        drawOval(x, y, xRadius, yRadius, thickness, thickness, quality, texture, left, right);
    }
    public static void drawOval(double x, double y, double xRadius, double yRadius, double xThickness, double yThickness, int quality, int texture, int left, int right){
        if(quality<3){
            throw new IllegalArgumentException("Quality must be >=3!");
        }
        while(left<0)left+=quality;
        while(right<0)right+=quality;
        while(left>quality)left-=quality;
        while(right>quality)right-=quality;
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_QUADS);
        double angle = 0;
        for(int i = 0; i<quality; i++){
            boolean inRange = false;
            if(left>right)inRange = i>=left||i<=right;
            else inRange = i>=left&&i<=right;
            if(inRange){
                double X = x+Math.cos(Math.toRadians(angle-90))*xRadius;
                double Y = y+Math.sin(Math.toRadians(angle-90))*yRadius;
                GL11.glVertex2d(X, Y);
                X = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
                Y = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
                GL11.glVertex2d(X, Y);
            }
            angle+=(360D/quality);
            if(inRange){
                double X = x+Math.cos(Math.toRadians(angle-90))*(xRadius-xThickness);
                double Y = y+Math.sin(Math.toRadians(angle-90))*(yRadius-yThickness);
                GL11.glVertex2d(X, Y);
                X = x+Math.cos(Math.toRadians(angle-90))*xRadius;
                Y = y+Math.sin(Math.toRadians(angle-90))*yRadius;
                GL11.glVertex2d(X, Y);
            }
        }
        GL11.glEnd();
    }
    public static void drawHorizontalLine(double x1, double y, double x2, double thickness, int texture){
        drawRect(x1, y-thickness/2, x2, y+thickness/2, texture);
    }
    public static void drawVerticalLine(double x, double y1, double y2, double thickness, int texture){
        drawRect(x-thickness/2, y1, x+thickness/2, y2, texture);
    }
    public static void drawCosGear(double x, double y, double averageRadius, double toothSize, int teeth, int texture, double rot){
        drawCosGear(x, y, averageRadius, toothSize, teeth, texture, rot, 10);
    }
    public static void drawCosGear(double x, double y, double averageRadius, double toothSize, int teeth, int texture, double rot, int resolution){
        if(teeth<3){
            throw new IllegalArgumentException("A gear must have at least 3 teeth!");
        }
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_TRIANGLES);
        double angle = rot;
        double radius = averageRadius+toothSize/2;
        for(int i = 0; i<teeth*resolution; i++){
            GL11.glVertex2d(x, y);
            double X = x+Math.cos(Math.toRadians(angle-90))*radius;
            double Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X,Y);
            angle+=(360d/(teeth*resolution));
            if(angle>=360)angle-=360;
            radius = averageRadius+(toothSize/2)*Math.cos(Math.toRadians(teeth*(angle-rot)));
            X = x+Math.cos(Math.toRadians(angle-90))*radius;
            Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X,Y);
        }
        GL11.glEnd();
    }
    public static void drawHollowCosGear(double x, double y, double holeRadius, double averageRadius, double toothSize, int teeth, int texture, double rot){
        drawCosGear(x, y, averageRadius, toothSize, teeth, texture, rot, 10);
    }
    public static void drawHollowCosGear(double x, double y, double holeRadius, double averageRadius, double toothSize, int teeth, int texture, double rot, int resolution){
        if(teeth<3){
            throw new IllegalArgumentException("A gear must have at least 3 teeth!");
        }
        ImageStash.instance.bindTexture(texture);
        GL11.glBegin(GL11.GL_TRIANGLES);
        double angle = rot;
        double radius = averageRadius+toothSize/2;
        for(int i = 0; i<teeth*resolution; i++){
            double X = x+Math.cos(Math.toRadians(angle-90))*radius;
            double Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
            X = x+Math.cos(Math.toRadians(angle-90))*holeRadius;
            Y = y+Math.sin(Math.toRadians(angle-90))*holeRadius;
            GL11.glVertex2d(X, Y);
            angle+=(360d/(teeth*resolution));
            radius = averageRadius+(toothSize/2)*Math.cos(Math.toRadians(teeth*(angle-rot)));
            X = x+Math.cos(Math.toRadians(angle-90))*holeRadius;
            Y = y+Math.sin(Math.toRadians(angle-90))*holeRadius;
            GL11.glVertex2d(X, Y);
            X = x+Math.cos(Math.toRadians(angle-90))*radius;
            Y = y+Math.sin(Math.toRadians(angle-90))*radius;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
    }
    public static void winLevel(int i){
        latestLevel = Math.min(LEVELS,Math.max(latestLevel, i+1));
    }
    public static void loadGame(String name, int level, WorldGenerator gen, Story story, boolean tutorial){
        Game g = Game.load(name);
        if(g==null){
            if(gen==null)throw new IllegalArgumentException("Tried to load invalid game!");
            gui.open(new MenuGame(gui, Game.generate(name, level, gen, new BoundingBox(-960, -540, 1920, 1080), story, tutorial)));
        }else{
            gui.open(new MenuGame(gui, g));
        }
    }
    /**
     * @return the currently loaded game
     * @deprecated ONLY USED FOR MUSIC
     */
    @Deprecated
    public static Game getGame(){
        if(gui.menu instanceof MenuGame)return ((MenuGame) gui.menu).game;
        return null;
    }
    public static String drawCenteredTextWithWordWrap(double leftPossibleEdge, double topEdge, double rightPossibleEdge, double bottomEdge, String text){
        String[] words = text.split(" ");
        String str = words[0];
        double height = bottomEdge-topEdge;
        double length = rightPossibleEdge-leftPossibleEdge;
        for(int i = 1; i<words.length; i++){
            String string = str+" "+words[i];
            if(FontManager.getLengthForStringWithHeight(string.trim(), height)>=length){
                drawCenteredTextWithWrap(leftPossibleEdge, topEdge, rightPossibleEdge, bottomEdge, str.trim());
                return text.replaceFirst("\\Q"+str, "").trim();
            }else{
                str = string;
            }
        }
        return drawCenteredTextWithWrap(leftPossibleEdge, topEdge, rightPossibleEdge, bottomEdge, text);
    }
    public static String drawTextWithWordWrap(double leftEdge, double topEdge, double rightPossibleEdge, double bottomEdge, String text){
        String[] words = text.split(" ");
        String str = words[0];
        double height = bottomEdge-topEdge;
        double length = rightPossibleEdge-leftEdge;
        for(int i = 1; i<words.length; i++){
            String string = str+" "+words[i];
            if(FontManager.getLengthForStringWithHeight(string.trim(), height)>=length){
                drawTextWithWrap(leftEdge, topEdge, rightPossibleEdge, bottomEdge, str.trim());
                return text.replaceFirst("\\Q"+str, "").trim();
            }else{
                str = string;
            }
        }
        return drawTextWithWrap(leftEdge, topEdge, rightPossibleEdge, bottomEdge, text);
    }
}