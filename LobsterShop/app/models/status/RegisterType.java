package models.status;

/**
 * Created by Jensen on 2015/6/1.
 */
public enum RegisterType {
    PHONE(0, "PHONE"),
    WEIXIN(1, "WEIXIN"),
    QQ(2, "QQ"),
    WEIBO(3, "WEIBO");

    private RegisterType(int value, String displayName) {
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
