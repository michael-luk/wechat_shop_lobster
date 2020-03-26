package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Rouson on 2016/2/26.
 */
@Entity
@Table(name = "lineactivities")
public class LineActivitiesModel extends Model implements Serializable, IConst {

    @Id
    public Long id;

    public Long sponsorId ;// 发起人ID

    public String sponsorName ;// 发起用户名

    public Long productId; // 产品ID

    public String product; // 产品

/*    @Lob
    public String images; // 产品图*/

    @Column(columnDefinition = "Decimal(10,2)")
    public double originalPrice = 0D; // 原价

    @Column(columnDefinition = "Decimal(10,2)")
    public double presentPrice = 0D; // 现价

    @Column(columnDefinition = "Decimal(10,2)")
    public double beforeCutPrice = 0D; // 砍前价

    public int theNumberHasBeenCut = 0;  // 已砍刀数

    public int sabreplayLimit; // 限制刀数

    public String createdAtStr; // 创建日期字符串表示

    public String comment; //备注

    public LineActivitiesModel() {
        createdAtStr = DateUtil.Date2Str(new Date());
    }

    // -- Queries

    public static Finder<Long, LineActivitiesModel> find = new Finder(Long.class, LineActivitiesModel.class);

    /**
     * Retrieve all .
     */
    public static List<LineActivitiesModel> findAll() {
        return find.all();
    }

    public String toString() {
        return "LineActivities [sponsorId:" + sponsorId + "]";
    }


}
