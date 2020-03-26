/*
package controllers;

*/
/**
 * Created by Rouson on 2016/2/29.
 *//*

public class CommercialActivitiesController {
}
*/
package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.CatalogModel;
import models.CommercialActivitiesModel;
import models.InfoModel;
import models.UserModel;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by Rouson on 2016/2/22.
 */
public class CommercialActivitiesController extends Controller implements IConst {
    @Security.Authenticated(SecuredAdmin.class)
    public static Result backendPage() {
        return ok(LineActivities_backend.render());
    }

    // @Security.Authenticated(SecuredAPI.class)
    public static Result add() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<CommercialActivitiesModel> msg = new Msg<>();

        Form<CommercialActivitiesModel> httpForm = form(CommercialActivitiesModel.class).bindFromRequest();
        if (!httpForm.hasErrors()) {
            CommercialActivitiesModel formObj = httpForm.get();
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

    public static Result getAll(Integer page, Integer size) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        if (size == 0)
            size = PAGE_SIZE;
        if (page <= 0)
            page = 1;

        Msg<List<CommercialActivitiesModel>> msg = new Msg<>();
        Page<CommercialActivitiesModel> records;

            records = CommercialActivitiesModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);

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

    public static Result get(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<CommercialActivitiesModel> msg = new Msg<>();

        CommercialActivitiesModel found = CommercialActivitiesModel.find.byId(id);
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
        Msg<CommercialActivitiesModel> msg = new Msg<>();

        CommercialActivitiesModel found = CommercialActivitiesModel.find.byId(id);
        if (found != null) {
            try {
                Ebean.delete(found);
            } catch (Exception ex) {
                msg.message = "从数据库删除失败";
                return ok(Json.toJson(msg));
            }
            msg.flag = true;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + DELETE_SUCCESS);
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
        }
        return ok(Json.toJson(msg));
    }

    public static Result update(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<CommercialActivitiesModel> msg = new Msg<>();

        CommercialActivitiesModel found = CommercialActivitiesModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        Form<CommercialActivitiesModel> httpForm = form(CommercialActivitiesModel.class).bindFromRequest();

        if (!httpForm.hasErrors()) {
            CommercialActivitiesModel formObj = httpForm.get();


            // 逐个赋值
            found.productname = formObj.productname;
            found.images = formObj.images;
            found.Price = formObj.Price;
            found.bargainBottomLine = formObj.bargainBottomLine;
            found.stock = formObj.stock;
            found.activityTime = formObj.activityTime;
            found.activeState = formObj.activeState;
            found.peopleParticipate = formObj.peopleParticipate;
            found.salesVolume = formObj.salesVolume;
            found.comment = formObj.comment;
            try {
                Ebean.update(found);
            } catch (Exception ex) {
                msg.message = "数据更新失败";
                return ok(Json.toJson(msg));
            }

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
