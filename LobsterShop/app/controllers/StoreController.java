package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import models.StoreModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.store_backend;

import java.util.Date;
import java.util.List;

import static play.data.Form.form;

public class StoreController extends Controller implements IConst {

	@Security.Authenticated(SecuredAdmin.class)
	public static Result backendPage() {
		return ok(store_backend.render());
	}

	@Security.Authenticated(Secured.class)
	public static Result getAll(Long storeId, Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<StoreModel>> msg = new Msg<>();
		Page<StoreModel> records;

		if(storeId > 0){
			records = StoreModel.find.where().eq("id",storeId).orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
		}else{
			records = StoreModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
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
		Msg<StoreModel> msg = new Msg<>();

		Form<StoreModel> httpForm = form(StoreModel.class).bindFromRequest();
		if (!httpForm.hasErrors()) {
			StoreModel formObj = httpForm.get();
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
		Msg<StoreModel> msg = new Msg<>();

		StoreModel found = StoreModel.find.byId(id);
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
		Msg<StoreModel> msg = new Msg<>();

		StoreModel found = StoreModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		Form<StoreModel> httpForm = form(StoreModel.class).bindFromRequest();

		if (!httpForm.hasErrors()) {
			StoreModel formObj = httpForm.get();

			// 逐个赋值
			found.area = formObj.area;
			found.phone = formObj.phone;
			found.mailbox = formObj.mailbox;
			found.displayDate = formObj.displayDate;
			found.description1 = formObj.description1;
			found.description2 = formObj.description2;
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

	@Security.Authenticated(Secured.class)
	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<StoreModel> msg = new Msg<>();

		StoreModel found = StoreModel.find.byId(id);
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
}
