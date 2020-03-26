package controllers;

        import LyLib.Interfaces.IConst;
        import LyLib.Utils.DateUtil;
        import LyLib.Utils.Msg;
        import LyLib.Utils.PageInfo;
        import LyLib.Utils.StrUtil;
        import com.avaje.ebean.Ebean;
        import com.avaje.ebean.Page;
        import com.fasterxml.jackson.databind.node.ObjectNode;
        import models.*;
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
public class ActivityHistoryController extends Controller implements IConst {
    @Security.Authenticated(SecuredAdmin.class)
    public static Result backendPage() {
        return ok(ActivityHistory_backend.render());
    }

    // @Security.Authenticated(SecuredAPI.class)
    @Security.Authenticated(Secured.class)
    public static Result add() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<ActivityHistoryModel> msg = new Msg<>();


        Form<ActivityHistoryModel> httpForm = form(ActivityHistoryModel.class).bindFromRequest();
        if (!httpForm.hasErrors()) {

            ActivityHistoryModel formObj = httpForm.get();

            //ProductModel dbProduct = ProductModel.find.byId(formObj.activitiesProductId);

            LineActivitiesModel found = LineActivitiesModel.find.byId(formObj.theLineActivities);
  /*          Double beforeCutPrice = new Double(found.beforeCutPrice);
            formObj.bargain = formObj.randomNumber(beforeCutPrice);*/
            formObj.originalPrice = found.originalPrice ;//原价
            formObj.beforeCutPrice = found.beforeCutPrice  ;//砍前价
            formObj.afterCutPrice = found.presentPrice ;//砍后价
            formObj.bargain = found.beforeCutPrice - found.presentPrice ;//砍价金額

            Ebean.save(formObj);

         /*   dbProduct.activityhistory.add(formObj);
            Ebean.update(dbProduct);*/

            msg.flag = true;
            msg.data = formObj;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
        } else {
            msg.message = httpForm.errors().toString();
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    public static Result getAll(Integer page, Integer size, Long cutUserId, String theLineActivities, Long activitiesProductId) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        if (size == 0)
            size = PAGE_SIZE;
        if (page <= 0)
            page = 1;

        Msg<List<ActivityHistoryModel>> msg = new Msg<>();
        Page<ActivityHistoryModel> records;

            if (cutUserId > 0) {
                records = ActivityHistoryModel.find.where().eq("cutUserId", cutUserId).orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
            }
            else{
              if (StrUtil.isNotNull(theLineActivities)) {
                    records = ActivityHistoryModel.find.where().eq("theLineActivities", theLineActivities).orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
                }
                else{
                records = ActivityHistoryModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
              }
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

    public static Result get(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<ActivityHistoryModel> msg = new Msg<>();

        ActivityHistoryModel found = ActivityHistoryModel.find.byId(id);
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
        Msg<ActivityHistoryModel> msg = new Msg<>();

        ActivityHistoryModel found = ActivityHistoryModel.find.byId(id);
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
        Msg<ActivityHistoryModel> msg = new Msg<>();

        ActivityHistoryModel found = ActivityHistoryModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        Form<ActivityHistoryModel> httpForm = form(ActivityHistoryModel.class).bindFromRequest();

        if (!httpForm.hasErrors()) {
            ActivityHistoryModel formObj = httpForm.get();


            // 逐个赋值
            found.theLineActivities = formObj.theLineActivities;
            found.cutUserId = formObj.cutUserId;
            found.originalPrice = formObj.originalPrice;
            found.bargain = formObj.bargain;
            found.createdAtStr = formObj.createdAtStr;
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
