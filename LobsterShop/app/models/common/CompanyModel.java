package models.common;

import LyLib.Interfaces.IConst;
import LyLib.Utils.StrUtil;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "company")
public class CompanyModel extends Model implements Serializable, IConst {

    @Id
    public Long id;

    public String name;

    public String location;

    public String email;

    public String phone;

    public String cellPhone;

    public String weibo;

    public String qq;

    public String weixin;

    @Lob
    public String description1;

    @Lob
    public String description2;

    public String registerInfo1;

    public String registerInfo2;

    public String barcodeImg1;
    public String barcodeImg2;
    public String barcodeImg3;

    public String logo1;
    public String logo2;
    public String logo3;

    @Column(columnDefinition = "Decimal(10,2)")
    public float marketing1; // 1级分销提成百分比

    @Column(columnDefinition = "Decimal(10,2)")
    public float marketing2; // 2级分销提成百分比

    @Column(columnDefinition = "Decimal(10,2)")
    public float marketing3; // 3级分销提成百分比

    // -- Queries

    public static Model.Finder<Long, CompanyModel> find = new Model.Finder(Long.class, CompanyModel.class);

    /**
     * Retrieve all users.
     */
    public static List<CompanyModel> findAll() {
        return find.all();
    }

    public String validate() {
        if (StrUtil.isNull(name)) {
            return "必须要有企业名称";
        }
        if (StrUtil.isNull(email)) {
            return "必须要有email";
        }
        if (StrUtil.isNull(phone)) {
            return "必须要有电话号码";
        }
        if (StrUtil.isNull(cellPhone)) {
            return "必须要有手机号码";
        }
        if (StrUtil.isNull(location)) {
            return "必须要有地址";
        }
        if (StrUtil.isNull(description1)) {
            return "必须要有企业描述";
        }
        return null;
    }

    @Override
    public String toString() {
        return "Company [name:" + name + "]";
    }
}
