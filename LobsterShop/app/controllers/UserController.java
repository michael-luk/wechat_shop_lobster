package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import LyLib.Utils.StrUtil;
import models.CatalogModel;
import models.CouponModel;
import models.OrderModel;
import models.ProductModel;
import models.ThemeModel;
import models.UserModel;
import models.UserModel;
import models.status.UserRole;
import models.status.UserStatus;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.user_backend;

public class UserController extends Controller implements IConst {

	@Security.Authenticated(SecuredAdmin.class)
	public static Result backendPage() {
		return ok(user_backend.render());
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result updateUserStatus(long id, int userStatus) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		// 更改用户状态
		if (userStatus == 0 || userStatus == 1 || userStatus == 2 || userStatus == 3 || userStatus == 4) {
			found.userStatus = userStatus;
			Ebean.update(found);

			// 密码不返回
			found.password = "";

			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
		} else {
			msg.message = "状态不对";
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}

		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result getAllUsers(String keyword, Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<UserModel>> msg = new Msg<>();
		Page<UserModel> records;

		if (StrUtil.isNull(keyword)) {
			records = UserModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
		} else {
			records = UserModel.find.where()
					.or(Expr.like("nickname", "%" + keyword + "%"), Expr.like("resellerCode", "%" + keyword + "%"))
					.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
		}

		if (records.getTotalRowCount() > 0) {
			msg.flag = true;

			PageInfo pageInfo = new PageInfo();
			pageInfo.current = page;
			pageInfo.total = records.getTotalPageCount();
			pageInfo.desc = records.getDisplayXtoYofZ("-", "/");
			pageInfo.size = size;
			if (records.hasPrev())
				pageInfo.hasPrev = true;
			if (records.hasNext())
				pageInfo.hasNext = true;

			// 密码不返回
			List<UserModel> users = records.getList();
			for (int i = 0; i < users.size(); i++) {
				users.get(i).password = "";
			}
			msg.data = users;
			msg.page = pageInfo;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + size);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result updateUser(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		Form<UserModel> httpForm = form(UserModel.class).bindFromRequest();

		if (!httpForm.hasErrors()) {
			UserModel formObj = httpForm.get();

			// 逐个赋值
			found.jifen = formObj.jifen;
			found.isReseller = formObj.isReseller;
			found.refUplineUserId = formObj.refUplineUserId;
			found.refUplineUserName = formObj.refUplineUserName;
			found.refUplineUserHeadImgUrl = formObj.refUplineUserHeadImgUrl;
			Ebean.update(found);

			// 密码不返回
			found.password = "";

			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
		} else {
			msg.message = httpForm.errors().toString();
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result delete(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		if (found != null) {
			// 解除多对多的关联
			for (CouponModel coupon : found.coupons) {
				coupon.users.remove(found);
				Ebean.update(coupon);
			}
			found.coupons = new ArrayList<>();
			Ebean.update(found);
			Ebean.delete(found);
			msg.flag = true;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + DELETE_SUCCESS);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result getDownLineUsers(Long id, Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<UserModel>> msg = new Msg<>();

		Integer firstDownlineCount = 0;
		Integer secondDownlineCount = 0;
		// Integer thirdDownlineCount = 0;
		// 一级下线
		List<UserModel> records1 = UserModel.find.where().eq("refUplineUserId", id).orderBy("becomeDownlineTime desc")
				.findList();
		firstDownlineCount = records1.size();
		// 二级下线
		for (int i = 0; i < records1.size(); i++) {
			List<UserModel> records2 = UserModel.find.where().eq("refUplineUserId", records1.get(i).id)
					.orderBy("becomeDownlineTime desc").findList();
			secondDownlineCount += records2.size();

		}

		if (records1.size() > 0) {
			msg.flag = true;
			msg.data = records1;
			msg.message = firstDownlineCount.toString();
			msg.message1 = secondDownlineCount.toString();
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records1.size());
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		Page<UserModel> records;
		records = UserModel.find.where().eq("refUplineUserId", id).orderBy("becomeDownlineTime desc")
				.findPagingList(size).setFetchAhead(false).getPage(page - 1);

		if (records.getTotalRowCount() > 0) {
			msg.flag = true;

			PageInfo pageInfo = new PageInfo();
			pageInfo.current = page;
			pageInfo.total = records.getTotalPageCount();
			pageInfo.desc = records.getDisplayXtoYofZ("-", "/");
			pageInfo.size = size;
			if (records.hasPrev())
				pageInfo.hasPrev = true;
			if (records.hasNext())
				pageInfo.hasNext = true;

			// 密码不返回
			List<UserModel> users = records.getList();
			for (int i = 0; i < users.size(); i++) {
				users.get(i).password = "";
			}
			msg.data = users;
			msg.page = pageInfo;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records.getTotalRowCount());
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	// @Security.Authenticated(SecuredAdmin.class)
	// public static Result getResellerOrders(Long id, Integer page, Integer
	// size) {
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - " +
	// request().method() + ": " + request().uri()
	// + " | DATA: " + request().body().asJson());
	// if (size == 0)
	// size = PAGE_SIZE;
	// if (page <= 0)
	// page = 1;
	//
	// Msg<List<OrderModel>> msg = new Msg<>();
	// Page<OrderModel> records;
	//
	// records = OrderModel.find.where().eq("refResellerId", id).orderBy("id
	// desc").findPagingList(size)
	// .setFetchAhead(false).getPage(page - 1);
	//
	// if (records.getTotalRowCount() > 0) {
	// msg.flag = true;
	//
	// PageInfo pageInfo = new PageInfo();
	// pageInfo.current = page;
	// pageInfo.total = records.getTotalPageCount();
	// pageInfo.desc = records.getDisplayXtoYofZ("-", "/");
	// pageInfo.size = size;
	// if (records.hasPrev())
	// pageInfo.hasPrev = true;
	// if (records.hasNext())
	// pageInfo.hasNext = true;
	//
	// msg.data = records.getList();
	// msg.page = pageInfo;
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// records.getTotalRowCount());
	// } else {
	// msg.message = NO_FOUND;
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// msg.message);
	// }
	// return ok(Json.toJson(msg));
	// }

	@Security.Authenticated(Secured.class)
	public static Result getUser(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

	/*	UserModel found;
		if(storeId > 0){
			found = UserModel.find.where().and(Expr.eq("id",id),Expr.eq("shipInfos.storeId",storeId)).findUnique();
		}else{
			found = UserModel.find.byId(id);
		}*/
		UserModel found = UserModel.find.byId(id);
		if (found != null) {
			// 密码不返回
			found.password = "";

			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + found);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result updateStatus(long id, int status) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		found.userStatus = status;
		Ebean.update(found);

		// 密码不返回
		found.password = "";

		msg.flag = true;
		msg.data = found;
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
		return ok(Json.toJson(msg));
	}

	public static Result getCurrentUser(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();
		UserModel found;

		if (id == 0) {
			if (StrUtil.isNull(session(SESSION_USER_ID))) {
				msg.message = "请点击商城首页进入";// 针对微信商城专用 //NO_LOGIN
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
				return ok(Json.toJson(msg));
			} else {
				Long uid = 0l;
				try {
					uid = Long.parseLong(session(SESSION_USER_ID));
				} catch (NumberFormatException ex) {
					msg.message = NO_LOGIN + ",ID有误";
					play.Logger
							.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message + " - exception: " + ex);
					return ok(Json.toJson(msg));
				}
				found = UserModel.find.byId(uid);
			}
		} else {
			found = UserModel.find.byId(id);
		}

		if (found == null) {
			msg.message = "用户" + NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		// 密码不返回
		found.password = "";

		msg.flag = true;
		msg.data = found;
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + found);
		return ok(Json.toJson(msg));
	}

	// 根据传入的微信openid来检查系统是否已经注册了这个微信用户
	@Security.Authenticated(SecuredAdmin.class)
	public static Result checkWeixinUser(String openId) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		if (StrUtil.isNull(openId)) {
			msg.message = PARAM_ISSUE;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		UserModel found = UserModel.find.where().eq("wxOpenId", openId).findUnique();
		if (found == null) {
			// session(SESSION_USER_NAME, "");
			session(SESSION_USER_ID, "");

			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		// 密码不返回
		found.password = "";

		msg.flag = true;
		msg.data = found;

		// session(SESSION_USER_NAME, found.nickname);
		session(SESSION_USER_ID, found.id.toString());

		play.Logger.info(DateUtil.Date2Str(new Date()) + " - 检测到微信用户: " + session(SESSION_USER_ID));
		return ok(Json.toJson(msg));
	}

	public static Result addWeixinUser(String openId) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<UserModel> msg = new Msg<>();

		if (StrUtil.isNull(openId)) {
			msg.message = PARAM_ISSUE;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		UserModel newObj = new UserModel();
		newObj.wxOpenId = openId;
		newObj.sex = 0;
		newObj.resellerCode = UserModel.generateResellerCode();// 分销码, 需自动生成
		try {
			newObj.resellerCodeImage = WeiXinController.generateResellerCodeBarcode(newObj.resellerCode);
		} catch (Exception e) {
			play.Logger.error(DateUtil.Date2Str(new Date()) + " - error on create reseller barcode: " + e.getMessage());
		} // 分销二维码, 需自动生成
		newObj.refUplineUserId = 0l;// 上线用户
		Ebean.save(newObj);

		msg.flag = true;
		msg.data = newObj;

		// session(SESSION_USER_NAME, found.nickname);
		session(SESSION_USER_ID, newObj.id.toString());

		play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
		return ok(Json.toJson(msg));
	}
}
