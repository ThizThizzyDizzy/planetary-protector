package planetaryprotector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
public class Main{
    private static final String versionListURL = "https://raw.githubusercontent.com/ThizThizzyDizzy/planetary-protector/master/versions.txt";
    public static final String applicationName = "Planetary Protector";
    public static final String discordAppID = "592509210277838848";
    public static final String issueTrackerLink = "https://github.com/ThizThizzyDizzy/planetary-protector/issues";
    private static final ArrayList<String[]> requiredLibraries = new ArrayList<>();
    //Download details
    private static int total;
    private static int current;
    //OS details
    public static boolean hasAWT = true;
    public static boolean hasAWTAfterStartup = false;
    private static final int OS_UNKNOWN = -1;
    static final int OS_WINDOWS = 0;
    private static final int OS_MACOS = 1;
    private static final int OS_LINUX = 2;
    static int os = OS_UNKNOWN;//Should not be directly referenced from other classes, as there are always better ways of handling OS-compatibility
    private static void addRequiredLibrary(String url, String filename){
        requiredLibraries.add(new String[]{url,filename});
    }
    private static void addRequiredLibraries(){
        addRequiredLibrary("https://www.dropbox.com/s/phv29x1suv4i4b0/jl1.0.1.jar?dl=1", "jl1.0.1.jar");
        addRequiredLibrary("https://www.dropbox.com/s/ml0rg2ze9ks4xbe/jna-5.3.1.jar?dl=1", "jna-5.3.1.jar");
        addRequiredLibrary("https://www.dropbox.com/s/qb9i7dq98qt0pd6/java-discord-rpc-2.0.2.jar?dl=1", "java-discord-rpc-2.0.2.jar");
    }
    public static void main(String[] args){
        List<String> argses = Arrays.asList(args);
        try{
            if(argses.contains("noAWT")){
                hasAWT = false;
            }
            if(argses.contains("noAWTDuringStartup")){
                hasAWT = false;
                hasAWTAfterStartup = true;
            }
            System.out.println("Initializing...");
            args = update(args);
            if(args==null){
                return;
            }
            Core.main(args);
        }catch(Exception ex){
            if(hasAWT){
                String trace = "";
                for(StackTraceElement e : ex.getStackTrace()){
                    trace+="\n"+e.toString();
                }
                trace = trace.isEmpty()?trace:trace.substring(1);
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage()+"\n"+trace, "CAUGHT ERROR: "+ex.getClass().getName()+" on main thread!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }else{
                throw new RuntimeException("Exception on main thread!", ex);
            }
        }
    }
    public static String getAppdataRoot(){
        switch(os){
            case OS_LINUX:
                return System.getProperty("user.home")+File.separatorChar+"ThizzyGames"+File.separatorChar+applicationName;
            case OS_MACOS:
                return System.getProperty("user.home")+File.separatorChar+"Library"+File.separatorChar+"Application Support"+File.separatorChar+"ThizzyGames"+File.separatorChar+applicationName;
            case OS_WINDOWS:
                return System.getenv("APPDATA")+File.separatorChar+"ThizzyGames"+File.separatorChar+applicationName;
            default:
                return "ThizzyGames"+File.separatorChar+applicationName;
        }
    }
    private static String getLibraryRoot(){
        switch(os){
            case OS_LINUX:
                return System.getProperty("user.home")+File.separatorChar+"ThizzyGames"+File.separatorChar+"Libraries";
            case OS_MACOS:
                return System.getProperty("user.home")+File.separatorChar+"Library"+File.separatorChar+"Application Support"+File.separatorChar+"ThizzyGames"+File.separatorChar+"Libraries";
            case OS_WINDOWS:
                return System.getenv("APPDATA")+File.separatorChar+"ThizzyGames"+File.separatorChar+"Libraries";
            default:
                return "ThizzyGames"+File.separatorChar+"Libraries";
        }
    }
    private static String[] update(String[] args) throws URISyntaxException, IOException, InterruptedException{
        ArrayList<String> theargs = new ArrayList<>(Arrays.asList(args));
        if(args.length<1||!args[0].equals("Skip Dependencies")){
            if(versionListURL.isEmpty()){
                System.err.println("Version list URL is empty! assuming latest version.");
            }else{
                System.out.println("Checking for updates...");
                Updater updater = Updater.read(versionListURL, VersionManager.currentVersion, applicationName);
                if(updater!=null&&updater.getVersionsBehindLatestDownloadable()>0){
                    boolean allowUpdate = false;
                    if(hasAWT){
                        allowUpdate = javax.swing.JOptionPane.showConfirmDialog(null, "Version "+updater.getLatestDownloadableVersion()+" is out!  Would you like to update "+applicationName+" now?", applicationName+" "+VersionManager.currentVersion+"- Update Available", javax.swing.JOptionPane.YES_NO_OPTION)==javax.swing.JOptionPane.YES_OPTION;
                    }else{
                        System.out.println("Version "+updater.getLatestDownloadableVersion()+" is out! Would you like to update "+applicationName+" now? (Y/N)");
                        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                        String s = r.readLine();
                        if(s==null)s = "";
                        s = s.trim();
                        r.close();
                        allowUpdate = s.equalsIgnoreCase("Y")||s.equalsIgnoreCase("Yes");
                    }
                    if(allowUpdate){
                        System.out.println("Updating...");
                        startJava(new String[0], new String[]{"justUpdated"}, updater.update(updater.getLatestDownloadableVersion()));
                        System.exit(0);
                    }
                }
                System.out.println("Update Check Complete.");
            }
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl-assimp.jar", "lwjgl-assimp.jar");
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl-glfw.jar", "lwjgl-glfw.jar");
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl-openal.jar", "lwjgl-openal.jar");
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl-opengl.jar", "lwjgl-opengl.jar");
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl-stb.jar", "lwjgl-stb.jar");
            addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/jar/lwjgl.jar", "lwjgl.jar");
            String osName = System.getProperty("os.name");
            if(osName==null)osName = "null";
            osName = osName.toLowerCase(Locale.ENGLISH);
            if(osName.contains("win"))os = OS_WINDOWS;
            if(osName.contains("mac"))os = OS_MACOS;
            if(osName.contains("nix")||osName.contains("nux")||osName.contains("aix"))os = OS_LINUX;
            if(os==OS_UNKNOWN){
                System.out.println("Unknown OS: "+osName);
                if(hasAWT){
                    os = javax.swing.JOptionPane.showOptionDialog(null, "Unrecognized OS \""+osName+"\"!\nPlease report this problem at "+issueTrackerLink+".\nIn the meantime, which OS are you using?", "Unrecognized Operating System", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, new String[]{"Windows", "Mac OS", "Linux"}, "Windows");
                }else{
                    System.out.println("Unrecognized OS \""+osName+"\"! Please report this problem at "+issueTrackerLink+"\nWhich OS are you using? (Windows|Mac|Linux)");
                    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                    String s = r.readLine();
                    if(s==null)s = "";
                    s = s.trim();
                    r.close();
                    if(s.equalsIgnoreCase("Windows"))os = OS_WINDOWS;
                    if(s.equalsIgnoreCase("Mac")||s.equalsIgnoreCase("MacOS")||s.equalsIgnoreCase("Mac OS"))os = OS_MACOS;
                    if(s.equalsIgnoreCase("Linux"))os = OS_LINUX;
                }
                if(os<0||os>2){
                    System.exit(0);
                }
            }
            switch(os){
                case OS_WINDOWS:
                    {
                        final int ARCH_UNKNOWN = -1;
                        final int ARCH_X86 = 0;
                        final int ARCH_X64 = 1;
                        int arch = ARCH_UNKNOWN;
                        String osArch = System.getProperty("os.arch");
                        if(osArch==null)osArch = "null";
                        osArch = osArch.toLowerCase(Locale.ENGLISH);
                        if(osArch.equals("amd64"))arch = ARCH_X64;
                        if(osArch.equals("x86"))arch = ARCH_X86;
                        System.out.println("OS: Windows");
                        if(arch==ARCH_UNKNOWN){
                            System.out.println("Unknown Architecture: "+osArch);
                            if(hasAWT){
                                arch = javax.swing.JOptionPane.showOptionDialog(null, "Unrecognized Architecture \""+osArch+"\"!\nPlease report this problem at "+issueTrackerLink+".\nIn the meantime, what is your OS architecture?", "Unrecognized Operating System", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, new String[]{"x86", "x64"}, "x64");
                            }else{
                                System.out.println("Unrecognized Architecture \""+osArch+"\"! Please report this problem at "+issueTrackerLink+"\nWhat is your OS architecture? (x86|x64)");
                                BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                                String s = r.readLine();
                                if(s==null)s = "";
                                s = s.trim();
                                r.close();
                                if(s.equalsIgnoreCase("x86")||s.equalsIgnoreCase("86")||s.equalsIgnoreCase("x32")||s.equalsIgnoreCase("32"))arch = ARCH_X86;
                                if(s.equalsIgnoreCase("x64")||s.equalsIgnoreCase("64"))arch = ARCH_X64;
                            }
                            if(arch<0||arch>1){
                                System.exit(0);
                            }
                        }
                        switch(arch){
                            case ARCH_X86:
                                System.out.println("OS Architecture: x86");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-assimp-natives-windows-x86.jar", "lwjgl-assimp-natives-windows-x86.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-glfw-natives-windows-x86.jar", "lwjgl-glfw-natives-windows-x86.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-natives-windows-x86.jar", "lwjgl-natives-windows-x86.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-openal-natives-windows-x86.jar", "lwjgl-openal-natives-windows-x86.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-opengl-natives-windows-x86.jar", "lwjgl-opengl-natives-windows-x86.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/x86/lwjgl-stb-natives-windows-x86.jar", "lwjgl-stb-natives-windows-x86.jar");
                                break;
                            case ARCH_X64:
                                System.out.println("OS Architecture: x64");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-assimp-natives-windows.jar", "lwjgl-assimp-natives-windows.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-glfw-natives-windows.jar", "lwjgl-glfw-natives-windows.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-natives-windows.jar", "lwjgl-natives-windows.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-openal-natives-windows.jar", "lwjgl-openal-natives-windows.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-opengl-natives-windows.jar", "lwjgl-opengl-natives-windows.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/windows/lwjgl-stb-natives-windows.jar", "lwjgl-stb-natives-windows.jar");
                                break;
                        }
                    }
                    break;
                case OS_MACOS:
                    System.out.println("OS: Mac OS");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-assimp-natives-macos.jar", "lwjgl-assimp-natives-macos.jar");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-glfw-natives-macos.jar", "lwjgl-glfw-natives-macos.jar");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-natives-macos.jar", "lwjgl-natives-macos.jar");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-openal-natives-macos.jar", "lwjgl-openal-natives-macos.jar");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-opengl-natives-macos.jar", "lwjgl-opengl-natives-macos.jar");
                    addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/macos/lwjgl-stb-natives-macos.jar", "lwjgl-stb-natives-macos.jar");
                    break;
                case OS_LINUX:
                    {
                        final int ARCH_UNKNOWN = -1;
                        final int ARCH_X64 = 0;
                        final int ARCH_ARM32 = 1;
                        final int ARCH_ARM64 = 2;
                        int arch = ARCH_UNKNOWN;
                        String osArch = System.getProperty("os.arch");
                        if(osArch==null)osArch = "null";
                        osArch = osArch.toLowerCase(Locale.ENGLISH);
                        if(osArch.equals("amd64"))arch = ARCH_X64;
                        if(osArch.equals("x64"))arch = ARCH_X64;
                        if(osArch.equals("arm32"))arch = ARCH_ARM32;
                        if(osArch.equals("arm64"))arch = ARCH_ARM64;
                        System.out.println("OS: Linux");
                        if(arch==ARCH_UNKNOWN){
                            System.out.println("Unknown Architecture: "+osArch);
                            if(hasAWT){
                                arch = javax.swing.JOptionPane.showOptionDialog(null, "Unrecognized Architecture \""+osArch+"\"!\nPlease report this problem at "+issueTrackerLink+".\nIn the meantime, what is your OS architecture?", "Unrecognized Operating System", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.ERROR_MESSAGE, null, new String[]{"x64", "arm32", "arm64"}, "x64");
                            }else{
                                System.out.println("Unrecognized Architecture \""+osArch+"\"! Please report this problem at "+issueTrackerLink+"\nWhat is your OS architecture? (x64|arm32|arm64)");
                                BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                                String s = r.readLine();
                                if(s==null)s = "";
                                s = s.trim();
                                r.close();
                                if(s.equalsIgnoreCase("x64")||s.equalsIgnoreCase("64"))arch = ARCH_X64;
                                if(s.equalsIgnoreCase("arm32"))arch = ARCH_ARM32;
                                if(s.equalsIgnoreCase("arm64"))arch = ARCH_ARM64;
                            }
                            if(arch<0||arch>2){
                                System.exit(0);
                            }
                        }
                        switch(arch){
                            case ARCH_X64:
                                System.out.println("OS Architecture: x64");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-assimp-natives-linux.jar", "lwjgl-assimp-natives-linux.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-glfw-natives-linux.jar", "lwjgl-glfw-natives-linux.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-natives-linux.jar", "lwjgl-natives-linux.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-openal-natives-linux.jar", "lwjgl-openal-natives-linux.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-opengl-natives-linux.jar", "lwjgl-opengl-natives-linux.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/lwjgl-stb-natives-linux.jar", "lwjgl-stb-natives-linux.jar");
                                break;
                            case ARCH_ARM32:
                                System.out.println("OS Architecture: arm32");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-assimp-natives-linux-arm32.jar", "lwjgl-assimp-natives-linux-arm32.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-glfw-natives-linux-arm32.jar", "lwjgl-glfw-natives-linux-arm32.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-natives-linux-arm32.jar", "lwjgl-natives-linux-arm32.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-openal-natives-linux-arm32.jar", "lwjgl-openal-natives-linux-arm32.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-opengl-natives-linux-arm32.jar", "lwjgl-opengl-natives-linux-arm32.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm32/lwjgl-stb-natives-linux-arm32.jar", "lwjgl-stb-natives-linux-arm32.jar");
                                break;
                            case ARCH_ARM64:
                                System.out.println("OS Architecture: arm64");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-assimp-natives-linux-arm64.jar", "lwjgl-assimp-natives-linux-arm64.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-glfw-natives-linux-arm64.jar", "lwjgl-glfw-natives-linux-arm64.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-natives-linux-arm64.jar", "lwjgl-natives-linux-arm64.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-openal-natives-linux-arm64.jar", "lwjgl-openal-natives-linux-arm64.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-opengl-natives-linux-arm64.jar", "lwjgl-opengl-natives-linux-arm64.jar");
                                addRequiredLibrary("https://github.com/ThizThizzyDizzy/thizzyz-games-launcher/raw/master/libraries/lwjgl-3.2.3/native/linux/arm64/lwjgl-stb-natives-linux-arm64.jar", "lwjgl-stb-natives-linux-arm64.jar");
                                break;
                        }
                    }
                    break;
            }
            addRequiredLibraries();
            boolean hasAnythingToDownload = false;
            for(String[] lib : requiredLibraries){
                if(!new File(getLibraryRoot()+File.separatorChar+lib[1]).exists()){
                    hasAnythingToDownload = true;
                }
            }
            total = requiredLibraries.size();
            File[] requiredLibs = new File[requiredLibraries.size()];
            if(hasAWT){
                javax.swing.JFrame frame = new javax.swing.JFrame("Download Progress");
                javax.swing.JProgressBar bar = new javax.swing.JProgressBar(0, total);
                frame.add(bar);
                frame.setSize(300, 70);
                bar.setBounds(0, 0, 300, 70);
                if(hasAnythingToDownload){
                    frame.setVisible(true);
                }
                int n = 0;
                System.out.println("Checking Libraries...");
                for(String[] lib : requiredLibraries){
                    String url = lib[0];
                    String filename = lib[1];
                    requiredLibs[n] = downloadFile(url, new File(getLibraryRoot()+File.separatorChar+filename));
                    bar.setValue(current);
                    n++;
                }
                System.out.println("Libraries OK");
                frame.dispose();
            }else{
                int n = 0;
                System.out.println("Checking Libraries...");
                for(String[] lib : requiredLibraries){
                    String url = lib[0];
                    String filename = lib[1];
                    requiredLibs[n] = downloadFile(url, new File(getLibraryRoot()+File.separatorChar+filename));
                    System.out.println("Checking... "+Math.round(100d*current/total)+"% ("+current+"/"+total+")");
                    n++;
                }
                System.out.println("Libraries OK");
            }
            String[] additionalClasspathElements = new String[requiredLibs.length+4];
            for(int i = 0; i<requiredLibs.length; i++){
                if(requiredLibs[i]==null){
                    if(hasAWT){
                        javax.swing.JOptionPane.showMessageDialog(null, "Failed to download dependencies!\n"+applicationName+" will now exit.", "Exit", javax.swing.JOptionPane.OK_OPTION);
                    }else{
                        System.err.println("Failed to download dependencies!");//TODO yes, but WHAT dependencies?
                    }
                    System.exit(0);
                }
                additionalClasspathElements[i] = requiredLibs[i].getAbsolutePath();
            }
            theargs.add(0, "Skip Dependencies");
            final Process p = restart(new String[0], theargs.toArray(new String[theargs.size()]), additionalClasspathElements, Main.class);
            final int[] finished = {0};
            new Thread("System.out transfer"){
                public void run(){
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    try{
                        while((line=in.readLine())!=null){
                            System.out.println(line);
                        }
                    }catch(IOException ex){
                        throw new RuntimeException(ex);
                    }
                    finished[0]++;
                    if(finished[0]>1){
                        System.exit(0);
                    }
                }
            }.start();
            new Thread("System.err transfer"){
                public void run(){
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    String line;
                    try{
                        while((line=in.readLine())!=null){
                            System.err.println(line);
                        }
                    }catch(IOException ex){
                        throw new RuntimeException(ex);
                    }
                    finished[0]++;
                    if(finished[0]>1){
                        System.exit(0);
                    }
                }
            }.start();
            Thread inTransfer = new Thread("System.in transfer"){
                public void run(){
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    PrintWriter out = new PrintWriter(p.getOutputStream());
                    String line;
                    try{
                        while((line=in.readLine())!=null){
                            out.println(line);
                            out.flush();
                        }
                    }catch(IOException ex){
                        throw new RuntimeException(ex);
                    }
                }
            };
            inTransfer.setDaemon(true);
            inTransfer.start();
            return null;
        }
        theargs.remove(0);
        return theargs.toArray(new String[theargs.size()]);
    }
    private static File downloadFile(String link, File destinationFile){
        current++;
        if(destinationFile.exists()||link==null){
            return destinationFile;
        }
        System.out.println("Downloading "+destinationFile.getName()+"...");
        if(destinationFile.getParentFile()!=null)destinationFile.getParentFile().mkdirs();
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
                try (InputStream inputstream=getRemoteInputStream(targetFile, urlconnection)) {
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
    public static InputStream getRemoteInputStream(String currentFile, final URLConnection urlconnection) throws Exception {
        final InputStream[] is = new InputStream[1];
        for (int j = 0; (j < 3) && (is[0] == null); j++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        is[0] = urlconnection.getInputStream();
                    }catch (IOException localIOException){}
                }
            };
            t.setName("FileDownloadStreamThread");
            t.start();
            int iterationCount = 0;
            while ((is[0] == null) && (iterationCount++ < 5)){
                try {
                    t.join(1000L);
                } catch (InterruptedException localInterruptedException) {
                }
            }
            if (is[0] != null){
                continue;
            }
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException localInterruptedException1) {
            }
        }
        if (is[0] == null) {
            throw new Exception("Unable to download "+currentFile);
        }
        return is[0];
    }
    /**
     * Restarts the program.  This method will return normally if the program was properly restarted or throw an exception if it could not be restarted.
     * @param vmArgs The VM arguments for the new instance
     * @param applicationArgs The application arguments for the new instance
     * @param additionalFiles Any additional files to include in the classpath
     * @param mainClass The program's main class.  The new instance is started with this as the specified main class.
     * @throws URISyntaxException if a URI cannot be created to obtain the filepath to the jarfile
     * @throws IOException if Java is not in the PATH environment variable
     */
    public static Process restart(String[] vmArgs, String[] applicationArgs, String[] additionalFiles, Class<?> mainClass) throws URISyntaxException, IOException{
        ArrayList<String> params = new ArrayList<>();
        params.add("java");
        if(os==OS_MACOS)params.add("-XstartOnFirstThread");
        params.addAll(Arrays.asList(vmArgs));
        params.add("-classpath");
        String filepath = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String separator = System.getProperty("path.separator");
        for(String str : additionalFiles){
            filepath+=separator+str;
        }
        params.add(filepath);
        params.add(mainClass.getName());
        params.addAll(Arrays.asList(applicationArgs));
        ProcessBuilder builder = new ProcessBuilder(params);
        return builder.start();
    }
    /**
     * Starts the requested Java application the program.  This method will return normally if the program was properly restarted or throw an exception if it could not be restarted.
     * @param vmArgs The VM arguments for the new instance
     * @param applicationArgs The application arguments for the new instance
     * @param file The program file.  The new instance is started with this as the specified main class.
     * @throws URISyntaxException if a URI cannot be created to obtain the filepath to the jarfile
     * @throws IOException if Java is not in the PATH environment variable
     */
    public static Process startJava(String[] vmArgs, String[] applicationArgs, File file) throws URISyntaxException, IOException{
        ArrayList<String> params = new ArrayList<>();
        params.add("java");
        if(os==OS_MACOS)params.add("-XstartOnFirstThread");
        params.addAll(Arrays.asList(vmArgs));
        params.add("-jar");
        params.add(file.getAbsolutePath());
        params.addAll(Arrays.asList(applicationArgs));
        ProcessBuilder builder = new ProcessBuilder(params);
        return builder.start();
    }
}