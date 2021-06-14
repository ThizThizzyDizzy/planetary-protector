package planetaryprotector.enemy;
import java.util.ArrayList;
import java.util.Random;
import simplelibrary.image.Color;
public enum AsteroidMaterial{
    STONE("stone", 4, null),
    COAL("coal", 1, new Color(0, 0, 0)),
    IRON("iron", 8, new Color(142, 63, 11));
    public final String texture;
    public boolean forceDrop = false;
    private final int weight;
    public final Color color;
    private AsteroidMaterial(String texture, int weight, Color color){
        this.texture = texture;
        this.weight = weight;
        this.color = color;
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