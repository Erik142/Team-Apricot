public class AchievementType {

    private int typeNr;
    private int points;
    private Description description;

    public AchievementType(int typeNr, int points, Description description){
        this.typeNr = typeNr;
        this.points = points;
        this.description = description;
    }

    public int getTypeNr(){
        return this.typeNr;
    }

    public int getPoints(){
        return this.points;
    }

    public Description getDescription(){
        return this.description;
    }
}
