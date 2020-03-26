package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "themes")
public class ThemeModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String name; // 名称

	@Lob
	public String images; // 图片

	@Column(columnDefinition = "Decimal(10,2)")
	public double price;// 单价

	public Long refProductId; // 所属产品的ID

	// @Required
	@JsonIgnore
	@ManyToOne
	public ProductModel product;// 所属的产品

	public String comment;

	public static Finder<Long, ThemeModel> find = new Finder(Long.class, ThemeModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<ThemeModel> findAll() {
		return find.all();
	}

	public String validate() {
		if (StrUtil.isNull(name)) {
			return "必须要有名称";
		}
		// if (product == null) {
		// return "必须要有所属的产品";
		// }
		return null;
	}

	@Override
	public String toString() {
		return "Theme [name:" + name + "]";
	}
}