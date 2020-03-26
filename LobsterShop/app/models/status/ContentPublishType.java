package models.status;

/**
 * Created by Jan on 2015/5/21.
 */
public enum  ContentPublishType {
    OFFICIAL(0, "OFFICIAL"),//官方
    BUSINESS(1, "BUSINESS"),//商户
    PERSONAL(2, "PERSONAL"); //个人

    private ContentPublishType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private int value = 0;
    private String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public int value() {
        return this.value;
    }
}
