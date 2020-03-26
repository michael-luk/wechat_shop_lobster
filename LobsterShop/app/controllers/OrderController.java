package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import LyLib.Utils.StrUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import models.OrderModel;
import models.ProductModel;
import models.StoreModel;
import models.UserModel;
import models.common.CompanyModel;
import models.common.ResellerRecord;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.order_backend;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

public class OrderController extends Controller implements IConst {

    @Security.Authenticated(SecuredAdmin.class)
    public static Result backendPage() {
        return ok(order_backend.render());
    }

    public static char getRamdonLetter() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return chars.charAt((int) (Math.random() * 52));
    }

    @Security.Authenticated(Secured.class)
    public static Result addOrder() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        Form<OrderModel> httpForm = form(OrderModel.class).bindFromRequest();
        if (!httpForm.hasErrors()) {
            OrderModel formObj = httpForm.get();

            if (formObj.status != 0 || !StrUtil.isNull(formObj.payReturnCode) || !StrUtil.isNull(formObj.payReturnCode)
                    || !StrUtil.isNull(formObj.payReturnMsg) || !StrUtil.isNull(formObj.payResultCode)
                    || !StrUtil.isNull(formObj.payTransitionId) || !StrUtil.isNull(formObj.payAmount)
                    || !StrUtil.isNull(formObj.payBank) || !StrUtil.isNull(formObj.payRefOrderNo)
                    || !StrUtil.isNull(formObj.paySign) || !StrUtil.isNull(formObj.payTime)
                    || !StrUtil.isNull(formObj.payThirdPartyId) || !StrUtil.isNull(formObj.payThirdPartyUnionId)
                    || formObj.resellerProfit1 > 0 || formObj.resellerProfit2 > 0 || formObj.resellerProfit3 > 0) {
                play.Logger.error("*******新增订单状态异常, 须检查. 发起IP: " + request().remoteAddress());
                msg.message = "新增订单状态异常";
                return ok(Json.toJson(msg));
            }

            // 新订单状态必须为0新增
            formObj.status = 0;

            formObj.orderNo = DateUtil.Date2Str(new Date(), "yyyyMMddHHmmss") + getRamdonLetter();
            formObj.createClientIP = request().remoteAddress();

            if (formObj.refResellerId != 0) {
                UserModel reseller = UserModel.find.byId(formObj.refResellerId);
                if (reseller != null) {
                    formObj.reseller = reseller;
                }
            }

           /* if (formObj.storeId != 0) {
                StoreModel store = StoreModel.find.byId(formObj.storeId);
                if (store != null) {
                    formObj.store = store;
                }
            }*/

            //TODO: 稳定无误后去掉
            String productStr = "";
            for (ProductModel product : formObj.orderProducts) {
                productStr += product.name + "";
            }
            play.Logger.info(DateUtil.Date2Str(new Date()) + "订单商品顺序: " + productStr);
            play.Logger.info(DateUtil.Date2Str(new Date()) + "订单商品数量: " + formObj.quantity);

            // 处理正确的产品数量
            formObj.quantity = OrderModel.getCorrectQuantityStr(formObj.orderProducts, formObj.quantity);

            Ebean.save(formObj);

            //TODO: 稳定无误后去掉
            OrderModel newOrder = OrderModel.find.byId(formObj.id);
            productStr = "";
            for (ProductModel product : newOrder.orderProducts) {
                productStr += product.name + ",";
            }
            play.Logger.info(DateUtil.Date2Str(new Date()) + "数据库订单商品顺序: " + productStr);
            play.Logger.info(DateUtil.Date2Str(new Date()) + "数据库订单商品数量: " + newOrder.quantity);

            // 复查订单商品总价 TODO:目前无视口味, 仅计算产品价格
            double amount = 0d;
            List<Integer> integerListFromSplitStr = StrUtil.getIntegerListFromSplitStr(newOrder.quantity);
            for (int i = 0; i < integerListFromSplitStr.size(); i++) {
                amount += newOrder.orderProducts.get(i).price * integerListFromSplitStr.get(i);
            }
            if (Math.abs(amount - formObj.productAmount) > 1) {// 与json商品总额比对
                play.Logger.error(String.format("*******新增订单商品总额复核异常, 须检查. 原商品额: %s, 计算结果: %s, 发起IP: %s", formObj.productAmount, amount, request().remoteAddress()));
                msg.message = "订单商品总额复核异常";
                return ok(Json.toJson(msg));
            } else {
                play.Logger.info(DateUtil.Date2Str(new Date()) + "订单商品总额复核正确: " + newOrder.orderNo);
            }


            msg.flag = true;
            msg.data = formObj;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + CREATE_SUCCESS);
        } else {
            msg.message = httpForm.errors().toString();
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(SecuredAdmin.class)
    public static Result updateOrder(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        Form<OrderModel> httpForm = form(OrderModel.class).bindFromRequest();

        if (!httpForm.hasErrors()) {
            OrderModel formObj = httpForm.get();

            // 逐个赋值
            found.shipTimeStr = formObj.shipTimeStr;
            found.comment = formObj.comment;
            Ebean.update(found);

            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

            msg.flag = true;
            msg.data = found;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
        } else {
            msg.message = httpForm.errors().toString();
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(SecuredAdmin.class)
    public static Result updateOrderStatus(long id, int status) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        // 更改订单状态

        if (status == 0 || status == 1 || status == 2 || status == 3 || status == 4 || status == 5 || status == 6
                || status == 7 || status == 8 || status == 9) {
            found.status = status;

            // 发货时给用户回复消息
            if (status == 4) {
                found.shipTimeStr = DateUtil.Date2Str(new Date());

                String openid = found.buyer.wxOpenId;
                String orderno = found.orderNo;
                if (found.buyer != null) {
                    if (!StrUtil.isNull(found.buyer.wxOpenId)) {
                        // 你的订单已发货
                        try {
                            String wxMessageText = "亲，您的订单已经发货！订单号为：" + orderno.toUpperCase();
                            WxMpCustomMessage wxMessage = WxMpCustomMessage.TEXT().toUser(openid).content(wxMessageText)
                                    .build();
                            WeiXinController.wxService.customMessageSend(wxMessage);
                            play.Logger.info("用户订单已经发货通知" + orderno);
                        } catch (WxErrorException e) {
                            play.Logger.error("用户订单发货通知错误" + orderno);

                        }
                    }
                }
            }

            Ebean.update(found);

            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

            msg.flag = true;
            msg.data = found;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
        } else {
            msg.message = "状态不对";
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }

        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result updateOrderStatusByUser(long id, int status) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        // 更改订单状态, 只能改取消和确认收货
        if (status == 2 || status == 5) {
            found.status = status;
            Ebean.update(found);
            // 给用户回复消息
            String openid = found.buyer.wxOpenId;
            String orderno = found.orderNo;
            if (found.buyer != null) {
                if (!StrUtil.isNull(found.buyer.wxOpenId)) {
                    if (status == 2) {
                        // 你的订单已取消
                        try {
                            String wxMessageText = "您的订单已经取消！订单号为：" + orderno.toUpperCase();
                            WxMpCustomMessage wxMessage = WxMpCustomMessage.TEXT().toUser(openid).content(wxMessageText)
                                    .build();
                            WeiXinController.wxService.customMessageSend(wxMessage);
                            play.Logger.info("用户订单已取消通知： " + orderno);
                        } catch (WxErrorException e) {
                            play.Logger.error("用户订单已取消通知失败： " + orderno);

                        }
                    }
                    if (status == 5) {
                        // 你的订单已确认
                        try {
                            String wxMessageText = "您的订单已经确认！订单号为：" + orderno.toUpperCase();
                            WxMpCustomMessage wxMessage = WxMpCustomMessage.TEXT().toUser(openid).content(wxMessageText)
                                    .build();
                            WeiXinController.wxService.customMessageSend(wxMessage);
                            play.Logger.info("用户订单已确认通知： " + orderno);
                        } catch (WxErrorException e) {
                            play.Logger.error("用户订单已确认通知失败" + orderno);

                        }
                    }
                }
            }
            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

            msg.flag = true;
            msg.data = found;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
        } else {
            msg.message = "状态不对";
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }

        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result updateOrderStatusByWxPay(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.byId(id);
        if (found == null) {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }

        // 更改订单状态, 只能从"0待支付"改为"12等待支付通知"
        if (found.status == 0) {
            found.status = 12;
            Ebean.update(found);

            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

            msg.flag = true;
            msg.data = found;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + UPDATE_SUCCESS);
        } else {
            if (found.status == 1)
                msg.message = "订单已支付";
            else
                msg.message = "订单初始状态不对, status: " + String.valueOf(found.status);
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result getAllOrders(Integer status,Long storeId, String keyword, Integer page, Integer size) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        if (size == 0)
            size = PAGE_SIZE;
        if (page <= 0)
            page = 1;

        Msg<List<OrderModel>> msg = new Msg<>();
        Page<OrderModel> records;

        if (storeId > 0 && status == -1) {
            if (StrUtil.isNull(keyword)) {
                records = OrderModel.find.where().eq("storeId",storeId).orderBy("id desc").findPagingList(size).setFetchAhead(false)
                        .getPage(page - 1);
            } else {
                records = OrderModel.find.where().eq("storeId",storeId)
                        .or(Expr.like("orderNo", "%" + keyword + "%"), Expr.like("buyer.nickname", "%" + keyword + "%"))
                        .orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
            }
        } else {
            if (StrUtil.isNull(keyword)) {
                records = OrderModel.find.where().and(Expr.eq("storeId", storeId),Expr.eq("status", status)).orderBy("id desc").findPagingList(size)
                    .setFetchAhead(false).getPage(page - 1);
            } else {
                records = OrderModel.find.where().and(Expr.eq("storeId", storeId),Expr.eq("status", status))
                        .or(Expr.like("orderNo", "%" + keyword + "%"), Expr.like("buyer.nickname", "%" + keyword + "%"))
                        .orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
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
            // 用户密码不返回
            List<OrderModel> orders = records.getList();
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).buyer != null) {
                    orders.get(i).buyer.password = "";
                }
            }
            msg.data = orders;
            msg.page = pageInfo;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records.getTotalRowCount());
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    // @Security.Authenticated(SecuredAdmin.class)
    public static Result checkAndKillUselessOrders() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<Integer> msg = new Msg<>();
        List<OrderModel> records = OrderModel.find.where().eq("status", 0).findList();
        Integer expireHours = 2;
        Long expireMillSecconds = expireHours * 60 * 60 * 1000L;

        Integer killOrderCount = 0;
        for (OrderModel order : records) {
            Date orderDate = DateUtil.Str2Date(order.createdAtStr);
            Date nowDate = new Date();
            Long MillSecconds = nowDate.getTime() - orderDate.getTime();

            if (MillSecconds >= expireMillSecconds) {
                order.status = 3;
                Ebean.update(order);
                // 两个小时之内没有支付
                String openid = order.buyer.wxOpenId;
                String orderno = order.orderNo;
                if (order.buyer != null) {
                    if (!StrUtil.isNull(order.buyer.wxOpenId)) {
                        try {
                            String wxMessageText = "亲，由于您在两小时内没有支付，很遗憾，您的订单已经取消！订单号为：" + orderno.toUpperCase();
                            WxMpCustomMessage wxMessage = WxMpCustomMessage.TEXT().toUser(openid).content(wxMessageText)
                                    .build();
                            WeiXinController.wxService.customMessageSend(wxMessage);
                            play.Logger.info("用户订单自动取消通知：" + orderno);
                        } catch (WxErrorException e) {
                            play.Logger.error("用户订单自动取消通知失败: " + orderno);

                        }
                    }
                }
                killOrderCount++;
            }
        }

        List<OrderModel> payRecords = OrderModel.find.where().eq("status", 1).findList();
        Integer payHours = 3;
        long payMillSeconds = payHours * 60 * 60 * 1000L;
        for (OrderModel order : payRecords) {
            if (!StrUtil.isNull(order.payTime)) {
                Date payDate = DateUtil.Str2Date(order.payTime, "yyyyMMddHHmmss");
                if (payDate == null) {
                    play.Logger.error(DateUtil.Date2Str(new Date()) + "处理订单自动确认出错, 订单: " + order.id + ", 订单支付时间: "
                            + order.payTime);
                }
                Date nowDate = new Date();
                Long MillSeconds = nowDate.getTime() - payDate.getTime();
                if (MillSeconds >= payMillSeconds) {
                    order.status = 5;
                    Ebean.update(order);
                    // 三小时之内没有确认收货
                    String openid = order.buyer.wxOpenId;
                    String orderno = order.orderNo;
                    if (order.buyer != null) {
                        if (!StrUtil.isNull(order.buyer.wxOpenId)) {
                            try {
                                String wxMessageText = "亲，由于您在三个小时之内没有确认，系统已经自动帮您确认！订单号为：" + orderno.toUpperCase();
                                WxMpCustomMessage wxMessage = WxMpCustomMessage.TEXT().toUser(openid)
                                        .content(wxMessageText).build();
                                WeiXinController.wxService.customMessageSend(wxMessage);
                                play.Logger.info("用户订单自动确认通知：" + orderno);
                            } catch (WxErrorException e) {
                                play.Logger.error("用户订单自动确认通知失败：" + orderno);

                            }
                        }
                    }
                }
            }
        }

        if (records.size() > 0) {
            msg.flag = true;
            msg.data = killOrderCount;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records.size());
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result getOrder(long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.byId(id);
        if (found != null) {

            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

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
    public static Result getOrdersByUser(Integer status, long refBuyerId,long storeId, Integer page, Integer size) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        if (size == 0)
            size = PAGE_SIZE;
        if (page <= 0)
            page = 1;

        Msg<List<OrderModel>> msg = new Msg<>();
        Page<OrderModel> records;

        List<OrderModel> found = OrderModel.find.where().eq("refBuyerId", refBuyerId).orderBy("id desc").findList();
        if (found != null) {
            // 用户密码不返回
            for (int i = 0; i < found.size(); i++) {
                if (found.get(i).buyer != null) {
                    found.get(i).buyer.password = "";
                }
            }

           if(status < 0 && storeId>0){
                records = OrderModel.find.where().and(Expr.eq("refBuyerId", refBuyerId),Expr.eq("storeId", storeId)).orderBy("id desc").findPagingList(size)
                        .setFetchAhead(false).getPage(page - 1);
            }else {
                records = OrderModel.find.where().eq("storeId", storeId).and(Expr.eq("refBuyerId", refBuyerId), Expr.eq("status", status))
                        .orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);
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
            }

        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result getOrderByOrderNo(String orderNo) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel found = OrderModel.find.where().eq("orderNo", orderNo).findUnique();
        if (found != null) {

            // 用户密码不返回
            if (found.buyer != null)
                found.buyer.password = "";

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
    public static Result getResellerOrders(Long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<List<OrderModel>> msg = new Msg<>();

        if (id == 0) {
            if (StrUtil.isNull(session(SESSION_USER_ID))) {
                msg.message = NO_FOUND;
                return ok(Json.toJson(msg));
            } else {
                id = Long.parseLong(session(SESSION_USER_ID));
            }
        }

        // 未支付的订单不显示
        List<OrderModel> records = OrderModel.find.where().and(Expr.eq("refResellerId", id), Expr.gt("status", 0))
                .orderBy("id desc").findList();

        if (records.size() > 0) {
            // 用户密码不返回
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).buyer != null) {
                    records.get(i).buyer.password = "";
                }
            }

            msg.flag = true;
            msg.data = records;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + records.size());
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(Secured.class)
    public static Result getResellerOrderAmount(Long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<Double> msg = new Msg<>();

        if (id == 0) {
            if (StrUtil.isNull(session(SESSION_USER_ID))) {
                msg.message = NO_FOUND;
                return ok(Json.toJson(msg));
            } else {
                id = Long.parseLong(session(SESSION_USER_ID));
            }
        }

        // 计算已确认/已计算佣金/已取消计算佣金的订单
        play.Logger.info("start do reseller order count: " + DateUtil.Date2Str(new Date()));
        List<Integer> statusList = new ArrayList<>();
        statusList.add(5);
        statusList.add(7);
        statusList.add(8);

        List<OrderModel> orders = OrderModel.find.where()
                .and(Expr.eq("refResellerId", id), Expr.in("status", statusList)).orderBy("id desc").findList();

        List<UserModel> allDownlineUsers = new ArrayList<>();

        // 找下线
        List<UserModel> downlineUsers1 = UserModel.find.where().eq("refUplineUserId", id).findList();

        play.Logger.info("1st level downline count: " + downlineUsers1.size());
        allDownlineUsers.addAll(downlineUsers1);

        // 找下线的下线
        for (UserModel downlineUser : downlineUsers1) {
            allDownlineUsers.addAll(UserModel.find.where().eq("refUplineUserId", downlineUser.id).findList());
        }
        play.Logger.info("total downline count: " + allDownlineUsers.size());

        for (UserModel downlines : allDownlineUsers) {
            orders.addAll(OrderModel.find.where().and(Expr.eq("refResellerId", downlines.id), Expr.eq("status", 5))
                    .orderBy("id desc").findList());
        }

        Double totalProductAmount = 0D;
        for (OrderModel record : orders) {
            totalProductAmount += record.productAmount;
        }

        if (orders.size() > 0) {
            msg.flag = true;
            msg.data = totalProductAmount;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + totalProductAmount);
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(SecuredSuperAdmin.class)
    public static Result doCalculate(Long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel order = OrderModel.find.byId(id);

        if (order != null) {
            // 已确认收货的订单才能分销, 订单有用户才能分销
            if (order.status == 5 && order.refBuyerId != 0 && order.refResellerId > 0) {
                // 1. 拿到分成比例
                // 2. 拿到订单的分销用户(即下单用户的上线)
                // 3. 拿到分销用户的上线(若有)及上上线(若有)
                // 4. 计算订单的商品的累计分销额
                // 5. 按分成比例设置订单的三层佣金
                // 6. 对分销用户(最多3个)进行佣金的加入

                CompanyModel companyInfo = CompanyModel.findAll().get(0);
                List<ResellerRecord> resultList = new ArrayList<>();
                resultList.add(new ResellerRecord(0));// A用户(订单买家的上线),分最多
                resultList.add(new ResellerRecord(0));// A的上线B用户
                resultList.add(new ResellerRecord(0));// B的上线C用户, 分最少

                play.Logger.info("执行分销计算， 于订单: " + order.orderNo);

                // 分销限制3层, 注意不一定每层都存在可拿佣金的用户(不存在上线或未开通分销都是有可能的)
                UserModel tempUser = UserModel.find.byId(order.buyer.refUplineUserId);
                for (int i = 0; i < resultList.size(); i++) {
                    if (tempUser != null && tempUser.isReseller && tempUser.userStatus < 1) {
                        // 存在该用户(并且不是上帝),
                        // 他是分销商,
                        // 且不被冻结删除,
                        // 才能参加分销
                        resultList.get(i).user = tempUser;

                        // 如果无上线, 则循环结束
                        if (resultList.get(i).user.refUplineUserId == -1)
                            break;

                        UserModel tempUser2 = UserModel.find.byId(resultList.get(i).user.refUplineUserId);
                        if (tempUser2.id != tempUser.id) {
                            tempUser = tempUser2;
                        }
                    }
                }

                // 将无下线的分销比例累加到上级
                if (resultList.get(2).user != null) {// 三级都有
                    resultList.get(2).rate = companyInfo.marketing1;
                    resultList.get(1).rate = companyInfo.marketing2;
                    resultList.get(0).rate = companyInfo.marketing3;
                } else {
                    if (resultList.get(1).user != null) {// 有一, 二级
                        resultList.get(1).rate = companyInfo.marketing1;
                        resultList.get(0).rate = companyInfo.marketing2 + companyInfo.marketing3;
                    } else {// 仅一级
                        resultList.get(0).rate = companyInfo.marketing1 + companyInfo.marketing2
                                + companyInfo.marketing3;
                    }
                }

                play.Logger.info("reseller list: " + resultList);

                // 取得订单额作为分销额(邮费等不包括)
                Double totalAvailableResellerAmount = order.productAmount;

                // 计算佣金
                DecimalFormat df = new DecimalFormat("#.##");
                for (int i = 0; i < resultList.size(); i++) {
                    ResellerRecord record = resultList.get(i);
                    if (record.user != null) {
                        record.profit = Double.parseDouble(df.format(record.rate * totalAvailableResellerAmount));
                        record.user.currentResellerProfit += record.profit;
                        Ebean.update(record.user);

                        if (i == 0) {
                            order.resellerProfit1 = record.profit;
                        }
                        if (i == 1) {
                            order.resellerProfit2 = record.profit;
                        }
                        if (i == 2) {
                            order.resellerProfit3 = record.profit;
                        }
                    }
                }

                // 改状态为"已执行分销"
                order.status = 7;
                Ebean.update(order);

                msg.flag = true;
                msg.data = order;
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + order.resellerProfit1 + ", "
                        + order.resellerProfit2 + ", " + order.resellerProfit3 + ". total: "
                        + totalAvailableResellerAmount.toString());
            } else {
                msg.message = "订单尚未确认收货, 或不存在上线分销商, 不能执行分销佣金计算";
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            }
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
        }
        return ok(Json.toJson(msg));
    }

    @Security.Authenticated(SecuredSuperAdmin.class)
    public static Result cancelCalculate(Long id) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Msg<OrderModel> msg = new Msg<>();

        OrderModel order = OrderModel.find.byId(id);
        if (order != null) {
            // 已分销的订单才能取消分销, 订单有用户才能分销
            if (order.status == 7 && order.refBuyerId != 0 && order.refResellerId > 0) {
                UserModel buyer = UserModel.find.byId(order.refBuyerId);

                if (buyer != null && buyer.refUplineUserId > 0) {
                    UserModel buyerUpline = UserModel.find.byId(buyer.refUplineUserId);

                    if (buyerUpline != null && buyerUpline.refUplineUserId > 0) {
                        buyerUpline.currentResellerAvailableAmount -= order.productAmount;
                        buyerUpline.currentResellerProfit -= order.resellerProfit1;
                        Ebean.update(buyerUpline);

                        UserModel buyerUplineUpline = UserModel.find.byId(buyerUpline.refUplineUserId);

                        if (buyerUplineUpline != null && buyerUplineUpline.refUplineUserId > 0) {
                            buyerUplineUpline.currentResellerAvailableAmount -= order.productAmount;
                            buyerUplineUpline.currentResellerProfit -= order.resellerProfit2;
                            Ebean.update(buyerUplineUpline);

                            UserModel buyerUplineUplineUpline = UserModel.find.byId(buyerUplineUpline.refUplineUserId);
                            if (buyerUplineUplineUpline != null) {
                                buyerUplineUplineUpline.currentResellerAvailableAmount -= order.productAmount;
                                buyerUplineUplineUpline.currentResellerProfit -= order.resellerProfit3;
                                Ebean.update(buyerUplineUplineUpline);
                            }
                        }
                    }
                }
                order.resellerProfit1 = 0d;
                order.resellerProfit2 = 0d;
                order.resellerProfit3 = 0d;

                // 改状态为"已取消分销"
                order.status = 8;
                Ebean.update(order);

                msg.flag = true;
                msg.data = order;
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + order.resellerProfit1 + ", "
                        + order.resellerProfit2 + ", " + order.resellerProfit3);
            } else {
                msg.message = "订单尚未分销, 不能执行取消分销";
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            }
        } else {
            msg.message = NO_FOUND;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + NO_FOUND);
        }
        return ok(Json.toJson(msg));
    }
}
