

package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Rouson on 2016/2/26.
 */
@Entity
@Table(name = "commercialactivities")
public class CommercialActivitiesModel extends Model implements Serializable, IConst {

    @Id
    public Long id;

    public String productname; // 产品

    @Lob
    public String images; // 产品图*/

    @Column(columnDefinition = "Decimal(10,2)")
    public double Price = 0D; // 价格

    @Column(columnDefinition = "Decimal(10,2)")
    public double bargainBottomLine = 0D; // 砍价底线

    public int stock; // 库存

    public String activityTime; // 活动时间

    public String activeState; // 活动状态

    public String peopleParticipate; // 参与人次

    public String salesVolume; // 销量

    public String createdAtStr; // 创建日期字符串表示

    public String comment; //备注

    public CommercialActivitiesModel() {
        createdAtStr = DateUtil.Date2Str(new Date());
    }

    // -- Queries

    public static Finder<Long, CommercialActivitiesModel> find = new Finder(Long.class, CommercialActivitiesModel.class);

    /**
     * Retrieve all .
     */
    public static List<CommercialActivitiesModel> findAll() {
        return find.all();
    }

    public String toString() {
        return "LineActivities [productname:" + productname + "]";
    }
}
