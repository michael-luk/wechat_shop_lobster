package models;

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
 * Created by Rouson on 2016/2/22.
 */
@Entity
@Table(name = "enjoythecode")
public class EnjoyTheCodeModel extends Model implements Serializable {

    @Id
    public Long id;

    public Long refUserId; // 所属用户ID

    public int number; //尊享码

    @Column(columnDefinition = "Decimal(10,2)")
    public float discount; //折扣

    public boolean codeType; //开放或封闭

    public boolean state; //启用或取消

    public String comment; //备注

    public String createdAtStr; // 创建日期字符串表示

    public EnjoyTheCodeModel() {
        createdAtStr = DateUtil.Date2Str(new Date());
    }

    public static Finder<Long, EnjoyTheCodeModel> find = new Finder(Long.class, EnjoyTheCodeModel.class);

    /**
     * Retrieve all .
     */
    public static List<EnjoyTheCodeModel> findAll() {
        return find.all();
    }

    @Override
    public String toString() {
        return "enjoythecode [number:" + number + "]";
    }

}
