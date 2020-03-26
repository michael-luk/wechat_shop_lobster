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
import models.ThemeModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.theme_backend;

public class ThemeController extends Controller implements IConst {

	@Security.Authenticated(SecuredAdmin.class)
	public static Result backendPage() {
		return ok(theme_backend.render());
	}

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result add() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ThemeModel> msg = new Msg<>();

		Form<ThemeModel> httpForm = form(ThemeModel.class).bindFromRequest();
		if (!httpForm.hasErrors()) {
			ThemeModel formObj = httpForm.get();

			// 判断名称是否重复
			List<ThemeModel> themesOfParent = ThemeModel.find.where().eq("refProductId", formObj.refProductId)
					.findList();
			for (ThemeModel theme : themesOfParent) {
				if (formObj.name.equals(theme.name)) {
					msg.message = "名称重复";
					play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
					return ok(Json.toJson(msg));
				}
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

	@Security.Authenticated(Secured.class)
	public static Result getAll(Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<ThemeModel>> msg = new Msg<>();
		Page<ThemeModel> records;

		records = ThemeModel.find.orderBy("refProductId desc, id").findPagingList(size).setFetchAhead(false)
				.getPage(page - 1);

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

			// json里面增加产品名称
			ObjectNode resultJson = (ObjectNode) Json.toJson(msg);
			for (int i = 0; i < msg.data.size(); i++) {
				((ObjectNode) resultJson.path("data").get(i)).put("productName", msg.data.get(i).product.name);
			}
			return ok(resultJson);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}
	}

	@Security.Authenticated(Secured.class)
	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ThemeModel> msg = new Msg<>();

		ThemeModel found = ThemeModel.find.byId(id);
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
		Msg<ThemeModel> msg = new Msg<>();

		ThemeModel found = ThemeModel.find.byId(id);
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

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result update(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ThemeModel> msg = new Msg<>();

		ThemeModel found = ThemeModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		Form<ThemeModel> httpForm = form(ThemeModel.class).bindFromRequest();

		if (!httpForm.hasErrors()) {
			ThemeModel formObj = httpForm.get();

			// 逐个赋值
			found.name = formObj.name;
			found.images = formObj.images;
			found.price = formObj.price;
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
