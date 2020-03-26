package models;

import LyLib.Interfaces.IConst;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shoppingcart")
public class ShoppingCartModel extends Model implements IConst {

	@Id
	public Long id;

	public Long refBuyerId;// 用户ID

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
	public List<ShoppingCartItemModel> items;

	public int totalQuantity; // 购物车商品总数量

	@Column(columnDefinition = "Decimal(10,2)")
	public double productAmount; // 商品总额

	public static Finder<Long, ShoppingCartModel> find = new Finder(Long.class, ShoppingCartModel.class);

	public static List<ShoppingCartModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "ShoppingCart [totalQuantity:" + totalQuantity + "]";
	}

	public String validate() {
		// if (adultNumber < 2) {
		// return "订单两个成人起订";
		// }
		// if (amount <= 0) {
		// return "订单总额必须大于0";
		// }
		return null;
	}
}
