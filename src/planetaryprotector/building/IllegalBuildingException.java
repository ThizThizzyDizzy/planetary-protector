package planetaryprotector.building;
public class IllegalBuildingException extends IllegalArgumentException{
    public IllegalBuildingException(BuildingType type) {
        super(type.name);
    }
}