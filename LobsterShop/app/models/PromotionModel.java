package models;

import LyLib.Interfaces.IConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "promotions")
public class PromotionModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String promotionType; // 类型 满减优惠, 首单优惠, 免运费

	public String title; // 名称

	@Column(columnDefinition = "Decimal(10,2)")
	public double reachMoney; // 满足金额

	@Column(columnDefinition = "Decimal(10,2)")
	public double discount; // 减少金额

	public String startTime; // 优惠开始时间

	public String endTime; // 优惠结束时间

	public boolean available = true; // 促销是否有效

	public String comment;

	@JsonIgnore
	@ManyToMany(targetEntity = models.OrderModel.class)
	public List<OrderModel> orders;

	public static Finder<Long, PromotionModel> find = new Finder(Long.class, PromotionModel.class);

	public static List<PromotionModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "Promotion [promotionType:" + promotionType + "]";
	}

}
