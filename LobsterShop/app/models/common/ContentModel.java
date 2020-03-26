package models.common;

import LyLib.Utils.DateUtil;
import LyLib.Utils.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Reference;
import models.UserModel;
import models.status.ContentType;
import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contents")
public class ContentModel extends Model implements Serializable {

    @Id
    public Long id;

    public ContentType contentType;     //类型

    public String catalogs;         //所属多个分类(多个以分号";"隔开)

    public String catalog1;         //所属分类1
    public String catalog2;         //所属分类2
    public String catalog3;         //所属分类3
    public String catalog4;         //所属分类4
    public String catalog5;         //所属分类5

    public String tags;             //标签(多个以","隔开)

    public String name;            //名称
    public String title;            //标题

    @Lob
    public String plateContent;

    @Lob
    public String richContent;

//    public String priceDates;       //价格日历, 用逗号分隔的日期字符串, 如"2015-11-1,2015-11-2"
//    public String priceDateValues;  //价格日历价格数值, 用逗号分隔的数值, 如"2999,3999"

//    public Long sceneid;//景点id

//    public String city;//城市

//    public String address;//地址

    public String smallPic;

    public String bigPic;

    public String bannerPic;

    public String phoneNumber;

    public String fax;

    public String email;

//    public String navigateMap;//导游地图

    //    public String level;//景区等级

    public String links;        //链接(多个以","隔开)

    public String videos;       //多个视频用逗号隔开

    public int orderIndex;      //排序

//    @ManyToMany
//    public List<TagModel> tags;

//    public ContentStatus status;

    public String statusCode;           //状态码
//    public ContentLanguage language;

    public boolean deleteFlag = false;          //删除标识
    public boolean activeFlag = true;           //激活标识

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date createdAt = new Date();         //创建日期
    public String createdAtStr;                 //创建日期字符串表示

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date deletedAt;                      //删除日期
    public String deletedAtStr;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date restoredAt;                      //还原(取消删除)日期
    public String restoredAtStr;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date activedAt;                      //激活日期
    public String activedAtStr;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date deactivedAt;                    //取消激活日期
    public String deactivedAtStr;

    @JsonIgnore
    @Reference
    public ContentModel parent;         //所属
    public String parentId;             //所属Id

    @JsonIgnore
    @Reference
    public UserModel author;            //发布者信息

    public Long authorId;               //发布者id
    public String authorName;           //发布者名字
    public String authorHeadImg;        //发布者头像

    @JsonIgnore
    @Reference
    public List<ContentModel> childContents;    //下属内容
    public String childContentIds;              //下属内容id(多个以分号";"隔开)

    @Lob
    public String comment;              //备注

    public String additionalString1;    //保留字段
    public String additionalString2;
    public String additionalString3;
    public String additionalString4;
    public String additionalString5;

    public String additionalInt1;
    public String additionalInt2;
    public String additionalInt3;

    public ContentModel() {
        createdAtStr = DateUtil.Date2Str(createdAt);
        setActiveFlag(true);
    }

    // -- Queries

    public static Finder<Long, ContentModel> find = new Finder(Long.class, ContentModel.class);

    /**
     * Retrieve all.
     */
    public static List<ContentModel> findAll() {
        return find.all();
    }

    public String validate() {
        if (StrUtil.isNull(title)) {
            return "标题不能为空";
        }
        if (StrUtil.isNull(plateContent) && StrUtil.isNull(richContent)) {
            return "正文不能为空";
        }
        return null;
    }

    @Override
    public String toString() {
        return "ContentModel [title:" + title
                + "]";
    }

//    public static List<ContentModel> getContentsByTypeTag(Long contentType, String tag, String orderby, Long page) {
//        if (tag != null && tag.length() > 0) {
////        return find.fetch("tags").where().eq("contentType", contentType).eq("deleteFlag", false)
////        		.eq("tags.name", tag)
////                .orderBy(orderby == null?"orderindex":orderby)
////                .findPagingList(Constants.AMOUNT_PER_PAGE).getPage(page.intValue() - 1)
////                .getList();
//            String sql = "select distinct t0.id id, t0.name name, t0.level level, t0.price price, t0.ref_price refPrice, " +
//                    "t0.org_price orgPrice, t0.sceneid sceneid, t0.city city, t0.address address, t0.small_pic smallPic , t0.big_pic bigPic , " +
//                    "t0.content_type contentType, t0.status status, t0.language language, t0.delete_flag deleteFlag, t0.created_at createdAt, t0.orderindex orderindex " +
//                    "from content t0 left outer join content_tags u1z_ on u1z_.content_id = t0.id  " +
//                    "left outer join tags u1 on u1.id = u1z_.tags_id  " +
//                    "where u1.name ='" + tag + "' and delete_flag=false  order by " + (orderby == null ? "orderindex desc, createdAt desc" : orderby) + " limit 10 offset "
//                    + (page - 1) * 10;
//
////            List<SqlRow> sqlRows =
////                    Ebean.createSqlQuery(sql)
////                            .findList();
//
//            RawSql rawSql = RawSqlBuilder
//                    // let ebean parse the SQL so that it can
//                    // add expressions to the WHERE and HAVING
//                    // clauses
//                    .parse(sql)
//                            // map resultSet columns to bean properties
//                    .create();
//
//
//            com.avaje.ebean.Query<ContentModel> query = Ebean.find(ContentModel.class);
//            query.setRawSql(rawSql);
//
//            List<ContentModel> list = query.findList();
//            return list;
//        } else {
//            return find.where().eq("contentType", contentType).eq("deleteFlag", false)
//                    .orderBy(orderby == null ? "createdAt desc" : orderby)
//                    .findPagingList(Constants.AMOUNT_PER_PAGE).getPage(page.intValue() - 1)
//                    .getList();
//        }
//    }
//
//    public static List<ContentModel> searchContentsByKey(String key, Long page) {
//        if (key != null && key.length() > 0) {
//            String sql = "select distinct t0.id id, t0.name name, t0.level level, t0.price price, t0.ref_price refPrice, " +
//                    "t0.org_price orgPrice, t0.sceneid sceneid, t0.city city, t0.address address, t0.small_pic smallPic , t0.big_pic bigPic , " +
//                    "t0.content_type contentType, t0.status status, t0.language language, t0.delete_flag deleteFlag, t0.created_at createdAt " +
//                    "from content t0 left outer join content_tags u1z_ on u1z_.content_id = t0.id  " +
//                    "left outer join tags u1 on u1.id = u1z_.tags_id  " +
//                    "where (t0.city like '%" + key + "%'  or t0.name like '%" + key + "%'  or u1.name like '%" + key + "%' ) and delete_flag=false limit 10 offset "
//                    + (page - 1) * 10;
//
////            List<SqlRow> sqlRows =
////                    Ebean.createSqlQuery(sql)
////                            .findList();
//
//            RawSql rawSql = RawSqlBuilder
//                    // let ebean parse the SQL so that it can
//                    // add expressions to the WHERE and HAVING
//                    // clauses
//                    .parse(sql)
//                            // map resultSet columns to bean properties
//                    .create();
//
//
//            com.avaje.ebean.Query<ContentModel> query = Ebean.find(ContentModel.class);
//            query.setRawSql(rawSql);
//
//            List<ContentModel> list = query.findList();
//            return list;
//
//
//        } else {
//            return find.where().eq("deleteFlag", false)
//                    .findPagingList(Constants.AMOUNT_PER_PAGE).getPage(page.intValue() - 1)
//                    .getList();
//        }
//    }

//    public static ContentModel getContentBySceneId(Long sceneid) {
//        return find.where().eq("sceneid", sceneid).eq("deleteFlag", false).findUnique();
//    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public void setDeleteFlag(boolean flag) {
        if (deleteFlag != flag) {

            if (flag == true) {
                deletedAt = new Date();
                deletedAtStr = DateUtil.Date2Str(deletedAt);
                restoredAt = null;
                restoredAtStr = "";
            } else {
                if (deleteFlag == true) {    //确保是先删除后还原时才重设时间
                    deletedAt = null;
                    deletedAtStr = "";
                    restoredAt = new Date();
                    restoredAtStr = DateUtil.Date2Str(restoredAt);
                }
            }
            deleteFlag = flag;
        }
    }

    public static void setDeleteFlag(Long id, boolean flag) {
        ContentModel found = find.ref(id);
        found.setDeleteFlag(flag);
    }

    public void setActiveFlag(boolean flag) {
        if (activeFlag != flag) {

            if (flag == true) {
                activedAt = new Date();
                activedAtStr = DateUtil.Date2Str(activedAt);
                deactivedAt = null;
                deactivedAtStr = "";
            } else {
                activedAt = null;
                activedAtStr = "";
                deactivedAt = new Date();
                deactivedAtStr = DateUtil.Date2Str(deactivedAt);
            }
            activeFlag = flag;
        }
    }

    public static void setActiveFlag(Long id, boolean flag) {
        ContentModel found = find.ref(id);
        found.setActiveFlag(flag);
    }

    public static void updateContentImage(Long content, String imgName, Boolean isBig) {
        ContentModel found = find.byId(content);
        if (found != null) {
            if (isBig) {
                found.bigPic = imgName;
            } else {
                found.smallPic = imgName;
            }
            found.update();
        }
    }

    public static void saveOrUpdate(ContentModel cm) {
        ContentModel found = find.byId(cm.id);
        if (found != null) {
            cm.update();
        } else {
            cm.save();
        }
    }

}
