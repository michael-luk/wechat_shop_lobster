package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stores")
public class StoreModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String area; // 门店地区

	public String phone; // 联系电话

	public String mailbox; // 邮箱

	public String displayDate; // 日期字符串表示

	public String createdAtStr; // 创建日期字符串表示

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
	public List<ShipAreaPriceModel> shipAreaPrices; // 区域运费

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
	public List<ProductModel> products; // 产品

	@Lob
	public String description1;

	@Lob
	public String description2;

	public StoreModel() {
		createdAtStr = DateUtil.Date2Str(new Date());
	}

	public static Finder<Long, StoreModel> find = new Finder(Long.class, StoreModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<StoreModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "Store [area:" + area + "]";
	}

	public String validate() {
//		 if (StrUtil.isNull(name)) {
//		 return "必须要有名称或标题";
//		 }
		return null;
	}
}