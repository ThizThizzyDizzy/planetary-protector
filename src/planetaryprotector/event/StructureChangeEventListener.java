package planetaryprotector.event;
import planetaryprotector.structure.Structure;
public interface StructureChangeEventListener{
    public void onStructureChange(Structure from, Structure to);
}