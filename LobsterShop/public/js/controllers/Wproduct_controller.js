var app = angular.module('WproductApp', [ 'ui.bootstrap' ]);

app.filter('safehtml', function($sce) {
	return function(htmlString) {
		return $sce.trustAsHtml(htmlString);
	}
});

app.filter('cartCalculate', function () {
	return function (cartObj) {
		var cartItemsCount = 0
		for(x in cartObj.items){
			cartItemsCount += cartObj.items[x].num
		}
		for (x in cartObj.items) {
			if(GetQueryString('storeId') != cartObj.items[x].storeId){
				cartItemsCount -= cartObj.items[x].num
			}
		}
		return cartItemsCount;
	}
});

app
		.controller(
				'WproductController',
				[
						'$scope',
						'$http',
						function($scope, $http) {
							$scope.selectTab = 1
							$scope.description = []
							$scope.quantity = 1
							$scope.product = {}
							$scope.Comments = []
							$scope.newOrder = {}

							$scope.selectedTheme = {}
							$scope.selectedThemeDiv = 0
							$scope.selectedWinebody = {}
							$scope.selectedWinebodyDiv = 0
							$scope.selectedBottleSpec = {}
							$scope.selectedBottleSpecDiv = 0
							$scope.selectedDecoration = {}
							$scope.selectedDecorationDiv = 0

							$scope.shipInfoList = []
							$scope.selectedShipInfo = {}

							$scope.favoriteProducts = []
							$scope.favoriteProduct = false

							$scope.images = []
							//$scope.userId = GetQueryString('userId')
							$scope.myInterval = 2000;// 轮播时间间隔, 毫秒
							$scope.slides = []
							$scope.Themes = []
							$scope.ThemeImages = []

							$scope.winebodys = []
							$scope.bottleSpecs = []
							$scope.canDecorations = []
							$scope.selectprice = 0
							
						    $scope.cart = {"items":[]}

							$scope.storeId = GetQueryString('storeId')

							$http.get('/cart/get').success(function (data, status, headers, config) {
						        $scope.cart = JSON.parse(data.data);
						    });


							$scope.user = {}
							$http.get('/users/current/login').success(
								function(data, status, headers, config) {
									if (data.flag) {
										$scope.user = data.data;
										$scope.userId=$scope.user.id

									} else {
										//alert(data.message);
									} });
                       ////////////////////////添加产品到购物车//////////////////////////
							 $scope.addCart = function (product, num) {
								 if(product.inventory > 0 ){

                                    /// //////////////显示已加入购物车提示//////////////////
									 document.getElementById("okcat").style.display = ""
									 setTimeout(" document.getElementById('okcat').style.display='none'", 1000);

                          ///////////////////////////获取购物车//////////////////////////
							        $http.get('/cart/get').success(function (data, status, headers, config) {
							            $scope.cart = JSON.parse(data.data);
							            var hasItem = false
							            for(x in $scope.cart.items){
							            	if(product.id == $scope.cart.items[x].pid){
							            		$scope.cart.items[x].num += num
							            		hasItem = true
							            		break
							            	}
							            }
							            
							            if(!hasItem){
							            	$scope.cart.items.push({"pid":product.id,"num":num, "select": true, "storeId":$scope.storeId})
							            }
							            
							            $http({
							            	method: 'PUT',
							            	url: '/cart/set',
							            	data: $scope.cart
							            })
							            .success(
							            		function (data, status, headers,
							            				config) {
							            			if (data.flag) {
							            			} else {
							            				alert(data.message)
							            			}
							            		});
							        });
								 }else{
									 alert('该商品已抢光,亲可以去看看其他商品哦!')
								 }
							    }

							var url = window.location.pathname
							var id = url.substring(url.lastIndexOf("/") + 1);
							$http
									.get('/products/' + id +  "?storeId=" + $scope.storeId)
									.success(
											function(data, status, headers,
													config) {
												if (data.flag) {
													$scope.product = data.data;


													if ($scope.product.themes.length > 0) {
														$scope.selectedTheme = $scope.product.themes[0]

													} else {

														$scope.selectedTheme = {
															id : 1,
															images : $scope.product.images.split(",")[0]
														}
													}

													$scope.Themes = $scope.product.themes
													if ($scope.product.price != null) {
														$scope.selectprice = $scope.product.price
													} else {
														$scope.selectprice = $scope.Themes.price
													}


													var imageList = $scope.selectedTheme.images
															.split(",")

													for (i in imageList) {
														$scope.slides
																.push({
																	"id" : i,
																	"image" : '/showimg/upload/'
																			+ imageList[i]
																})
													}

												} else {
													//alert(data.message);
												}
											});

							$http
									.get('/shipareaprices')
									.success(
											function(data, status, headers,
													config) {
												if (data.flag) {
													$scope.shipInfoList = data.data;
													if (data.data.length > 0) {
														$scope.selectedShipInfo = data.data[0]
													}
												} else {
													//alert(data.message);
												}
											});

							$scope.selectWinebody = function(indexNo) {
								$scope.selectedWinebodyDiv = indexNo
								$scope.selectedWinebody = $scope.winebodys[$scope.selectedWinebodyDiv]
								$scope.selectprice = $scope.selectedWinebody.price
								updatePrice()
							};




							$scope.Commentimage = []
							$scope.Commentimages = []
							$scope.Commentnumber = 0
							$http
							.get(
									'/foodcomments?productId=' + id + '&storeId=' + $scope.storeId)
							.success(
									function(data, status, headers,
											config) {
										
										$scope.Comments = data.data
										if($scope.Comments.length > 0){
											$scope.Commentnumber = $scope.Comments.length	
										}
										
										
									/*	$scope.Commentimage = $scope.Comments[0].images.split(",")*/
										for(var i=0 ; i <$scope.Comments.length ; i++ ){
											$scope.Comments[i].imageList = $scope.Comments[i].images.split(",")
										}
									
									});




							$http
									.get(
											'/users/' + $scope.userId
													+ '/favoriteproducts/' + id)
									.success(
											function(data, status, headers,
													config) {
												$scope.favoriteProduct = data.flag
											});
							refreshData()



							function refreshData() {
								$http
										.get(
												'/users/' + $scope.userId
														+ '/favoriteproducts')
										.success(
												function(data, status, headers,
														config) {
													if (data.flag) {
														$scope.images = []
														$scope.favoriteProducts = data.data
														for (var i = 0; i < $scope.favoriteProducts.length; i++) {
															$scope.images
																	.push($scope.favoriteProducts[i].images
																			.split(
																					",",
																					1)[0]);
														}
													} else {
														$scope.favoriteProducts = []
													}
												});
							}

							/*
							 * $scope.favorite = function() { $http( { method :
							 * 'PUT', url : '/users/' + $scope.userId +
							 * '/favoriteproduct/' + $scope.product.id + '/on',
							 * data : $scope.product }) .success( function(data,
							 * status, headers, config) { if (data.flag) {
							 * 
							 * $scope.Shipinfo = {} $log($scope.Shipinfo)
							 *  } else { alert(data.message) } });
							 * $scope.favoriteProduct = true };
							 */
							/*
							 * $scope.cancelFavorite = function() { $http( {
							 * method : 'PUT', url : '/users/' + $scope.userId +
							 * '/favoriteproduct/' + $scope.product.id + '/off',
							 * data : $scope.product }) .success( function(data,
							 * status, headers, config) { if (data.flag) {
							 * 
							 * $scope.Shipinfo = {} $log($scope.Shipinfo)
							 *  } else { alert(data.message) } });
							 * $scope.favoriteProduct = false };
							 */

							/*
							 * $scope.cancelFavoriteFromMyFavoritePage =
							 * function( obj) { $http( { method : 'PUT', url :
							 * '/users/' + $scope.userId + '/favoriteproduct/' +
							 * obj.id + '/off', data : obj }) .success(
							 * function(data, status, headers, config) { if
							 * (data.flag) { refreshData() } else {
							 * alert(data.message) } }); };
							 */

							/*
							 * $scope.setTab = function(tabNumber) {
							 * $scope.selectTab = tabNumber };
							 */

							$scope.setAdd = function() {
								$scope.quantity = $scope.quantity + 1
							};
							$scope.setMinus = function() {
								if ($scope.quantity > 1) {
									$scope.quantity = $scope.quantity - 1
								}
							};

							
							$scope.number = 1
							$scope.lookAll = function() {
								$scope.number = 1000000;
								
							};
							$scope.top = function() {
								$scope.number = 1;
								
							};


							$scope.select = function(indexNo) {
								$scope.selectedThemeDiv = indexNo
								$scope.selectedTheme = $scope.Themes[indexNo]
								var imageList = $scope.selectedTheme.images
										.split(",")
								if (imageList.length > 0)
									$scope.slides = []
								for (i in imageList) {
									$scope.slides.push({
										"id" : i,
										"image" : '/showimg/upload/'
												+ imageList[i]
									})
								}
							};

							$scope.ImmediateOrder = function (product){
                                       if(product.inventory > 0){
										   window.location.href = window.location.protocol + '//' + window.location.host + '/w/pay?pid=' + product.id + '&num=' + $scope.quantity + '&price=' + $scope.selectprice  + '&kouwei=' + $scope.selectedTheme.name  + '&productAmount=' + $scope.selectprice*$scope.quantity  + '&storeId='+ GetQueryString('storeId')
									   }
								else{
										   alert('该商品已抢光,亲可以去看看其他商品哦!')
									   }
							}

							$scope.show = function (IndexNo) {
								$scope.Comments[IndexNo].show = true
							}

							$scope.hide = function (IndexNo) {
								$scope.Comments[IndexNo].show = false
							}

						} ]);

function GetQueryString(name) {
	var url = decodeURI(window.location.search);
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = url.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
