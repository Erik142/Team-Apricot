public class Achievement {

    private String achivementId;
    private boolean status;
    private AchievementType type;

    public Achievement(String achivementId, boolean status, AchievementType type){
        this.achivementId = achivementId;
        this.status = status;
        this.type = type;
    }

    public String getAchivementId(){
        return this.achivementId;
    }

    public boolean getstatus(){
        return this.status;
    }

    public AchievementType getType(){
        return this.type;
    }

    public void changeStatus(String achivementId){
        this.status = true;
    }
}
