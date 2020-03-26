package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.StrUtil;
import com.avaje.ebean.Ebean;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import models.*;
import models.common.CompanyModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import play.api.mvc.Session;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class Application extends Controller implements IConst {
    public static String domainName = "www.77wm.com.cn";
    public static String domainNameWithProtocal = "http://" + domainName;
    public static float thumbSize = 100F;

    public static Result checkAlive() {
        return ok("alive");
    }

    public static class LoginParser {

        public String username;
        public String password;

        public String validate() {
            if (password != null && password.length() < 32) {
                password = LyLib.Utils.MD5.getMD5(password);
            }
            if (UserModel.authenticate(username, password) == null) {
                return "用户名或密码不正确";
            }
            return null;
        }
    }

    public static class CartParser {

        public List<CartItemParser> items;

        // public String validate() {
        // if (password != null && password.length() < 32) {
        // password = LyLib.Utils.MD5.getMD5(password);
        // }
        // if (UserModel.authenticate(username, password) == null) {
        // return "用户名或密码不正确";
        // }
        // return null;
        // }
    }

    public static class CartItemParser {

        public Long pid;
        public Integer num;
		public Long storeId;
        public ProductModel product;
        public Boolean select;

        // public String validate() {
        // if (password != null && password.length() < 32) {
        // password = LyLib.Utils.MD5.getMD5(password);
        // }
        // if (UserModel.authenticate(username, password) == null) {
        // return "用户名或密码不正确";
        // }
        // return null;
        // }
    }

	public static Result setStore(Long storeId) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        if (storeId > 0) {
            session("STORE", storeId.toString());
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 保存店铺session成功");
        } else {
            play.Logger.error(DateUtil.Date2Str(new Date()) + " - result: 保存店铺session失败");
        }
        return ok("");
    }

    public static Result getStore() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<String> msg = new Msg<>();
        String sessionStore = session("STORE");
        msg.flag = true;
        msg.data = sessionStore;
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 获取购物车 - " + sessionStore);

        return ok(Json.toJson(msg));
    }
	
	
	 public static Result sesseionClear() {
        session().clear();
        return ok("session clear");
    }


  public static Result onePage(String resellerCode) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());

        play.Logger.info("loading / with session: " + session("WX_OPEN_ID"));
        play.Logger.info("resellerCode: " + resellerCode);

        if (session("WX_OPEN_ID") == null || !StrUtil.isNull(resellerCode)) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + resellerCode + "&state=home" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
//            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
            return ok(one.render());
        }
    }

	
    public static Result setCart() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<CartParser> msg = new Msg<>();
        Form<CartParser> httpForm = form(CartParser.class).bindFromRequest();
        if (!httpForm.hasErrors()) {
            CartParser formObj = httpForm.get();

            if (formObj.items == null) {
                formObj.items = new ArrayList<>();
            } else {
                // 防止session过长
                for (CartItemParser item : formObj.items) {
                    item.product = null;
                }
            }

            session("CART", Json.toJson(formObj).toString());
            msg.flag = true;
            msg.data = formObj;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 更新购物车");
        } else {
            play.Logger.error(DateUtil.Date2Str(new Date()) + " - result: 更新购物车失败");
            msg.message = httpForm.errors().toString();
        }
        return ok(Json.toJson(msg));
    }

    public static Result getCart() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<String> msg = new Msg<>();
        String sessionCart = session("CART");
        if (StrUtil.isNull(sessionCart)) {
            sessionCart = "{\"items\":[]}";
            msg.data = sessionCart;
            session("CART", sessionCart);
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 创建空购物车");
        } else {
            msg.flag = true;
            msg.data = sessionCart;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 获取购物车 - " + sessionCart);
        }

        return ok(Json.toJson(msg));
    }

    public static Result clearCart() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        session("CART", "{\"items\":[]}");
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: 清除购物车session");

        return ok("清除购物车session");
    }

    public static Result login() {
        UserModel userModel = UserModel.findByloginName(session(SESSION_USER_NAME));
        if (userModel != null && userModel.userRole == 2) {
            // return redirect("assets/backend/index.html#/");
            return ok(cms.render());
        } else
            return ok(login.render(form(LoginParser.class)));
    }

    public static Result backendLogin() {
        UserModel userModel = UserModel.findByloginName(session(SESSION_USER_NAME));
        if (userModel != null && userModel.userRole == 2) {
            return redirect(routes.OrderController.backendPage());
        } else
            return ok(backend_login.render(form(LoginParser.class)));
    }

    public static Result logout() {
        session().clear();
        flash("logininfo", "您已登出,请重新登录");
        return redirect(routes.Application.login());
    }

    public static Result backendLogout() {
        session().clear();
        flash("logininfo", "您已登出,请重新登录");
        return redirect(routes.Application.backendLogin());
    }

    public static Result backendPage() {
        return redirect(routes.OrderController.backendPage());
    }

    public static Result indexPage() {
        return ok(index.render());
    }

    public static Result errorPage() {
        return ok(errpage.render());
    }

    public static Result blank() {
        return ok(blank.render());
    }

    public static Result blankPage4Platform() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());
        return ok(blank4Weixin.render());
    }

    public static Result blankPage4WeixinOpenId() {
        return ok(blank4Weixin.render());
    }

    public static Result recruitmentPage() {
        return ok(recruitment.render());
    }

    public static Result winePcPage() {
        // play.Logger.info(DateUtil.Date2Str(new Date()) + " - " +
        // request().method() + ": " + request().uri());
        return ok(pc_index.render());
    }

    public static Result themeOrderPage() {
        return ok(themes.render());
    }

    public static Result charitablePage() {
        return ok(charitable.render());
    }

    public static Result themeProducPage() {
        return ok(themeProduct.render());
    }

    public static Result detailsPage() {
        return ok(details.render());
    }

    public static Result makePage() {
        return ok(make.render());
    }

    public static Result numCenterPage() {
        return ok(num_center.render());
    }

    public static Result pcMyorderPage() {
        return ok(pc_myorder.render());
    }

    public static Result pcOrderPage() {
        return ok(pc_order.render());
    }

    public static Result pcPayPage() {
        return ok(pc_pay.render());
    }

    public static Result productAddPage() {
        return ok(product_add.render());
    }

    public static Result themePage(long id) {
        return ok(themePct.render());
    }

    public static Result aboutUsPage() {
        return ok(aboutUs.render());
    }

    public static Result helpPage() {
        return ok(pc_help.render());
    }

    public static Result shoppingPage() {
        return ok(pc_shopping.render());
    }

    //砍价
    // 砍价首页
    public static Result bargainPage() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());
        play.Logger.info("loading with session: " + session("WX_OPEN_ID"));

        String ticket = null;
        WxJsapiSignature signature = null;// +

        try {
            play.Logger.info("accesstoken: " + WeiXinController.wxService.getAccessToken());
            ticket = WeiXinController.wxService.getJsapiTicket();
            signature = WeiXinController.wxService.createJsapiSignature(Application.domainNameWithProtocal + request().uri());
            play.Logger.info("create signature: " + signature.getSignature());
            play.Logger.info("nonce: " + signature.getNoncestr());
            play.Logger.info("timestamp: " + signature.getTimestamp());
            play.Logger.info("url: " + signature.getUrl());
//            play.Logger.info("raw url: " + Application.domainNameWithProtocal + request().uri());
        } catch (WxErrorException e) {
            play.Logger.error("微信分享: 签名失败, ex: " + e.getMessage());
            flash("error", "微信分享: 签名失败");
            return redirect(routes.Application.errorPage());
        }

        if (StrUtil.isNull(ticket) || signature == null) {
            play.Logger.error("微信分享: 签名失败, ex: " + "ticket为空或签名为空");
            flash("error", "微信分享: 签名失败, 票据有误");
            return redirect(routes.Application.errorPage());
        }

//        session("ticket", ticket);
        session("appid", WeiXinController.wxAppId);
        session("nonce", signature.getNoncestr());
        session("timestamp", Long.toString(signature.getTimestamp()));
        session("signature", signature.getSignature());


       if (StrUtil.isNull(session("WX_OPEN_ID"))) {
            String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId
                    + "&redirect_uri=http%3A%2F%2F" + domainName + "%2Fdowxuser%3FresellerCode=" + "%26path=bargain"
                    + "&response_type=code&scope=snsapi_base#wechat_redirect"; //
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         /*   session("userid", "2");*/
            return ok(bargain.render());
        }
    }

    // 斩价邀请朋友
    public static Result bargain4FriendsPage(Long aid) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());

        play.Logger.info("loading / with session: " + session("WX_OPEN_ID"));
        play.Logger.info("砍价活动线id: " + aid.toString());

        String ticket = null;
        WxJsapiSignature signature = null;// +

        try {
            play.Logger.info("accesstoken: " + WeiXinController.wxService.getAccessToken());
            ticket = WeiXinController.wxService.getJsapiTicket();
            signature = WeiXinController.wxService.createJsapiSignature(Application.domainNameWithProtocal + request().uri());
            play.Logger.info("create signature: " + signature.getSignature());
            play.Logger.info("nonce: " + signature.getNoncestr());
            play.Logger.info("timestamp: " + signature.getTimestamp());
            play.Logger.info("url: " + signature.getUrl());
//            play.Logger.info("raw url: " + Application.domainNameWithProtocal + request().uri());
        } catch (WxErrorException e) {
            play.Logger.error("微信分享: 签名失败, ex: " + e.getMessage());
            flash("error", "微信分享: 签名失败");
            return redirect(routes.Application.errorPage());
        }

        if (StrUtil.isNull(ticket) || signature == null) {
            play.Logger.error("微信分享: 签名失败, ex: " + "ticket为空或签名为空");
            flash("error", "微信分享: 签名失败, 票据有误");
            return redirect(routes.Application.errorPage());
        }

//        session("ticket", ticket);
        session("appid", WeiXinController.wxAppId);
        session("nonce", signature.getNoncestr());
        session("timestamp", Long.toString(signature.getTimestamp()));
        session("signature", signature.getSignature());

       /* return redirect("/bargain/go/go");*/
//        return ok(bargain4friends.render(aid.toString()));
//        return redirect("/bargain/go/" + aid.toString());

        if (aid == 0) {// 无活动ID则引导到介绍页
            return redirect(
                    "http://mp.weixin.qq.com/s?__biz=MzI0NTE4OTkxNA==&mid=402891824&idx=1&sn=653c604d8cec67c35f3580bb7b3b910e#rd");
        }

       if (StrUtil.isNull(session("WX_OPEN_ID"))) {
            String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId
                    + "&redirect_uri=http%3A%2F%2F" + domainName + "%2Fdowxuser%3FresellerCode=" + aid.toString()
                    + "%26path=bargain4friend" + "&response_type=code&scope=snsapi_base#wechat_redirect"; //
            play.Logger.info("oauthUrl: " + oauthUrl);

            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
/*        session("userid", "2");*/
            return ok(bargain4friends.render(aid.toString()));
            //return redirect("/bargain/go/" + aid.toString());
     }
    }
    public static Result bargainactivityendPage() {
      /*  session("userid", "2");*/
        return ok(bargainActivityEnd.render());
    }
    public static Result buyImmediatelyPage() {
     /*   session("userid", "2");*/
        return ok(BuyImmediately.render());
    }

    //砍价结束

    public static Result winePage(String resellerCode) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());

        play.Logger.info("loading / with session: " + session("WX_OPEN_ID"));
        play.Logger.info("resellerCode: " + resellerCode);

        String ticket = null;
        WxJsapiSignature signature = null;// +

        try {
            play.Logger.info("accesstoken: " + WeiXinController.wxService.getAccessToken());
            ticket = WeiXinController.wxService.getJsapiTicket();
            signature = WeiXinController.wxService.createJsapiSignature(Application.domainNameWithProtocal + request().uri());
            play.Logger.info("create signature: " + signature.getSignature());
            play.Logger.info("nonce: " + signature.getNoncestr());
            play.Logger.info("timestamp: " + signature.getTimestamp());
            play.Logger.info("url: " + signature.getUrl());
//            play.Logger.info("raw url: " + Application.domainNameWithProtocal + request().uri());
        } catch (WxErrorException e) {
            play.Logger.error("微信分享: 签名失败, ex: " + e.getMessage());
            flash("error", "微信分享: 签名失败");
            return redirect(routes.Application.errorPage());
        }

        if (StrUtil.isNull(ticket) || signature == null) {
            play.Logger.error("微信分享: 签名失败, ex: " + "ticket为空或签名为空");
            flash("error", "微信分享: 签名失败, 票据有误");
            return redirect(routes.Application.errorPage());
        }

//        session("ticket", ticket);
        session("appid", WeiXinController.wxAppId);
        session("nonce", signature.getNoncestr());
        session("timestamp", Long.toString(signature.getTimestamp()));
        session("signature", signature.getSignature());


        if (session("WX_OPEN_ID") == null || !StrUtil.isNull(resellerCode)) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + resellerCode + "&state=home" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
//            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
            return ok(wine.render());
        }

 //      session("userid", "2");
  //    return ok(wine.render());
    }

    public static Result userCenterPage() {
        play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=userCenter" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
            return ok(userCenter.render());
        }

//        session("userid", "5");
//        return ok(userCenter.render());
    }

    public static Result marryPage(long id) {
		
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=marry" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
            return ok(marry.render());
        }

    }

    public static Result WproductPage(long id) {
		
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=Wproduct" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
        return ok(Wproduct.render());
        }

    }

    public static Result orderPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=order" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
             return ok(order.render());
        }

    }

    public static Result payPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=pay" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                  return ok(pay.render());
        }

    }

    public static Result locationPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=location" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));

                      return ok(location.render());
        }

    }

    public static Result myLocationPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=myLocation" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                     return ok(myLocation.render());
        }
       
    }
    public static Result addlocationPage() {
        play.Logger.info("loading /w/addLoction with session: " + session("WX_OPEN_ID"));

       if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=location" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));

                      return ok(addLoction.render());
        }
/*        session("userid", "2");
        return ok(addLoction.render());*/
    }

    public static Result myOrderPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=myOrder" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
              return ok(myOrder.render());
        }

    }

    public static Result distributorPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=distributor" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                    return ok(distributor.render());
        }

    }

    public static Result DistributionOrderPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=DistributionOrder" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                      return ok(DistributionOrder.render());
        }
    
    }

    public static Result QRcodePage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=QRcode" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
               return ok(QRcode.render());
        }
    
    
    }

    public static Result teamPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=team" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                      return ok(team.render());
        }

    }

    public static Result processPage() {
			 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=process" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                   return ok(process.render());
        }     
  
    }

    public static Result OrderMessagePage() {
			 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=OrderMessage" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
             return ok(OrderMessage.render());
        }     
      
    }

    public static Result allProductPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=allProduct" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
           return ok(allProduct.render());
        }     
      
    }

    public static Result invoiceTitlePage() {
			 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=invoiceTitle" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
            return ok(invoiceTitle.render());
        }     
  
    }

    public static Result collectPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=collect" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
            return ok(collect.render());
        }     
      
    }

    public static Result aboutPage() {
		 play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=about" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
              return ok(about.render());
        }     
    
    }

    public static Result weixinPayPage() {
        return ok(weixinPay.render());
    }

    public static Result setPage() {
		play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=set" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                return ok(set.render());
        }     
  
    }

    public static Result catPage() {
		play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=cat" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
                return ok(cat.render());
        }     
      
    }

    public static Result evaluatePage() {
		play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=evaluate" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
             return ok(evaluate.render());
        }     
   
    }

    public static Result HotSaleProductsPage() {
		play.Logger.info("loading /w/userCenter with session: " + session("WX_OPEN_ID"));

        if (session("WX_OPEN_ID") == null) {
            String oauthUrl =
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinController.wxAppId +
                            "&redirect_uri=http%3A%2F%2F" + domainName +
                            "%2Fdowxuser%3FresellerCode=" + "&state=HotSaleProducts" +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
            play.Logger.info("oauthUrl: " + oauthUrl);
            return redirect(oauthUrl);
        } else {
            play.Logger.info("wx open id: " + session("WX_OPEN_ID"));
         
          	return ok(HotSaleProducts.render());
        }     
	
	}

    public static Result privilegePage() {
        return ok(privilege.render());
    }

    public static Result myPrivilegePage() {
        return ok(myPrivilege.render());
    }

    /**
     * Handle login form submission.
     */
    // 登录验证
    public static Result authenticate() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());
        Form<LoginParser> loginForm = form(LoginParser.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + loginForm.errors().toString());
            return badRequest(login.render(loginForm));// @form.globalError.message
        } else {
            UserModel userModel = UserModel.findByloginName(loginForm.get().username);
            session().clear();
            session(SESSION_USER_NAME, userModel.loginName);
            session(SESSION_USER_ID, userModel.id.toString());
            if (userModel != null) {
                Integer role = userModel.userRole;
                session(SESSION_USER_ROLE, role.toString());
                if (role > 0) {
                    return ok(wine.render());
                } else {
                    // return redirect("assets/backend/index.html#/");
                    return forbidden("登录失败");
                }
            }
            return redirect(routes.Application.login());
        }
    }

    // 登录验证
    public static Result backendAuthenticate() {
        Form<LoginParser> loginForm = form(LoginParser.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            play.Logger.info(DateUtil.Date2Str(new Date()) + " form error: " + loginForm.errors().toString());
            // return badRequest(login.render(loginForm));//
            flash("logininfo", "登录失败,请重试");
            return redirect(routes.Application.backendLogin());
        } else {
            UserModel userModel = UserModel.findByloginName(loginForm.get().username);
            session().clear();
            session(SESSION_USER_NAME, userModel.loginName);
            session(SESSION_USER_ID, userModel.id.toString());
            if (userModel != null) {
                Integer role = userModel.userRole;
                session(SESSION_USER_ROLE, role.toString());
                if (role > 0) {// 1管理员, 2超级管理员
                    // TODO: 检查当前登录和最后一次登录IP, 如果不同, 要报警通知
                    // 更新最后一次登录的IP
                    userModel.lastLoginIP = request().remoteAddress();
                    Ebean.update(userModel);
                    return redirect(routes.OrderController.backendPage());
                } else {
                    // return redirect("assets/backend/index.html#/");
                    return forbidden("您没有权限登录后台");
                }
            }
            return redirect(routes.Application.backendLogin());
        }
    }

    // @Security.Authenticated(Secured.class)
    // @Cached(key = "showImage")
    public static Result showImage(String filename) {
        String path = Play.application().path().getPath() + "/public/upload/" + filename;

        try {
            response().setContentType("image");
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    IOUtils.toByteArray(new FileInputStream(new File(path))));
            return ok(bais);
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return notFound(filename + " is Not Found!");
    }

    // @Security.Authenticated(Secured.class)
    public static Result showBarcode(String filename) {
        String path = Play.application().path().getPath() + "/public/barcode/" + filename;

        try {
            response().setContentType("image");
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    IOUtils.toByteArray(new FileInputStream(new File(path))));
            return ok(bais);
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return notFound(filename + " barcode is Not Found!");
    }

    // @Security.Authenticated(Secured.class)
    // @Cached(key = "showImg")
    public static Result showImg(String folder, String filename) {
        String path = Play.application().path().getPath() + "/public/" + folder + "/" + filename;

        try {
            response().setContentType("image");
            return ok(getImageByte(path));
        } catch (IOException ex) {
            play.Logger.error(DateUtil.Date2Str(new Date()) + " - 找不到图片: " + folder + filename);
        }
        return notFound(folder + filename + " is Not Found!");
    }

    public static ByteArrayInputStream getImageByte(String path) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(new File(path))));
        return bais;
    }

    // @Security.Authenticated(Secured.class)
    public static Result uploadImage() {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        Msg<String> msg = new Msg<>();

        Http.MultipartFormData body = request().body().asMultipartFormData();

        Map map = body.asFormUrlEncoded();
        if (!map.containsKey("className") || !map.containsKey("property")) {
            msg.message = PARAM_ISSUE;
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
            return ok(Json.toJson(msg));
        }
        Long cid = 0l;
        if (map.containsKey("cid")) {
            cid = Long.parseLong(form().bindFromRequest().data().get("cid"));
        }
        String className = form().bindFromRequest().data().get("className");
        String property = form().bindFromRequest().data().get("property");

        Http.MultipartFormData.FilePart imgFile = body.getFile("file");
        if (imgFile != null) {
            // 图片地址及文件名, 以毫秒命名的文件名如"1449837445671"
            String path = Play.application().path().getPath() + "/public/upload/";
            String destFileName = String.valueOf(System.currentTimeMillis());

            String contentType = imgFile.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                msg.message = "error:not image file";
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                return ok(Json.toJson(msg));
            }

            File file = imgFile.getFile();
            try {
                // 生成原始图片
                FileUtils.copyFile(file, new File(path + destFileName));
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - upload img success");
                // 生成缩略图
                String thumbNailPath = Play.application().path().getPath() + "/public/thumb/";
                try {
                    if (!GenerateThumbNailImg(path + destFileName, thumbNailPath + destFileName, thumbSize))
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - generate thumbnail img issue: unknown");
                    else
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - generate thumbnail img success");

                } catch (Exception ex) {
                    play.Logger.error(
                            DateUtil.Date2Str(new Date()) + " - generate thumbnail img issue: " + ex.getMessage());
                }

                // 不指定ID也可以上传图片, 直接返回文件名
                if (cid == 0l) {
                    msg.flag = true;
                    msg.data = destFileName;
                    play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + destFileName);
                    return ok(Json.toJson(msg));
                }

                // 更新模型的字段为文件名
                if ("CompanyModel".equals(className)) {
                    CompanyModel found = CompanyModel.find.byId(cid);
                    if (found != null) {
                        if ("logo1".equals(property)) {
                            found.logo1 = destFileName;
                            Ebean.update(found);
                        } else if ("barcodeImg1".equals(property)) {
                            found.barcodeImg1 = destFileName;
                            Ebean.update(found);
                        } else if ("barcodeImg2".equals(property)) {
                            found.barcodeImg2 = destFileName;
                            Ebean.update(found);
                        } else if ("barcodeImg3".equals(property)) {
                            found.barcodeImg3 = destFileName;
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                } else if ("CatalogModel".equals(className)) {
                    CatalogModel found = CatalogModel.find.byId(cid);
                    if (found != null) {
                        if ("images".equals(property)) {
                            found.images = destFileName;
                            Ebean.update(found);
                        } else if ("smallImages".equals(property)) {
                            found.smallImages = destFileName;
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                } else if ("InfoModel".equals(className)) {
                    InfoModel found = InfoModel.find.byId(cid);
                    if (found != null) {
                        if ("images".equals(property)) {
                            if (LyLib.Utils.StrUtil.isNull(found.images)) {
                                found.images = destFileName;
                            } else {
                                found.images += "," + destFileName;
                            }
                            Ebean.update(found);
                        } else if ("smallImages".equals(property)) {
                            if (LyLib.Utils.StrUtil.isNull(found.smallImages)) {
                                found.smallImages = destFileName;
                            } else {
                                found.smallImages += "," + destFileName;
                            }
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                } else if ("ProductModel".equals(className)) {
                    ProductModel found = ProductModel.find.byId(cid);
                    if (found != null) {
                        if ("images".equals(property)) {
                            if (LyLib.Utils.StrUtil.isNull(found.images)) {
                                found.images = destFileName;
                            } else {
                                found.images += "," + destFileName;
                            }
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                } else if ("ThemeModel".equals(className)) {
                    ThemeModel found = ThemeModel.find.byId(cid);
                    if (found != null) {
                        if ("images".equals(property)) {
                            if (LyLib.Utils.StrUtil.isNull(found.images)) {
                                found.images = destFileName;
                            } else {
                                found.images += "," + destFileName;
                            }
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                } else if ("FoodcommentModel".equals(className)) {
                    FoodcommentModel found = FoodcommentModel.find.byId(cid);
                    if (found != null) {
                        if ("images".equals(property)) {
                            if (LyLib.Utils.StrUtil.isNull(found.images)) {
                                found.images = destFileName;
                            } else {
                                found.images += "," + destFileName;
                            }
                            Ebean.update(found);
                        } else {
                            msg.message = PARAM_ISSUE;
                            play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                            return ok(Json.toJson(msg));
                        }
                    } else {
                        msg.message = NO_FOUND;
                        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                        return ok(Json.toJson(msg));
                    }
                }
                msg.flag = true;
                msg.data = destFileName;
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + destFileName);
                return ok(Json.toJson(msg));
            } catch (IOException e) {
                msg.message = e.getMessage();
                play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
                return ok(Json.toJson(msg));
            }
        }
        msg.message = "error:Missing file";
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - result: " + msg.message);
        return ok(Json.toJson(msg));
    }

    public static boolean GenerateThumbNailImg(String baseFilePath, String thumbNailPath, float tagsize)
            throws Exception {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        if (tagsize == 0)
            tagsize = 100;

        String newUrl = thumbNailPath;
        java.awt.Image bigJpg = javax.imageio.ImageIO.read(new java.io.File(baseFilePath));

        if (bigJpg == null) {
            return false;
        }

        int old_w = bigJpg.getWidth(null);
        int old_h = bigJpg.getHeight(null);
        int new_w = 0;
        int new_h = 0;

        float tempdouble;
        tempdouble = old_w > old_h ? old_w / tagsize : old_h / tagsize;
        new_w = Math.round(old_w / tempdouble);
        new_h = Math.round(old_h / tempdouble);

        java.awt.image.BufferedImage tag = new java.awt.image.BufferedImage(new_w, new_h,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(bigJpg, 0, 0, new_w, new_h, null);

        try {
            File outputfile = new File(newUrl);
            ImageIO.write(tag, "png", outputfile);
        } catch (IOException e) {

        }
        return true;
    }

    @Security.Authenticated(SecuredSuperAdmin.class)
    public static Result generateAllThumbNailImg(float tagsize) {
        play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
                + " | DATA: " + request().body().asJson());

        if (tagsize == 0)
            tagsize = 300;

        String path = Play.application().path().getPath() + "/public/upload/";
        File file = new File(path);
        String[] fileNameList = file.list();

        String thumbNailPath = Play.application().path().getPath() + "/public/thumb/";
        for (String fileName : fileNameList) {
            play.Logger.info(DateUtil.Date2Str(new Date()) + " - generate thumb nail img: " + fileName);
            // 生成缩略图
            try {
                if (!GenerateThumbNailImg(path + fileName, thumbNailPath + fileName, tagsize))
                    play.Logger.info(DateUtil.Date2Str(new Date()) + " - generate thumbnail img issue: unknown");
                else
                    play.Logger.info(DateUtil.Date2Str(new Date()) + " - generate thumbnail img success");
            } catch (Exception ex) {
                play.Logger
                        .error(DateUtil.Date2Str(new Date()) + " - generate thumbnail img issue: " + ex.getMessage());
                return notFound("生成图片的缩略图出错");
            }
        }
        return ok("已生成所有图片的缩略图");
    }
}
