package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.StrUtil;
import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "infos")
public class InfoModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String classify;// 分类

	public String name; // 名称

	public String phone; // 联系电话

	public String url; // url

	@Lob
	public String images; // 图片(多个图片逗号分隔)

	@Lob
	public String smallImages; // 小图片

	public String displayDate; // 日期字符串表示

	public String createdAtStr; // 创建日期字符串表示

	@Lob
	public String description1;

	@Lob
	public String description2;

	public InfoModel() {
		createdAtStr = DateUtil.Date2Str(new Date());
	}

	public static Finder<Long, InfoModel> find = new Finder(Long.class, InfoModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<InfoModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "Info [name:" + name + "]";
	}

	public String validate() {
		// if (StrUtil.isNull(name)) {
		// return "必须要有名称或标题";
		// }
		return null;
	}
}