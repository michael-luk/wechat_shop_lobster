package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import com.avaje.ebean.Expr;
import models.CatalogModel;
import models.InfoModel;
import models.ProductModel;
import models.ShipInfoModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class ShipInfosController extends Controller implements IConst {

	@Security.Authenticated(Secured.class)
	public static Result getAllShipInfos(long id,Long storeId) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<List<ShipInfoModel>> msg = new Msg<>();

		List<ShipInfoModel> found;
		if(storeId > 0){
			found = ShipInfoModel.find.where().and(Expr.eq("refUserId",id),Expr.eq("storeId",storeId)).findList();
		}else{
			found = ShipInfoModel.find.where().eq("storeId",storeId).findList();
		}
		if (found != null) {
				msg.flag = true;
				msg.data = found;
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + found);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - shipInfo result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result addShipInfo(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ShipInfoModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		if (found != null) {
			Form<ShipInfoModel> httpForm = form(ShipInfoModel.class).bindFromRequest();
			if (!httpForm.hasErrors()) {
				ShipInfoModel formObj = httpForm.get();

				// 若当前地址设为默认, 则要去掉现存的地址的默认
				if (formObj.isDefault) {
					List<ShipInfoModel> allObjs = ShipInfoModel.find.where().eq("refUserId", id).findList();
					if (allObjs.size() > 0) {
						for (ShipInfoModel obj : allObjs) {
							if (obj.isDefault) {
								obj.isDefault = false;
								Ebean.update(obj);
							}
						}
					}
				}

				found.shipInfos.add(formObj);
				formObj.refUserId = found.id;
				formObj.user = found;
				Ebean.update(found);
				Ebean.save(formObj);

				msg.flag = true;
				msg.data = formObj;
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
			} else {
				msg.message = httpForm.errors().toString();
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			}
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - shipInfo result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result deleteShipInfo(long id, long sid) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ShipInfoModel> msg = new Msg<>();

		UserModel user = UserModel.find.byId(id);
		ShipInfoModel shipInfo = ShipInfoModel.find.byId(sid);
		if (user != null && shipInfo != null) {
			Ebean.delete(shipInfo);
			msg.flag = true;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + DELETE_SUCCESS);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result updateShipInfo(long id, long sid) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ShipInfoModel> msg = new Msg<>();

		UserModel user = UserModel.find.byId(id);
		ShipInfoModel shipInfo = ShipInfoModel.find.byId(sid);
		if (user != null && shipInfo != null) {
			Form<ShipInfoModel> httpForm = form(ShipInfoModel.class).bindFromRequest();

			if (!httpForm.hasErrors()) {
				ShipInfoModel formObj = httpForm.get();

				if (formObj.isDefault) {
					List<ShipInfoModel> allObjs = ShipInfoModel.find.where().eq("refUserId", id).findList();
					if (allObjs.size() > 0) {
						for (ShipInfoModel obj : allObjs) {
							if (obj.isDefault) {
								obj.isDefault = false;
								Ebean.update(obj);
							}
						}
					}
				}

				// 逐个赋值
				shipInfo.name = formObj.name;
				shipInfo.isDefault = formObj.isDefault;
				shipInfo.phone = formObj.phone;
				shipInfo.postCode = formObj.postCode;
				shipInfo.provice = formObj.provice;
				shipInfo.city = formObj.city;
				shipInfo.zone = formObj.zone;
				shipInfo.area = formObj.area;
				shipInfo.location = formObj.location;
				shipInfo.comment = formObj.comment;
				Ebean.update(shipInfo);

				msg.flag = true;
				msg.data = shipInfo;
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
			} else {
				msg.message = httpForm.errors().toString();
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			}
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result getShipInfo(long id, long sid) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ShipInfoModel> msg = new Msg<>();

		UserModel user = UserModel.find.byId(id);
		ShipInfoModel shipInfo = ShipInfoModel.find.byId(sid);

		if (user != null && shipInfo != null) {
			msg.flag = true;
			msg.data = shipInfo;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + shipInfo);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}
}
