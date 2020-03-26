var app = angular.module('WineApp', ['ui.bootstrap']);

app.filter('cartCalculate', function () {
    return function (cartObj,storeIdObj) {
        var cartItemsCount = 0
        for (x in cartObj.items) {
            cartItemsCount += cartObj.items[x].num
        }
        for (x in cartObj.items) {
            if(storeIdObj > 0){
                if (storeIdObj != cartObj.items[x].storeId) {
                    cartItemsCount -= cartObj.items[x].num
                }
            }else{
                if (GetQueryString('storeId') != cartObj.items[x].storeId) {
                    cartItemsCount -= cartObj.items[x].num
                }
            }
        }
        return cartItemsCount;
    }
});

app.filter('inventoryDisplay', function () {
    return function (inventory) {
        if (inventory > 0) {
            //return '库存(' + inventory + ')'
        }
        else {
            return '(抢光了)'
        }
    }
});

app.filter('getFirstImageFromSplitStr', function () {
    return function (splitStr, position) {
        return '/showimg/thumb/' + GetListFromStrInSplit(splitStr)[position];
    }
});

app.filter('UploadImageFromSplitStr', function () {
    return function (splitStr, position) {
        return '/showimg/upload/' + GetListFromStrInSplit(splitStr)[position];
    }
});


app.filter('showMoreDisplay', function () {
    return function (pageInfoObj) {
        if (pageInfoObj) {
            if (pageInfoObj.hasNext) {
                return '看更多'
            }
            else {
                return '到底啦'
            }
        }
        else {
            return '加载中'
        }
    }
});


/*app.filter('getImageFromSplitStr', function () {
 return function (CatalogImages, position) {
 return '/showimg/upload/' + GetListFromStrInSplit(CatalogImages)[position];
 }
 });*/


app.controller('WineController', ['$scope', '$http', '$log', function ($scope, $http, $log) {

    /////////////////////////取数////////////////////////
    //1广告
    //2分类
    //3产品
    //4购物车
    $scope.adList = []
    $scope.CatalogList = []
    $scope.ProductListHotSale = []
    $scope.findProduct = null
    $scope.ProductListZhaoPai = []
    $scope.cart = {"items": []}
    $scope.store = 0
    $scope.myInterval = 2000;//轮播时间间隔, 毫秒

    $scope.page = 1;
    $scope.pageInfo = {}
    $scope.ProductList = []
    $scope.stores = []
    //$scope.selectStoreId = 1
    $scope.StoreList = []
    $scope.sid = GetQueryString('storeId')

    $scope.$watch('page', function () {
        if (document.location.pathname.indexOf('/allProduct') > 0) {
            refreshDate();
        }
    }, false);

    $scope.goNextPage = function () {
        $scope.page = $scope.pageInfo.current + 1;
    }

    $scope.$watch('selectStoreId', function () {
        if ($scope.stores.length > 0) {
            $scope.sid = 0
            refreshDate1()
            refreshDate()
        }
    }, false);

    //取店铺session
        $http.get('/store/get').success(function (data, status, headers, config) {
            if(data.data != null){
                $scope.store = JSON.parse(data.data);
                $scope.selectStoreId = $scope.store
                refreshDate1()
                refreshDate()
            }else{
                if ($scope.sid > 0) {
                    $scope.selectStoreId = $scope.sid
                    refreshDate1()
                    refreshDate()
                }else {
                    $scope.selectStoreId = 1
                    refreshDate1()
                    refreshDate()
                }
            }
        });

    //获取下拉框的店铺
    $http.get('/stores').success(function (data, status, headers, config) {
        $log.log('get stores from api')
        if (data.flag) {
            $log.log(data)
            $scope.stores = data.data;
        }
        else {
            bootbox.alert(data.message)
        }
    });

    function refreshDate() {
        $scope.storeId = $scope.selectStoreId
        //获取对应的店铺，用于更换客服热线
        $http.get('/stores/' + $scope.storeId).success(function (data, status, headers, config) {
            if (data.flag) {
                $scope.StoreList = data.data;
            } else {
               // alert(data.message);
            }

        });

        //选中店铺存session
        $scope.store = $scope.selectStoreId
        $http({
            method: 'GET',
            url: '/store/set?storeId=' + $scope.store,
            data: $scope.store
        });

        //获取所有产品
        $http.get('/products?size=10&page=' + $scope.page + '&storeId=' + $scope.storeId).success(function (data, status, headers, config) {
            if (data.flag) {
                $scope.ProductList = $scope.ProductList.concat(data.data)
                $scope.pageInfo = data.page;
            } else {
//						alert(data.message);
            }
            $http.get('/cart/get').success(function (data, status, headers, config) {
                $scope.cart = JSON.parse(data.data);
            });
        });
    }



    if (document.location.pathname.indexOf('/allProduct') > 0) {
        // 产品页
        //refreshDate()
    }
    else {
        refreshDate1()
    }

    function refreshDate1() {
        // 首页
        // 拿广告
        $http.get('/infos?classify=guanggao').success(
            function (data, status, headers, config) {
                if (data.flag) {
                    $scope.adList = data.data
                } else {
//						alert(data.message);
                }

                // 拿热销产品
                $http.get('/products?size=3&isHotSale=true&sort=desc' + '&storeId=' + $scope.selectStoreId).success(function (data, status, headers, config) {
                    if (data.flag) {
                        $scope.ProductListHotSale = data.data;
                    } else {
//										alert(data.message);
                    }

                    // 拿分类
                    $http.get('/catalogs?size=4&orderBy=comment desc,catalogIndex').success(function (data, status, headers, config) {
                        if (data.flag) {
                            $scope.CatalogList = data.data;
                        } else {
                            //								alert(data.message);
                        }

                        // 拿招牌产品
                        $http.get('/products?size=8&orderBy=isZhaoPai&sort=desc' + '&storeId=' + $scope.selectStoreId).success(function (data, status, headers, config) {
                            if (data.flag) {
                                $scope.ProductListZhaoPai = data.data;
                            } else {
//										alert(data.message);
                            }

                            // 拿购物车
                            $http.get('/cart/get').success(function (data, status, headers, config) {
                                $scope.cart = JSON.parse(data.data);
                            });

                        });
                    });
                });
            });
    }

    //////////////////////操作类////////////////////////////////////

    $scope.addCart = function (product, num) {
        if (product.inventory > 0) {
            //添加购物车提示
            document.getElementById("okcat").style.display = ""
            setTimeout(" document.getElementById('okcat').style.display='none'", 1000);

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
                    $scope.cart.items.push({"pid": product.id, "num": num, "select": true, "storeId": $scope.storeId})
                }

                $http({
                    method: 'PUT',
                    url: '/cart/set',
                    data: $scope.cart
                })
                    .success(
                        function (data, status, headers, config) {
                            if (data.flag) {
                                $http.get('/cart/get').success(function (data, status, headers, config) {
                                    $scope.cart = JSON.parse(data.data);
                                });
                            } else {
                                alert(data.message)
                            }
                        });
            });
        } else {
            alert('该商品已抢光,亲可以去看看其他商品哦!')
        }

    }

    // 产品搜索页使用
    $scope.find = function () {
        var url = '/products?size=1000&keyword=' + $scope.findProduct + '&storeId=' + $scope.storeId
        $http.get(url).success(function (data, status, headers, config) {
            /*$log.log(data)*/
            if (data.flag) {
                $scope.ProductList = data.data;
                for (var i = 0, len = $scope.ProductList.length; i < len; i++) {
                    $scope.images.push($scope.ProductList[i].images.split(",", 1)[0]);
                }
            }
            else {
                $scope.ProductList = []
                alert(data.message)
            }
        });
    }

    $scope.number = 20
    $scope.lookAll = function () {
        $scope.number = 10000;
    };

}]);

function GetQueryString(name) {
    var url = decodeURI(window.location.search);
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = url.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}