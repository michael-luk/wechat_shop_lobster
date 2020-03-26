package models.status;


/**
 * The status of account.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum ContentStyle {

	SINGLEIMG(0, "SINGLEIMG"),
    MULTIIMG(1, "MULTIIMG"),
    COLUMN(2, "COLUMN"),//专栏，显示banner图片
    CIRCLEPOST(3, "CIRCLEPOST"),//和新闻的区别是有个头像显示+名字
    ADVERTISE(4, "ADVERTISE")//和专栏的区别是标题位置
    ;

	private ContentStyle(int value, String displayName) {
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
