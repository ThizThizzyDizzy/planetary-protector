package planetaryprotector.item;
import planetaryprotector.menu.component.MenuComponentClickable;
import java.util.ArrayList;
public class Item{
    public static final ArrayList<Item> items = new ArrayList<>();
    public static final Item stone = new Item("Stone", "Stone");
    public static final Item ironOre = new Item("Iron Chunk", "Iron Chunk");
    public static final Item ironIngot = new Item("Iron Ingot", "Iron Ingot");
    public static final Item coal = new Item("Coal", "Coal");
    public static final Item star = new Item("Shooting Star", "Shooting Star");
    static{
        items.add(stone);
        items.add(ironOre);
        items.add(ironIngot);
        items.add(coal);
    }
    public static Item getItemByName(String get) {
        for(Item item : items){
            if(item.name.equals(get)){
                return item;
            }
        }
        return null;
    }
    public final String name;
    public final String texture;
    public MenuComponentClickable button;
    public Item(String name, String texture){
        this.name = name;
        this.texture = texture;
    }
}