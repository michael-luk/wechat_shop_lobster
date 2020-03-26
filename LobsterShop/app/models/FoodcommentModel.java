package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import play.db.ebean.Model;

@Entity
@Table(name = "foodcomments")
public class FoodcommentModel extends Model implements Serializable, IConst {

    @Id
    public Long id;

    public Long storeId = 0L;//店铺Id

    @Lob
    public String description; // 点评

    @Lob
    public String images; // 图片

    public Long refUserId; // 所属用户ID

    @JsonIgnore
    @ManyToOne
    public UserModel user;// 所属的用户

    public Long refProductId; // 所属产品的ID

    @JsonIgnore
    @ManyToOne
    public ProductModel product;// 所属的产品

    public Long refOrderId;//所属订单的ID

    public String initNickName;

    public String comment;

    public String createdAtStr; // 创建日期字符串表示

    public FoodcommentModel() {
        createdAtStr = DateUtil.Date2Str(new Date());
    }

    public static Finder<Long, FoodcommentModel> find = new Finder(Long.class, FoodcommentModel.class);

    /**
     * Retrieve all .
     */
    public static List<FoodcommentModel> findAll() {
        return find.all();
    }

    @Override
    public String toString() {
        return "FoodComments [description:" + description + "]";
    }
}