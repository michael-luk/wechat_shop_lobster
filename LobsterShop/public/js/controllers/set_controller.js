var app = angular.module('SetApp', []);

app.filter('cartCalculate', function () {
    return function (cartObj) {
        var cartItemsCount = 0
        for (x in cartObj.items) {
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


app.filter('getFirstImageFromSplitStr', function () {
    return function (splitStr, position) {
        return '/showimg/upload/' + GetListFromStrInSplit(splitStr)[position];
    }
});


app.controller('SetController', ['$scope', '$http', '$log', function ($scope, $http, $log) {

    /////////////////////////取数////////////////////////
    //1广告
    //2分类
    //3产品
    //4购物车
    $scope.CatalogList = []
    $scope.cart = {"items": []}
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

                            // 拿分类
                            $http.get('/catalogs').success(function (data, status, headers, config) {
                                if (data.flag) {
                                    $scope.CatalogList = data.data;
                                    // 拿购物车

                                } else {
                                    //								alert(data.message);
                                }

                            });






    //////////////////////操作类////////////////////////////////////

    $scope.addCart = function (product, num) {
        $http.get('/cart/get').success(function (data, status, headers, config) {
            $scope.cart = JSON.parse(data.data);
            var hasItem = false
            for (x in $scope.cart.items) {
                if (product.id == $scope.cart.items[x].pid) {
                    $scope.cart.items[x].num += num
                    hasItem = true
                    break
                }
            }

            if (!hasItem) {
                $scope.cart.items.push({"pid": product.id, "num": num, "select": true})
            }

            $http({
                method: 'PUT',
                url: '/cart/set',
                data: $scope.cart
            })
                .success(
                    function (data, status, headers, config) {
                        if (data.flag) {
                        } else {
                            alert(data.message)
                        }
                    });
        });
    }

}]);

function GetQueryString(name) {
    var url = decodeURI(window.location.search);
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = url.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}