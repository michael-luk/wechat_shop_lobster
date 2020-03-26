package models.status;

/**
 * Created by Jensen on 2015/6/1.
 */
public enum TagType {
    CATEGORY(0, "CATEGORY"), //分类
    BRAND(1, "BRAND"), //品牌
    TOPIC(2, "TOPIC"); //话题

    private TagType(int value, String displayName) {
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
