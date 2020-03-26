package models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import LyLib.Interfaces.IConst;
import play.db.ebean.Model;

@Entity
@Table(name = "catalogs")
public class CatalogModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public int catalogIndex;// 顺序

	public String name; // 名称

	@Lob
	public String images; // 图片

	@Lob
	public String smallImages; // 小图片

	public String comment;

	@JsonIgnore
	@ManyToMany(targetEntity = models.ProductModel.class)
	public List<ProductModel> products; // 分类下产品

	public static Finder<Long, CatalogModel> find = new Finder(Long.class, CatalogModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<CatalogModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "Catalog [name:" + name + "]";
	}
}