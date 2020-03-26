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
import LyLib.Utils.StrUtil;
import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "shipinfos")
public class ShipInfoModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public Long refUserId; // 所属用户ID

	public Long storeId; // 店铺ID

	@JsonIgnore
	@ManyToOne
	public UserModel user;// 所属的用户

	public boolean isDefault = false;// 是否默认

	public String name; // 名称

	public String phone; // 联系电话

	public String postCode; // 邮编

	public String provice; // 省

	public String city; // 市

	public String zone; // 区

	public String area; // 地址

	public String location; // 详细地址

	public String comment;

	public static Finder<Long, ShipInfoModel> find = new Finder(Long.class, ShipInfoModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<ShipInfoModel> findAll() {
		return find.all();
	}

	public String validate() {
		if (StrUtil.isNull(name)) {
			return "必须要有联系名称";
		}
		if (StrUtil.isNull(phone)) {
			return "必须要有联系电话";
		}
		// if (StrUtil.isNull(postCode)) {
		// return "必须要有地址邮编";
		// }
		// if (StrUtil.isNull(provice)) {
		// return "必须要有省份信息";
		// }
		if (StrUtil.isNull(location)) {
			return "必须要有详细地址";
		}
		return null;
	}

	@Override
	public String toString() {
		return "ShipInfo [name:" + name + "]";
	}
}