package models.status;


/**
 * The type of corp.
 * 
 * @author chenyu
 * @since 1.0.0
 * 
 */
public enum GroupType {

    COLLEGE(0, "COLLEGE"), //学校
    ACADEMY(1, "ACADEMY"), //院系
    PROFESSION(2, "PROFESSION"),//专业
    CLASSES(3, "CLASSES"),//班级

    COMPANY(4, "COMPANY"),//公司
    DEPARTMENT(5, "DEPARTMENT"),//部门
    CUSTOM(6, "CUSTOM"); //自定义

	private GroupType(int value, String displayName) {
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
