package controllers;

import LyLib.Interfaces.IConst;
import LyLib.Utils.DateUtil;
import LyLib.Utils.Msg;
import LyLib.Utils.StrUtil;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import models.*;
import models.common.CompanyModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.Region;

import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class ReportController extends Controller implements IConst {

	public static Result exportOrderReport(boolean all,long storeId) {
		String fileName = DateUtil.NowString("yyyy_MM_dd_HH_mm_ss");//

		// 创建工作薄对象
		HSSFWorkbook workbook2007 = new HSSFWorkbook();
		// 把订单表放进集合
		List<OrderModel> order;
		if (all) {
			fileName += "_ALL.xls";
			order = OrderModel.find.where().eq("storeId",storeId).orderBy("id desc").findList();

		} else {
			fileName += ".xls";
			//order = OrderModel.find.where().eq("status", 1).orderBy("id desc").findList();
			order = OrderModel.find.where().eq("storeId",storeId).and(Expr.ne("status", 0),Expr.ne("status", 2)).and(Expr.ne("status", 3),Expr.ne("status", 9)).orderBy("id desc").findList();
		}

		// 创建单元格样式
		HSSFCellStyle cellStyle = workbook2007.createCellStyle();
		// 设置边框属性
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// // 设置单元格字体
		HSSFFont font = workbook2007.createFont();
		font.setFontName("宋体");
		// 大小
		font.setFontHeightInPoints((short) 10);
		// 加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);

		HSSFCellStyle style = workbook2007.createCellStyle();
		// 指定单元格居中对齐
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 指定单元格垂直居中对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font1 = workbook2007.createFont();
		font1.setFontName("宋体");
		font1.setFontHeightInPoints((short) 10);
		// 加粗
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font1);

		// for (OrderModel module : order) {
		// List<RegInfo> regInfoList = RegInfo.find.where().eq("module_id",
		// module.id).findList();

		// List<OrderModel> order2 = new ArrayList<OrderModel>();
		// for (RegInfo regInfo : regInfoList) {
		// users.add(regInfo.user);
		// }

		// 创建工作表对象，并命名
		HSSFSheet sheet2 = workbook2007.createSheet("订单列表");
		// 设置列宽
		sheet2.setColumnWidth(0, 2200);
		sheet2.setColumnWidth(1, 2500);
		sheet2.setColumnWidth(2, 2500);
		sheet2.setColumnWidth(3, 3800);
		sheet2.setColumnWidth(4, 1300);
		sheet2.setColumnWidth(6, 1300);
		sheet2.setColumnWidth(7, 1300);
		sheet2.setColumnWidth(8, 1800);
		sheet2.setColumnWidth(9, 1800);
		sheet2.setColumnWidth(10, 1900);
		sheet2.setColumnWidth(11, 1800);
		sheet2.setColumnWidth(12, 2500);
		sheet2.setColumnWidth(13, 3000);
		sheet2.setColumnWidth(14, 2500);
		sheet2.setColumnWidth(15, 2500);
		sheet2.setColumnWidth(16, 3500);
		sheet2.setColumnWidth(17, 3500);
		sheet2.setColumnWidth(18, 2500);
		sheet2.setColumnWidth(20, 2500);
		sheet2.setColumnWidth(21, 3000);
		sheet2.setColumnWidth(22, 3000);
		sheet2.setColumnWidth(23, 3000);
		sheet2.setColumnWidth(24, 3000);
		sheet2.setColumnWidth(25, 3000);
		sheet2.setColumnWidth(26, 3000);
		sheet2.setColumnWidth(27, 3000);
		sheet2.setColumnWidth(28, 3000);
		sheet2.setColumnWidth(29, 3000);
		sheet2.setColumnWidth(30, 3000);
		sheet2.setColumnWidth(31, 3000);
		sheet2.setColumnWidth(32, 3000);
		sheet2.setColumnWidth(33, 3000);
		sheet2.setColumnWidth(34, 3000);
		sheet2.setColumnWidth(35, 3000);
		sheet2.setColumnWidth(36, 3000);
		sheet2.setColumnWidth(37, 3000);
		sheet2.setColumnWidth(38, 3000);

		sheet2.setDefaultColumnStyle(0, cellStyle);
		sheet2.setDefaultColumnStyle(1, cellStyle);
		sheet2.setDefaultColumnStyle(2, cellStyle);
		sheet2.setDefaultColumnStyle(3, cellStyle);
		sheet2.setDefaultColumnStyle(4, cellStyle);
		sheet2.setDefaultColumnStyle(5, cellStyle);
		sheet2.setDefaultColumnStyle(6, cellStyle);
		sheet2.setDefaultColumnStyle(7, cellStyle);
		sheet2.setDefaultColumnStyle(8, cellStyle);
		sheet2.setDefaultColumnStyle(9, cellStyle);
		sheet2.setDefaultColumnStyle(10, cellStyle);
		sheet2.setDefaultColumnStyle(11, cellStyle);
		sheet2.setDefaultColumnStyle(12, cellStyle);
		sheet2.setDefaultColumnStyle(13, cellStyle);
		sheet2.setDefaultColumnStyle(14, cellStyle);
		sheet2.setDefaultColumnStyle(15, cellStyle);
		sheet2.setDefaultColumnStyle(16, cellStyle);
		sheet2.setDefaultColumnStyle(17, cellStyle);
		sheet2.setDefaultColumnStyle(18, cellStyle);
		sheet2.setDefaultColumnStyle(19, cellStyle);
		sheet2.setDefaultColumnStyle(20, cellStyle);
		sheet2.setDefaultColumnStyle(21, cellStyle);
		sheet2.setDefaultColumnStyle(22, cellStyle);
		sheet2.setDefaultColumnStyle(23, cellStyle);
		sheet2.setDefaultColumnStyle(24, cellStyle);
		sheet2.setDefaultColumnStyle(25, cellStyle);
		sheet2.setDefaultColumnStyle(26, cellStyle);
		sheet2.setDefaultColumnStyle(27, cellStyle);
		sheet2.setDefaultColumnStyle(28, cellStyle);
		sheet2.setDefaultColumnStyle(29, cellStyle);
		sheet2.setDefaultColumnStyle(30, cellStyle);
		sheet2.setDefaultColumnStyle(31, cellStyle);
		sheet2.setDefaultColumnStyle(32, cellStyle);
		sheet2.setDefaultColumnStyle(33, cellStyle);
		sheet2.setDefaultColumnStyle(34, cellStyle);
		sheet2.setDefaultColumnStyle(35, cellStyle);
		sheet2.setDefaultColumnStyle(36, cellStyle);
		sheet2.setDefaultColumnStyle(37, cellStyle);
		sheet2.setDefaultColumnStyle(38, cellStyle);

		// 创建表头
		HSSFRow title = sheet2.createRow(0);
		title.setHeightInPoints(50);
		title.createCell(0).setCellValue("珠海小龙虾外卖");
		title.createCell(1).setCellValue("");
		title.createCell(2).setCellValue("");
		title.createCell(3).setCellValue("");
		title.createCell(4).setCellValue("");
		title.createCell(5).setCellValue("");
		title.createCell(6).setCellValue("");
		title.createCell(7).setCellValue("");
		title.createCell(8).setCellValue("");
		title.createCell(9).setCellValue("");
		title.createCell(10).setCellValue("");
		title.createCell(11).setCellValue("");
		title.createCell(12).setCellValue("");
		title.createCell(13).setCellValue("");
		title.createCell(14).setCellValue("");
		title.createCell(15).setCellValue("");
		title.createCell(16).setCellValue("");
		title.createCell(17).setCellValue("");
		title.createCell(18).setCellValue("");
		title.createCell(19).setCellValue("");
		title.createCell(20).setCellValue("");
		title.createCell(21).setCellValue("");
		title.createCell(22).setCellValue("");
		title.createCell(23).setCellValue("");
		title.createCell(24).setCellValue("");
		title.createCell(25).setCellValue("");
		title.createCell(26).setCellValue("");
		title.createCell(27).setCellValue("");
		title.createCell(28).setCellValue("");
		title.createCell(29).setCellValue("");
		title.createCell(30).setCellValue("");
		title.createCell(31).setCellValue("");
		title.createCell(32).setCellValue("");
		title.createCell(33).setCellValue("");
		title.createCell(34).setCellValue("");
		title.createCell(35).setCellValue("");
		title.createCell(36).setCellValue("");
		title.createCell(37).setCellValue("");
		title.createCell(38).setCellValue("");
		sheet2.addMergedRegion(new Region(0, (short) 0, 0, (short) 38));
		HSSFCell ce = title.createCell((short) 1);

		HSSFRow titleRow = sheet2.createRow(1);
		// titleRow.setRowStyle(cellStyle);
		// 设置行高
		titleRow.setHeightInPoints(30);
		titleRow.createCell(0).setCellValue("订单号");
		titleRow.createCell(1).setCellValue("下单时间");
		titleRow.createCell(2).setCellValue("购买用户");
		titleRow.createCell(3).setCellValue("产品名");
		titleRow.createCell(4).setCellValue("产品口味");
		titleRow.createCell(5).setCellValue("数量");
		titleRow.createCell(6).setCellValue("单价");
		titleRow.createCell(7).setCellValue("配送费 ");
		titleRow.createCell(8).setCellValue("商品总额");
		titleRow.createCell(9).setCellValue("尊享码优惠");
		titleRow.createCell(10).setCellValue("优惠额");
		titleRow.createCell(11).setCellValue("订单总额");
		titleRow.createCell(12).setCellValue("买家姓名");
		titleRow.createCell(13).setCellValue("联系电话");
		titleRow.createCell(14).setCellValue("区域");
		titleRow.createCell(15).setCellValue("街道");
		titleRow.createCell(16).setCellValue("详细地址");
		titleRow.createCell(17).setCellValue("买家留言");
		titleRow.createCell(18).setCellValue("订单状态");
		titleRow.createCell(19).setCellValue("分销Id");
		titleRow.createCell(20).setCellValue("微信实际支付(分)");
		titleRow.createCell(21).setCellValue("发货时间");
		titleRow.createCell(22).setCellValue("会员号");
		titleRow.createCell(23).setCellValue("上线会员号");
		titleRow.createCell(24).setCellValue("上线微信号");
		titleRow.createCell(25).setCellValue("1级佣金 ");
		titleRow.createCell(26).setCellValue("2级佣金");
		titleRow.createCell(27).setCellValue("3级佣金");
		titleRow.createCell(28).setCellValue("执行分销日期");
		titleRow.createCell(29).setCellValue("创单客户IP");
		titleRow.createCell(30).setCellValue("支付客户IP");
		titleRow.createCell(31).setCellValue("第三方的返回码");
		titleRow.createCell(32).setCellValue("第三方的返回消息");
		titleRow.createCell(33).setCellValue("第三方的业务结果");
		titleRow.createCell(34).setCellValue("第三方的流水ID");
		titleRow.createCell(35).setCellValue("第三方的支付银行");
		titleRow.createCell(36).setCellValue("第三方返回我们的订单号");
		titleRow.createCell(37).setCellValue("第三方的支付时间");
		titleRow.createCell(38).setCellValue("第三方的用户ID");
		HSSFCell ce2 = title.createCell((short) 2);
		ce2.setCellStyle(cellStyle); // 样式，居中

		// 遍历集合对象创建行和单元格
		for (int i = 0; i < order.size(); i++) {
			// 取出对象
			OrderModel order2 = order.get(i);
			// 创建行
			HSSFRow row = sheet2.createRow(i + 2);
			// 创建单元格并赋值

			// 订单号
			HSSFCell orderNoCell = row.createCell(0);
			orderNoCell.setCellValue(order2.orderNo);

			// 下单时间
			HSSFCell orderTimeCell = row.createCell(1);
			orderTimeCell.setCellValue(order2.createdAtStr);

			// 用户姓名
			HSSFCell nameCell = row.createCell(2);
			if (order2.buyer == null) {
				nameCell.setCellValue("无");
			} else {
				if ("".equals(order2.buyer.nickname)) {
					nameCell.setCellValue("无");
				}
				nameCell.setCellValue(order2.buyer.nickname);
			}

			// 产品名称
			HSSFCell productCell = row.createCell(3);

			if (order2.orderProducts != null && order2.orderProducts.size() > 0) {
				String orderProductList = "";
				for(ProductModel product : order2.orderProducts){
					orderProductList += product.name + "\n";
				}
					productCell.setCellValue(orderProductList);
			} else {
				productCell.setCellValue("无");
			}

			// 口味
			HSSFCell themeNameCell = row.createCell(4);
			themeNameCell.setCellValue(order2.themeName);
			// 数量
			HSSFCell quantityCell = row.createCell(5);
			quantityCell.setCellValue(order2.quantity);
			// 单价
			HSSFCell priceCell = row.createCell(6);
			priceCell.setCellValue(order2.price);
			// 配送费
			HSSFCell shipFeeCell = row.createCell(7);
			shipFeeCell.setCellValue(order2.shipFee);
			// 商品总额
			HSSFCell productAmountCell = row.createCell(8);
			productAmountCell.setCellValue(order2.productAmount);
			// 尊享码优惠
			HSSFCell commentCell = row.createCell(10);
			commentCell.setCellValue(order2.comment);
			// 优惠额
			HSSFCell promotionAmountCell = row.createCell(10);
			promotionAmountCell.setCellValue(order2.promotionAmount);
			// 订单总额
			HSSFCell amountCell = row.createCell(11);
			amountCell.setCellValue(order2.amount);
			// 收货人姓名
			HSSFCell shipNameCell = row.createCell(12);
			shipNameCell.setCellValue(order2.shipName);
			// 电话
			HSSFCell shipPhoneCell = row.createCell(13);
			shipPhoneCell.setCellValue(order2.shipPhone);
			// 区域
			HSSFCell shipZoneCell = row.createCell(14);
			shipZoneCell.setCellValue(order2.shipZone);
			// 街道
			HSSFCell shipAreaCell = row.createCell(15);
			shipAreaCell.setCellValue(order2.shipArea);
			// 地址
			HSSFCell shipLocationCell = row.createCell(16);
			shipLocationCell.setCellValue(order2.shipLocation);
			// 留言
			HSSFCell liuYanCell = row.createCell(17);
			liuYanCell.setCellValue(order2.liuYan);
			// 支付状态
			HSSFCell statusCell = row.createCell(18);

			if (order2.status == 1) {
				statusCell.setCellValue("已支付");
			} else if (order2.status == 2) {
				statusCell.setCellValue("已取消");
			} else if (order2.status == 3) {
				statusCell.setCellValue("已删除");
			} else if (order2.status == 4) {
				statusCell.setCellValue("已发货");
			} else if (order2.status == 5) {
				statusCell.setCellValue("已确认");
			} else if (order2.status == 6) {
				statusCell.setCellValue("已评价");
			} else if (order2.status == 7) {
				statusCell.setCellValue("已计算佣金");
			} else if (order2.status == 8) {
				statusCell.setCellValue("已取消计算佣金");
			} else if (order2.status == 9) {
				statusCell.setCellValue("支付失败");
			} else if (order2.status == 10) {
				statusCell.setCellValue("待评价");
			} else if (order2.status == 11) {
				statusCell.setCellValue("数据有误");
			} else if (order2.status == 12) {
				statusCell.setCellValue("等待支付结果");
			}

			//分销Id
			HSSFCell refResellerIdCell = row.createCell(19);
			refResellerIdCell.setCellValue(order2.refResellerId);
			//微信实际支付(分)
			HSSFCell payAmountCell = row.createCell(20);
			payAmountCell.setCellValue(order2.payAmount);
			//发货时间
			HSSFCell shipTimeStrCell = row.createCell(21);
			shipTimeStrCell.setCellValue(order2.shipTimeStr);
			//会员号
			HSSFCell resellerCodeCell = row.createCell(22);
			if (order2.buyer == null) {
				resellerCodeCell.setCellValue("无");
			} else {
				if ("".equals(order2.buyer.resellerCode)) {
					resellerCodeCell.setCellValue("无");
				}
				resellerCodeCell.setCellValue(order2.buyer.resellerCode);
			}
			//上线会员号
			HSSFCell refResellerCodeCell = row.createCell(23);
			if (order2.reseller == null) {
				refResellerCodeCell.setCellValue("无");
			} else {
				if ("".equals(order2.reseller.resellerCode)) {
					refResellerCodeCell.setCellValue("无");
				}
				refResellerCodeCell.setCellValue(order2.reseller.resellerCode);
			}
			//上线微信号
			HSSFCell refNicknameCell = row.createCell(24);
			if (order2.reseller == null) {
				refNicknameCell.setCellValue("无");
			} else {
				if ("".equals(order2.reseller.nickname)) {
					refNicknameCell.setCellValue("无");
				}
				refNicknameCell.setCellValue(order2.reseller.nickname);
			}
			//1级佣金
			HSSFCell resellerProfit1Cell = row.createCell(25);
			resellerProfit1Cell.setCellValue(order2.resellerProfit1);
			//2级佣金
			HSSFCell resellerProfit2Cell = row.createCell(26);
			resellerProfit2Cell.setCellValue(order2.resellerProfit2);
			//3级佣金
			HSSFCell resellerProfit3Cell = row.createCell(27);
			resellerProfit3Cell.setCellValue(order2.resellerProfit3);
			//执行分销日期
			HSSFCell doResellerStrCell = row.createCell(28);
			doResellerStrCell.setCellValue(order2.doResellerStr);
			//创单客户IP
			HSSFCell createClientIPCell = row.createCell(29);
			createClientIPCell.setCellValue(order2.createClientIP);
			//支付客户IP
			HSSFCell payClientIPCell = row.createCell(30);
			payClientIPCell.setCellValue(order2.payClientIP);
			//第三方的返回码
			HSSFCell payReturnCodeCell = row.createCell(31);
			payReturnCodeCell.setCellValue(order2.payReturnCode);
			//第三方的返回消息
			HSSFCell payReturnMsgCell = row.createCell(32);
			payReturnMsgCell.setCellValue(order2.payReturnMsg);
			//第三方的业务结果
			HSSFCell payResultCodeCell = row.createCell(33);
			payResultCodeCell.setCellValue(order2.payResultCode);
			//第三方的流水ID
			HSSFCell payTransitionIdCell = row.createCell(34);
			payTransitionIdCell.setCellValue(order2.payTransitionId);
			//第三方的支付银行
			HSSFCell payBankCell = row.createCell(35);
			payBankCell.setCellValue(order2.payBank);
			//第三方返回我们的订单号
			HSSFCell payRefOrderNoCell = row.createCell(36);
			payRefOrderNoCell.setCellValue(order2.payRefOrderNo);
			//第三方的支付时间
			HSSFCell payTimeCell = row.createCell(37);
			payTimeCell.setCellValue(order2.payTime);
			//第三方的用户ID
			HSSFCell payThirdPartyIdCell = row.createCell(38);
			payThirdPartyIdCell.setCellValue(order2.payThirdPartyId);
		}

		// 生成文件
		String path = Play.application().path().getPath() + "/public/report/" + fileName;
		File file = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			workbook2007.write(fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// response().setHeader("Content-Disposition", "attachment; filename=" +
		// fileName);
		return ok(file);
	}
}