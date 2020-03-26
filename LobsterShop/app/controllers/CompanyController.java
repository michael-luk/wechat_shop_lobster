package controllers;

import static play.data.Form.form;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import models.InfoModel;
import models.common.CompanyModel;
import models.common.ContentModel;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.company_backend;

public class CompanyController extends Controller implements IConst {

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result backendPage() {
		return ok(company_backend.render());
	}

	// @Security.Authenticated(Secured.class)
	public static Result getCompanyInfo() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());

		Msg<CompanyModel> msg = new Msg<>();
		List<CompanyModel> list = CompanyModel.findAll();

		if (list.size() > 0) {
			msg.flag = true;
			msg.data = list.get(0);
			play.Logger.info(msg.data.toString());
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(msg.message);
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(SecuredSuperAdmin.class)
	public static Result updateCompany(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<CompanyModel> msg = new Msg<>();

		CompanyModel found = CompanyModel.find.byId(id);
		if (found == null) {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			return ok(Json.toJson(msg));
		}

		Form<CompanyModel> httpForm = form(CompanyModel.class).bindFromRequest();

		if (!httpForm.hasErrors()) {
			CompanyModel formObj = httpForm.get();

			// 逐个赋值
			found.name = formObj.name;
			found.location = formObj.location;
			found.email = formObj.email;
			found.phone = formObj.phone;
			found.cellPhone = formObj.cellPhone;
			found.weibo = formObj.weibo;
			found.qq = formObj.qq;
			found.weixin = formObj.weixin;
			found.description1 = formObj.description1;
			found.description2 = formObj.description2;
			found.registerInfo1 = formObj.registerInfo1;
			found.registerInfo2 = formObj.registerInfo2;
			found.barcodeImg1 = formObj.barcodeImg1;
			found.barcodeImg2 = formObj.barcodeImg2;
			found.barcodeImg3 = formObj.barcodeImg3;
			found.logo1 = formObj.logo1;
			found.logo2 = formObj.logo2;
			found.logo3 = formObj.logo3;
			found.marketing1 = formObj.marketing1;
			found.marketing2 = formObj.marketing2;
			found.marketing3 = formObj.marketing3;
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
