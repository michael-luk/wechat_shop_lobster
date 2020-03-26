/**
 * Created by yanglu on 15/11/16.
 */

var app = angular.module('ProductBackendApp', ['ngGrid', 'angularFileUpload', 'fundoo.services']);
var cellEditableTemplateProduct = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" ng-blur=\"updateEntity(col, row)\"/>";
var uploadTemplateProduct = '<div> <input type="file" name="files[]" accept="image/*" ng-file-select="uploadImage($files, \'images\', row.entity)"/> <div ng-repeat="imageName in row.entity.imageList"> <a class="fancybox" target="_blank" data-fancybox-group="gallery" fancybox ng-if="isShowImg(imageName)" ng-href="/showImage/{{imageName}}"><img ng-src="/showImage/{{imageName}}" style="width:50px;height:50px;float:left"></a><input type="button" ng-if="isShowImg(imageName)" ng-click="deleteImage(row.entity, \'images\', imageName)" value="删除" style="float:left" /></div></div>';

app.filter('safehtml', function($sce) {
    return function(htmlString) {
        return $sce.trustAsHtml(htmlString);
    }
});

app.controller('ProductBackendController', ['$scope', '$http', '$upload', 'createDialog', '$log', function ($scope, $http, $upload, createDialogService, $log) {
	
    $scope.currentObj = {}
    $scope.list = []
    $scope.list2 = []
    $scope.page = 1;
    $scope.page2 = 1;
    $scope.pageInfo = {}
    $scope.pageInfo2 = {}
    $scope.catalogs=[]
    $scope.findProduct = ""
    $scope.selectCatalogId = 0//0即选择"全部"
    $scope.stores=[]
    $scope.selectStoreId = 1

    $scope.$watch('page', function(){
        refreshDate();
    }, false);

    $scope.$watch('selectStoreId', function(){

        if($scope.stores.length > 0) {
            if ($scope.selectStoreId > 0) {
                if ($scope.page != 1) {
                    $scope.page = 1
                }
            } else {
                if ($scope.page != 1) {
                    $scope.page = 1
                }
                $scope.selectStoreId = 1
            }
            refreshDate()
        }
    }, false);

    $scope.$watch('selectCatalogId', function(){
        if($scope.catalogs.length > 0) {
            if ($scope.selectCatalogId) {
                if ($scope.page != 1) {
                    $scope.page = 1
                }
            } else {
                if ($scope.page != 1) {
                    $scope.page = 1
                }
                $scope.selectCatalogId = 0
            }
            refreshDate()
        }
    }, false);

    $scope.goHomePage = function() {
    	$scope.page = 1;
    }

    $scope.goPrevPage = function() {
		$scope.page = $scope.pageInfo.current -1;
    }

    $scope.goNextPage = function() {
		$scope.page = $scope.pageInfo.current +1;
    }

    $scope.goLastPage = function() {
    	$scope.page = $scope.pageInfo.total;
    }

    $scope.goJumpPage = function() {
        if($scope.jumpPage > $scope.pageInfo.total){
            $scope.jumpPage = $scope.pageInfo.current
            bootbox.alert('总页数最多为' +$scope.pageInfo.total+ '页');
        }else{
            $scope.page = $scope.jumpPage;
        }
    }

    $scope.goNextPage2 = function() {
    	$scope.page2 = $scope.pageInfo2.current +1;
    	fillGridWithCatalogs();
    }

    $scope.goPrevPage2 = function() {
    	$scope.page2 = $scope.pageInfo2.current -1;
    	fillGridWithCatalogs();
    }

    // 搜索
    $scope.findContent = function(){
    	if($scope.page != 1){
    		$scope.page = 1
    	}
    	else{
    		refreshDate()
    	}
    }

    function refreshDate(){
        var url = '/products?page=' + $scope.page + '&keyword=' + $scope.findProduct + '&catalogId=' + $scope.selectCatalogId + '&storeId=' + $scope.selectStoreId
    	$log.log(url)
        //if($scope.selectCatalogId != null) url += '&catalogId=' + $scope.selectCatalogId
    	$log.log('get products from api: ' + url)

        $http.get(url).success(function (data, status, headers, config) {
        	$log.log(data)
            if (data.flag) {
            	for(x in data.data){
            		if(data.data[x].images){
            			data.data[x].imageList = data.data[x].images.split(',')
            		}
        			else{
        				data.data[x].imageList = ''
        			}
            	}

                $scope.list = data.data;
                $scope.pageInfo = data.page;
            }
            else {
            	$scope.list = []
            	bootbox.alert(data.message)
            }
        });
    }

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

    $http.get('/catalogs').success(function (data, status, headers, config) {
    	$log.log('get catalogs from api')
    	if (data.flag) {
    		$log.log(data)
    		$scope.catalogs = data.data;
    	}
    	else {
    		bootbox.alert(data.message)
    	}
    });

    $scope.gridOptions = { data: 'list',
        rowHeight: 130,
        // showSelectionCheckbox:true,
        // enableCellSelection: false,
        enableRowSelection: true,
        selectedItems: [],
        multiSelect:false,
        // enableCellEdit: false,
        plugins:[new ngGridFlexibleHeightPlugin()],
        columnDefs: [
            {field: 'id', displayName: 'Id', width: '40'},
            {field: 'catalogs', displayName: '所属主题', width: '120',cellTemplate: '<div ng-repeat="catalog in COL_FIELD">{{catalog.name}},</div>'},
            {field: 'productNo', displayName: '产品编号', width: '100', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'name', displayName: '名称', width: '200', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'unit', displayName: '单位', width: '80', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'inventory', displayName: '库存', width: '80', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'imageList', displayName: '产品图', width: '200', cellTemplate: uploadTemplateProduct},
            {field: 'shortDesc', displayName: '产品简述', width: '280', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'price', displayName: '价格', width: '70', enableCellEdit: true, editableCellTemplate: cellEditableTemplateProduct},
            {field: 'isHotSale', displayName: '是否热推', width: '70', cellTemplate: '<div><input type="checkbox" ng-model="COL_FIELD" ng-click="updateHotSale(row.entity)" /></div>'},
            {field: 'isZhaoPai', displayName: '设为招牌', width: '70', cellTemplate: '<div><input type="checkbox" ng-model="COL_FIELD" ng-click="updateZhaoPai(row.entity)" /></div>'},
            {field: 'isActiveProduct', displayName: '是否设为活动产品', width: '120', cellTemplate: '<div><input type="checkbox" ng-model="COL_FIELD" ng-click="updateActiveProduct(row.entity)" /></div>'},
            {field: 'soldNumber', displayName: '卖出数', width: '70'},
            {field: 'thumbUp', displayName: '点赞数', width: '70'},
            {field: 'comment', displayName: '备注', width: '200'},
        ] };

	$scope.uploadImage = function($files, imageField, parentObj) {
        for (var i = 0; i < $files.length; i++) {
    		var file = $files[i];

    		$log.log('start upload image file on id: '
    				+ parentObj.id + ', file: ' + file
    				+ ', property: ' + imageField)

    		$scope.upload = $upload.upload({
    			url : '/upload/image',
    			data : {
    				cid : parentObj.id,
    				className : 'ProductModel',
    				property : imageField
    			},
    			file : file
    		})
    				.progress(
    						function(evt) {
    							$log.log('upload percent: '
    									+ parseInt(100.0 * evt.loaded
    											/ evt.total));
    						})
    				.success(function(data, status, headers, config) {
    					$log.log(data);
    					if (data.flag) {
    						if (imageField == 'images') {
    							if(parentObj[imageField])
        							parentObj[imageField] += ',' + data.data;
        						else
        							parentObj[imageField] = data.data;
        						parentObj.imageList = parentObj[imageField].split(',');
    						} else {
    							bootbox.alert('字段不存在: ' + property)
    						}
    					} else {
    						bootbox.alert(data.message)
    					}
    				});
    		// .error(...)
    		// .then(success, error, progress);
        }
	};

	// 删除图片
	$scope.deleteImage = function(obj, property, imageName) {
		$scope.currentObj = obj;
		var index = obj.imageList.indexOf(imageName)
		obj.imageList.splice(index, 1)// 在数组中删掉这个图片文件名
		obj[property] = obj.imageList.join(",")// 数组转为字符串, 以逗号分隔
		$log.log('更新后的images字符串: ' + obj[property])

		$scope.saveContent();
	};

	$scope.isShowImg = function(url) {
		return (url) && (url.length > 0);
	};

	// 产品是否热销
	$scope.updateHotSale = function(obj, checkStatus) {
		obj.isHotSale = !obj.isHotSale
		$scope.currentObj = obj;
		$scope.saveContent();
	};

	// 产品设为招牌
	$scope.updateZhaoPai = function(obj, checkStatus) {
		obj.isZhaoPai = !obj.isZhaoPai
        $scope.currentObj = obj;
        $scope.saveContent();
	};
    // 产品是否设为活动产品
    $scope.updateActiveProduct = function(obj, checkStatus) {
        obj.isActiveProduct = !obj.isActiveProduct
        $scope.currentObj = obj;
        $scope.saveContent();
    };

    // 当前行更新字段
    $scope.updateEntity = function(column, row) {
        $scope.currentObj = row.entity;
        $scope.saveContent();
    };

    // 新建或更新对象
    $scope.saveContent = function() {
        var content = $scope.currentObj;
        var isNew = !content.id
        var url = '/products'
        if(isNew){
        	var http_method = "POST";
        	content.catalogs = $scope.gridCatalogs.selectedItems
            content.refStoreId = $scope.selectStoreId
        	content.images = ''
        	content.description = ''
        }else{
        	var http_method = "PUT";
        	var pos = $scope.list.indexOf(content);
        	url += '/' + content.id

        	if($scope.list2.length > 0){
        		content.catalogs = []
        		for(x in $scope.list2){
        			if($scope.list2[x].refCatalog){
        				content.catalogs.push($scope.list2[x])
        			}
        		}
        	}
        }

		$log.log(content)
        $http({method: http_method, url: url, data:content}).success(function(data, status, headers, config) {
                if(data.flag){
                    if(isNew){
                        $scope.list.push(data.data);
                        refreshDate();
                        bootbox.alert('新建[' + data.data.name + ']成功');
                    }else{
                        $scope.list[pos] = data.data;
                    }
                }else{
                    bootbox.alert(data.message);
                }

                // 记得把list2设为空, 否则会影响删除图片(catalogs被设空)
                $scope.list2 = []
            $scope.gridCatalogs.selectedItems = []
            });
    };

    $scope.deleteContent = function(){
        var items = $scope.gridOptions.selectedItems;
        if(items.length == 0){
            bootbox.alert("请至少选择一个对象.");
        }else{
            var content = items[0];
            if(content.id){
                bootbox.confirm("您确定要删除这个对象[" + content.name + "]吗?", function(result) {
                    if(result) {
                        $http.delete('/products/' + content.id).success(function(data, status, headers, config) {
                            if (data.flag) {
                            	var index = $scope.list.indexOf(content);
                                $scope.gridOptions.selectItem(index, false);
                                $scope.list.splice(index, 1);
                                bootbox.alert("删除成功");
                            }
                            else {
                                bootbox.alert(data.message);
                            }
                        });
                    }
                });
            }
        }
    };

    $scope.formSave = function(formOk){
    	if(!formOk){
            bootbox.alert('验证有误, 请重试');
            return
    	}
        $scope.saveContent();
        $scope.$modalClose();
    };

    $scope.dialogClose = function(){
        $scope.gridCatalogs.selectedItems = [];
        $scope.$modalClose();
    };

    $scope.addContent = function(){
    	$scope.currentObj = {};
    	$scope.list2 = [];
    	$scope.pageInfo2 = {}
    	$scope.page2 = 1;

    	fillGridWithCatalogs();

    	createDialogService("/assets/js/controllers/product_editortemplate.html",{
    		id: 'editor',
    		title: '新增产品',
    		scope: $scope,
            footerTemplate: '<div></div>'
    	});
    };

    $scope.updateContent = function(){
    	var items = $scope.gridOptions.selectedItems;
        if(items.length == 0){
            bootbox.alert("请至少选择一个对象.");
        }else{
            var content = items[0];
            if(content.id){
            	$scope.currentObj =  items[0];
            	}

        $scope.list2 = [];
        $scope.pageInfo2 = {}
        $scope.page2 = 1;

        fillGridWithCatalogs();

        createDialogService("/assets/js/controllers/product_editortemplate.html",{
            id: 'editor',
            title: '更新产品',
            scope: $scope,
            footerTemplate: '<div></div>'
        });
        }
    };

    function fillGridWithCatalogs(){
    	if($scope.page2){
    		$http.get('/catalogs?size=100&page=' + $scope.page2).success(function (data, status, headers, config) {
    			$log.log(data)
    			if (data.flag) {
    				$scope.list2 = data.data;
    				for(x in $scope.list2){
    					for(y in $scope.currentObj.catalogs){
    						if($scope.list2[x].id === $scope.currentObj.catalogs[y].id){
    							$scope.list2[x].refCatalog = true
    							break
    						}
							else{
								$scope.list2[x].refCatalog = false
							}
    					}
    				}
    				$scope.pageInfo2 = data.page;
    			}
    			else {
    				bootbox.alert(data.message)
    			}
    		});
    	}
    }

    $scope.gridCatalogs = { data: 'list2',
            rowHeight: 30,
             showSelectionCheckbox:true,
            // enableCellSelection: false,
//            enableRowSelection: true,
            selectWithCheckboxOnly: true,
            multiSelect:true,
            selectedItems: [],
            // enableCellEdit: false,
            plugins:[new ngGridFlexibleHeightPlugin()],
            checkboxCellTemplate: '<div class="ngSelectionCell"><input tabindex="-1" class="ngSelectionCheckbox" type="checkbox" ng-model="row.entity.refCatalog" /></div>',
            columnDefs: [
                {field: 'id', displayName: 'ID', width: '50'},
                {field: 'name', displayName: '所属主题', width: '200'},
            ] };
}]);
