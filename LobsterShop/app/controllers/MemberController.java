package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.MD5;
import LyLib.Utils.StrUtil;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.UserModel;
import models.status.RegisterType;
import models.status.UserRole;
import models.status.UserStatus;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.List;

import static play.data.Form.form;

public class MemberController extends Controller implements IConst {

	private static long accessTokenFailureTime = 0;
	private static String accessToken;
	private static ObjectMapper mapper = new ObjectMapper();

	public static Result getAllMember() {
		Msg<List<UserModel>> msg = new Msg<>();

		List<UserModel> found = UserModel.find.findList();
		if (found.size() > 0) {
			for (UserModel user : found) {
				user.password = "";
			}
			msg.flag = true;
			msg.data = found;
		}
		return ok(Json.toJson(msg));
	}

	public static Result updateMoreInfo(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Form<UserMoreInfoParser> moreInfoForm = form(UserMoreInfoParser.class).bindFromRequest();
		Msg<UserModel> msg = new Msg<>();

		if (moreInfoForm.hasErrors()) {
			msg.message = moreInfoForm.globalError().message();

		} else {
			UserMoreInfoParser info = moreInfoForm.get();
			UserModel um = UserModel.find.byId(id);

			if (um == null) {
				msg.message = NO_FOUND;
				return ok(Json.toJson(msg));
			} else {
				um.nickname = info.nickname;
				um.signature = info.signature;
				um.sex = info.sex;
				um.headImgUrl = info.headImgUrl;
				Ebean.update(um);

				msg.flag = true;
				msg.data = um;
			}
		}
		return ok(Json.toJson(msg));
	}

	public static class UserMoreInfoParser {

		public String nickname;
		public String signature;
		public int sex;
		public String headImgUrl;

		public String validate() {
			return null;
		}
	}

	public static class Login {

		public String username;
		public String password;
		public String device;// login device desc
		public boolean force;// force to login

		public String validate() {
			if (password != null && password.length() < 32) {
				password = MD5.getMD5(password);
			}
			if (UserModel.authenticate(username, password) == null) {
				return "用户名密码错误!";
			}
			return null;
		}
	}

	public static class ChangePwd {

		public String username;
		public String newpassword;
		// public String oldpassword;

		public String validate() {
			// if (oldpassword != null && oldpassword.length() < 32) {
			// oldpassword = MD5.getMD5(oldpassword);
			// }
			if (newpassword != null && newpassword.length() < 32) {
				newpassword = MD5.getMD5(newpassword);
			}
			// if (UserModel.authenticate(username, oldpassword) == null) {
			// return "原密码错误!";
			// }
			return null;
		}

	}

	public static class Register {

		public String username;
		public String name;
		public String password;
		public String device;
		public String unionId;
		public String registerType;

		public String validate() {
			if ("".equals(username) || username == null) {
				return "用户名不能为空";
			}
			if (!StrUtil.isNull(unionId)) {
				if (StrUtil.isNull(registerType))
					return "注册类型不能为空";
			}
			if (!StrUtil.isNull(registerType)) {
				if (!"WEIXIN".equals(registerType) && !"WEIBO".equals(registerType) && !"QQ".equals(registerType))
					return "注册类型不正确";
				if (StrUtil.isNull(unionId))
					return "第三方账号不能为空";
			}
			if ("".equals(password) || password == null) {
				return "密码不能为空";
			}
			if (!UserModel.verifyUser(username)) {
				return "用户名已注册";
			}
			if (password.length() < 32) {
				password = MD5.getMD5(password);
			}
			return null;
		}
	}

	public static Result login() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		Msg<UserModel> msg = new Msg<>();

		if (!loginForm.hasErrors()) {
			UserModel user = UserModel.findByloginName(loginForm.get().username);
			// get sessionid from utils
			user.sessionid = MD5.generateHash().substring(22);
			user.device = loginForm.get().device;
			user.update();

			msg.flag = true;
			msg.data = user;
			msg.message = user.id.toString();
		} else {
			msg.message = "用户名密码错误!";
		}
		return ok(Json.toJson(msg));
	}

	public static Promise<Result> registerAsync() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Form<Register> registerForm = form(Register.class).bindFromRequest();

		if (registerForm.hasErrors()) {
			Promise<Result> resultPromise = Promise.promise(() -> {
				Msg<UserModel> msg = new Msg<>();
				msg.message = registerForm.globalError().message();
				return ok(Json.toJson(msg));
			});
			return resultPromise;
		} else {
			Msg<UserModel> msg = new Msg<>();
			Register register = registerForm.get();

			// 检查是否已经存在注册的第三方账户
			UserModel exist3PartyUser = UserModel.get3PartyUser(register.registerType, register.unionId);
			if (exist3PartyUser != null) {
				msg.message = "已存在第三方登录账号";
				msg.errorCode = 2;
				msg.data = exist3PartyUser;
				Promise<Result> resultPromise = Promise.promise(() -> ok(Json.toJson(msg)));
				return resultPromise;
			}

			UserModel um = new UserModel();
			um.loginName = register.username;
			um.createdAtStr = LyLib.Utils.DateUtil.Date2Str(new Date());
			if (um.birthday != null)
				um.birthdayStr = LyLib.Utils.DateUtil.Date2Str(um.birthday);
			um.password = register.password;
			um.device = register.device;
			um.sessionid = MD5.generateHash().substring(22);

			if (StrUtil.isNull(register.registerType)) {
				um.registerType = RegisterType.PHONE;
			} else {
				um.registerType = RegisterType.valueOf(register.registerType);
				um.unionId = register.unionId;
			}

			// huanxin handle
			String huanxinUserName = huanxinUsernameProfix + um.loginName;
			JsonNode jsonData4Reg = Json.newObject().put("username", huanxinUserName).put("password", um.password)
					.put("nickname", um.nickname);

			String url4Reg = "https://a1.easemob.com/" + huanxinOrgName + "/" + huanxinAppName + "/users";
			final Promise<Result> resultPromise = WS.url(url4Reg).post(jsonData4Reg)
					.map(new Function<WSResponse, Result>() {
						public Result apply(WSResponse response) {
							int responseCode = response.getStatus();
							if (responseCode == 200) {
								JsonNode resultJson = response.asJson();
								if ("post".equals(resultJson.get("action").asText())) {
									if (resultJson.get("entities") != null) {
										if (resultJson.get("entities").isArray()) {
											String actulHuanxinUsername = resultJson.get("entities").get(0)
													.get("username").asText();
											if (huanxinUserName.equals(actulHuanxinUsername)) {
												Ebean.save(um);
												msg.flag = true;
												msg.data = um;
											} else {
												msg.message = "环信注册失败, 环信用户名不匹配: " + actulHuanxinUsername;
											}
										} else {
											msg.message = "环信注册失败, 环信服务器返回数据不正确: " + resultJson.toString();
										}
									} else {
										msg.message = "环信注册失败, 环信服务器返回数据不正确: " + resultJson.toString();
									}
								} else {
									msg.message = "环信注册失败, 环信服务器返回数据不正确: " + resultJson.toString();
								}
							} else {
								msg.message = "环信注册失败, 环信服务器HTTP Status: " + responseCode;
							}
							return ok(Json.toJson(msg));
							/*
							 * 环信返回值参考: { "action" : "post", "application" :
							 * "c6d608e0-3201-11e5-bb3e-5dd5c14c4b96", "path" :
							 * "/users", "uri" :
							 * "https://a1.easemob.com/644841777/qiyuu/users",
							 * "entities" : [ { "uuid" :
							 * "d46b482a-3d65-11e5-ae1e-815b261e2645", "type" :
							 * "user", "created" : 1438994326946, "modified" :
							 * 1438994326946, "username" :
							 * "qiyuuhuanxinuser_test_username_15.0306832913",
							 * "activated" : true } ], "timestamp" :
							 * 1438994326946, "duration" : 30, "organization" :
							 * "644841777", "applicationName" : "qiyuu" }
							 */
						}
					});
			return resultPromise;
		}
	}

	public static Promise<Result> wsActionExample() {

		// Make a REST call
		Promise<WSResponse> response = WS.url("http://baker.com/slicedBread").get();

		// Map it to a result and return as an asynchronous result
		return response.map(resp -> (Result) ok(resp.asJson()));
	}

	public static Result register() {
		Form<Register> registerForm = form(Register.class).bindFromRequest();
		Msg<UserModel> msg = new Msg<>();

		if (registerForm.hasErrors()) {
			msg.message = registerForm.globalError().message();

		} else {
			Register register = registerForm.get();
			UserModel um = new UserModel();
			um.loginName = register.username;
			um.createdAtStr = LyLib.Utils.DateUtil.Date2Str(new Date());
			if (um.birthday != null)
				um.birthdayStr = LyLib.Utils.DateUtil.Date2Str(um.birthday);
			um.password = register.password;
			um.device = register.device;
			um.sessionid = MD5.generateHash().substring(22);
			Ebean.save(um);

			huanxinRegister(huanxinUsernameProfix + um.loginName, um.password, um.nickname);

			msg.flag = true;
			msg.data = um;
		}
		return ok(Json.toJson(msg));
	}

	private static String huanxinUsernameProfix = "qiyuuhuanxinuser_";
	private static String huanxinOrgName = "644841777";
	private static String huanxinAppName = "qiyuu";

	public static Promise<Result> huanxinRegister(String username, String password, String nickname) {
		JsonNode jsonData4Reg = Json.newObject().put("username", username).put("password", password).put("nickname",
				nickname);

		String url4Reg = "https://a1.easemob.com/" + huanxinOrgName + "/" + huanxinAppName + "/users";
		final Promise<Result> resultPromise = WS.url(url4Reg).post(jsonData4Reg)
				.map(new Function<WSResponse, Result>() {
					public Result apply(WSResponse response) {
						return ok(response.getBody());
					}
				});
		return resultPromise;
	}

	private static String huanxinClientId = "YXA6xtYI4DIBEeW7Pl3VwUxLlg";
	private static String huanxinClientSecret = "YXA6w8wYSWgEA0GAb2bJkCniZ3QPWk8";

	public static Promise<Result> huanxinToken() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());
		// curl -X POST "https://a1.easemob.com/644841777/qiyuu/token"
		// -d
		// '{"grant_type":"client_credentials","client_id":"YXA6xtYI4DIBEeW7Pl3VwUxLlg","client_secret":"YXA6w8wYSWgEA0GAb2bJkCniZ3QPWk8"}'
		JsonNode jsonData4Reg = Json.newObject().put("grant_type", "client_credentials")
				.put("client_id", huanxinClientId).put("client_secret", huanxinClientSecret);

		String url4Reg = "https://a1.easemob.com/" + huanxinOrgName + "/" + huanxinAppName + "/token";
		final Promise<Result> resultPromise = WS.url(url4Reg).post(jsonData4Reg)
				.map(new Function<WSResponse, Result>() {
					public Result apply(WSResponse response) {
						return ok(response.getBody());
					}
				});
		return resultPromise;
	}

	// 授权注册, 先不用
	// public static F.Promise<Result> huanxinRegister(String clientId, String
	// clientSecret, String username, String password) {
	// JsonNode jsonData4Token = Json.newObject()
	// .put("grant_type", "client_credentials")
	// .put("client_id", clientId)
	// .put("client_secret", clientSecret);
	//
	// JsonNode jsonData4Reg = Json.newObject()
	// .put("username", username)
	// .put("password", password);
	//
	// String url4Token =
	// "https://a1.easemob.com/easemob-demo/chatdemoui/token";
	// String url4Reg = "https://a1.easemob.com/easemob-demo/chatdemoui/users";
	// final F.Promise<Result> resultPromise =
	// WS.url(url4Token).post(Json.toJson(jsonData4Token)).map(
	// new F.Function<WSResponse, Result>() {
	// public Result apply(WSResponse response) {
	// String token = response.getBody();
	// final F.Promise<Result> resultPromise =
	// WS.url(url4Reg).post(jsonData4Reg).map(
	// new F.Function<WSResponse, Result>() {
	// public Result apply(WSResponse response) {
	// String token = response.getBody();
	//
	//
	// return ok(response.getBody());
	// }
	// }
	// );
	// return ok(response.getBody());
	// }
	// }
	// );
	// return resultPromise;
	// }

	public static Result changePsw() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Form<ChangePwd> loginForm = form(ChangePwd.class).bindFromRequest();
		Msg<UserModel> msg = new Msg<>();

		if (!loginForm.hasErrors()) {
			String name = loginForm.get().username;
			String passwd = loginForm.get().newpassword;

			UserModel user = UserModel.findByloginName(name);

			if (user != null) {
				user.password = passwd;
				user.sessionid = MD5.generateHash().substring(22);
				user.update();

				msg.flag = true;
				msg.data = user;
			} else {
				msg.errorCode = SESSION_UNAVAILABLE;
				msg.message = NO_FOUND;
			}

		} else {
			msg.message = loginForm.errorsAsJson().toString();
		}
		return ok(Json.toJson(msg));
	}

	public static Result forgetPsw(String username) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri());
		Msg<UserModel> msg = new Msg<>();

		if (username != null) {
			UserModel user = UserModel.findByloginName(username);
			if (user != null) {
				String newpwd = MD5.generateHash().substring(26);

				if (newpwd != null) {
					user.password = newpwd;
					user.update();

					msg.data = user;
					msg.flag = true;
				}
			} else {
				msg.errorCode = SESSION_UNAVAILABLE;
			}

		} else {
			msg.message = "name is not allowed null";
		}
		return ok(Json.toJson(msg));
	}

}
