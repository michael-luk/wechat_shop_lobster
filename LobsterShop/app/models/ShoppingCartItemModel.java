package models;

import LyLib.Interfaces.IConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "shoppingcartitem")
public class ShoppingCartItemModel extends Model implements IConst {

    @Id
    public Long id;

    @JsonIgnore
    @ManyToOne
    public ProductModel product; // 产品

    public int quantity;// 数量

    public String themeName; // 产品口味

    @JsonIgnore
    @ManyToOne
    public ShoppingCartModel cart;// 所属的购物车

    public ShoppingCartItemModel(ProductModel product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static Finder<Long, ShoppingCartItemModel> find = new Finder(Long.class, ShoppingCartItemModel.class);

    public static List<ShoppingCartItemModel> findAll() {
        return find.all();
    }

    @Override
    public String toString() {
        return String.format("ShoppingCartItem - pid: %s, pname: %s, pnum: %d", product.id, product.name, quantity);
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
