package planetaryprotector.research;
import planetaryprotector.item.Item;
public class ResearchEvent{
    public final Type type;
    public final Item item;
    public final int value;
    public ResearchEvent(Type type, Item item, int value){
        this.type = type;
        this.item = item;
        this.value = value;
    }
    public static enum Type{
        GAIN_RESOURCE,USE_RESOURCE;
    }
}