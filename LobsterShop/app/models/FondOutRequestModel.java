package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "fondoutrequests")
public class FondOutRequestModel extends Model implements IConst {

	@Id
	public Long id;

	public Long refUserId; // 所属用户ID

	public String phone; // 联系电话

	@Column(columnDefinition = "Decimal(10,2)")
	public double yongJin; // 佣金

	public String createdAtStr; // 创建日期字符串表示

	public int status; // 0-待结算, 1-已结算

	public String comment; // 备注

	public FondOutRequestModel() {
		createdAtStr = DateUtil.Date2Str(new Date());
	}

	public static Finder<Long, FondOutRequestModel> find = new Finder(Long.class, FondOutRequestModel.class);

	/**
	 * Retrieve all .
	 */
	public static List<FondOutRequestModel> findAll() {
		return find.all();
	}

	@Override
	public String toString() {
		return "FondOutRequest [refUserId:" + refUserId + "]";
	}
}
