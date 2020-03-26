package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import models.InfoModel;
import models.OrderModel;
import models.UserModel;
import models.CouponModel;
import models.FoodcommentModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

public class CouponController extends Controller implements IConst {

	@Security.Authenticated(SecuredAdmin.class)
	public static Result backendPage() {
		return ok(coupon_backend.render());
	}

	@Security.Authenticated(Secured.class)
	public static Result getAll(Long userId, Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<CouponModel>> msg = new Msg<>();
		Page<CouponModel> records;

		if (userId > 0) {
			records = CouponModel.find.where().eq("users.id", userId).orderBy("id desc").findPagingList(size)
					.setFetchAhead(false).getPage(page - 1);
		} else {
			records = CouponModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
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

			msg.data = records.getList();
			msg.page = pageInfo;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records.getTotalRowCount());
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result add() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<CouponModel> msg = new Msg<>();

		Form<CouponModel> httpForm = form(CouponModel.class).bindFromRequest();
		if (!httpForm.hasErrors()) {
			CouponModel formObj = httpForm.get();
			Ebean.save(formObj);

			msg.flag = true;
			msg.data = formObj;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
		} else {
			msg.message = httpForm.errors().toString();
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<CouponModel> msg = new Msg<>();

		CouponModel found = CouponModel.find.byId(id);
		if (found != null) {
			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + found);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result delete(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<CouponModel> msg = new Msg<>();

		CouponModel found = CouponModel.find.byId(id);
		if (found != null) {
			for (UserModel user : found.users) {
				user.coupons.remove(found);
				Ebean.update(user);
			}
			found.users = new ArrayList<>();
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

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result update(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<CouponModel> msg = new Msg<>();

		CouponModel found = CouponModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		Form<CouponModel> httpForm = form(CouponModel.class).bindFromRequest();

		if (!httpForm.hasErrors()) {
			CouponModel formObj = httpForm.get();

			// 逐个赋值
			found.reachMoney = formObj.reachMoney;
			found.discount = formObj.discount;
			found.startTime = formObj.startTime;
			found.endTime = formObj.endTime;
			found.comment = formObj.comment;
			Ebean.update(found);

			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
		} else {
			msg.message = httpForm.errors().toString();
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

}
