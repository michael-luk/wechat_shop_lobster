package controllers;

        import LyLib.Interfaces.IConst;
        import LyLib.Utils.DateUtil;
        import LyLib.Utils.Msg;
        import LyLib.Utils.PageInfo;
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
public class LineActivitiesController extends Controller implements IConst {
    @Security.Authenticated(SecuredAdmin.class)
    public static Result backendPage() {
        return ok(LineActivities_backend.render());
    }

/*
   public static Result randomNumber(Integer obj) {
        int randomAmount = 0 ;
        obj = obj - randomAmount ;
        randomAmount =  (int) (1 + Math.random() * (obj - 1 + 1));
               return randomAmount;
      */
/*  System.out.println(randomAmount);*//*

    }
*/


    // @Security.Authenticated(SecuredAPI.class)
    public static Result add() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<LineActivitiesModel> msg = new Msg<>();

        double bargain = 0;
        Form<LineActivitiesModel> httpForm = form(LineActivitiesModel.class).bindFromRequest();


        if (!httpForm.hasErrors()) {
            LineActivitiesModel formObj = httpForm.get();

            bargain = (double)(0.1+Math.random()*(formObj.presentPrice*0.1-0.1+1));
            formObj.theNumberHasBeenCut = formObj.theNumberHasBeenCut + 1;
            formObj.presentPrice = formObj.presentPrice - bargain;
            formObj.beforeCutPrice = formObj.originalPrice;

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

    public static Result getAll(Integer page, Integer size,Long sponsorId) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        if (size == 0)
            size = PAGE_SIZE;
        if (page <= 0)
            page = 1;

        Msg<List<LineActivitiesModel>> msg = new Msg<>();
        Page<LineActivitiesModel> records;
            if (sponsorId > 0) {
                records = LineActivitiesModel.find.where().eq("sponsorId", sponsorId).orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
            }else{
                records = LineActivitiesModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
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
        Msg<LineActivitiesModel> msg = new Msg<>();

        LineActivitiesModel found = LineActivitiesModel.find.byId(id);
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
        Msg<LineActivitiesModel> msg = new Msg<>();

        LineActivitiesModel found = LineActivitiesModel.find.byId(id);
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
        Msg<LineActivitiesModel> msg = new Msg<>();
        double bargainUpt = 0;

        LineActivitiesModel found = LineActivitiesModel.find.byId(id);

        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        Form<LineActivitiesModel> httpForm = form(LineActivitiesModel.class).bindFromRequest();

        if (!httpForm.hasErrors()) {
            LineActivitiesModel formObj = httpForm.get();
            bargainUpt = (double)(0.1+Math.random()*(found.presentPrice*0.1-0.1+1));
            if(found.sabreplayLimit <= found.theNumberHasBeenCut){
                bargainUpt = (double)(0.1+Math.random()*(found.presentPrice-0.1+1));
                if(bargainUpt > found.presentPrice){
                    bargainUpt = found.presentPrice;
                }
            }
            // 逐个赋值
            found.sponsorId = formObj.sponsorId;
            found.product = formObj.product;
            found.originalPrice = formObj.originalPrice;
            if(formObj.comment == null){
               /* found.presentPrice = formObj.presentPrice - bargain;
                found.theNumberHasBeenCut = formObj.theNumberHasBeenCut + 1;
                found.beforeCutPrice = found.beforeCutPrice - bargain;*/
                    formObj.presentPrice = found.presentPrice - bargainUpt;
                    formObj.theNumberHasBeenCut = found.theNumberHasBeenCut + 1;
                    formObj.beforeCutPrice = found.presentPrice ;

            }
            found.comment = formObj.comment;

            try {
                Ebean.update(formObj);
            } catch (Exception ex) {
                msg.message = "数据更新失败";
                return ok(Json.toJson(msg));
            }

            msg.flag = true;
            msg.data = formObj;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
        } else {
            msg.message = httpForm.errors().toString();
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }


}
