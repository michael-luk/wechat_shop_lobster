package controllers;

import static play.data.Form.form;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import models.ShipInfoModel;
import models.ShoppingCartModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.theme_backend;

public class ShoppingCartController extends Controller implements IConst {

	public static Result backendPage() {
		return ok(theme_backend.render());
	}

	// @Security.Authenticated(SecuredAPI.class)
	// public static Result add() {
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - " +
	// request().method() + ": " + request().uri()
	// + " | DATA: " + request().body().asJson());
	// Msg<ShoppingCartModel> msg = new Msg<>();
	//
	// Form<ShoppingCartModel> httpForm =
	// form(ShoppingCartModel.class).bindFromRequest();
	// if (!httpForm.hasErrors()) {
	// ShoppingCartModel formObj = httpForm.get();
	// Ebean.save(formObj);
	//
	// msg.flag = true;
	// msg.data = formObj;
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// CREATE_SUCCESS);
	// } else {
	// msg.message = httpForm.errors().toString();
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// msg.message);
	// }
	// return ok(Json.toJson(msg));
	// }

	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ShoppingCartModel> msg = new Msg<>();

		List<ShoppingCartModel> foundList = ShoppingCartModel.find.where().eq("refBuyerId", id).orderBy("id desc")
				.findList();
		if (foundList.size() > 0) {
			msg.flag = true;
			msg.data = foundList.get(0);
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.data);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - shoppingcart result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	public static Result getAll() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<List<ShoppingCartModel>> msg = new Msg<>();

		List<ShoppingCartModel> foundList = ShoppingCartModel.findAll();

		if (foundList.size() > 0) {
			msg.flag = true;
			msg.data = foundList;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.data);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - shoppingcart result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	// public static Result update(long id) {
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - " +
	// request().method() + ": " + request().uri()
	// + " | DATA: " + request().body().asJson());
	// Msg<ShoppingCartModel> msg = new Msg<>();
	//
	// ShoppingCartModel found = ShoppingCartModel.find.byId(id);
	// if (found == null) {
	// msg.message = NO_FOUND;
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// msg.message);
	// return ok(Json.toJson(msg));
	// }
	//
	// Form<ShoppingCartModel> httpForm =
	// form(ShoppingCartModel.class).bindFromRequest();
	//
	// if (!httpForm.hasErrors()) {
	// ShoppingCartModel formObj = httpForm.get();
	//
	// // 逐个赋值
	// found.name = formObj.name;
	// found.images = formObj.images;
	// found.comment = formObj.comment;
	// Ebean.update(found);
	//
	// msg.flag = true;
	// msg.data = found;
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// UPDATE_SUCCESS);
	// } else {
	// msg.message = httpForm.errors().toString();
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " +
	// msg.message);
	// }
	// return ok(Json.toJson(msg));
	// }

}
