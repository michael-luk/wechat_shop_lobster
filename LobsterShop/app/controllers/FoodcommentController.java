package controllers;

import static play.data.Form.form;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import models.FoodcommentModel;
import models.ProductModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.foodcomment_backend;

public class FoodcommentController extends Controller implements IConst {

	public static Result backendPage() {
		return ok(foodcomment_backend.render());
	}

	// @Security.Authenticated(SecuredAPI.class)
	public static Result add(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FoodcommentModel> msg = new Msg<>();

		UserModel found = UserModel.find.byId(id);
		Form<FoodcommentModel> httpForm = form(FoodcommentModel.class).bindFromRequest();
		if (found != null) {
			if (!httpForm.hasErrors()) {
				FoodcommentModel formObj = httpForm.get();
				Ebean.save(formObj);

				msg.flag = true;
				msg.data = formObj;
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
			}else {
				msg.message = httpForm.errors().toString();
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
			}
		}  else if(found == null){
			if (!httpForm.hasErrors()) {
				FoodcommentModel formObj = httpForm.get();
				ProductModel product = ProductModel.find.byId(formObj.refProductId);
				formObj.product = product;
				Ebean.save(formObj);

				msg.flag = true;
				msg.data = formObj;
				play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
			}
		}else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - shipInfo result: " + NO_FOUND);
		}
		return ok(Json.toJson(msg));
	}

	public static Result getAll(Long productId, Integer page, Integer size, Long storeId) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<FoodcommentModel>> msg = new Msg<>();
		Page<FoodcommentModel> records;

		if (productId > 0 && storeId > 0) {
			records = FoodcommentModel.find.where().and(Expr.eq("product.refStoreId", storeId),Expr.eq("product.id", productId)).orderBy("id desc").findPagingList(size)
					.setFetchAhead(false).getPage(page - 1);
		} else {
			records = FoodcommentModel.find.where().eq("product.refStoreId", storeId).orderBy("id desc").findPagingList(size).setFetchAhead(false)
					.getPage(page - 1);
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

			// json里面增加产品名称
			ObjectNode resultJson = (ObjectNode) Json.toJson(msg);
            for (int i = 0; i < msg.data.size(); i++) {
               try {
                if(msg.data.get(i).product != null && msg.data.get(i).product.name != null){
                        ((ObjectNode) resultJson.path("data").get(i)).put("productName", msg.data.get(i).product.name);
                }else{
                    ((ObjectNode) resultJson.path("data").get(i)).put("productName", "无");
                }
                if( msg.data.get(i).user != null &&  msg.data.get(i).user.nickname != null){
                    ((ObjectNode) resultJson.path("data").get(i)).put("userName", msg.data.get(i).user.nickname);
                }else if( msg.data.get(i).user == null){
					((ObjectNode) resultJson.path("data").get(i)).put("userName", msg.data.get(i).initNickName);
				}else{
                    ((ObjectNode) resultJson.path("data").get(i)).put("userName", "无");
                }
               }catch (Exception ex){
                    play.Logger.error(DateUtil.Date2Str(new Date()) + " - result: " + msg.data.get(i));
                }
            }
			return ok(resultJson);
		} else {
			msg.message = NO_FOUND;
			play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
		}
		return ok(Json.toJson(msg));
	}

	public static Result get(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FoodcommentModel> msg = new Msg<>();

		FoodcommentModel found = FoodcommentModel.find.byId(id);
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

	@Security.Authenticated(SecuredAdmin.class)
	public static Result delete(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<FoodcommentModel> msg = new Msg<>();

		FoodcommentModel found = FoodcommentModel.find.byId(id);
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

}
