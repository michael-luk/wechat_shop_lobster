package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import LyLib.Utils.StrUtil;
import models.CatalogModel;
import models.OrderModel;
import models.ShipAreaPriceModel;
import models.ShipInfoModel;
import models.ThemeModel;
import models.FondOutRequestModel;
import models.InfoModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.fondOutRequest_backend;

public class FondOutRequestController extends Controller implements IConst {

	@Security.Authenticated(SecuredAdmin.class)
	public static Result backendPage() {
		return ok(fondOutRequest_backend.render());
	}

	@Security.Authenticated(Secured.class)
	public static Result add() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FondOutRequestModel> msg = new Msg<>();

		Form<FondOutRequestModel> httpForm = form(FondOutRequestModel.class).bindFromRequest();
		if (!httpForm.hasErrors()) {
			FondOutRequestModel formObj = httpForm.get();
			UserModel found = UserModel.find.byId(formObj.refUserId);

			if (found != null) {
				if (formObj.yongJin > found.currentResellerProfit) {
					play.Logger.error("*******提款状态异常, 须检查. 发起IP: " + request().remoteAddress());
					return notFound("提款状态异常");
				}
				found.currentResellerProfit = 0;
				Ebean.update(found);
			}
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

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result delete(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FondOutRequestModel> msg = new Msg<>();

		FondOutRequestModel found = FondOutRequestModel.find.byId(id);
		if (found != null) {
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
	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FondOutRequestModel> msg = new Msg<>();

		FondOutRequestModel found = FondOutRequestModel.find.byId(id);

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

	@Security.Authenticated(Secured.class)
	public static Result getAll(Long refUserId, Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<FondOutRequestModel>> msg = new Msg<>();
		Page<FondOutRequestModel> records;

		if (refUserId == 0) {
			records = FondOutRequestModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false)
					.getPage(page - 1);
		} else {
			records = FondOutRequestModel.find.where().eq("refUserId", refUserId).orderBy("id desc")
					.findPagingList(size).setFetchAhead(false).getPage(page - 1);
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
	public static Result update(int status, long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FondOutRequestModel> msg = new Msg<>();

		FondOutRequestModel found = FondOutRequestModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		if (status == 0 || status == 1) {
			found.status = status;
			Ebean.update(found);

			msg.flag = true;
			msg.data = found;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
		} else {
			msg.message = "状态不对";
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}

		return ok(Json.toJson(msg));
	}
}
