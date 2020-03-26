# Host: localhost  (Version: 5.1.62-community)
# Date: 2020-03-26 12:00:37
# Generator: MySQL-Front 5.3  (Build 4.120)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "activityhistory"
#

DROP TABLE IF EXISTS `activityhistory`;
CREATE TABLE `activityhistory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activities_product_id` bigint(20) DEFAULT NULL,
  `the_line_activities` bigint(20) DEFAULT NULL,
  `line_activities_user_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `line_activities_user_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cut_user_id` bigint(20) DEFAULT NULL,
  `head_img_url` longtext COLLATE utf8_unicode_ci,
  `nickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `before_cut_price` decimal(10,2) DEFAULT NULL,
  `after_cut_price` decimal(10,2) DEFAULT NULL,
  `bargain` decimal(10,2) DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "activityhistory"
#


#
# Structure for table "catalogs"
#

DROP TABLE IF EXISTS `catalogs`;
CREATE TABLE `catalogs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `catalog_index` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `images` longtext COLLATE utf8_unicode_ci,
  `small_images` longtext COLLATE utf8_unicode_ci,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "catalogs"
#

INSERT INTO `catalogs` VALUES (1,1,'小龙虾系列','longxia.png','xilie1.jpg',NULL),(2,2,'鸭后系列','yahou.png','xilie2.jpg',NULL),(3,3,'小丸子系列','wanzi.png','xilie3.jpg',NULL),(4,4,'烧烤系列','shaokao.png','xilie4.jpg',NULL);

#
# Structure for table "commercialactivities"
#

DROP TABLE IF EXISTS `commercialactivities`;
CREATE TABLE `commercialactivities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `productname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `images` longtext COLLATE utf8_unicode_ci,
  `price` decimal(10,2) DEFAULT NULL,
  `bargain_bottom_line` decimal(10,2) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `activity_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active_state` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `people_participate` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sales_volume` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "commercialactivities"
#


#
# Structure for table "company"
#

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cell_phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weibo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qq` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weixin` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description1` longtext COLLATE utf8_unicode_ci,
  `description2` longtext COLLATE utf8_unicode_ci,
  `register_info1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `register_info2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `barcode_img1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `barcode_img2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `barcode_img3` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo3` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `marketing1` decimal(10,2) DEFAULT NULL,
  `marketing2` decimal(10,2) DEFAULT NULL,
  `marketing3` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "company"
#

INSERT INTO `company` VALUES (1,'',NULL,NULL,NULL,NULL,NULL,'17040643',NULL,NULL,NULL,NULL,NULL,'weibo.png','weixin.png',NULL,'logo.jpg',NULL,NULL,0.00,0.00,0.00);

#
# Structure for table "contents"
#

DROP TABLE IF EXISTS `contents`;
CREATE TABLE `contents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` int(11) DEFAULT NULL,
  `catalogs` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `catalog1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `catalog2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `catalog3` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `catalog4` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `catalog5` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tags` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `plate_content` longtext COLLATE utf8_unicode_ci,
  `rich_content` longtext COLLATE utf8_unicode_ci,
  `small_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `big_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `banner_pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `links` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `videos` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `order_index` int(11) DEFAULT NULL,
  `status_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_flag` tinyint(1) DEFAULT '0',
  `active_flag` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `deleted_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `restored_at` datetime DEFAULT NULL,
  `restored_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actived_at` datetime DEFAULT NULL,
  `actived_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deactived_at` datetime DEFAULT NULL,
  `deactived_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `author_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `author_head_img` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `child_content_ids` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` longtext COLLATE utf8_unicode_ci,
  `additional_string1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_string2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_string3` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_string4` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_string5` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_int1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_int2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `additional_int3` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "contents"
#


#
# Structure for table "coupons"
#

DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reach_money` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `start_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "coupons"
#

INSERT INTO `coupons` VALUES (1,500.00,100.00,'2016/2/1','2016/2/28',NULL),(2,300.00,80.00,'2016/2/1','2016/2/28',NULL),(3,200.00,50.00,'2016/2/1','2016/2/28',NULL);

#
# Structure for table "enjoythecode"
#

DROP TABLE IF EXISTS `enjoythecode`;
CREATE TABLE `enjoythecode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_user_id` bigint(20) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `code_type` tinyint(1) DEFAULT '0',
  `state` tinyint(1) DEFAULT '0',
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "enjoythecode"
#


#
# Structure for table "fondoutrequests"
#

DROP TABLE IF EXISTS `fondoutrequests`;
CREATE TABLE `fondoutrequests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_user_id` bigint(20) DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `yong_jin` decimal(10,2) DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "fondoutrequests"
#


#
# Structure for table "infos"
#

DROP TABLE IF EXISTS `infos`;
CREATE TABLE `infos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `classify` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `images` longtext COLLATE utf8_unicode_ci,
  `small_images` longtext COLLATE utf8_unicode_ci,
  `display_date` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description1` longtext COLLATE utf8_unicode_ci,
  `description2` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "infos"
#

INSERT INTO `infos` VALUES (1,'guanggao',NULL,NULL,NULL,'jiechan.jpg',NULL,NULL,'2020-03-19 13:05:24',NULL,NULL),(2,'guanggao',NULL,NULL,NULL,'xinpin1.jpg',NULL,NULL,'2020-03-19 13:05:24',NULL,NULL),(3,'guanggao',NULL,NULL,NULL,'xinpin3.jpg',NULL,NULL,'2020-03-19 13:05:24',NULL,NULL);

#
# Structure for table "lineactivities"
#

DROP TABLE IF EXISTS `lineactivities`;
CREATE TABLE `lineactivities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sponsor_id` bigint(20) DEFAULT NULL,
  `sponsor_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `present_price` decimal(10,2) DEFAULT NULL,
  `before_cut_price` decimal(10,2) DEFAULT NULL,
  `the_number_has_been_cut` int(11) DEFAULT NULL,
  `sabreplay_limit` int(11) DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "lineactivities"
#


#
# Structure for table "play_evolutions"
#

DROP TABLE IF EXISTS `play_evolutions`;
CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text COLLATE utf8_unicode_ci,
  `revert_script` text COLLATE utf8_unicode_ci,
  `state` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_problem` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "play_evolutions"
#

INSERT INTO `play_evolutions` VALUES (1,'2b436b83742785c03c32e43b221ab29907e3f808','2020-03-19 13:05:22','create table activityhistory (\nid                        bigint auto_increment not null,\nactivities_product_id     bigint,\nthe_line_activities       bigint,\nline_activities_user_id   varchar(255),\nline_activities_user_name varchar(255),\ncut_user_id               bigint,\nhead_img_url              longtext,\nnickname                  varchar(255),\noriginal_price            Decimal(10,2),\nbefore_cut_price          Decimal(10,2),\nafter_cut_price           Decimal(10,2),\nbargain                   Decimal(10,2),\ncreated_at_str            varchar(255),\ncomment                   varchar(255),\nconstraint pk_activityhistory primary key (id))\n;\n\ncreate table catalogs (\nid                        bigint auto_increment not null,\ncatalog_index             integer,\nname                      varchar(255),\nimages                    longtext,\nsmall_images              longtext,\ncomment                   varchar(255),\nconstraint pk_catalogs primary key (id))\n;\n\ncreate table commercialactivities (\nid                        bigint auto_increment not null,\nproductname               varchar(255),\nimages                    longtext,\nprice                     Decimal(10,2),\nbargain_bottom_line       Decimal(10,2),\nstock                     integer,\nactivity_time             varchar(255),\nactive_state              varchar(255),\npeople_participate        varchar(255),\nsales_volume              varchar(255),\ncreated_at_str            varchar(255),\ncomment                   varchar(255),\nconstraint pk_commercialactivities primary key (id))\n;\n\ncreate table company (\nid                        bigint auto_increment not null,\nname                      varchar(255),\nlocation                  varchar(255),\nemail                     varchar(255),\nphone                     varchar(255),\ncell_phone                varchar(255),\nweibo                     varchar(255),\nqq                        varchar(255),\nweixin                    varchar(255),\ndescription1              longtext,\ndescription2              longtext,\nregister_info1            varchar(255),\nregister_info2            varchar(255),\nbarcode_img1              varchar(255),\nbarcode_img2              varchar(255),\nbarcode_img3              varchar(255),\nlogo1                     varchar(255),\nlogo2                     varchar(255),\nlogo3                     varchar(255),\nmarketing1                Decimal(10,2),\nmarketing2                Decimal(10,2),\nmarketing3                Decimal(10,2),\nconstraint pk_company primary key (id))\n;\n\ncreate table contents (\nid                        bigint auto_increment not null,\ncontent_type              integer,\ncatalogs                  varchar(255),\ncatalog1                  varchar(255),\ncatalog2                  varchar(255),\ncatalog3                  varchar(255),\ncatalog4                  varchar(255),\ncatalog5                  varchar(255),\ntags                      varchar(255),\nname                      varchar(255),\ntitle                     varchar(255),\nplate_content             longtext,\nrich_content              longtext,\nsmall_pic                 varchar(255),\nbig_pic                   varchar(255),\nbanner_pic                varchar(255),\nphone_number              varchar(255),\nfax                       varchar(255),\nemail                     varchar(255),\nlinks                     varchar(255),\nvideos                    varchar(255),\norder_index               integer,\nstatus_code               varchar(255),\ndelete_flag               tinyint(1) default 0,\nactive_flag               tinyint(1) default 0,\ncreated_at                datetime,\ncreated_at_str            varchar(255),\ndeleted_at                datetime,\ndeleted_at_str            varchar(255),\nrestored_at               datetime,\nrestored_at_str           varchar(255),\nactived_at                datetime,\nactived_at_str            varchar(255),\ndeactived_at              datetime,\ndeactived_at_str          varchar(255),\nparent_id                 varchar(255),\nauthor_id                 bigint,\nauthor_name               varchar(255),\nauthor_head_img           varchar(255),\nchild_content_ids         varchar(255),\ncomment                   longtext,\nadditional_string1        varchar(255),\nadditional_string2        varchar(255),\nadditional_string3        varchar(255),\nadditional_string4        varchar(255),\nadditional_string5        varchar(255),\nadditional_int1           varchar(255),\nadditional_int2           varchar(255),\nadditional_int3           varchar(255),\nconstraint ck_contents_content_type check (content_type in (0,1,2)),\nconstraint pk_contents primary key (id))\n;\n\ncreate table coupons (\nid                        bigint auto_increment not null,\nreach_money               Decimal(10,2),\ndiscount                  Decimal(10,2),\nstart_time                varchar(255),\nend_time                  varchar(255),\ncomment                   varchar(255),\nconstraint pk_coupons primary key (id))\n;\n\ncreate table enjoythecode (\nid                        bigint auto_increment not null,\nref_user_id               bigint,\nnumber                    integer,\ndiscount                  Decimal(10,2),\ncode_type                 tinyint(1) default 0,\nstate                     tinyint(1) default 0,\ncomment                   varchar(255),\ncreated_at_str            varchar(255),\nconstraint pk_enjoythecode primary key (id))\n;\n\ncreate table fondoutrequests (\nid                        bigint auto_increment not null,\nref_user_id               bigint,\nphone                     varchar(255),\nyong_jin                  Decimal(10,2),\ncreated_at_str            varchar(255),\nstatus                    integer,\ncomment                   varchar(255),\nconstraint pk_fondoutrequests primary key (id))\n;\n\ncreate table foodcomments (\nid                        bigint auto_increment not null,\nstore_id                  bigint,\ndescription               longtext,\nimages                    longtext,\nref_user_id               bigint,\nuser_id                   bigint,\nref_product_id            bigint,\nproduct_id                bigint,\nref_order_id              bigint,\ninit_nick_name            varchar(255),\ncomment                   varchar(255),\ncreated_at_str            varchar(255),\nconstraint pk_foodcomments primary key (id))\n;\n\ncreate table infos (\nid                        bigint auto_increment not null,\nclassify                  varchar(255),\nname                      varchar(255),\nphone                     varchar(255),\nurl                       varchar(255),\nimages                    longtext,\nsmall_images              longtext,\ndisplay_date              varchar(255),\ncreated_at_str            varchar(255),\ndescription1              longtext,\ndescription2              longtext,\nconstraint pk_infos primary key (id))\n;\n\ncreate table lineactivities (\nid                        bigint auto_increment not null,\nsponsor_id                bigint,\nsponsor_name              varchar(255),\nproduct_id                bigint,\nproduct                   varchar(255),\noriginal_price            Decimal(10,2),\npresent_price             Decimal(10,2),\nbefore_cut_price          Decimal(10,2),\nthe_number_has_been_cut   integer,\nsabreplay_limit           integer,\ncreated_at_str            varchar(255),\ncomment                   varchar(255),\nconstraint pk_lineactivities primary key (id))\n;\n\ncreate table orders (\nid                        bigint auto_increment not null,\norder_no                  varchar(255),\nstore_id                  bigint,\nref_reseller_id           bigint,\nref_buyer_id              bigint,\nbuyer_id                  bigint,\nprice                     Decimal(10,2),\nquantity                  varchar(255),\ntheme_name                varchar(255),\nship_fee                  Decimal(10,2),\nproduct_amount            Decimal(10,2),\namount                    Decimal(10,2),\njifen                     integer,\njifen_amount              Decimal(10,2),\npromotion_amount          Decimal(10,2),\nstatus                    integer,\nship_name                 varchar(255),\nship_phone                varchar(255),\nship_post_code            varchar(255),\nship_provice              varchar(255),\nship_city                 varchar(255),\nship_zone                 varchar(255),\nship_area                 varchar(255),\nship_location             varchar(255),\nliu_yan                   varchar(255),\nship_time_str             varchar(255),\ncreate_client_ip          varchar(255),\ncreate_clientarea         varchar(255),\npay_client_ip             varchar(255),\npay_clientarea            varchar(255),\npay_return_code           varchar(255),\npay_return_msg            varchar(255),\npay_result_code           varchar(255),\npay_transition_id         varchar(255),\npay_amount                varchar(255),\npay_bank                  varchar(255),\npay_ref_order_no          varchar(255),\npay_sign                  varchar(255),\npay_time                  varchar(255),\npay_third_party_id        varchar(255),\npay_third_party_union_id  varchar(255),\nreseller_profit1          Decimal(10,2),\nreseller_profit2          Decimal(10,2),\nreseller_profit3          Decimal(10,2),\ncomment                   varchar(255),\ncreated_at_str            varchar(255),\ndo_reseller_str           varchar(255),\nconstraint pk_orders primary key (id))\n;\n\ncreate table products (\nid                        bigint auto_increment not null,\nproduct_no                varchar(255),\nname                      varchar(255),\nunit                      varchar(255),\nimages                    longtext,\nshort_desc                longtext,\nprice                     Decimal(10,2),\noriginal_price            Decimal(10,2),\nis_hot_sale               tinyint(1) default 0,\nis_zhao_pai               tinyint(1) default 0,\nsold_number               integer,\nthumb_up                  integer,\ninventory                 integer,\ncomment                   varchar(255),\nstatus                    integer,\ncreated_at_str            varchar(255),\nis_active_product         tinyint(1) default 0,\nbargain_bottom_line       Decimal(10,2),\nsabreplay_limit           integer,\nactive_state              tinyint(1) default 0,\nactive_time               varchar(255),\nactive_sold_number        integer,\nref_store_id              bigint,\nstore_id                  bigint,\nconstraint pk_products primary key (id))\n;\n\ncreate table promotions (\nid                        bigint auto_increment not null,\npromotion_type            varchar(255),\ntitle                     varchar(255),\nreach_money               Decimal(10,2),\ndiscount                  Decimal(10,2),\nstart_time                varchar(255),\nend_time                  varchar(255),\navailable                 tinyint(1) default 0,\ncomment                   varchar(255),\nconstraint pk_promotions primary key (id))\n;\n\ncreate table shipareaprices (\nid                        bigint auto_increment not null,\ncity                      varchar(255),\nzone                      varchar(255),\narea                      varchar(255),\nlocation                  varchar(255),\nship_price                Decimal(10,2),\nref_store_id              bigint,\nstore_id                  bigint,\ncomment                   varchar(255),\nconstraint pk_shipareaprices primary key (id))\n;\n\ncreate table shipinfos (\nid                        bigint auto_increment not null,\nref_user_id               bigint,\nstore_id                  bigint,\nuser_id                   bigint,\nis_default                tinyint(1) default 0,\nname                      varchar(255),\nphone                     varchar(255),\npost_code                 varchar(255),\nprovice                   varchar(255),\ncity                      varchar(255),\nzone                      varchar(255),\narea                      varchar(255),\nlocation                  varchar(255),\ncomment                   varchar(255),\nconstraint pk_shipinfos primary key (id))\n;\n\ncreate table shoppingcartitem (\nid                        bigint auto_increment not null,\nproduct_id                bigint,\nquantity                  integer,\ntheme_name                varchar(255),\ncart_id                   bigint,\nconstraint pk_shoppingcartitem primary key (id))\n;\n\ncreate table shoppingcart (\nid                        bigint auto_increment not null,\nref_buyer_id              bigint,\ntotal_quantity            integer,\nproduct_amount            Decimal(10,2),\nconstraint pk_shoppingcart primary key (id))\n;\n\ncreate table stores (\nid                        bigint auto_increment not null,\narea                      varchar(255),\nphone                     varchar(255),\nmailbox                   varchar(255),\ndisplay_date              varchar(255),\ncreated_at_str            varchar(255),\ndescription1              longtext,\ndescription2              longtext,\nconstraint pk_stores primary key (id))\n;\n\ncreate table themes (\nid                        bigint auto_increment not null,\nname                      varchar(255),\nimages                    longtext,\nprice                     Decimal(10,2),\nref_product_id            bigint,\nproduct_id                bigint,\ncomment                   varchar(255),\nconstraint pk_themes primary key (id))\n;\n\ncreate table users (\nid                        bigint auto_increment not null,\nlogin_name                varchar(255),\nnickname                  varchar(255),\nsignature                 varchar(255),\ndevice                    varchar(255),\npassword                  varchar(255),\nsessionid                 varchar(255),\nphone                     varchar(255),\ndescriptions              varchar(255),\nwx_open_id                varchar(255),\nunion_id                  varchar(255),\nregister_type             integer,\ntitle                     varchar(255),\ncreated_at_str            varchar(255),\ncountry                   varchar(255),\nprovince                  varchar(255),\ncity                      varchar(255),\nzone                      varchar(255),\narea                      varchar(255),\nlocation                  varchar(255),\nhead_img_url              varchar(255),\nsex                       integer,\nage                       integer,\njifen                     integer,\njifen_rate                Decimal(10,2),\nbirthday                  datetime,\nbirthday_str              varchar(255),\nregister_ip               varchar(255),\nregister_iparea           varchar(255),\nlast_login_ip             varchar(255),\nlast_login_iparea         varchar(255),\nlast_pay_ip               varchar(255),\nlast_pay_iparea           varchar(255),\nuser_status               integer,\nuser_role                 integer,\nupline_user_id            bigint,\nbecome_downline_time      varchar(255),\nis_reseller               tinyint(1) default 0,\nreseller_code             varchar(255),\nreseller_code_image       varchar(255),\nref_upline_user_id        bigint,\nref_upline_user_name      varchar(255),\nref_upline_user_head_img_url varchar(255),\ncurrent_total_order_amount Decimal(10,2),\ncurrent_reseller_available_amount Decimal(10,2),\ncurrent_reseller_profit   Decimal(10,2),\nconstraint ck_users_register_type check (register_type in (0,1,2,3)),\nconstraint pk_users primary key (id))\n;\n\n\ncreate table catalogs_products (\ncatalogs_id                    bigint not null,\nproducts_id                    bigint not null,\nconstraint pk_catalogs_products primary key (catalogs_id, products_id))\n;\n\ncreate table coupons_users (\ncoupons_id                     bigint not null,\nusers_id                       bigint not null,\nconstraint pk_coupons_users primary key (coupons_id, users_id))\n;\n\ncreate table orders_products (\norders_id                      bigint not null,\nproducts_id                    bigint not null,\nconstraint pk_orders_products primary key (orders_id, products_id))\n;\n\ncreate table orders_promotions (\norders_id                      bigint not null,\npromotions_id                  bigint not null,\nconstraint pk_orders_promotions primary key (orders_id, promotions_id))\n;\n\ncreate table products_catalogs (\nproducts_id                    bigint not null,\ncatalogs_id                    bigint not null,\nconstraint pk_products_catalogs primary key (products_id, catalogs_id))\n;\n\ncreate table products_orders (\nproducts_id                    bigint not null,\norders_id                      bigint not null,\nconstraint pk_products_orders primary key (products_id, orders_id))\n;\n\ncreate table promotions_orders (\npromotions_id                  bigint not null,\norders_id                      bigint not null,\nconstraint pk_promotions_orders primary key (promotions_id, orders_id))\n;\n\ncreate table users_coupons (\nusers_id                       bigint not null,\ncoupons_id                     bigint not null,\nconstraint pk_users_coupons primary key (users_id, coupons_id))\n;\nalter table foodcomments add constraint fk_foodcomments_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;\ncreate index ix_foodcomments_user_1 on foodcomments (user_id);\nalter table foodcomments add constraint fk_foodcomments_product_2 foreign key (product_id) references products (id) on delete restrict on update restrict;\ncreate index ix_foodcomments_product_2 on foodcomments (product_id);\nalter table orders add constraint fk_orders_buyer_3 foreign key (buyer_id) references users (id) on delete restrict on update restrict;\ncreate index ix_orders_buyer_3 on orders (buyer_id);\nalter table products add constraint fk_products_store_4 foreign key (store_id) references stores (id) on delete restrict on update restrict;\ncreate index ix_products_store_4 on products (store_id);\nalter table shipareaprices add constraint fk_shipareaprices_store_5 foreign key (store_id) references stores (id) on delete restrict on update restrict;\ncreate index ix_shipareaprices_store_5 on shipareaprices (store_id);\nalter table shipinfos add constraint fk_shipinfos_user_6 foreign key (user_id) references users (id) on delete restrict on update restrict;\ncreate index ix_shipinfos_user_6 on shipinfos (user_id);\nalter table shoppingcartitem add constraint fk_shoppingcartitem_product_7 foreign key (product_id) references products (id) on delete restrict on update restrict;\ncreate index ix_shoppingcartitem_product_7 on shoppingcartitem (product_id);\nalter table shoppingcartitem add constraint fk_shoppingcartitem_cart_8 foreign key (cart_id) references shoppingcart (id) on delete restrict on update restrict;\ncreate index ix_shoppingcartitem_cart_8 on shoppingcartitem (cart_id);\nalter table themes add constraint fk_themes_product_9 foreign key (product_id) references products (id) on delete restrict on update restrict;\ncreate index ix_themes_product_9 on themes (product_id);\n\n\n\nalter table catalogs_products add constraint fk_catalogs_products_catalogs_01 foreign key (catalogs_id) references catalogs (id) on delete restrict on update restrict;\n\nalter table catalogs_products add constraint fk_catalogs_products_products_02 foreign key (products_id) references products (id) on delete restrict on update restrict;\n\nalter table coupons_users add constraint fk_coupons_users_coupons_01 foreign key (coupons_id) references coupons (id) on delete restrict on update restrict;\n\nalter table coupons_users add constraint fk_coupons_users_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;\n\nalter table orders_products add constraint fk_orders_products_orders_01 foreign key (orders_id) references orders (id) on delete restrict on update restrict;\n\nalter table orders_products add constraint fk_orders_products_products_02 foreign key (products_id) references products (id) on delete restrict on update restrict;\n\nalter table orders_promotions add constraint fk_orders_promotions_orders_01 foreign key (orders_id) references orders (id) on delete restrict on update restrict;\n\nalter table orders_promotions add constraint fk_orders_promotions_promotions_02 foreign key (promotions_id) references promotions (id) on delete restrict on update restrict;\n\nalter table products_catalogs add constraint fk_products_catalogs_products_01 foreign key (products_id) references products (id) on delete restrict on update restrict;\n\nalter table products_catalogs add constraint fk_products_catalogs_catalogs_02 foreign key (catalogs_id) references catalogs (id) on delete restrict on update restrict;\n\nalter table products_orders add constraint fk_products_orders_products_01 foreign key (products_id) references products (id) on delete restrict on update restrict;\n\nalter table products_orders add constraint fk_products_orders_orders_02 foreign key (orders_id) references orders (id) on delete restrict on update restrict;\n\nalter table promotions_orders add constraint fk_promotions_orders_promotions_01 foreign key (promotions_id) references promotions (id) on delete restrict on update restrict;\n\nalter table promotions_orders add constraint fk_promotions_orders_orders_02 foreign key (orders_id) references orders (id) on delete restrict on update restrict;\n\nalter table users_coupons add constraint fk_users_coupons_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;\n\nalter table users_coupons add constraint fk_users_coupons_coupons_02 foreign key (coupons_id) references coupons (id) on delete restrict on update restrict;','SET FOREIGN_KEY_CHECKS=0;\n\ndrop table activityhistory;\n\ndrop table catalogs;\n\ndrop table catalogs_products;\n\ndrop table commercialactivities;\n\ndrop table company;\n\ndrop table contents;\n\ndrop table coupons;\n\ndrop table coupons_users;\n\ndrop table enjoythecode;\n\ndrop table fondoutrequests;\n\ndrop table foodcomments;\n\ndrop table infos;\n\ndrop table lineactivities;\n\ndrop table orders;\n\ndrop table orders_products;\n\ndrop table orders_promotions;\n\ndrop table products;\n\ndrop table products_catalogs;\n\ndrop table products_orders;\n\ndrop table promotions;\n\ndrop table promotions_orders;\n\ndrop table shipareaprices;\n\ndrop table shipinfos;\n\ndrop table shoppingcartitem;\n\ndrop table shoppingcart;\n\ndrop table stores;\n\ndrop table themes;\n\ndrop table users;\n\ndrop table users_coupons;\n\nSET FOREIGN_KEY_CHECKS=1;','applied','');

#
# Structure for table "promotions"
#

DROP TABLE IF EXISTS `promotions`;
CREATE TABLE `promotions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promotion_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reach_money` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `start_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `available` tinyint(1) DEFAULT '0',
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "promotions"
#

INSERT INTO `promotions` VALUES (1,'满减优惠','满500减100',500.00,100.00,NULL,NULL,0,NULL),(2,'满减优惠','满1000减300',1000.00,300.00,NULL,NULL,0,NULL),(3,'满减优惠','满800减200',800.00,200.00,NULL,NULL,0,NULL),(4,'首单优惠','首单优惠',0.00,10.00,NULL,NULL,0,NULL),(5,'普通优惠','开张大吉下单即减',0.00,10.00,NULL,NULL,1,NULL),(6,'免运费','免运费',100.00,0.00,NULL,NULL,0,NULL);

#
# Structure for table "shoppingcart"
#

DROP TABLE IF EXISTS `shoppingcart`;
CREATE TABLE `shoppingcart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_buyer_id` bigint(20) DEFAULT NULL,
  `total_quantity` int(11) DEFAULT NULL,
  `product_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "shoppingcart"
#

INSERT INTO `shoppingcart` VALUES (1,1,6,522.00);

#
# Structure for table "stores"
#

DROP TABLE IF EXISTS `stores`;
CREATE TABLE `stores` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mailbox` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display_date` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description1` longtext COLLATE utf8_unicode_ci,
  `description2` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "stores"
#


#
# Structure for table "shipareaprices"
#

DROP TABLE IF EXISTS `shipareaprices`;
CREATE TABLE `shipareaprices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `area` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_price` decimal(10,2) DEFAULT NULL,
  `ref_store_id` bigint(20) DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_shipareaprices_store_5` (`store_id`),
  CONSTRAINT `fk_shipareaprices_store_5` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "shipareaprices"
#

INSERT INTO `shipareaprices` VALUES (1,'珠海市','香洲区','富华里',NULL,15.00,NULL,NULL,NULL),(2,'珠海市','香洲区','新香洲',NULL,30.00,NULL,NULL,NULL),(3,'珠海市','香洲区','吉大',NULL,30.00,NULL,NULL,NULL),(4,'珠海市','香洲区','兰埔',NULL,20.00,NULL,NULL,NULL),(5,'珠海市','香洲区','南屏',NULL,25.00,NULL,NULL,NULL),(6,'珠海市','香洲区','柠溪',NULL,20.00,NULL,NULL,NULL),(7,'珠海市','香洲区','唐家湾',NULL,50.00,NULL,NULL,NULL),(8,'珠海市','香洲区','夏湾',NULL,0.00,NULL,NULL,NULL),(9,'珠海市','香洲区','拱北',NULL,0.00,NULL,NULL,NULL),(10,'珠海市','香洲区','华发世纪城',NULL,20.00,NULL,NULL,NULL),(11,'珠海市','香洲区','华发商都',NULL,25.00,NULL,NULL,NULL),(12,'珠海市','香洲区','摩尔广场',NULL,15.00,NULL,NULL,NULL),(13,'珠海市','香洲区','明珠商业广场',NULL,30.00,NULL,NULL,NULL),(14,'珠海市','香洲区','前山',NULL,25.00,NULL,NULL,NULL),(15,'珠海市','香洲区','湾仔',NULL,50.00,NULL,NULL,NULL),(16,'珠海市','香洲区','香洲',NULL,30.00,NULL,NULL,NULL),(17,'珠海市','斗门区','井岸',NULL,80.00,NULL,NULL,NULL),(18,'珠海市','金湾区','红旗镇',NULL,100.00,NULL,NULL,NULL),(19,'珠海市','金湾区','三灶镇',NULL,100.00,NULL,NULL,NULL),(20,'珠海市','横琴新区','横琴新区',NULL,80.00,NULL,NULL,NULL);

#
# Structure for table "products"
#

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `images` longtext COLLATE utf8_unicode_ci,
  `short_desc` longtext COLLATE utf8_unicode_ci,
  `price` decimal(10,2) DEFAULT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `is_hot_sale` tinyint(1) DEFAULT '0',
  `is_zhao_pai` tinyint(1) DEFAULT '0',
  `sold_number` int(11) DEFAULT NULL,
  `thumb_up` int(11) DEFAULT NULL,
  `inventory` int(11) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_active_product` tinyint(1) DEFAULT '0',
  `bargain_bottom_line` decimal(10,2) DEFAULT NULL,
  `sabreplay_limit` int(11) DEFAULT NULL,
  `active_state` tinyint(1) DEFAULT '0',
  `active_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active_sold_number` int(11) DEFAULT NULL,
  `ref_store_id` bigint(20) DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_products_store_4` (`store_id`),
  CONSTRAINT `fk_products_store_4` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "products"
#

INSERT INTO `products` VALUES (1,NULL,'漫天香口味虾',NULL,'zhaopai1.jpg','18种香料精心制作,回味在舌尖',91.00,0.00,0,1,200,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(2,NULL,'漫天香油焖大虾',NULL,'zhaopai2.jpg,zhaopai6.jpg','18种香料精心制作,回味在舌尖',90.00,0.00,1,1,1000,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(3,NULL,'水煮牛肉',NULL,'zhaopai3.jpg,zhaopai6.jpg','18种香料精心制作,回味在舌尖',70.00,0.00,1,1,23123,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(4,NULL,'干锅田鸡',NULL,'zhaopai4.jpg','18种香料精心制作,回味在舌尖',40.00,0.00,0,1,123123,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(5,NULL,'剁椒鱼头',NULL,'zhaopai5.jpg,','18种香料精心制作,回味在舌尖',70.00,0.00,0,1,123,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(6,NULL,'香辣蟹',NULL,'zhaopai6.jpg,zhaopai7.jpg','18种香料精心制作,回味在舌尖',40.00,0.00,1,1,12312,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(7,NULL,'香虾沙虾',NULL,'zhaopai7.jpg','18种香料精心制作,回味在舌尖',70.00,0.00,0,1,1231,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL),(8,NULL,'酸菜鱼火锅',NULL,'zhaopai8.jpg','18种香料精心制作,回味在舌尖',40.00,0.00,0,1,1,0,0,NULL,0,'2020-03-19 13:05:24',0,0.00,0,0,NULL,0,NULL,NULL);

#
# Structure for table "shoppingcartitem"
#

DROP TABLE IF EXISTS `shoppingcartitem`;
CREATE TABLE `shoppingcartitem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `theme_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cart_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_shoppingcartitem_product_7` (`product_id`),
  KEY `ix_shoppingcartitem_cart_8` (`cart_id`),
  CONSTRAINT `fk_shoppingcartitem_cart_8` FOREIGN KEY (`cart_id`) REFERENCES `shoppingcart` (`id`),
  CONSTRAINT `fk_shoppingcartitem_product_7` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "shoppingcartitem"
#

INSERT INTO `shoppingcartitem` VALUES (1,1,2,NULL,1),(2,2,3,NULL,1),(3,3,1,NULL,1);

#
# Structure for table "products_catalogs"
#

DROP TABLE IF EXISTS `products_catalogs`;
CREATE TABLE `products_catalogs` (
  `products_id` bigint(20) NOT NULL,
  `catalogs_id` bigint(20) NOT NULL,
  PRIMARY KEY (`products_id`,`catalogs_id`),
  KEY `fk_products_catalogs_catalogs_02` (`catalogs_id`),
  CONSTRAINT `fk_products_catalogs_catalogs_02` FOREIGN KEY (`catalogs_id`) REFERENCES `catalogs` (`id`),
  CONSTRAINT `fk_products_catalogs_products_01` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "products_catalogs"
#

INSERT INTO `products_catalogs` VALUES (1,1),(2,1),(3,3),(4,4),(5,2),(6,1),(7,1),(8,2);

#
# Structure for table "catalogs_products"
#

DROP TABLE IF EXISTS `catalogs_products`;
CREATE TABLE `catalogs_products` (
  `catalogs_id` bigint(20) NOT NULL,
  `products_id` bigint(20) NOT NULL,
  PRIMARY KEY (`catalogs_id`,`products_id`),
  KEY `fk_catalogs_products_products_02` (`products_id`),
  CONSTRAINT `fk_catalogs_products_catalogs_01` FOREIGN KEY (`catalogs_id`) REFERENCES `catalogs` (`id`),
  CONSTRAINT `fk_catalogs_products_products_02` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "catalogs_products"
#

INSERT INTO `catalogs_products` VALUES (1,1),(1,2),(1,6),(1,7),(2,5),(2,8),(3,3),(4,4);

#
# Structure for table "themes"
#

DROP TABLE IF EXISTS `themes`;
CREATE TABLE `themes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `images` longtext COLLATE utf8_unicode_ci,
  `price` decimal(10,2) DEFAULT NULL,
  `ref_product_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_themes_product_9` (`product_id`),
  CONSTRAINT `fk_themes_product_9` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "themes"
#

INSERT INTO `themes` VALUES (1,'不辣','zhaopai1.jpg,zhaopai2.jpg',0.00,1,1,NULL),(2,'微辣','zhaopai1.jpg,zhaopai2.jpg',0.00,1,1,NULL),(3,'麻辣','zhaopai1.jpg,zhaopai2.jpg',0.00,1,1,NULL),(4,'不辣','zhaopai2.jpg,zhaopai1.jpg',0.00,2,2,NULL),(5,'微辣','zhaopai2.jpg,zhaopai1.jpg',0.00,2,2,NULL),(6,'麻辣','zhaopai2.jpg,zhaopai3.jpg',0.00,2,2,NULL),(7,'不辣','zhaopai3.jpg',0.00,3,3,NULL),(8,'微辣','zhaopai3.jpg,zhaopai2.jpg',0.00,3,3,NULL),(9,'麻辣','zhaopai3.jpg',0.00,3,3,NULL),(10,'不辣','zhaopai4.jpg,zhaopai2.jpg',0.00,4,4,NULL),(11,'微辣','zhaopai4.jpg,zhaopai2.jpg',0.00,4,4,NULL),(12,'麻辣','zhaopai4.jpg',0.00,4,4,NULL);

#
# Structure for table "users"
#

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `signature` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `device` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sessionid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `descriptions` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wx_open_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `union_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `register_type` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `province` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `area` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `head_img_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `jifen` int(11) DEFAULT NULL,
  `jifen_rate` decimal(10,2) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `birthday_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `register_ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `register_iparea` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login_ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login_iparea` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_pay_ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_pay_iparea` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  `user_role` int(11) DEFAULT NULL,
  `upline_user_id` bigint(20) DEFAULT NULL,
  `become_downline_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_reseller` tinyint(1) DEFAULT '0',
  `reseller_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reseller_code_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ref_upline_user_id` bigint(20) DEFAULT NULL,
  `ref_upline_user_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ref_upline_user_head_img_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `current_total_order_amount` decimal(10,2) DEFAULT NULL,
  `current_reseller_available_amount` decimal(10,2) DEFAULT NULL,
  `current_reseller_profit` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "users"
#

INSERT INTO `users` VALUES (1,'superadmin','萧霖',NULL,NULL,'e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2020-03-19 13:05:23',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,100,0.00,NULL,NULL,NULL,NULL,'0:0:0:0:0:0:0:1',NULL,NULL,NULL,0,2,-1,NULL,0,'20200319130523NONz',NULL,-1,NULL,NULL,0.00,0.00,0.00),(2,'18675629612','小燕',NULL,NULL,'e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,'111',NULL,NULL,NULL,'2020-03-19 13:05:23',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,110,0.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,-1,NULL,1,'20200319130523MLLf',NULL,-1,NULL,NULL,0.00,0.00,0.00),(3,'18675629633','小郑',NULL,NULL,'e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2020-03-19 13:05:23',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,120,0.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,-1,NULL,0,'20200319130523jiBU',NULL,-1,NULL,NULL,0.00,0.00,0.00);

#
# Structure for table "shipinfos"
#

DROP TABLE IF EXISTS `shipinfos`;
CREATE TABLE `shipinfos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_user_id` bigint(20) DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `post_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `provice` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `area` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_shipinfos_user_6` (`user_id`),
  CONSTRAINT `fk_shipinfos_user_6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "shipinfos"
#

INSERT INTO `shipinfos` VALUES (1,1,NULL,1,0,'张三','13715673589','516000','香洲区','珠海市',NULL,NULL,'翠香路274号',NULL),(2,1,NULL,1,0,'李斯','13715673614','513000','香洲区','珠海市',NULL,NULL,'安平大厦',NULL),(3,1,NULL,1,0,'杨过','13715673451','513085','香洲区','珠海市',NULL,NULL,'中国银行',NULL);

#
# Structure for table "orders"
#

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  `ref_reseller_id` bigint(20) DEFAULT NULL,
  `ref_buyer_id` bigint(20) DEFAULT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `theme_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_fee` decimal(10,2) DEFAULT NULL,
  `product_amount` decimal(10,2) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `jifen` int(11) DEFAULT NULL,
  `jifen_amount` decimal(10,2) DEFAULT NULL,
  `promotion_amount` decimal(10,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ship_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_post_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_provice` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_zone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_area` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `liu_yan` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_time_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_client_ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_clientarea` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_client_ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_clientarea` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_return_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_return_msg` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_result_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_transition_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_amount` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_bank` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_ref_order_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_third_party_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_third_party_union_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reseller_profit1` decimal(10,2) DEFAULT NULL,
  `reseller_profit2` decimal(10,2) DEFAULT NULL,
  `reseller_profit3` decimal(10,2) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `do_reseller_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_orders_buyer_3` (`buyer_id`),
  CONSTRAINT `fk_orders_buyer_3` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "orders"
#

INSERT INTO `orders` VALUES (1,'100100100',0,0,2,2,138.00,'2,1','微辣',0.00,0.00,0.00,0,0.00,0.00,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,NULL,'2020-03-19 13:05:24',NULL),(2,'100100101',0,0,3,3,99.00,'1','中辣',0.00,0.00,0.00,0,0.00,0.00,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,NULL,'2020-03-19 13:05:24',NULL),(3,'100100101',0,0,0,NULL,88.00,'1','麻辣',0.00,0.00,0.00,0,0.00,0.00,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,NULL,'2020-03-19 13:05:24',NULL);

#
# Structure for table "promotions_orders"
#

DROP TABLE IF EXISTS `promotions_orders`;
CREATE TABLE `promotions_orders` (
  `promotions_id` bigint(20) NOT NULL,
  `orders_id` bigint(20) NOT NULL,
  PRIMARY KEY (`promotions_id`,`orders_id`),
  KEY `fk_promotions_orders_orders_02` (`orders_id`),
  CONSTRAINT `fk_promotions_orders_orders_02` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_promotions_orders_promotions_01` FOREIGN KEY (`promotions_id`) REFERENCES `promotions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "promotions_orders"
#


#
# Structure for table "products_orders"
#

DROP TABLE IF EXISTS `products_orders`;
CREATE TABLE `products_orders` (
  `products_id` bigint(20) NOT NULL,
  `orders_id` bigint(20) NOT NULL,
  PRIMARY KEY (`products_id`,`orders_id`),
  KEY `fk_products_orders_orders_02` (`orders_id`),
  CONSTRAINT `fk_products_orders_orders_02` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_products_orders_products_01` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "products_orders"
#

INSERT INTO `products_orders` VALUES (1,1),(2,1),(3,2),(4,3);

#
# Structure for table "orders_promotions"
#

DROP TABLE IF EXISTS `orders_promotions`;
CREATE TABLE `orders_promotions` (
  `orders_id` bigint(20) NOT NULL,
  `promotions_id` bigint(20) NOT NULL,
  PRIMARY KEY (`orders_id`,`promotions_id`),
  KEY `fk_orders_promotions_promotions_02` (`promotions_id`),
  CONSTRAINT `fk_orders_promotions_orders_01` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_orders_promotions_promotions_02` FOREIGN KEY (`promotions_id`) REFERENCES `promotions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "orders_promotions"
#


#
# Structure for table "orders_products"
#

DROP TABLE IF EXISTS `orders_products`;
CREATE TABLE `orders_products` (
  `orders_id` bigint(20) NOT NULL,
  `products_id` bigint(20) NOT NULL,
  PRIMARY KEY (`orders_id`,`products_id`),
  KEY `fk_orders_products_products_02` (`products_id`),
  CONSTRAINT `fk_orders_products_orders_01` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_orders_products_products_02` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "orders_products"
#

INSERT INTO `orders_products` VALUES (1,1),(1,2),(2,3),(3,4);

#
# Structure for table "foodcomments"
#

DROP TABLE IF EXISTS `foodcomments`;
CREATE TABLE `foodcomments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `images` longtext COLLATE utf8_unicode_ci,
  `ref_user_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `ref_product_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `ref_order_id` bigint(20) DEFAULT NULL,
  `init_nick_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at_str` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_foodcomments_user_1` (`user_id`),
  KEY `ix_foodcomments_product_2` (`product_id`),
  CONSTRAINT `fk_foodcomments_product_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_foodcomments_user_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "foodcomments"
#

INSERT INTO `foodcomments` VALUES (1,0,'漫天香小龙虾好吃','zhaopai1.jpg',1,1,1,1,NULL,NULL,NULL,'2020-03-19 13:05:24'),(2,0,'漫天香小龙虾好吃','zhaopai2.jpg',1,1,2,2,NULL,NULL,NULL,'2020-03-19 13:05:24'),(3,0,'水煮牛肉好吃','zhaopai3.jpg',1,1,3,3,NULL,NULL,NULL,'2020-03-19 13:05:24'),(4,0,'干锅田鸡好吃','zhaopai4.jpg',2,2,4,4,NULL,NULL,NULL,'2020-03-19 13:05:24'),(5,0,'赞一个,剁椒鱼头很正宗','zhaopai5.jpg',2,2,5,5,NULL,NULL,NULL,'2020-03-19 13:05:24'),(6,0,'螃蟹很划算','zhaopai6.jpg,xinpin3.jpg',2,2,6,6,NULL,NULL,NULL,'2020-03-19 13:05:24'),(7,0,'下次还会光顾','zhaopai7.jpg',3,3,7,7,NULL,NULL,NULL,'2020-03-19 13:05:24'),(8,0,'下次还会光顾','zhaopai8.jpg',3,3,8,8,NULL,NULL,NULL,'2020-03-19 13:05:24');

#
# Structure for table "coupons_users"
#

DROP TABLE IF EXISTS `coupons_users`;
CREATE TABLE `coupons_users` (
  `coupons_id` bigint(20) NOT NULL,
  `users_id` bigint(20) NOT NULL,
  PRIMARY KEY (`coupons_id`,`users_id`),
  KEY `fk_coupons_users_users_02` (`users_id`),
  CONSTRAINT `fk_coupons_users_coupons_01` FOREIGN KEY (`coupons_id`) REFERENCES `coupons` (`id`),
  CONSTRAINT `fk_coupons_users_users_02` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "coupons_users"
#

INSERT INTO `coupons_users` VALUES (1,2),(2,2),(3,2);

#
# Structure for table "users_coupons"
#

DROP TABLE IF EXISTS `users_coupons`;
CREATE TABLE `users_coupons` (
  `users_id` bigint(20) NOT NULL,
  `coupons_id` bigint(20) NOT NULL,
  PRIMARY KEY (`users_id`,`coupons_id`),
  KEY `fk_users_coupons_coupons_02` (`coupons_id`),
  CONSTRAINT `fk_users_coupons_coupons_02` FOREIGN KEY (`coupons_id`) REFERENCES `coupons` (`id`),
  CONSTRAINT `fk_users_coupons_users_01` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Data for table "users_coupons"
#

INSERT INTO `users_coupons` VALUES (2,1),(2,2),(2,3);
