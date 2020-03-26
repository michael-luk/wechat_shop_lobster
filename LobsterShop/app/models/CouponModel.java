package models;

import LyLib.Interfaces.IConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "coupons")
public class CouponModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	@Column(columnDefinition = "Decimal(10,2)")
	public double reachMoney; // 满足金额

	@Column(columnDefinition = "Decimal(10,2)")
	public double discount; // 减少金额

	public String startTime; // 优惠开始时间

	public String endTime; // 优惠结束时间

	public String comment;

	@JsonIgnore
	@ManyToMany(targetEntity = models.UserModel.class)
	public List<UserModel> users;// 所属的用户

	public static Finder<Long, CouponModel> find = new Finder(Long.class, CouponModel.class);

	public static List<CouponModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "CouponModel [reachMoney=" + reachMoney + "]";
	}

}
