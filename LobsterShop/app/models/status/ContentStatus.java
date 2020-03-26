package models.status;


/**
 * The status of content.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum ContentStatus {

	HOT(0, "热门"),
	LOWPRICE(0, "降价"),
	PROMOTE(0, "促销"),
    ACTIVITY(1, "活动");

    private int value = 0;

	private ContentStatus(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

	String displayName;

	public String getDisplayName() {
		return this.displayName;
	}

    public int value() {
        return this.value;
    }
}
