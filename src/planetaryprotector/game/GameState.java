package planetaryprotector.game;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Game state for save/load with Gson
 *
 * @author Thiz
 */
public class GameState{
    public String saveName;//name of the save, as different from filename
    public boolean tutorial;
    public int level = 1;
    public String worldGenerator;
    public String story;
    public String version;
    public double meteorTimer;
    public int mothershipHealth = -1;
    public int furnaceLevel;
    public int furnaceTimer = 100;
    public int furnaceOre;
    public int furnaceCoal;
    public int furnaceXP;
    public double enemyStrength = 1;
    public int enemyTimer = 20*30;
    public ArrayList<Item> droppedItems = new ArrayList<>();
    public boolean meteorShower;
    public int meteorShowerTimer;
    public int workers = 1;
    public ArrayList<Structure> structures = new ArrayList<>();
    public int workerCooldown;
    public int phase;
    public int phaseCounter;
    public int targetPopulation;
    public int popPerFloor;
    public double meteorShowerDelay;
    public double meteorShowerIntensity;
    public Expedition pendingExpedition;
    public ArrayList<Expedition> activeExpeditions = new ArrayList<>();
    public ArrayList<Expedition> finishedExpeditions = new ArrayList<>();
    public int fogTimer;
    public int cloudTimer;
    public double fogTime;
    public double fogTimeIncrease;
    public double fogIntensity;
    public double fogHeightIntensity;
    public int secretTimer;
    public int secretWaiting;
    public boolean observatoryUnlocked;
    public int time;
    public HashMap<String, Research> researches = new HashMap<>();
    public ArrayList<Resource> resources = new ArrayList<>();
    public int losing;
    int bboxX;
    int bboxY;
    int bboxWidth;
    int bboxHeight;
    public static class Item{
        public String item = planetaryprotector.item.Item.ironIngot.name;
        public int life;
        public int x;
        public int y;
    }
    public static class Resource{
        public String item;
        public int count;
    }
    public static class Structure{
        public String type;
        public int level;
        public ArrayList<String> upgrades = new ArrayList<>();
        public ArrayList<Damage> damages = new ArrayList<>();
        public int x;
        public int y;
        public double fire;
        public int fireDamage;
        public double fireIncreaseRate;
        public ArrayList<Fire> fires = new ArrayList<>();
        public double power;
        public double starlight;
        public int shield;
        public static class Damage{
            public double x;
            public double y;
            public int tex1;
            public int tex2;
            public double size;
            public double opacity;
        }
        public static class Fire{
            public int x;
            public int y;
        }
    }
    public static class Expedition{
        public int requiredWorkers = 1;
        public int workers = 1;
        public int time;
        public int totalTime;
        public int civilians;
        public int civilianCooldown;
        public int dieCooldown;
        public boolean returning;
        public boolean recalled;
        public boolean done;
        public boolean returned;
    }
    public static class Research{
        public int discovery;
        public ArrayList<DiscoveryStage> stages = new ArrayList<>();
        public int power;
        public double starlight;
        public int time;
        public ArrayList<Integer> itemCosts = new ArrayList<>();
        public boolean completed;
        public static class DiscoveryStage{
            public double progress;
            public ArrayList<Prerequisite> prerequisites = new ArrayList<>();
            public static class Prerequisite{
                public double progress;
                public int discovery;
                public ArrayList<Prerequisite> prerequisites = new ArrayList<>();
            }
        }
    }
}
