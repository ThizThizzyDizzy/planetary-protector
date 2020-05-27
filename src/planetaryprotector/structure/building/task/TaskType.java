package planetaryprotector.structure.building.task;
public enum TaskType{
    WRECK_CLEAN("wreck"),
    SKYSCRAPER_ADD_FLOOR("skyscraper/addFloor"),
    CONSTRUCT("construct"),
    UPGRADE("upgrade"),
    TRAIN_WORKER("trainWorker"),
    REPAIR("repair");
    public final String textureRoot;
    private TaskType(String textureRoot){
        this.textureRoot = textureRoot;
    }
}
