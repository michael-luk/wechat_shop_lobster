package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.PageInfo;
import com.avaje.ebean.Page;
import models.ProductModel;
import models.UserModel;
import models.common.CompanyModel;
import models.common.ContentModel;
import models.status.ContentType;
import org.apache.commons.io.FileUtils;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

//@Security.Authenticated(Secured.class)
public class CMS extends Controller implements IConst {

	@Security.Authenticated(Secured.class)
	public static Result getAllProducts(Integer page, Integer size) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		if (size == 0)
			size = PAGE_SIZE;
		if (page <= 0)
			page = 1;

		Msg<List<ProductModel>> msg = new Msg<>();
		Page<ProductModel> records;

		records = ProductModel.find.orderBy("id desc").findPagingList(size).setFetchAhead(false).getPage(page - 1);

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
		} else {
			msg.message = NO_FOUND;
		}
		return ok(Json.toJson(msg));
	}

	@Security.Authenticated(Secured.class)
	public static Result getProductById(long id) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());
		Msg<ProductModel> msg = new Msg<>();
		ProductModel found = ProductModel.find.byId(id);
		if (found != null) {
			msg.flag = true;
			msg.data = found;
		} else {
			msg.message = NO_FOUND;
		}
		return ok(Json.toJson(msg));
	}

	// public static Result getTags() {
	// return play.mvc.Results.TODO;
	// }
	//
	// public static Result getContentsByTags(String tagNames) {
	// return play.mvc.Results.TODO;
	// }
	//
	// public static Result getContentsByTag(String tagName) {
	// return play.mvc.Results.TODO;
	// }
	//
	// public static Result getContentsByCatalog(String catalogName) {
	// play.Logger.info(DateUtil.Date2Str(new Date()) + " - " +
	// request().method() + ": " + request().uri() + " | DATA: " +
	// request().body().asJson());
	// Msg<List<ContentModel>> msg = new Msg<>();
	// List<ContentModel> list = ContentModel.find
	// .where().eq("catalogs", catalogName).findList();
	//
	// if (list.size() > 0){
	// msg.flag = true;
	// msg.data = list;
	// }
	// else{
	// msg.message = NO_FOUND;
	// }
	// return ok(Json.toJson(msg));
	// }

	public static Result index() {
		return play.mvc.Results.TODO;
	}

	// public static Result login() {
	// Config conf = ConfigFactory.load();
	// String userModuleUrl = conf.getString("usermodule");
	// return ok(login.render(userModuleUrl));
	// }

	public static class Content {

		public Long id;

		public String title;
		public String level;// 景区等级

		public Long price;// 现价
		public Long refPrice;// 网络参考价
		public Long orgPrice;// 原价

		public String city;// 城市

		public String address;// 地址

		public String navigateMap;// 导游地图

		public int orderindex;// 排序

		public String smallPic;

		public String bigPic;

		public String bannerPic;

		public ContentType contentType;

		// public List<TagModel> tags;

		public String validate() {
			if (title == null) {
				return "Invalid title";
			}
			return null;
		}

	}

	// public static Result index() {
	// return ok(intro.render());
	// }

	// public static Result cms() {
	// return ok(cms.render());
	// }

	@Security.Authenticated(SecuredAdmin.class)
	public static Result saveContent() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());

		Form<Content> contentForm = form(Content.class).bindFromRequest();
		if (contentForm.hasErrors()) {
			return ok(contentForm.errorsAsJson());
		} else {
			final Long id = contentForm.get().id;
			ContentModel cm = null;
			if (id == null) {
				cm = new ContentModel();
			} else {
				cm = ContentModel.find.byId(id);
			}
			Content content = contentForm.get();
			cm.title = content.title;
			cm.contentType = content.contentType;
			cm.smallPic = content.smallPic;
			cm.bigPic = content.bigPic;
			cm.bannerPic = content.bannerPic;
			// List<Integer> tags = content.tags;
			// if(tags != null){
			// List<TagModel> tagList = TagModel.findAll();
			// List<TagModel> resList = new ArrayList<TagModel>();
			// for(TagModel tm : tagList){
			// for(Integer tagid: tags){
			// if(tagid == tm.id.intValue()){
			// resList.add(tm);
			// break;
			// }
			// }
			// }
			// cm.tags = content.tags;
			// }else{
			// cm.tags = new ArrayList<TagModel>();
			// }

			if (id == null) {
				cm.save();
			} else {
				cm.update();
			}

			Msg<ContentModel> mm = new Msg<ContentModel>();
			mm.flag = true;
			mm.data = cm;
			return ok(Json.toJson(mm));
		}
	}

	@Security.Authenticated(SecuredAdmin.class)
	public static Result deleteContent(Long cid) {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());

		ContentModel.delete(cid);
		return ok(RETURN_SUCCESS);
	}

	public static Result uploadImage() {
		play.Logger.info(DateUtil.Date2Str(new Date()) + " - " + request().method() + ": " + request().uri()
				+ " | DATA: " + request().body().asJson());

		Http.MultipartFormData body = request().body().asMultipartFormData();

		Map map = body.asFormUrlEncoded();
		if (!map.containsKey("cid") || !map.containsKey("isBig")) {
			return ok("error:bad parameters!");
		}
		Long cid;
		cid = Long.valueOf(((String[]) map.get("cid"))[0]);
		Boolean isBig = Boolean.valueOf(((String[]) map.get("isBig"))[0]);

		Http.MultipartFormData.FilePart imgFile = body.getFile("file");
		if (imgFile != null) {
			String path = Play.application().path().getPath() + "/public/upload/";
			String destFileName = String.valueOf(System.currentTimeMillis());

			String contentType = imgFile.getContentType();

			if (contentType == null || !contentType.startsWith("image/")) {
				return ok(Json.toJson("error:not image file"));
			}

			File file = imgFile.getFile();
			try {
				FileUtils.copyFile(file, new File(path + destFileName));
				ContentModel.updateContentImage(cid, destFileName, isBig);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ok(destFileName);

		}
		return ok(Json.toJson("error:Missing file"));
	}

	public static Result getMembersByRole(String role, Long page) {
		Msg<List<UserModel>> mm = new Msg<List<UserModel>>();

		List<UserModel> list = UserModel.findByRole(role, page);
		mm.flag = true;
		mm.data = list;

		return ok(Json.toJson(mm));
	}

	// public static Result getOrdersByStatus(Long status, Long page) {
	// MessageModel<List<OrderModel>> mm = new MessageModel<List<OrderModel>>();
	//
	// List<OrderModel> list = OrderModel.findByStatus(status, page);
	// mm.setFlag(true);
	// mm.setData(list);
	//
	// return ok(Json.toJson(mm));
	// }

	// public static Result member() {
	// return ok(member.render());
	// }

	// public static Result order() {
	// return ok(order.render(null));
	// }

}
