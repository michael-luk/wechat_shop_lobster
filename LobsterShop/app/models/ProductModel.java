package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String productNo; // 产品编号

	public String name; // 名称

	public String unit; // 单位

	@Lob
	public String images; // 产品图

	@Lob
	public String shortDesc; // 简述

	@Column(columnDefinition = "Decimal(10,2)")
	public double price = 0D; // 价格

	@Column(columnDefinition = "Decimal(10,2)")
	public double originalPrice = 0D; // 原价

	public boolean isHotSale = false; // 是否热推

	public boolean isZhaoPai = false; // 招牌

	public int soldNumber; // 卖出数

	public int thumbUp; // 点赞数

	public int inventory; // 库存

	public String comment; // 备注

	public int status = 0; // 状态 0正常, 1隐藏, 2删除

	public String createdAtStr; // 创建日期字符串表示

//砍价
	public boolean isActiveProduct = false; // 是否设为活动产品

	@Column(columnDefinition = "Decimal(10,2)")
	public double bargainBottomLine = 0D; // 砍价底线

	public int sabreplayLimit; // 限制刀数

	public boolean activeState = false; // 活动状态

	public String activeTime;// 活动时间

	public int activeSoldNumber; // 活动卖出数
//砍价end
	@ManyToMany(targetEntity = models.CatalogModel.class)
	public List<CatalogModel> catalogs; // 所属主题

	@JsonIgnore
	@ManyToMany(targetEntity = models.OrderModel.class)
	public List<OrderModel> orders; // 产品对应的订单

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	public List<ThemeModel> themes; // 产品口味

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	public List<FoodcommentModel> foodcomments; // 美食评价

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	public List<ShoppingCartItemModel> cartItems; // 购物车商品

	public Long refStoreId; // 所属店面的ID

	@ManyToOne
	public StoreModel store;// 所属的店面

	public ProductModel() {
		createdAtStr = DateUtil.Date2Str(new Date());
	}

	// -- Queries

	public static Finder<Long, ProductModel> find = new Finder(Long.class, ProductModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<ProductModel> findAll() {
		return find.all();
	}

	public String toString() {
		return "Product [name:" + name + "]";
	}

	// public String validate() {
	// if (name == null) {
	// return "必须要有名称或标题";
	// }
	// if (ml == 0) {
	// return "必须要有容量信息";
	// }
	// if (flavor == null) {
	// return "必须要有风味信息";
	// }
	// if (price == 0) {
	// return "必须要有价格信息";
	// }
	// return null;
	// }
}
