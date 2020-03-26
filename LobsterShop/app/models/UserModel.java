package models;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.OrderController;
import models.status.RegisterType;
import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserModel extends Model implements Serializable, IConst {

	@Id
	public Long id;

	public String loginName;

	public String nickname;

	public String signature;

	public String device;

	public String password;

	public String sessionid;

	public String phone;

	public String descriptions;

	public String wxOpenId;// 微信openid

	public String unionId;// 第三方注册返回id

	public RegisterType registerType;// 用户注册方式

	public String title;// job title

	public String createdAtStr;

	public String country;

	public String province;

	public String city;

	public String zone;

	public String area;

	public String location;

	public String headImgUrl;

	public int sex = 1; // 用户的性别, 值为1时是男性，值为2时是女性，值为0时是未知

	public int age;

	public int jifen = 0; // 用户积分

	@Column(columnDefinition = "Decimal(10,2)")
	public float jifenRate; // 积分比例

	@Formats.DateTime(pattern = "yyyy-MM-dd")
	public Date birthday; // 生日
	public String birthdayStr; // 生日

	public String registerIP;// 注册时候的IP
	public String registerIParea; // IP所在区域
	public String lastLoginIP;// 最后一次登录的IP(只是管理员登录才会记录)
	public String lastLoginIParea; // IP所在区域
	public String lastPayIP;// 最后一次支付的IP
	public String lastPayIParea; // IP所在区域

	// @Enumerated(EnumType.STRING)
	// public UserStatus status;
	//
	// @Enumerated(EnumType.STRING)
	// public UserRole userRole = UserRole.MEMBER;

	public int userStatus = 0; // 状态: 0正常, 1冻结, 2已删除

	public int userRole = 0; // 状态: 0普通用户, 1管理员, 2超级管理员

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	public List<FoodcommentModel> foodcomments; // 产品评论

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	public List<ShipInfoModel> shipInfos; // 收货地址

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
	public List<OrderModel> orders; // 用户订单

	@ManyToMany(targetEntity = models.CouponModel.class)
	public List<CouponModel> coupons; // 优惠券

	// 分销相关
	public Long uplineUserId = -1L; // 已废弃字段, 使用 refUplineUserId

	public String becomeDownlineTime; // 成为下线时间

	public boolean isReseller = false;// 分销许可

	public String resellerCode;// 分销码

	public String resellerCodeImage;// 分销二维码图片

	public Long refUplineUserId = -1L;// 上线用户ID(默认是上帝子民, 上帝不抽佣金)

	public String refUplineUserName; // 上线用户name

	public String refUplineUserHeadImgUrl; // 上线用户headimgurl

	// @JsonIgnore
	// @ManyToOne
	// public UserModel uplineUser; // 上线用户

	// @JsonIgnore
	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "uplineUser")
	// public List<UserModel> downlineUsers; // 下线用户

	@Column(columnDefinition = "Decimal(10,2)")
	public double currentTotalOrderAmount = 0; // 当前有效订单总额

	@Column(columnDefinition = "Decimal(10,2)")
	public double currentResellerAvailableAmount = 0; // 当前有效分销额

	@Column(columnDefinition = "Decimal(10,2)")
	public double currentResellerProfit = 0; // 当前分销佣金

	public UserModel() {
		createdAtStr = DateUtil.Date2Str(new Date());
	}

	public boolean setReseller(String uplineResellerCode) {
		if (!StrUtil.isNull(uplineResellerCode)) {
			UserModel uplineUser = UserModel.find.where().eq("resellerCode", uplineResellerCode).findUnique();
			if (uplineUser == null) {
				return false;
			}
			if (uplineUser.userStatus > 0) {// 已冻结, 已删除用户不能作为上线
				return false;
			}
			if (uplineUser.isReseller && uplineUser.refUplineUserId != id && !uplineResellerCode.equals(resellerCode)) {// 防止互相加上下线循环
				refUplineUserId = uplineUser.id;
				refUplineUserName = uplineUser.nickname;
				refUplineUserHeadImgUrl = uplineUser.headImgUrl;
				return true;
			}
		}
		return false;
	}

	// -- Queries

	public static Model.Finder<Long, UserModel> find = new Model.Finder(Long.class, UserModel.class);

	/**
	 * Retrieve all users.
	 */
	public static List<UserModel> findAll() {
		return find.all();
	}

	// 处理积分
	public void handleJifen(Integer activePoint) {
		this.jifen += activePoint;
	}

	// 生成分销码
	public static String generateResellerCode() {
		// 时间+4位字母
		String code = DateUtil.Date2Str(new Date(), "yyyyMMddHHmmss") + OrderController.getRamdonLetter()
				+ OrderController.getRamdonLetter() + OrderController.getRamdonLetter()
				+ OrderController.getRamdonLetter();
		play.Logger.error(DateUtil.Date2Str(new Date()) + " - create reseller code: " + code);
		return code;
	}

	// public static String domainName = "http://192.168.0.87:9000";
//	public static String domainName = "http://www.longxin9.com/w/wine";

	// 生成分销二维码
	// public static String generateResellerCodeBarcode(String code) throws
	// Exception {
	// String text = domainName + "?resellerCode=" + code;
	// String path = Play.application().path().getPath() + "/public/barcode/";
	//
	// // 二维码的图片格式
	// int width = 300;
	// int height = 300;
	// String format = "gif";
	// Hashtable hints = new Hashtable();
	//
	// // 内容所使用编码
	// hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	// BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
	// BarcodeFormat.QR_CODE, width, height, hints);
	// // 生成二维码
	// File outputFile = new File(path + code + ".gif");
	// MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
	//
	// play.Logger.error(DateUtil.Date2Str(new Date()) + " - create reseller
	// barcode: " + code + ".gif");
	// return code + ".gif";
	// }

	// 从微信获取专用二维码, 用户扫码后进入公众号, 关注后进商城
	// public static String generateResellerCodeBarcode(String code) throws
	// Exception {
	// String text = domainName + "?resellerCode=" + code;
	// String path = Play.application().path().getPath() + "/public/barcode/";
	//
	// // 二维码的图片格式
	// int width = 300;
	// int height = 300;
	// String format = "gif";
	// Hashtable hints = new Hashtable();
	//
	// // 内容所使用编码
	// hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	// BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
	// BarcodeFormat.QR_CODE, width, height, hints);
	// // 生成二维码
	// File outputFile = new File(path + code + ".gif");
	// MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
	//
	// play.Logger.error(DateUtil.Date2Str(new Date()) + " - create reseller
	// barcode: " + code + ".gif");
	// return code + ".gif";
	// }

	/**
	 * Retrieve a User from loginName.
	 */
	public static UserModel findByloginName(String loginName) {
		if (StrUtil.isNull(loginName)) {
			return null;
		}
		return find.where().eq(SESSION_USER_NAME, loginName).findUnique();
	}

	public static UserModel findBySession(String sessionid) {
		return find.where().eq("sessionid", sessionid).findUnique();
	}

	/**
	 * Authenticate a User.
	 */
	public static UserModel authenticate(String loginName, String password) {
		return find.where().eq(SESSION_USER_NAME, loginName).eq("password", password).eq("userStatus", 0).findUnique();
	}

	public static List<UserModel> findByRole(String role, Long page) {
		return find.where().eq("userRole", role).findPagingList(AMOUNT_PER_PAGE).getPage(page.intValue() - 1).getList();
	}

	public static boolean verifyUser(String loginName) {
		return find.where().eq(SESSION_USER_NAME, loginName).findUnique() == null;
	}

	@Override
	public String toString() {
		return "User [name:" + nickname + "]";
	}

	public static UserModel get3PartyUser(String registerType, String unionId) {
		if (StrUtil.isNull(registerType) || StrUtil.isNull(unionId))
			return null;

		return find.where().eq("registerType", RegisterType.valueOf(registerType)).eq("unionId", unionId).findUnique();
	}
}
