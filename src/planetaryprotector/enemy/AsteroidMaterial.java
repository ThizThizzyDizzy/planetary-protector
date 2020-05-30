package planetaryprotector.enemy;
import java.util.ArrayList;
import java.util.Random;
public enum AsteroidMaterial{
    STONE("stone", 4),
    COAL("coal", 1),
    IRON("iron", 8);
    public String[] images = new String[19];
    public final String texture;
    public boolean forceDrop = false;
    private final int weight;
    private AsteroidMaterial(String texture, int weight){
        this.texture = texture;
        this.weight = weight;
    }
    private AsteroidMaterial forceDrop(){
        forceDrop = true;
        return this;
    }
    public static AsteroidMaterial random(Random rand){
        ArrayList<AsteroidMaterial> materials = new ArrayList<>();
        for(AsteroidMaterial material : values()){
            for(int i = 0; i<material.weight; i++){
                materials.add(material);
            }
        }
        return materials.get(rand.nextInt(materials.size()));
    }
}