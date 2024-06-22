package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import java.util.ArrayList;
import java.util.Random;
import planetaryprotector.item.Item;
public enum AsteroidMaterial{
    STONE(Item.stone, "stone", 4, null),
    COAL(Item.coal, "coal", 1, new Color(0, 0, 0)),
    IRON(Item.ironOre, "iron", 8, new Color(142, 63, 11));
    public final Item item;
    public final String texture;
    public boolean forceDrop = false;
    private final int weight;
    public final Color color;
    private AsteroidMaterial(Item item, String texture, int weight, Color color){
        this.item = item;
        this.texture = texture;
        this.weight = weight;
        this.color = color;
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
