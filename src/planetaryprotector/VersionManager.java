package planetaryprotector;
import java.util.ArrayList;
/**
 * A version manager class, created to make version management easy.
 * @author Bryan
 */
public class VersionManager{
    /**
     * The current version (This version)
     * @since Public Acceptance Text 1 (Initial Release)
     */
    public static final String currentVersion;
    /**
     * List of all recognized versions.
     * @since Public Acceptance Text 1 (Initial Release)
     */
    private static final ArrayList<String> versions = new ArrayList<>();
    /**
     * The earliest version that the current version has back compatibility for.
     * @since Public Acceptance Text 1 (Initial Release)
     */
    private static String backCompatibleTo;
    static{
        addVersion("1.0");
        breakBackCompatability();
        addVersion("1.1");
        breakBackCompatability();
        addVersion("1.1.1");
        breakBackCompatability();
        addVersion("1.2");
        addVersion("1.3.1");
        addVersion("1.4");
        addVersion("1.4.1");
        addVersion("1.4.2");
        addVersion("1.4.3");
        breakBackCompatability();
        addVersion("1.5");
        addVersion("1.5.1");
        addVersion("1.5.2");
        addVersion("1.5.3");
        addVersion("1.5.4");
        addVersion("1.5.5");
        addVersion("1.5.6");
        addVersion("1.6.0");
        addVersion("1.6.1");
        breakBackCompatability();
        addVersion("1.7.0");
        addVersion("1.7.1");
        addVersion("1.7.2");
        addVersion("1.7.3");
        addVersion("1.7.4");
        addVersion("1.7.5");
        addVersion("1.8");
        addVersion("1.8.1");
        addVersion("1.8.2");
        addVersion("1.8.3");
        addVersion("1.9");
        addVersion("1.9.1");
        addVersion("1.9.2");
        breakBackCompatability();
        addVersion("1.10");
        addVersion("1.10.1");
        addVersion("1.10.2");
        addVersion("1.10.3");
        addVersion("1.11");
        addVersion("1.11.1");
        addVersion("1.11.2");
        addVersion("1.11.3");
        addVersion("1.11.4");
        breakBackCompatability();
        addVersion("1.12");
        addVersion("1.12.1");
        currentVersion = versions.get(versions.size()-1);
    }
    /**
     * Adds a version to the versions list.  Used only in the static initializer of this class.
     * @param string The name of the version to add
     * @since Public Acceptance Text 1 (Initial Release)
     */
    private static void addVersion(String string){
        if(versions.contains(string)){
            throw new IllegalArgumentException("Cannot add same version twice!");
        }
        versions.add(string);
        if(breakBackCompatibility){
            breakBackCompatibility = false;
            backCompatibleTo = getVersion(versions.size()-1);
        }
    }
    /**
     * Gets the version ID for the specified String version
     * @param version The version to ID
     * @return The version ID (-1 if <code>version</code> is not a valid version)
     * @since Public Acceptance Text 1 (Initial Release)
     */
    public static int getVersionID(String version){
        return versions.indexOf(version);
    }
    /**
     * Gets the String version for the specified version ID
     * @param ID the version ID
     * @return The String version
     * @throws IndexOutOfBoundsException if the ID is not a valid version ID
     * @since Public Acceptance Text 1 (Initial Release)
     */
    public static String getVersion(int ID){
        return versions.get(ID);
    }
    private static boolean breakBackCompatibility;
    /**
     * Informs the version manager that there is no back compatibility.
     * Sets <code>backCompatibleTo</code> to the next index in <code>versions</code>
     * Used only in the static initializer of this class.
     * @since Public Acceptance Text 1 (Initial Release)
     */
    private static void breakBackCompatability(){
        breakBackCompatibility = true;
    }
    /**
     * Checks if the system has back compatibility for the specified version ID
     * @param versionID The version ID to check
     * @return If back compatibility exists for the specified version ID
     * @since Public Acceptance Text 1 (Initial Release)
     */
    public static boolean isCompatible(int versionID){
        if(backCompatibleTo==null){
            breakBackCompatability();
        }
        return getVersionID(backCompatibleTo)<=versionID;
    }
    public static boolean isCompatible(String version){
        return isCompatible(getVersionID(version));
    }
}
