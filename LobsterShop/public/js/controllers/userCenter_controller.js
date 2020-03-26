var app = angular.module('UserApp', []);


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

app.controller('UserController', [
		'$scope',
		'$http',
		function($scope, $http) {
			$scope.user = {}
			$scope.cart = {"items":[]}
			$scope.storeId = GetQueryString('storeId')
			$scope.StoreList = []

			$http.get('/stores/'+ $scope.storeId).success(function (data, status, headers, config) {
				if (data.flag) {
					$scope.StoreList = data.data;
				} else {
					alert(data.message);
				}

			});

			 $http.get('/cart/get').success(function (data, status, headers, config) {
			        $scope.cart = JSON.parse(data.data);
			    });


			$http.get('/users/current/login').success(
				function(data, status, headers, config) {
					if (data.flag) {
						$scope.user = data.data;
					} else {
						alert(data.message);
					}
				});
			
			
			$scope.CustomerService = function(){
				alert('本平台当前暂不支持在线自主退款功能，如需退款请联系客服 : 13823974773');
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