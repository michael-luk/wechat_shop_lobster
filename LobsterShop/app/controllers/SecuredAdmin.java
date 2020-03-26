package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.Msg;
import models.UserModel;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredAdmin extends Security.Authenticator implements IConst {

	@Override
	public String getUsername(Context ctx) {
//		return "developing";
		String userName = ctx.session().get(SESSION_USER_NAME);
		if (userName == null) {
			return null;
		} else {
			if ("2".equals(ctx.session().get(SESSION_USER_ROLE)) || "1".equals(ctx.session().get(SESSION_USER_ROLE)))
				// 返回非null代表验证成功
				return userName;
			else
				return null;
		}
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.backendLogin());
		// Msg<UserModel> msg = new Msg<>();
		// msg.errorCode = SESSION_UNAVAILABLE;
		// return ok(Json.toJson(msg));
	}

	// public static boolean isOwnerOf(String userName) {
	// return
	// Context.current().session().get(SESSION_USER_NAME).equals(userName);
	// }
	//
	// public static boolean isFromMobile() {
	// return
	// SESSION_DEVICE_MOBILE.equals(Context.current().session().get(SESSION_DESC));
	// }
	//
	// public static UserModel getCurrentUser() {
	// String name = Context.current().session().get(SESSION_USER_NAME);
	// return UserModel.findByloginName(name);
	// }
}