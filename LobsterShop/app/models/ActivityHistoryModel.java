package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created by Rouson on 2016/2/27.
 */
@Entity
@Table(name = "activityhistory")
public class ActivityHistoryModel extends Model implements Serializable, IConst {

    @Id
    public Long id;

    public Long activitiesProductId; // 活动产品ID

    public Long theLineActivities; // 所属活动线ID

    public String lineActivitiesUserId; // 发起用户Id

    public String lineActivitiesUserName; // 发起用户名

    public Long cutUserId; // 帮砍用户ID

    @Lob
    public String headImgUrl; // 头像

    public String nickname; // 微信用户名

    @Column(columnDefinition = "Decimal(10,2)")
    public double originalPrice = 0D; // 原价

    @Column(columnDefinition = "Decimal(10,2)")
    public double beforeCutPrice = 0D; // 砍前价

    @Column(columnDefinition = "Decimal(10,2)")
    public double afterCutPrice = 0D; // 砍后价

    @Column(columnDefinition = "Decimal(10,2)")
    public double bargain = 0D; // 砍价金额

/*    @JsonIgnore
    @ManyToOne
    public ProductModel product;// 所属的产品*/

    public String createdAtStr; // 创建日期字符串表示

    public String comment; //备注

    public ActivityHistoryModel() {
        createdAtStr = DateUtil.Date2Str(new Date());
    }

    // -- Queries

    public static Finder<Long, ActivityHistoryModel> find = new Finder(Long.class, ActivityHistoryModel.class);

    /**
     * Retrieve all .
     */
    public static List<ActivityHistoryModel> findAll() {
        return find.all();
    }

    public String toString() {
        return "ActivityHistory [theLineActivities:" + theLineActivities + "]";
    }

}
